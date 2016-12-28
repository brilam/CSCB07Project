package cs.b07.cscb07project.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.data.Itinerary;
import cs.b07.cscb07project.backend.databases.NoSuchFlightException;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A class that displays the booked itineraries of the user logged in, and if the user is an admin
 * they can also view the booked itineraries of any client.
 */
public class BookedItineraries extends AppCompatActivity {
  // Creates an EditText object reference for the client ID
  private EditText idField;
  // Stores the loginID of the user
  private String loginId;

  /**
   * Returns a String representation of all the booked itineraries of the user of the ID inputted.
   * Each flight represented in an Itinerary is separated by a new line, with each flight in the
   * format: Flight Number: NUM Origin: ORIGIN Departure: DEPARTURE Destination: DESTINATION
   * Arrival: ARRIVAL Airline: AIRLINE
   *
   * @param id the ID of the User, either a username or an email address
   * @return a string representing all of the User's booked itineraries
   */
  public static String getBookedItineraries(String id) {

    // Creates a String to store the booked itinerary string representations separated by
    // new lines
    String bookedItins = "";
    try {
      // Puts each itinerary string format into an array
      List<String> bookedItinerary;
      if (id.contains("@")) {
        // Checks if logged in by email by seeing if id contains the symbol "@"
        bookedItinerary = UserDatabase.getUserByEmail(id).getBookedItinerary();
      } else { // Otherwise, get the booked itineraries by username
        bookedItinerary = UserDatabase.getUserByUsername(id).getBookedItinerary();
      }

      for (String thisItineraryId : bookedItinerary) {
        // Makes an Itinerary for each Itinerary ID
        List<String> myItineraryList =
            new ArrayList<String>(Arrays.asList(thisItineraryId.split(",")));
        Itinerary thisItinerary = new Itinerary(myItineraryList);
        // Gets a nicely formatted string version of all Itinerary data
        bookedItins += thisItinerary.getItineraryAppData() + "\n";
      }
    } catch (NoSuchUserException | NoSuchFlightException e) {
      // Will never reach here
      System.out.println(e.getMessage());
    }
    return bookedItins;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_booked_itineraries);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Retrieves the intent that was used to move to get to this screen
    Intent oldIntent = getIntent();
    // Retrieve passed values from the intent
    String[] bookedItins = (String[]) oldIntent.getSerializableExtra("bookedItins");
    loginId = (String) oldIntent.getSerializableExtra("loginID");
    // Displays the booked itineraries of the logged in user in the ListView
    displayItineraries(bookedItins);
    // Allows admin buttons to be visible if the user is an admin
    boolean isAdmin = (boolean) oldIntent.getSerializableExtra("isAdmin");
    idField = (EditText) findViewById(R.id.client_ID);
    if (isAdmin) {
      (findViewById(R.id.view_booked_itinerary)).setVisibility(View.VISIBLE);
      idField.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Sets the adapter to the ListView and displays each String in the inputed String array on each
   * row of the ListView.
   *
   * @param bookedItins an array of formatted string representations for each booked itinerary
   */
  private void displayItineraries(String[] bookedItins) {
    // Sets the adapter to the ListView
    ListView itineraries = (ListView) findViewById(R.id.bookedItinList);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, bookedItins);
    itineraries.setAdapter(adapter);
  }

  /**
   * Displays the booked itineraries of the client represented by the login ID in the EditText.
   *
   * @param view the view that was clicked
   */

  public void viewBooked(View view) {
    String userId = idField.getText().toString();

    if (FieldChecks.isEmpty(userId)) {
      idField.setError(getString(R.string.empty_field));

    } else {
      String bookedItins = "";
      try {
        // Checks if logged in by email by seeing if id contains the symbol "@"
        if (userId.contains("@")) {
          // Checks if the inputted id is a client or the admin logged in
          if (!(UserDatabase.getUserByEmail(userId).getAccessKey()) || (userId.equals(loginId))) {
            bookedItins = getBookedItineraries(userId);
          }
        } else { // Otherwise, get the booked itineraries by username
          // Checks if the inputted id is a client or the admin logged in
          if (!(UserDatabase.getUserByUsername(userId).getAccessKey())
              || (userId.equals(loginId))) {
            bookedItins = getBookedItineraries(userId);
          }
        }

      } catch (NoSuchUserException e) {
        // Displays message to show that the client does not exist.
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
            String.format(" %s is not an existing client ID.", userId), duration);
        toast.show();
      }
      // Displays the booked itineraries of the client in the listview
      displayItineraries(bookedItins.split("/n"));
    }
  }
}
