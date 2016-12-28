package cs.b07.cscb07project.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.databases.NoSuchFlightException;
import cs.b07.cscb07project.backend.login.FieldChecks;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * A class that allows the user to edit any of the selected Flight's information.
 */
public class EditFlightInfo extends AppCompatActivity {
  // String array to contain selected flight information upon entering the screen
  private String[] oldFlight;
  // Stores id and status of the user logged in so it is not discarded
  private String loginId;
  private boolean isAdmin;
  // EditText for all information fields
  private EditText flightNumberField;
  private EditText departureField;
  private EditText arrivalField;
  private EditText airlineField;
  private EditText originField;
  private EditText destinationField;
  private EditText costField;
  private EditText seatsField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_flight_info);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Retrieves passed in flight string representation
    Intent oldIntent = getIntent();
    oldFlight = (((String) oldIntent.getSerializableExtra("flight")).split(","));
    // Retrieves user ID and status
    loginId = (String) oldIntent.getSerializableExtra("loginID");
    isAdmin = (boolean) oldIntent.getSerializableExtra("isAdmin");
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Gets all the fields by their ID
    flightNumberField = (EditText) findViewById(R.id.flightNum);
    departureField = (EditText) findViewById(R.id.departureDate);
    arrivalField = (EditText) findViewById(R.id.arrivalDate);
    airlineField = (EditText) findViewById(R.id.airline);
    originField = (EditText) findViewById(R.id.origin_field);
    destinationField = (EditText) findViewById(R.id.destination_field);
    costField = (EditText) findViewById(R.id.cost);
    seatsField = (EditText) findViewById(R.id.numSeats);

    // Sets the flight's existing information except for travelTime in the EditText
    flightNumberField.setText(oldFlight[Constants.DataConstants.FLIGHT_NUMBER_INDEX]);
    departureField.setText(oldFlight[Constants.DataConstants.DEPARTURE_DATE_INDEX]);
    arrivalField.setText(oldFlight[Constants.DataConstants.ARRIVAL_DATE_INDEX]);
    airlineField.setText(oldFlight[Constants.DataConstants.AIRLINE_INDEX]);
    originField.setText(oldFlight[Constants.DataConstants.ORIGIN_INDEX]);
    destinationField.setText(oldFlight[Constants.DataConstants.DESTINATION_INDEX]);
    costField.setText(oldFlight[Constants.DataConstants.PRICE_INDEX]);
    seatsField.setText(oldFlight[Constants.DataConstants.NUM_SEATS_INDEX]);
  }

  /**
   * Updates the information of the flight object based on what changes were indicated in the
   * EditText fields. Invalid flights will result in the flight being discarded, and using a flight
   * number that already exists in the database will result in that flight being overwritten.
   *
   * @param view the view that was clicked
   */
  public void updateInfo(View view) {
    // Reset errors
    flightNumberField.setError(null);
    departureField.setError(null);
    arrivalField.setError(null);
    airlineField.setError(null);
    originField.setError(null);
    destinationField.setError(null);
    costField.setError(null);
    seatsField.setError(null);
    // Gets all the String representations of all the fields
    String flightNum = flightNumberField.getText().toString();
    String departureDate = departureField.getText().toString();
    String arrivalDate = arrivalField.getText().toString();
    String airline = airlineField.getText().toString();
    String origin = originField.getText().toString();
    String destination = destinationField.getText().toString();
    String cost = costField.getText().toString();
    String seats = seatsField.getText().toString();
    // Sets focus on an initial target
    View focusView = flightNumberField;

    // Checks for invalid inputs
    if (FieldChecks.isEmpty(flightNum)) {
      flightNumberField.setError(getString(R.string.empty_field));
    } else if (FieldChecks.isEmpty(departureDate)) {
      departureField.setError(getString(R.string.empty_field));
      focusView = departureField;
    } else if (FieldChecks.isEmpty(arrivalDate)) {
      arrivalField.setError(getString(R.string.empty_field));
      focusView = arrivalField;
    } else if (FieldChecks.isEmpty(airline)) {
      airlineField.setError(getString(R.string.empty_field));
      focusView = airlineField;
    } else if (FieldChecks.isEmpty(origin)) {
      originField.setError(getString(R.string.empty_field));
      focusView = originField;
    } else if (FieldChecks.isEmpty(destination)) {
      destinationField.setError(getString(R.string.empty_field));
      focusView = destinationField;
    } else if (FieldChecks.isEmpty(cost)) {
      costField.setError(getString(R.string.empty_field));
      focusView = costField;
    } else if (FieldChecks.isEmpty(seats)) {
      seatsField.setError(getString(R.string.empty_field));
      focusView = seatsField;
    } else if (!FieldChecks.isValidDateTime(departureDate)) {
      departureField.setError(getString(R.string.invalid_datetime));
      focusView = departureField;
    } else if (!FieldChecks.isValidDateTime(arrivalDate)) {
      arrivalField.setError(getString(R.string.invalid_datetime));
      focusView = arrivalField;
    } else {
      DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_TIME_FORMAT);
      try {
        // Removes previous flight from the database, and adds new one
        Flight flight = new Flight(flightNum, df.parse(departureDate), df.parse(arrivalDate),
            airline, origin, destination, Double.parseDouble(cost), Integer.parseInt(seats));
        FlightDatabase.removeFlight(oldFlight[Constants.DataConstants.FLIGHT_NUMBER_INDEX]);
        FlightDatabase.addFlight(flight);
        if (FlightDatabase.containsFlight(flight)) {
          // Gets the path for saved user info file and flight info file
          File userData = this.getApplicationContext()
              .getDir(Constants.FileNameConstants.USER_DATA_DIR, MODE_PRIVATE);
          File flightsInfo = new File(userData, Constants.FileNameConstants.FLIGHT_INFO_FILE);
          Set<Flight> flights = FlightDatabase.getFlights();
          // Writes the changes to the file
          DataWriter.createFlightInfo(flights, flightsInfo);
        }
        // Creates intent to activity to display all flights again
        Intent intent = new Intent(this, DisplayFlights.class);
        // Passes loginID and isAdmin to the next activity to keep its values
        intent.putExtra("loginID", loginId);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);

      } catch (ParseException e) {
        // Will never happen since made sure to parse
        System.out.println(e.getMessage());
      } catch (NoSuchFlightException e) {
        // Will only happen if undo button of phone used, should be using main menu button
        // Displays message to show that the already edited flight does not exist anymore
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
            "You are trying to edit a flight that"
                + " you've already editted. Please click back to main menu on the previous"
                + " screen",
            duration);
        toast.show();
      }

    }
    focusView.requestFocus();
  }

}
