package cs.b07.cscb07project.frontend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.databases.FlightDatabase;

import java.util.Set;


/**
 * A class that displays all of the flights in the FlightDatabase.
 */
public class DisplayFlights extends AppCompatActivity {
  // The string representation of the flights currently selected on the ListView
  private String selectedFlight;
  // The string representation of all flights separated by new lines
  private String flights;
  // Stores id and status of the user logged in so it is not discarded
  private String loginId;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_flights);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Intent oldIntent = getIntent();
    // Retrieves user ID and status
    loginId = (String) oldIntent.getSerializableExtra("loginID");
    isAdmin = (boolean) oldIntent.getSerializableExtra("isAdmin");
    // Gets all Flights in the database
    Set<Flight> allFlights = FlightDatabase.getFlights();
    // Uses a string to hold all string representations
    flights = "";
    // Put a string representation of every flight into the string separated by /n
    for (Flight flight : allFlights) {
      flights += String.format("%s,%.2f,%s,%s/n", flight.getFlightData(), flight.getCost(),
          flight.getNumSeats(), flight.getTravelTime());

    }
    // Creates a new string format to display more nicely in a ListView
    String[] newFlightRep = FlightSearchPrompt.stringFormat(flights.split("/n"));
    // Sets adapter to the ListView
    ListView displayFlights = (ListView) findViewById(R.id.allFlightsList);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, newFlightRep);
    displayFlights.setAdapter(adapter);
    // Reads clicks on the ListView and stores the string representation of last clicked flight
    displayFlights.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Button editButton = (Button) findViewById(R.id.edit_flight);
        editButton.setVisibility(View.VISIBLE);
        selectedFlight = flights.split("/n")[position];
      }
    });
  }


  /**
   * Moves to screen for editing flights.
   *
   * @param view the view that was clicked
   */
  public void editFlight(View view) {

    // Starts the next activity if a Flight is selected
    if (selectedFlight.contains(",")) {
      Intent intent = new Intent(this, EditFlightInfo.class);
      intent.putExtra("flight", selectedFlight);
      // Passes loginID and isAdmin to the next activity to keep its values
      intent.putExtra("loginID", loginId);
      intent.putExtra("isAdmin", isAdmin);
      startActivity(intent);;
    }
  }

  /**
   * Returns to the menu screen.
   *
   * @param view the view that was clicked
   */
  public void toMain(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, MenuActivity.class);
    // Passes loginID and isAdmin to the next activity to keep its values
    intent.putExtra("loginID", loginId);
    intent.putExtra("isAdmin", isAdmin);
    // Starts the next activity
    startActivity(intent);
  }


}


