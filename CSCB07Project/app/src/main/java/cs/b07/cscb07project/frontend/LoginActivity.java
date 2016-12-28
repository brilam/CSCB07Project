package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants.FileNameConstants;
import cs.b07.cscb07project.backend.data.accessor.LoadedInfo;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {
  // Fields for the username and password
  private EditText loginIdField;
  private EditText passwordField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // Find out where the application stores its files
    File userData =
        this.getApplicationContext().getDir(FileNameConstants.USER_DATA_DIR, MODE_PRIVATE);
    // Make a File object with the path and file name
    File clientsInfo = new File(userData, FileNameConstants.SAVED_USER_INFO_FILE);

    /*
     * Attempts to the parse user information if there exists a file. If not, a new file is created
     * with only one user (the Admin user), and that file is parsed instead.
     */
    try {
      LoadedInfo.parseUserInfo(clientsInfo);
    } catch (FileNotFoundException e) { // Never happens, since a file will always be created
      e.printStackTrace();
    }
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_login, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Authenticates the login based on what is entered in the EditText.
   * 
   * @param view the view that is clicked
   */
  public void authLogin(View view) {
    loginIdField = (EditText) findViewById(R.id.login_id_field);
    passwordField = (EditText) findViewById(R.id.password);

    String loginId = loginIdField.getText().toString();
    String password = passwordField.getText().toString();

    // Resets errors on the fields
    loginIdField.setError(null);
    passwordField.setError(null);

    // Variable used for checking for empty fields, empty passwords, and short passwords
    boolean isSuccess = true;
    View focusView = null;


    // Checks empty loginID, password, and short password
    if (FieldChecks.isEmpty(loginId)) {
      loginIdField.setError(getString(R.string.empty_field));
      focusView = loginIdField;
      isSuccess = false;
    } else if (FieldChecks.isEmpty(password)) {
      passwordField.setError(getString(R.string.empty_field));
      focusView = passwordField;
      isSuccess = false;
    } else if (FieldChecks.isShortPassword(password)) {
      passwordField.setError(getString(R.string.short_password));
      focusView = passwordField;
      isSuccess = false;
    }

    boolean isEmail = false;
    boolean isAdmin = false;

    // If the login ID contains an @, it is an email
    if (loginId.contains("@")) {
      isEmail = true;
    }

    // Checks if everything above is successful
    if (isSuccess) {
      try {
        User user = null;
        if (isEmail) {
          // Gets the user object given an email
          user = UserDatabase.getUserByEmail(loginId);
        } else {
          // Gets the User object given a loginID
          user = UserDatabase.getUserByUsername(loginId);
        }

        // Checks if the user is an admin
        if (user.getAccessKey()) {
          isAdmin = true;
        }

        // If the password entered doesn't match the password, it fails!
        if (!user.getPassword().equals(password)) {
          passwordField.setError(getString(R.string.incorrect_password));
          focusView = passwordField;
          isSuccess = false;
        }
      } catch (NoSuchUserException e) { // If the user doesn't exist, it is an incorrect password
        loginIdField.setError(getString(R.string.incorrect_login_id));
        focusView = loginIdField;
        isSuccess = false;
      }
    }

    // Checks to see if it is failed check for short password, empty loginID, empty password,
    // non-existant account
    // and incorrect password
    if (!isSuccess) {
      focusView.requestFocus();
    } else if (isSuccess) { // if not, go to the next screen
      // Specifies the next Activity to move to: MenuActivity.
      Intent intent = new Intent(this, MenuActivity.class);
      // Passes the String data to MenuActivity.
      intent.putExtra("loginID", loginId);
      intent.putExtra("isAdmin", isAdmin);
      startActivity(intent); // Starts MenuActivity.
    }
  }

  /**
   * Signs up the user.
   * 
   * @param view the view that is clicked
   */
  public void signUp(View view) {
    // Specifies the next activity to move to: SignupActivity
    Intent intent = new Intent(this, SignUpActivity.class);
    // Starts SignUpActivity
    startActivity(intent);
  }
}
