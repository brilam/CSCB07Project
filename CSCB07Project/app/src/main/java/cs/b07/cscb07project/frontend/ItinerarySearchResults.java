package cs.b07.cscb07project.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.Itinerary;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.databases.NoSuchFlightException;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;
import cs.b07.cscb07project.backend.users.Admin;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class returns the search result of the itinerary search back to the user.
 */
public class ItinerarySearchResults extends AppCompatActivity {
  /**
   * The user viewing the activity.
   */
  private User user;
  /**
   * The itinerary selected for booking.
   */
  private Itinerary selectedItinerary;
  /**
   * The results of searched itineraries.
   */
  private ArrayList<Itinerary> searchResults;

  /**
   * Returns a string representation of each itinerary in the given list of itineraries.
   *
   * @param itineraries a list of itineraries
   * @return an array list where each index is the string representation of the itinerary in the
   *         given list of itineraries
   */
  public static ArrayList<String> getRepOfItineraries(ArrayList<Itinerary> itineraries) {
    ArrayList<String> result = new ArrayList<>();
    String itineraryRep;
    for (Itinerary itinerary : itineraries) {
      try {
        itineraryRep = String.format("%s%nCost: %.2f%nTotal time: %s%n",
            itinerary.getItineraryAppData(), itinerary.getCost(), itinerary.getTotalTime());
        result.add(itineraryRep);
      } catch (NoSuchFlightException e) {
        // does not occur
        e.printStackTrace();
      }
    }
    return result;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_itinerary_search_results);
    EditText clientText = (EditText) findViewById(R.id.client_field);

    Intent oldIntent = getIntent();
    searchResults = (ArrayList<Itinerary>) oldIntent.getSerializableExtra("results");
    String loginId = (String) oldIntent.getSerializableExtra("loginID");

    // Get current user
    try {
      if (UserDatabase.containsEmail(loginId)) {
        user = UserDatabase.getUserByEmail(loginId);
      } else {
        user = UserDatabase.getUserByUsername(loginId);
      }
    } catch (NoSuchUserException e) {
      e.printStackTrace();
    }

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
            getRepOfItineraries(searchResults));

    // Check for no search results
    if ((adapter.isEmpty())) {
      Context context = getApplicationContext();
      int duration = Toast.LENGTH_SHORT;
      Toast toast = Toast.makeText(context, "No results found", duration);
      toast.show();
    } else if (user.getAccessKey()) {
      // Check if the user is an admin
      clientText.setVisibility(View.VISIBLE);
    }
    // Populate itineraries list
    ListView itineraries = (ListView) findViewById(R.id.itinerary_list);

    itineraries.setAdapter(adapter);
    itineraries.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Button bookButton = (Button) findViewById(R.id.book_itinerary);
        bookButton.setVisibility(View.VISIBLE);
        selectedItinerary = searchResults.get(position);
      }
    });

  }

  /**
   * Books the selected Itinerary and assigns it to the current user.
   */
  public void bookSelectedItinerary(View view) {
    EditText clientText = (EditText) findViewById(R.id.client_field);
    String inputLoginId = clientText.getText().toString();
    Context context = getApplicationContext();
    boolean isFull = selectedItinerary.isFullyBooked();
    View focusView;

    int duration = Toast.LENGTH_SHORT;

    clientText.setError(null);
    // Check if the client box was empty
    if (FieldChecks.isEmpty(inputLoginId)) {
      // Check if itinerary was fully booked
      if (isFull) {
        Toast toast = Toast.makeText(context, "Itinerary is fully booked, sorry.", duration);
        toast.show();
      } else if (!(user.hasItinerary(selectedItinerary.getItineraryId()))) {
        // Check if user booking was successful
        user.bookItinerary(selectedItinerary);
        Toast toast = Toast.makeText(context,
            String.format("Booking for %s was successful", user.getFirstName()), duration);
        toast.show();
      } else {
        // Check for duplicate bookings
        Toast toast = Toast.makeText(context,
            String.format("Booking was unsuccessful, " + "%s already has this booked!",
                user.getFirstName()),
            duration);
        toast.show();
      }
      // Process of booking for another user by admin
    } else {
      // We know the user is an admin at this point
      Admin admin = (Admin) user;
      // Get user to book for
      User otherUser;
      focusView = clientText;
      try {
        if (UserDatabase.containsEmail(inputLoginId)) {
          otherUser = UserDatabase.getUserByEmail(inputLoginId);
        } else {
          otherUser = UserDatabase.getUserByUsername(inputLoginId);
        }

        // Book for any client or admin them self
        // Don't book if user is another admin
        if (!(otherUser.getAccessKey()) || (otherUser.getEmail().equals(user.getEmail()))) {
          // Check for duplicate booking on the other client
          if (isFull) {
            Toast toast = Toast.makeText(context, "Itinerary is fully booked, sorry.", duration);
            toast.show();
          } else if (!(otherUser.hasItinerary(selectedItinerary.getItineraryId()))) {
            // Book for other user if possible
            admin.bookItineraries(selectedItinerary, otherUser);
            // otherUser.bookItinerary(selectedItinerary);
            Toast toast = Toast.makeText(context,
                String.format("Booking for %s was successful", otherUser.getFirstName()), duration);
            toast.show();
          } else {
            // Check if other user already has itinerary booked
            Toast toast = Toast.makeText(context,
                String.format("Booking was unsuccessful, " + "%s already has this booked!",
                    otherUser.getFirstName()),
                duration);
            toast.show();
          }
        } else {
          clientText.setError(otherUser.getFirstName() + " is an admin, you are not allowed to "
              + "book an itinerary for another admin, sorry!");
          focusView.requestFocus();
        }
        // Check for user that does not exist
      } catch (NoSuchUserException e) {
        clientText.setError(" No such user with " + inputLoginId + " as their loginId exists.");
        focusView.requestFocus();
      }

    }
    // Gets the users
    Set<User> users = UserDatabase.getUsers();
    Set<Flight> flights = FlightDatabase.getFlights();
    // Gets the path for saved user info file and flight info file
    File userData = this.getApplicationContext().getDir(Constants.FileNameConstants.USER_DATA_DIR,
        MODE_PRIVATE);
    File clientsInfo = new File(userData, Constants.FileNameConstants.SAVED_USER_INFO_FILE);
    File flightsInfo = new File(userData, Constants.FileNameConstants.FLIGHT_INFO_FILE);
    // Writes the changes to the file
    DataWriter.createUserInfo(users, clientsInfo);
    DataWriter.createFlightInfo(flights, flightsInfo);
  }
}
