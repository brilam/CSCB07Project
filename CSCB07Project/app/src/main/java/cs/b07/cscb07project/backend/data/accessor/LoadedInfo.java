package cs.b07.cscb07project.backend.data.accessor;

import android.util.JsonReader;
import android.util.JsonToken;

import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.constants.Constants.JsonConstants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.writer.DataWriter;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.users.Admin;
import cs.b07.cscb07project.backend.users.Client;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class used to represent loaded information (the JSON files).
 *
 * @author Brian Lam
 */
public class LoadedInfo {
  // Object number
  private static int objectNumber;

  // Declare the variables which will be set when parsing the flights in the JSON file
  private static String flightNumber;
  private static Date departDate;
  private static Date arrivalDate;
  private static String airline;
  private static String origin;
  private static String destination;
  private static double price;
  private static Flight flight;

  // Declare the variables which will be set when parsing the flights in the JSON file
  private static String email;
  private static String username;
  private static String password;
  private static boolean isSetAdmin;
  private static boolean isAdmin;
  private static String firstName;
  private static String lastName;
  private static String address;
  private static String creditCardNumber;
  private static Date expiryDate;
  private static boolean modifiedBookedItinerary;
  private static String itinerary;
  private static User user;
  private static Admin admin;
  private static int numSeats;
  private static boolean isSetNumSeats;


  /**
   * Parses the user information given a path, and adds the users to the UserDatabase.
   *
   * @param path the path of the file to be parsed
   * @throws FileNotFoundException when the file name specified cannot be found
   */
  public static void parseUserInfo(File path) throws FileNotFoundException {
    // Date Format with the flight date format
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);

    if (!path.exists()) {
      try {
        path.createNewFile();
        Date expiryDate = df.parse("2016-11-09");
        Admin admin = new Admin("Brian", "brian!", "Lam", "Brian", "notmyemail@email.com",
            "123 Yonge St.", "133333337", expiryDate, new ArrayList<String>());
        Set<User> adminUser = new HashSet<>();
        adminUser.add(admin);
        DataWriter.createUserInfo(adminUser, path);

      } catch (IOException | ParseException e) {
        e.printStackTrace();
      }
    }

    // Creates a FileReader and JsonReader object
    FileReader fileReader = new FileReader(path);
    JsonReader jsonReader = new JsonReader(fileReader);

