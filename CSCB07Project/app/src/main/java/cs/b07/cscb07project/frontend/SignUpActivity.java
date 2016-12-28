package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;
import cs.b07.cscb07project.backend.users.Client;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
  // Stores the loginID from the previous intent
  private String loginId;

  // EditText for all the information fields
  private EditText emailField;
  private EditText usernameField;
  private EditText passwordField;
  private EditText lastNameField;
  private EditText firstNameField;
  private EditText addressField;
  private EditText creditCardNumberField;
  private EditText creditCardExpiryField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get the Intent that called for this
    Intent intent = getIntent();
    // Get the username that was put into the Intent with key loginID
    loginId = (String) intent.getSerializableExtra("loginID");

    setContentView(R.layout.activity_sign_up);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /**
   * Signs up a user with the credentials provided.
   *
   * @param view the view that was clicked
   */
  public void signUpResult(View view) {
    // Gets all the fields by their ID
    emailField = (EditText) findViewById(R.id.email);
    usernameField = (EditText) findViewById(R.id.username);
    passwordField = (EditText) findViewById(R.id.password);
    lastNameField = (EditText) findViewById(R.id.last_name);
    firstNameField = (EditText) findViewById(R.id.first_name);
    addressField = (EditText) findViewById(R.id.address);
    creditCardNumberField = (EditText) findViewById(R.id.credit_card_number);
    creditCardExpiryField = (EditText) findViewById(R.id.credit_card_expiry);

    // The view to be focused on
    View focusView = null;

    // Reset errors.
    emailField.setError(null);
    usernameField.setError(null);
    passwordField.setError(null);
    lastNameField.setError(null);
    firstNameField.setError(null);
    addressField.setError(null);
    creditCardNumberField.setError(null);
    creditCardExpiryField.setError(null);

    // A boolean used to determine if the signup process is a success
    boolean isSuccess = true;

    // Gets all the String representations of all the fields
    String email = emailField.getText().toString();
    String username = usernameField.getText().toString();
    String password = passwordField.getText().toString();
    String lastName = lastNameField.getText().toString();
    String firstName = firstNameField.getText().toString();
    String address = addressField.getText().toString();
    String creditCardNumber = creditCardNumberField.getText().toString();
    String creditCardExpiry = creditCardExpiryField.getText().toString();

    /*
     * Validity checks for email, username, password, last name, first name, address, credit card
     * number, and credit card expiry date
     */
    if (FieldChecks.isEmpty(email)) {
      emailField.setError(getString(R.string.empty_field));
      focusView = emailField;
      isSuccess = false;
    } else if (!FieldChecks.isAllowedEmail(email)) {
      emailField.setError(getString(R.string.invalid_email));
      focusView = emailField;
      isSuccess = false;
    } else if (FieldChecks.isEmpty(username)) {
      usernameField.setError(getString(R.string.empty_field));
      focusView = usernameField;
      isSuccess = false;
    } else if (!FieldChecks.isAllowedUsername(username)) {
      usernameField.setError(getString(R.string.username_not_allowed));
      focusView = usernameField;
      isSuccess = false;
    } else if (FieldChecks.isEmpty(password)) {
      passwordField.setError(getString(R.string.empty_field));
      focusView = passwordField;
      isSuccess = false;
    } else if (FieldChecks.isShortPassword(password)) {
      passwordField.setError(getString(R.string.short_password));
      focusView = passwordField;
      isSuccess = false;
    } else if (!FieldChecks.isAllowedName(lastName)) {
      lastNameField.setError(getString(R.string.name_not_allowed));
      focusView = lastNameField;
      isSuccess = false;
    } else if (!FieldChecks.isAllowedName(firstName)) {
      firstNameField.setError(getString(R.string.name_not_allowed));
      focusView = firstNameField;
      isSuccess = false;
    } else if (FieldChecks.isEmpty(address)) {
      addressField.setError(getString(R.string.empty_field));
      focusView = addressField;
      isSuccess = false;
    } else if (FieldChecks.isEmpty(creditCardNumber)) {
      creditCardNumberField.setError(getString(R.string.empty_field));
      focusView = creditCardNumberField;
      isSuccess = false;
    } else if (!FieldChecks.isValidCreditCard(creditCardNumber)) {
      creditCardNumberField.setError(getString(R.string.invalid_credit_card));
      focusView = creditCardNumberField;
      isSuccess = false;
    } else if (!FieldChecks.isValidDate(creditCardExpiry)) {
      creditCardExpiryField.setError(getString(R.string.invalid_expiry_date));
      focusView = creditCardExpiryField;
      isSuccess = false;
    }

    // If any of the above are unsuccessful, focus view on them
    if (!isSuccess) {
      focusView.requestFocus();
    } else { // otherwise, it is a success and add the user to the UserDatabase and let them login
      DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);
      Date expiryDate = null;
      try {
        expiryDate = df.parse(creditCardExpiry);
      } catch (ParseException e) { // Never occurs as it was checked above
        e.printStackTrace();
      }
      // Creates a new User
      Client newUser = new Client(username, password, lastName, firstName, email, address,
          creditCardNumber, expiryDate, new ArrayList<String>());
      // Adds them to the UserDatabase
      UserDatabase.addUser(newUser);

      // Writes the new user information to the user info file
      File userData = this.getApplicationContext().getDir(Constants.FileNameConstants.USER_DATA_DIR,
          MODE_PRIVATE);
      File clientsInfo = new File(userData, Constants.FileNameConstants.SAVED_USER_INFO_FILE);
      DataWriter.createUserInfo(UserDatabase.getUsers(), clientsInfo);

      Intent intent = new Intent(this, LoginActivity.class);
      // Starts LoginActivity
      startActivity(intent);
    }
  }
}
