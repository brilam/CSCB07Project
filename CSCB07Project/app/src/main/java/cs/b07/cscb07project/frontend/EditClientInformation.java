package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.users.User;

/**
 * This class is used to edit the client information.
 */
public class EditClientInformation extends AppCompatActivity {
  // Information about the person editing the client
  private boolean isAdmin;
  private String modifyingUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Get the Intent that called for this
    Intent intent = getIntent();
    modifyingUser = (String) intent.getSerializableExtra("modifyingUser");
    // Get isAdmin that was put into the Intent
    isAdmin = (boolean) intent.getSerializableExtra("isAdmin");
    setContentView(R.layout.activity_edit_client_information);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Edits the client information.
   * 
   * @param view the view that was clicked
   */
  public void editClientInfo(View view) {
    EditText loginIdField = (EditText) findViewById(R.id.login_id_field);

    String loginId = loginIdField.getText().toString();

    // Resets the error
    loginIdField.setError(null);
    // Resets the focus view
    View focusView = null;
    // Boolean for if the username or email exists
    boolean isSuccess = true;

    // Sets the next intent
    Intent intent = new Intent(this, EditInformation.class);

    // If the login ID contains an @, it is an email so pass it to the next intent
    // Initialize the email that we will pass on
    String email = "";
    try { // Checks if the user is in the database and if so, pass the email of the user to the next
      // intent
      if (loginId.contains("@")) {
        email = loginId;
      } else {
        // Convert the username to an email and store it
        User user = UserDatabase.getUserByUsername(loginId);
        email = user.getEmail();
      }
      // If it is an admin, and it isn't equal to the admin that logged in, not successful
      // Only successful on clients or the admin that logged in
      if ((UserDatabase.getUserByEmail(email).getAccessKey()) && (!(email.equals(loginId)))) {
        isSuccess = false;
      }
      // User does not exist, display a message
    } catch (NoSuchUserException e) {
      loginIdField.setError("Invalid username or email. Try again");
      focusView = loginIdField;
      isSuccess = false;
    }
    // Put values to pass to the next screen if successful
    intent.putExtra("modifyingUser", modifyingUser);
    intent.putExtra("email", email);
    intent.putExtra("isAdmin", isAdmin);



    if (!isSuccess) { // focuses the view on the field that gives an error
      focusView.requestFocus();
    } else { // Passes isAdmin to the next intent and starts it
      intent.putExtra("isAdmin", isAdmin);
      startActivity(intent);
    }

  }
}
