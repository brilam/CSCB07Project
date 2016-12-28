package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.accessor.LoadedInfo;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class MenuActivity extends AppCompatActivity {
  // Variables for storing login ID and whether or not a user is an admin
  private String loginId;
  private boolean isAdmin;

  // The invisible buttons
  private Button viewEditClientButton;
  private Button editFlightButton;
  private Button uploadFlightInfoButton;
  private Button uploadClientInfoButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    // Get the Intent that called for this
    Intent intent = getIntent();
    // Get the username that was put into the Intent with key loginID
    loginId = (String) intent.getSerializableExtra("loginID");
    // Gets the isAdmin that was put into the Intent with key isAdmin
    isAdmin = (boolean) intent.getSerializableExtra("isAdmin");

    // find the TextView object for TextView with id welcome_message
    TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);

    // Automatically assume it is email unless it fails the check below
    boolean isEmail = true;

    // If it doesn't contains an @, it is a username
    if (!loginId.contains("@")) {
      isEmail = false;
    }

    // Initializes firstName and lastName variables so it can be set later
    String firstName = null;
    String lastName = null;
    try {
      if (isEmail) {
        firstName = UserDatabase.getUserByEmail(loginId).getFirstName();
        lastName = UserDatabase.getUserByEmail(loginId).getLastName();
      } else {
        firstName = UserDatabase.getUserByUsername(loginId).getFirstName();
        lastName = UserDatabase.getUserByUsername(loginId).getLastName();
      }
    } catch (NoSuchUserException e) { // Never happens since it is valid username
      e.getMessage();
    }
    String name = String.format("%s %s", firstName, lastName);
    // Sets the text to be displayed in the TextView
    welcomeMessage.setText(String.format("Welcome back, %s!", name));

    // If the user is an admin, set the invisible buttons to visible
    if (isAdmin) {
      // Finds the invisible buttons by ID
      viewEditClientButton = (Button) findViewById(R.id.view_edit_client_info);
      uploadFlightInfoButton = (Button) findViewById(R.id.upload_flight_info);
      uploadClientInfoButton = (Button) findViewById(R.id.upload_client_info);
      editFlightButton = (Button) findViewById(R.id.edit_flight_info);

      // Sets them to visible
      editFlightButton.setVisibility(View.VISIBLE);
      viewEditClientButton.setVisibility(View.VISIBLE);
      uploadFlightInfoButton.setVisibility(View.VISIBLE);
      uploadClientInfoButton.setVisibility(View.VISIBLE);
    }

    // Find out where the application stores its files
    File userData = this.getApplicationContext().getDir(Constants.FileNameConstants.USER_DATA_DIR,
        MODE_PRIVATE);
    // Make a File object with the path and file name
    File flightInfoPath = new File(userData, Constants.FileNameConstants.FLIGHT_INFO_FILE);

    // Attempts to parse flight info
    try {
      LoadedInfo.parseFlightInfo(flightInfoPath);
    } catch (FileNotFoundException e) { // Creates an empty flight info file if it doesn't exist
      try {
        flightInfoPath.createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      Set<Flight> flightInfo = new HashSet<>();
      DataWriter.createFlightInfo(flightInfo, flightInfoPath);
    }
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Prompts a search for flight.
   *
   * @param view the view that was clicked
   */
  public void promptSearch(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, FlightSearchPrompt.class);
    // Starts the next activity
    startActivity(intent);
  }


  /**
   * Prompts a search for Itineraries.
   *
   * @param view the view that was clicked
   */
  public void promptItinerarySearch(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, ItinerarySearchPrompt.class);
    // Passes over login ID to the next intent
    intent.putExtra("loginID", loginId);
    // Starts the next activity
    startActivity(intent);
  }

  /**
   * Prompts for editing personal information.
   *
   * @param view the view that was clicked
   */
  public void editPersonalInfo(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, EditInformation.class);
    // If the login ID contains an @, the loginID is an email
    if (loginId.contains("@")) {
      // Passes modifyingUser, email and isAdmin to the next intent
      intent.putExtra("modifyingUser", loginId);
      intent.putExtra("email", loginId);
      intent.putExtra("isAdmin", isAdmin);
    } else { // otherwise, get the email from username
      // Initializes email
      String email = null;
      try {
        email = UserDatabase.getUserByUsername(loginId).getEmail();
      } catch (NoSuchUserException e) { // Never happens since loginID will be an existing user
        e.printStackTrace();
      }
      // Passes modifyingUser, email and isAdmin to the next intent
      intent.putExtra("modifyingUser", email);
      intent.putExtra("email", email);
      intent.putExtra("isAdmin", isAdmin);
    }
    // Starts the next activity
    startActivity(intent);
  }

  /**
   * Moves to a screen where all booked itineraries will be displayed.
   *
   * @param view the view that was clicked
   */
  public void viewBooked(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, BookedItineraries.class);
    // Use a String to store the booked itinerary string representations separated by
    // new lines
    String bookedItins = BookedItineraries.getBookedItineraries(loginId);
    // Moves to the next screen and passes in booked itineraries string, user ID, and status
    intent.putExtra("bookedItins", bookedItins.split("/n"));
    intent.putExtra("loginID", loginId);
    intent.putExtra("isAdmin", isAdmin);
    startActivity(intent);
  }

  /**
   * Moves to the screen where all flights in the FlightDatabase will be displayed.
   *
   * @param view the view that was clicked
   */
  public void displayFlights(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, DisplayFlights.class);
    intent.putExtra("loginID", loginId);
    intent.putExtra("isAdmin", isAdmin);
    // Starts the next activity
    startActivity(intent);
  }

  /**
   * Moves to the screen where you edit client information.
   *
   * @param view the view that was clicked
   */
  public void editClientInfo(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, EditClientInformation.class);
    intent.putExtra("modifyingUser", loginId);
    // Passes isAdmin to the next activity
    intent.putExtra("isAdmin", isAdmin);
    // Starts the next activity
    startActivity(intent);
  }


  /**
   * Moves to the screen where you upload flight information.
   *
   * @param view the view that was clicked
   */
  public void uploadFlightInfo(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, UploadFlightInfo.class);
    // Passes loginID and isAdmin to the next activity to keep its values
    intent.putExtra("loginID", loginId);
    intent.putExtra("isAdmin", isAdmin);
    // Starts the next activity
    startActivity(intent);
  }

  /**
   * Moves to the screen where you upload client information.
   *
   * @param view the view that was clicked
   */
  public void uploadClientInfo(View view) {
    // Sets the next intent
    Intent intent = new Intent(this, UploadUserInfo.class);
    // Passes loginID and isAdmin to the next activity
    intent.putExtra("loginID", loginId);
    intent.putExtra("isAdmin", isAdmin);
    // Starts the next activity
    startActivity(intent);
  }
}
