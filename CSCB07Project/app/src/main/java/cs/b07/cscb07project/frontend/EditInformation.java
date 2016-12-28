package cs.b07.cscb07project.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cs.b07.cscb07project.R;
import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.NoSuchUserException;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.login.FieldChecks;
import cs.b07.cscb07project.backend.users.Admin;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditInformation extends AppCompatActivity {
  // The email of the user
  private String email;
  // Information about the person editing
  private boolean isAdmin;
  private String modifyingUser;

  // EditText for all information fields
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
    // Get the email and isAdmin that was put into the Intent
    modifyingUser = (String) intent.getSerializableExtra("modifyingUser");
    email = (String) intent.getSerializableExtra("email");
    isAdmin = (boolean) intent.getSerializableExtra("isAdmin");

    setContentView(R.layout.activity_edit_information);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Gets all the fields by their ID
    usernameField = (EditText) findViewById(R.id.username);
    passwordField = (EditText) findViewById(R.id.password);
    lastNameField = (EditText) findViewById(R.id.last_name);
    firstNameField = (EditText) findViewById(R.id.first_name);
    addressField = (EditText) findViewById(R.id.address);
    creditCardNumberField = (EditText) findViewById(R.id.credit_card_number);
    creditCardExpiryField = (EditText) findViewById(R.id.credit_card_expiry);

    // Initializes user
    User user = null;
    try { // Gets the user object
      user = UserDatabase.getUserByEmail(email);
    } catch (NoSuchUserException e) { // Never happens since email is correct
      e.printStackTrace();
    }

    // Sets the user's existing information except for password in the EditText
    usernameField.setText(user.getUsername());
    passwordField.setHint(String.format("Current password: %s", user.getPassword()));
    lastNameField.setText(user.getLastName());
    firstNameField.setText(user.getFirstName());
    addressField.setText(user.getAddress());
    creditCardNumberField.setText(user.getCreditInfo());
    creditCardExpiryField.setText(user.getExpiryDate());
  }

  /**
   * Update the user information.
   *
   * @param view the view that was clicked
   */
  public void updateInfo(View view) {
    // Gets all the fields by their ID
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
    String username = usernameField.getText().toString();
    String password = passwordField.getText().toString();
    String lastName = lastNameField.getText().toString();
    String firstName = firstNameField.getText().toString();
    String address = addressField.getText().toString();
    String creditCardNumber = creditCardNumberField.getText().toString();
    String creditCardExpiry = creditCardExpiryField.getText().toString();

    /*
     * Validity checks for username, password, last name, first name, address, credit card number,
     * and credit card expiry date
     */
    if (FieldChecks.isEmpty(username)) {
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
    } else {
      // Parses the String to a Date
      DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_FORMAT);
      Date expiryDate = null;
      try {
        expiryDate = df.parse(creditCardExpiry);
      } catch (ParseException e) { // Never occurs as it was checked above
        e.printStackTrace();
      }

      try {
        // Modifies the existing user
        User existingUser = UserDatabase.getUserByEmail(email);
        // If the user that logged in is an admin, change the user to modify through
        // administrative methods
        if (isAdmin) {
          Admin admin;
          // Checks if login ID is username or password and finds corresponding admin
          if (modifyingUser.contains("@")) {
            admin = (Admin) UserDatabase.getUserByEmail(modifyingUser);
          } else {
            admin = (Admin) UserDatabase.getUserByUsername(modifyingUser);
          }
          admin.changeUsername(username, existingUser);
          admin.changePassword(password, existingUser);
          admin.changeFirstName(firstName, existingUser);
          admin.changeLastName(lastName, existingUser);
          admin.changeAddress(address, existingUser);
          admin.changeCreditInfo(creditCardNumber, existingUser);
          admin.changeExpiryDate(expiryDate, existingUser);
        } else { // If the user that logged in is him/herself, modify their own personal info
          existingUser.setUsername(username);
          existingUser.setPassword(password);
          existingUser.setLastName(lastName);
          existingUser.setFirstName(firstName);
          existingUser.setAddress(address);
          existingUser.setCreditInfo(creditCardNumber);
          existingUser.setExpiryDate(expiryDate);
        }
        // Writes the changes to the file
        File userData = this.getApplicationContext()
            .getDir(Constants.FileNameConstants.USER_DATA_DIR, MODE_PRIVATE);
        File clientsInfo = new File(userData, Constants.FileNameConstants.SAVED_USER_INFO_FILE);
        DataWriter.createUserInfo(UserDatabase.getUsers(), clientsInfo);

        // Sets the next intent to menu activity
        Intent intent = new Intent(this, MenuActivity.class);
        // Passes back email as the loginID
        intent.putExtra("loginID", modifyingUser);
        intent.putExtra("isAdmin", isAdmin);
        // Starts the next activity
        startActivity(intent);

      } catch (NoSuchUserException e) { // Never happens since the email is valid
        e.printStackTrace();
      }

    }
  }
}