    try {
      // Begin reading object
      jsonReader.beginObject();

      // While there is more to read, continue reading and parsing
      while (jsonReader.hasNext()) {
        List<String> bookedItinerary = new ArrayList<>();

        // Sets the object number to 0 when starting or resets the object number after completely
        // parsing one flight
        objectNumber = 0;
        // Reads the flight number
        email = jsonReader.nextName();
        jsonReader.beginObject();

        // While there exists objects that are not parsed, keep parsing them
        while (objectNumber < JsonConstants.USER_OBJECTS) {
          String name = jsonReader.nextName();

          // Depending on what the nextName is, it parses the next part accordingly
          switch (name) {
            case "Username":
              username = jsonReader.nextString();
              break;
            case "Password":
              password = jsonReader.nextString();
              break;
            case "IsAdmin":
              isSetAdmin = true;
              isAdmin = jsonReader.nextBoolean();
              break;
            case "LastName":
              lastName = jsonReader.nextString();
              break;
            case "FirstName":
              firstName = jsonReader.nextString();
              break;
            case "Address":
              address = jsonReader.nextString();
              break;
            case "CreditCardNumber":
              creditCardNumber = jsonReader.nextString();
              break;
            case "ExpiryDate":
              expiryDate = df.parse(jsonReader.nextString());
              break;
            case "BookedItinerary":
              jsonReader.beginArray();
              modifiedBookedItinerary = true;
              // While there are still Strings in the array, keep reading and adding them to the
              // booked itinerary
              while (jsonReader.peek() == JsonToken.STRING) {
                itinerary = jsonReader.nextString();
                bookedItinerary.add(itinerary);
              }
              jsonReader.endArray();
              break;
            default: // Never used as it will always be a case from above.
              break;
          }
          objectNumber++;
        }
        jsonReader.endObject();

        // If everything is all parsed, make a flight and add it to the flight info list
        if (isParsed(username, password, isSetAdmin, email, firstName, lastName, address,
            creditCardNumber, expiryDate, modifiedBookedItinerary)) {
          if (!isAdmin) {
            user = new Client(username, password, lastName, firstName, email, address,
                creditCardNumber, expiryDate, bookedItinerary);
            UserDatabase.addUser(user);
          } else {
            admin = new Admin(username, password, lastName, firstName, email, address,
                creditCardNumber, expiryDate, bookedItinerary);
            UserDatabase.addUser(admin);
          }
        }
      }
      // Closes the fileReader and jsonReader after everything is done
      fileReader.close();
      jsonReader.close();
    } catch (IOException | ParseException e) {
      e.getStackTrace();
    }
  }

  /**
   * Parses the flight information given a file name, and adds the flights to FlightDatabase.
   *
   * @param path the path of the file to be parsed
   * @throws FileNotFoundException when the file name specified cannot be found
   */
  public static void parseFlightInfo(File path) throws FileNotFoundException {
    // Date Format with the flight date format
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_TIME_FORMAT);

    // Creates a FileReader and JsonReader object
    FileReader fileReader = new FileReader(path);
    JsonReader jsonReader = new JsonReader(fileReader);

    try {
      // Begin reading object
      jsonReader.beginObject();

      // While there is more to read, continue reading and parsing
      while (jsonReader.hasNext()) {
        // Sets the object number to 0 when starting or resets the object number after completely
        // parsing one flight
        objectNumber = 0;
        // Reads the flight number
        flightNumber = jsonReader.nextName();
        jsonReader.beginObject();

        // While there exists objects that are not parsed, keep parsing them
        while (objectNumber < JsonConstants.FLIGHT_OBJECTS) {
          String name = jsonReader.nextName();

          // Depending on what the nextName is, it parses the next part accordingly
          switch (name) {
            case "DepartureDateTime":
              departDate = df.parse(jsonReader.nextString());
              break;
            case "ArrivalDateTime":
              arrivalDate = df.parse(jsonReader.nextString());
              break;
            case "Airline":
              airline = jsonReader.nextString();
              break;
            case "Origin":
              origin = jsonReader.nextString();
              break;
            case "Destination":
              destination = jsonReader.nextString();
              break;
            case "Price":
              price = jsonReader.nextDouble();
              break;
            case "NumSeats":
              numSeats = jsonReader.nextInt();
              isSetNumSeats = true;
              break;
            default: // Never used as it will always be a case from above.
              break;
          }
          objectNumber++;
        }
        jsonReader.endObject();

        // If everything is all parsed, make a flight and add it to the flight info list
        if (isParsed(flightNumber, departDate, arrivalDate, airline, origin, destination, price,
            isSetNumSeats)) {
          flight = new Flight(flightNumber, departDate, arrivalDate, airline, origin, destination,
              price, numSeats);
          FlightDatabase.addFlight(flight);
        }
      }
      // Closes the fileReader and jsonReader after everything is done
      fileReader.close();
      jsonReader.close();
    } catch (IOException | ParseException e) {
      e.getStackTrace();
    }
  }

  /**
   * Returns whether all the flight info is parsed or not.
   *
   * @param flightNumber the flight number of the flight
   * @param departDate the departure date of the flight
   * @param arrivalDate the arrival date of the flight
   * @param airline the airline for the flight
   * @param origin the origin of the flight
   * @param destination the destination of the flight
   * @param price the price of the flight
   * @return whether or not all the info is parsed
   */
  private static boolean isParsed(String flightNumber, Date departDate, Date arrivalDate,
      String airline, String origin, String destination, double price, boolean isSetNumSeats) {
    return flightNumber != null && departDate != null && arrivalDate != null && airline != null
        && origin != null && destination != null && price != 0.0 && isSetNumSeats != false;
  }

  /**
   * Returns whether or not all the user information is parsed or not.
   *
   * @param password the password of the user
   * @param isSetAdmin a boolean used to determine if the isAdmin is set yet
   * @param email the email of the user
   * @param lastName the last name of the user
   * @param firstName the first name of the user
   * @param address the address of the user
   * @param creditCardNumber the credit card number of the user
   * @param expiryDate the expiry date of the user's credit card
   * @param modifiedBookedItinerary whether or not the itinerary list is modified or not
   * @return whether or not all the info is parsed
   */
  private static boolean isParsed(String username, String password, boolean isSetAdmin,
      String email, String lastName, String firstName, String address, String creditCardNumber,
      Date expiryDate, boolean modifiedBookedItinerary) {
    return username != null && password != null && isSetAdmin != false && email != null
        && lastName != null && firstName != null && address != null && creditCardNumber != null
        && expiryDate != null && modifiedBookedItinerary != false;
  }
}
