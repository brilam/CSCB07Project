package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants.FileNameConstants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.users.Admin;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;


/**
 * This class is used to upload flight information.
 */
public class UploadFlightInfo extends AppCompatActivity {
  // Information about the admin calling for uploading flight
  private String loginId;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Get the Intent that called for this
    Intent intent = getIntent();
    loginId = (String) intent.getSerializableExtra("loginID");
    isAdmin = (boolean) intent.getSerializableExtra("isAdmin");
    setContentView(R.layout.activity_upload_flight_info);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Uploads the flight information.
   * 
   * @param view the view that is clicked.
   */
  public void uploadFlightInfo(View view) {
    // Finds the flightFileField
    EditText csvFileField = (EditText) findViewById(R.id.flightFileField);
    // Gets the file name from the EditText
    String fileName = csvFileField.getText().toString();

    // Find out where the application stores its files
    File uploadData =
        this.getApplicationContext().getDir(FileNameConstants.UPLOAD_DATA_DIR, MODE_PRIVATE);
    // Make a File object with the path and file name
    File uploadFlightInfo = new File(uploadData, fileName);
    // Resets the errors and the view to be focused on
    csvFileField.setError(null);
    View focusView = null;

    // Whether or not it was successful
    boolean isSuccess = true;

    try { // Attempts to upload the file
      Admin.uploadFlightInfo(uploadFlightInfo.toString());
      Set<Flight> flightInfo = FlightDatabase.getFlights();
      // Find out where the application stores its files
      File userData =
          this.getApplicationContext().getDir(FileNameConstants.USER_DATA_DIR, MODE_PRIVATE);
      // Make a File object with the path and file name
      File newFlightFile = new File(userData, FileNameConstants.FLIGHT_INFO_FILE);
      // Writes the new flight information
      DataWriter.createFlightInfo(flightInfo, newFlightFile);
    } catch (FileNotFoundException e) { // Sets an error on the field
      csvFileField.setError("The file doesn't exist!");
      focusView = csvFileField;
      isSuccess = false;
    }

    // If it is not successful, focus on the csvFileField
    if (!isSuccess) {
      focusView.requestFocus();
    } else {
      // Makes a Toast for successful upload
      Toast toast =
          Toast.makeText(getApplicationContext(), "Upload successful!", Toast.LENGTH_SHORT);
      // Show the toast
      toast.show();
      // Sets the next intent
      Intent intent = new Intent(this, MenuActivity.class);
      // Passes loginID and isAdmin back to the intent
      intent.putExtra("loginID", loginId);
      intent.putExtra("isAdmin", isAdmin);
      // Starts the next activity
      startActivity(intent);

    }
  }
}
