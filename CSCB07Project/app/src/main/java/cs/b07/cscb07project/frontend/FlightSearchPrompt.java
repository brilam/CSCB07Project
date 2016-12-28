package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.constants.Constants.DataConstants;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * A class that prompts the user to give inputs for the Flight search.
 */
public class FlightSearchPrompt extends AppCompatActivity {
  /**
   * Returns a more organized string representation of the array of strings. In the format (with
   * each line representing an element of the array returned): Flight #: NUM Duration: DEPARTURE -
   * ARRIVAL Airline: AIRLINE Cost: COST Travel Time:TIME Number of seats: SEATS
   *
   * @param flights the string array containing all searched flight results
   * @return an array of formatted string representations of the array of regular flight string
   *         representations
   */
  public static String[] stringFormat(String[] flights) {
    String[] indivFlight;
    // Checks if the array contains only an empty string
    if (!((flights.length == 1) && flights[0].equals(""))) {
      indivFlight = new String[flights.length];
      // Formats every flight string into a more organized format
      for (int currFlight = 0; currFlight < flights.length; currFlight++) {
        String[] info = flights[currFlight].split(",");
        indivFlight[currFlight] = String.format(
            "Flight #: %s%nDuration: %s  -  "
                + "%s%nAirline: %s%nFrom: %s to %s%nCost:%s            "
                + "     Number of seats:%s%nTravel Time: %s%n",
            info[DataConstants.FLIGHT_NUMBER_INDEX], info[DataConstants.DEPARTURE_DATE_INDEX],
            info[DataConstants.ARRIVAL_DATE_INDEX], info[DataConstants.AIRLINE_INDEX],
            info[DataConstants.ORIGIN_INDEX], info[DataConstants.DESTINATION_INDEX],
            info[DataConstants.PRICE_INDEX], info[DataConstants.NUM_SEATS_INDEX],
            info[DataConstants.TRAVEL_TIME_INDEX]);
      }
    } else {
      indivFlight = new String[] {"No results"};
    }
    return indivFlight;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flight_search_prompt);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Queries for flights matching the given flight origin, destination, and departure date provided
   * that the given information are all valid inputs.
   *
   * @param view the view that was clicked
   */
  public void searchFlights(View view) {
    // Create references to EditTexts
    EditText originText = (EditText) findViewById(R.id.origin_field);
    EditText destinationText = (EditText) findViewById(R.id.destination_field);
    EditText dateText = (EditText) findViewById(R.id.date_field);
    // Sets focus to an initial target
    View focusView = originText;
    // Resets error messages
    originText.setError(null);
    destinationText.setError(null);
    dateText.setError(null);
    String origin = originText.getText().toString();
    String destination = destinationText.getText().toString();
    String date = dateText.getText().toString();
    // Checks empty origin, destination, date
    if (FieldChecks.isEmpty(origin)) {
      originText.setError(getString(R.string.empty_field));
    } else if (FieldChecks.isEmpty(destination)) {
      destinationText.setError(getString(R.string.empty_field));
      focusView = destinationText;
    } else if (FieldChecks.isEmpty(date)) {
      dateText.setError(getString(R.string.empty_field));
      focusView = dateText;
    } else if (!FieldChecks.isValidDate(date)) {
      dateText.setError(getString(R.string.invalid_date));
      focusView = dateText;
    } else {
      DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_FORMAT);
      // This is where you would search but right now we just have premade search results
      String results = "";
      try {
        results = FlightDatabase.searchForDisplay(df.format(df.parse(date)), origin, destination);
      } catch (ParseException e) {
        // Will never reach here since made sure to be valid
        e.printStackTrace();
      }

      Intent intent = new Intent(this, FlightSearchResults.class);

      // Puts the organized format of every flight as a value to be passed on
      intent.putExtra("results", stringFormat(results.split("/n")));
      startActivity(intent);
    }
    focusView.requestFocus();
  }

}
