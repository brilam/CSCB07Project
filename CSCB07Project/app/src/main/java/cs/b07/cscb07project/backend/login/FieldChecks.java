package cs.b07.cscb07project.backend.login;

import android.text.TextUtils;

import cs.b07.cscb07project.backend.constants.Constants;
import cs.b07.cscb07project.backend.constants.Constants.FieldConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;



/**
 * A class used to represent a sign up process.
 */
public class FieldChecks {
  /**
   * Returns whether or not the field information is empty.
   *
   * @param fieldInfo the field information entered
   * @return whether or not the field information is empty
   */
  public static boolean isEmpty(String fieldInfo) {
    return TextUtils.isEmpty(fieldInfo);
  }

  /**
   * Returns whether or not the username is allowed. An allowed username must be at least four
   * characters long and cannot contain a @.
   *
   * @param username the username
   * @return whether or not hhe username length is greater or equal to the minimum username length
   */
  public static boolean isAllowedUsername(String username) {
    return username.length() >= FieldConstants.MINIMUM_USERNAME_LENGTH && !username.contains("@");
  }

  /**
   * Returns whether or not the password is less than the minimum password length.
   *
   * @param password the password
   * @return whether or not the password length is less than the minimum password length
   */
  public static boolean isShortPassword(String password) {
    return password.length() < FieldConstants.MINIMUM_PASSWORD_LENGTH;
  }

  /**
   * Checks if the name given is an allowed first name. A valid first/last name must be at least two
   * letters long
   *
   * @param name the user's first or last name
   * @return whether or not the name has two letters or more
   */
  public static boolean isAllowedName(String name) {
    return name.length() >= FieldConstants.MINIMUM_NAME_LENGTH;
  }

  /**
   * Returns whether the credit card number matches the valid credit card length.
   *
   * @param creditCardNumber the credit card number to be checked
   * @return whether or not the credit card number has a valid length
   */
  public static boolean isValidCreditCard(String creditCardNumber) {
    return creditCardNumber.length() == FieldConstants.CREDIT_CARD_LENGTH;
  }

  /**
   * Checks if the date input is in the correct format of "YYYY-MM-DD".
   * 
   * @param date the string representation of the date
   * @return whether or not the date string is in the valid date format
   */
  public static boolean isValidDate(String date) {
    DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_FORMAT);
    try {
      df.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Checks if the date input is in the correct format of "YYYY-MM-DD HH;MM".
   * 
   * @param date the string representation of the datetime
   * @return whether or not the date string is in the valid datetime format
   */
  public static boolean isValidDateTime(String date) {
    DateFormat df = new SimpleDateFormat(Constants.DateConstants.DATE_TIME_FORMAT);
    try {
      df.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Returns whether or not the email is allowed. An allowed email must contain an @, and must be at
   * least 7 characters long.
   *
   * @param email the email address provided
   * @return whether or not the email is allowed
   */
  public static boolean isAllowedEmail(String email) {
    return email.contains("@") && email.length() >= FieldConstants.MINIMUM_EMAIL_LENGTH;
  }
}
