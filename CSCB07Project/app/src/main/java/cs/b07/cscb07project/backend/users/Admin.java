package cs.b07.cscb07project.backend.users;

import cs.b07.cscb07project.backend.data.Itinerary;
import cs.b07.cscb07project.backend.data.accessor.UploadedInfo;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * Class representing an Admin.
 */
public class Admin extends User {


  private static final long serialVersionUID = -1687186179371183648L;

  /**
   * Initializes a new Admin with the given personal information.
   *
   * @param username the username of this Admin
   * @param password the password of this Admin
   * @param lastName the last name of this Admin
   * @param firstName the first name of this Admin
   * @param email the email address of this Admin
   * @param address the address of this Admin
   * @param creditInfo the credit info of this Admin
   * @param expiryDate the expiry date of this Admin's credit card (in the format YYYY-MM-DD)
   * @param bookedItinerary the list of booked itinerary
   */
  public Admin(String username, String password, String lastName, String firstName, String email,
      String address, String creditInfo, Date expiryDate, List<String> bookedItinerary) {
    super(username, password, lastName, firstName, email, address, creditInfo, expiryDate,
        bookedItinerary);
  }

  /**
   * Initializes a new Admin with the given personal information.
   *
   * @param lastName the last name of this Admin
   * @param firstName the first name of this Admin
   * @param email the email address of this Admin
   * @param address the address of this Admin
   * @param creditInfo the credit info of this Admin
   * @param expiryDate the expiry date of this Admin's credit card (in the format YYYY-MM-DD)
   */
  public Admin(String lastName, String firstName, String email, String address, String creditInfo,
      Date expiryDate) {
    super(lastName, firstName, email, address, creditInfo, expiryDate);
  }

  /**
   * Uploads the csv formatted file of flights information to the Driver.
   *
   * @param path the path to the csv file
   */
  public static void uploadFlightInfo(String path) throws FileNotFoundException {
    UploadedInfo.parseFlightInfo(path);
  }

  /**
   * Uploads the csv formatted file of client information to the Driver.
   *
   * @param path the path to the csv file
   */
  public static void uploadClientInfo(String path) throws FileNotFoundException {
    UploadedInfo.parseUserInfo(path);
  }

  /**
   * Changes Last name of user given.
   *
   * @param lastName the last name to change to
   * @param user the user requiring information change
   */
  public void changeLastName(String lastName, User user) {
    user.setLastName(lastName);
  }

  /**
   * Changes first name of user given.
   *
   * @param firstName the first name to change to
   * @param user the user requiring information change
   */
  public void changeFirstName(String firstName, User user) {
    user.setFirstName(firstName);
  }

  /**
   * Changes username of user given.
   *
   * @param username the username to change to
   * @param user the user requiring information change
   */
  public void changeUsername(String username, User user) {
    user.setUsername(username);
  }

  /**
   * Changes password of user given.
   *
   * @param password the password to change to
   * @param user the user requiring information change
   */
  public void changePassword(String password, User user) {
    user.setPassword(password);
  }

  /**
   * Changes address info of given user to given new address.
   *
   * @param address the new address of the user
   * @param user the user requiring information change
   */
  public void changeAddress(String address, User user) {
    user.setAddress(address);
  }

  /**
   * Changes credit info of given user to given creditInfo.
   *
   * @param creditInfo the new credit info
   * @param user the user requiring information change
   */
  public void changeCreditInfo(String creditInfo, User user) {
    user.setCreditInfo(creditInfo);
  }

  /**
   * Changes expiry date of given User's credit card to date given.
   *
   * @param expiryDate the expiry date to change for User's credit card (in the format YYYY-MM-DD)
   * @param user the user requiring information change
   */
  public void changeExpiryDate(Date expiryDate, User user) {
    user.setExpiryDate(expiryDate);
  }

  /**
   * Adds the given itinerary to given User and their booked Itinerary list.
   * 
   * @param itinerary the itinerary
   * @param user the User booking the itinerary
   */
  public void bookItineraries(Itinerary itinerary, User user) {
    user.bookItinerary(itinerary);
  }

  @Override
  public boolean getAccessKey() {
    return true;
  }
}
