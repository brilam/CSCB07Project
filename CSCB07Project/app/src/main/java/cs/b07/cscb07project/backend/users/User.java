package cs.b07.cscb07project.backend.users;


import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.data.Itinerary;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;





/**
 * Class representing an Abstract User.
 */
public abstract class User implements Serializable {

  private static final long serialVersionUID = 8825003715850118753L;
  private String username;
  private String password;
  private String lastName;
  private String firstName;
  private String email;
  private String address;
  private String creditInfo;
  private Date expiryDate;
  private List<String> bookedItinerary;

  /**
   * Initializes a new User with the given personal information.
   *
   * @param password the password of this User
   * @param lastName the last name of this User
   * @param firstName the first name of this User
   * @param email the email address of this User
   * @param address the address of this User
   * @param creditInfo the credit info of this User
   * @param expiryDate the expiry date of this User's credit card (in the format "YY-MM-DD")
   * @param bookedItinerary the list of booked itinerary
   */
  public User(String username, String password, String lastName, String firstName, String email,
      String address, String creditInfo, Date expiryDate, List<String> bookedItinerary) {
    this.username = username;
    this.password = password;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.address = address;
    this.creditInfo = creditInfo;
    this.expiryDate = expiryDate;
    this.bookedItinerary = bookedItinerary;
  }

  /**
   * Initializes a new User with the given personal information.
   * 
   * @param lastName the last name of this User
   * @param firstName the first name of this User
   * @param email the email address of this User
   * @param address the address of this User
   * @param creditInfo the credit info of this User
   * @param expiryDate the expiry date of this User's credit card (in the format "YY-MM-DD")
   */
  public User(String lastName, String firstName, String email, String address, String creditInfo,
      Date expiryDate) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.address = address;
    this.creditInfo = creditInfo;
    this.expiryDate = expiryDate;
    this.bookedItinerary = new ArrayList<String>();
  }

  /**
   * Returns this User's last name.
   * 
   * @return a last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets Last name of user.
   * 
   * @param lastName the last name to change to
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns this User's first name.
   * 
   * @return a first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets first name of user.
   * 
   * @param firstName the first name to change to
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns this User's email address.
   * 
   * @return an email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns this User's address.
   * 
   * @return an address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets address info to given new address.
   * 
   * @param address the new User address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Returns this User's credit card information.
   * 
   * @return some credit card information
   */
  public String getCreditInfo() {
    return creditInfo;
  }

  /**
   * Sets credit info to given creditInfo.
   * 
   * @param creditInfo the new credit info
   */
  public void setCreditInfo(String creditInfo) {
    this.creditInfo = creditInfo;
  }

  /**
   * Returns this User's credit card's expiry date.
   * 
   * @return an expiry date
   */
  public String getExpiryDate() {
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);
    return df.format(expiryDate);
  }

  /**
   * Sets expiry date of this user's credit card to date given.
   * 
   * @param expiryDate the expiry date to change for User's credit card (in the format "YY-MM-DD")
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Returns all personal information about this User.
   * 
   * @return the information stored for this User in this format:
   *         LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate (the ExpiryDate is stored
   *         in the format YYYY-MM-DD)
   */
  public String getPersonalInformation() {
    return String.format("%s,%s,%s,%s,%s,%s,%s", this.getLastName(), this.getFirstName(),
        this.getEmail(), this.getAddress(), this.getCreditInfo(), this.getExpiryDate(),
        this.getBookedItinerary());
  }

  /**
   * Returns all booked Itineraries that this user has booked.
   * 
   * @return all booked Itineraries associated with this User
   */
  public List<String> getBookedItinerary() {
    return bookedItinerary;
  }



  /**
   * Adds the given itinerary to this client's booked list if itinerary does not already exist.
   * 
   * @param itinerary the itinerary which needs to be added
   */
  public boolean bookItinerary(Itinerary itinerary) {
    if ((!(this.hasItinerary(itinerary.getItineraryId())))) {
      itinerary.bookItinerary();
      bookedItinerary.add(itinerary.getItineraryId());
      return true;
    }
    return false;
  }

  /**
   * Returns a boolean indicating if user already has the given itinerary stored.
   * 
   * @param itineraryId the Id of the itinerary to check
   * @return a boolean indicating if user already has the given itinerary stored
   */
  public boolean hasItinerary(String itineraryId) {
    for (String bookedItinerary : this.getBookedItinerary()) {
      if (bookedItinerary.equals(itineraryId)) {
        return true;
      }
    }
    return false;

  }

  /**
   * Returns boolean indicating if user has access key.
   * 
   * @return a boolean indicating whether user has access
   */
  public abstract boolean getAccessKey();

  @Override
  public String toString() {
    boolean isAdmin = false;

    if (this instanceof Admin) {
      isAdmin = true;
    }

    return String.format("%s, %s, %s,%s,%s,%s,%s,%s,%s", password, isAdmin, email, lastName,
        firstName, address, creditInfo, expiryDate, bookedItinerary);
  }

  /**
   * Returns the password of the user.
   * 
   * @return the password of the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password for the user.
   * 
   * @param password the password to be set for the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the username of the user.
   * 
   * @return the username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   * 
   * @param username the username to be set
   */
  public void setUsername(String username) {
    this.username = username;
  }
}


