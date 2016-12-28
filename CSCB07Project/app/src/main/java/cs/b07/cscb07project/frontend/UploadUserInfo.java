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
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.users.Admin;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public class UploadUserInfo extends AppCompatActivity {
  private String loginId;
  private boolean isAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Get the Intent that called for this
    Intent intent = getIntent();
    loginId = (String) intent.getSerializableExtra("loginID");
    isAdmin = (boolean) intent.getSerializableExtra("isAdmin");
    setContentView(R.layout.activity_upload_user_info);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Uploads the user information.
   * 
   * @param view the view clicked
   */
  public void uploadUserInfo(View view) {
    // Finds the userInfoFileFIeld
    EditText csvFileField = (EditText) findViewById(R.id.userInfoFileField);
    // Gets the file name from the EditText
    String fileName = csvFileField.getText().toString();

    // Find out where the application stores its files
    File uploadData =
        this.getApplicationContext().getDir(FileNameConstants.UPLOAD_DATA_DIR, MODE_PRIVATE);
    // Make a File object with the path and file name
    File uploadUserInfo = new File(uploadData, fileName);
    // Resets the errors and the view to be focused on
    csvFileField.setError(null);
    View focusView = null;

    // Whether or not it was successful
    boolean isSuccess = true;

    try { // Attempts to upload the file
      Admin.uploadClientInfo(uploadUserInfo.toString());
      Set<User> userInfo = UserDatabase.getUsers();
      // Find out where the application stores its files
      File userData =
          this.getApplicationContext().getDir(FileNameConstants.USER_DATA_DIR, MODE_PRIVATE);
      // Make a File object with the path and file name
      File newUserFile = new File(userData, FileNameConstants.SAVED_USER_INFO_FILE);
      // Writes the new user information
      DataWriter.createUserInfo(userInfo, newUserFile);
    } catch (FileNotFoundException e) { // Sets an error on the field
      csvFileField.setError("The file doesn't exist!");
      focusView = csvFileField;
      isSuccess = false;
    }

    // If it is not successful, focus on the csvFileField
    if (!isSuccess) {
      focusView.requestFocus();
    } else {
      String message = String
          .format("Upload successful! Please remember to update %s with credentials for the newly "
                  + "added users", FileNameConstants.SAVED_USER_INFO_FILE);
      // Makes a Toast for successful upload
      Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
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
