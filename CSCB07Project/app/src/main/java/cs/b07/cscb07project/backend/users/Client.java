package cs.b07.cscb07project.backend.users;

import java.util.Date;
import java.util.List;

/**
 * Class representing a Client.
 */
public class Client extends User {

  private static final long serialVersionUID = 6018782186425576193L;

  /**
   * Initializes a new Client with the given personal information.
   *
   * @param username the username of this Client
   * @param password the password of this Client
   * @param lastName the last name of this Client
   * @param firstName the first name of this Client
   * @param email the email address of this Client
   * @param address the address of this Client
   * @param creditInfo the credit info of this Client
   * @param expiryDate the expiry date of this Client's credit card (in the format "YY-MM-DD")
   * @param bookedItinerary the list of booked itinerary
   */
  public Client(String username, String password, String lastName, String firstName, String email,
      String address, String creditInfo, Date expiryDate, List<String> bookedItinerary) {
    super(username, password, lastName, firstName, email, address, creditInfo, expiryDate,
        bookedItinerary);
  }

  /**
   * Initializes a new Client with the given personal information.
   *
   * @param lastName the last name of this Client
   * @param firstName the first name of this Client
   * @param email the email address of this Client
   * @param address the address of this Client
   * @param creditInfo the credit info of this Client
   * @param expiryDate the expiry date of this Client's credit card (in the format "YY-MM-DD")
   */
  public Client(String lastName, String firstName, String email, String address, String creditInfo,
      Date expiryDate) {
    super(lastName, firstName, email, address, creditInfo, expiryDate);
  }

  @Override
  public boolean getAccessKey() {
    return false;
  }

}
