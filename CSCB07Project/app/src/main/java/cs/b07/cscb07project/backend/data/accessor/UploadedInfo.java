package cs.b07.cscb07project.backend.data.accessor;


import cs.b07.cscb07project.backend.constants.Constants.DataConstants;
import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.databases.UserDatabase;
import cs.b07.cscb07project.backend.users.Client;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;



/**
 * A class that accesses the uploaded information which are of the file format CSV, and it is able
 * to parse the information contained in the CSV files.
 *
 * @author Brian Lam
 */

public class UploadedInfo {
  /**
   * Parses the user information given a file name, and adds the users to the UserDatabase.
   *
   * @param fileName the file name of the file to be parsed
   * @throws FileNotFoundException when the file name specified cannot be found
   */
  public static void parseUserInfo(String fileName) throws FileNotFoundException {
    System.out.println("File name: " + fileName);
    // Creates a new Scanner instance which reads a file
    Scanner input = new Scanner(new File(fileName));

    // Sets a Date format which will be useful when parsing the expiry date of a credit card
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);

    // Reads the next line
    String nextLine = input.nextLine();
    String[] formattedLine = nextLine.split(",");

    // Initialize expiry date
    Date expiryDate = null;

    // Attempts to parse the expiry date string
    try {
      expiryDate = df.parse(formattedLine[DataConstants.EXPIRY_DATE_INDEX]);
    } catch (ParseException e) { // when date isn't formatted as required
      System.err.println("Error parsing the Strings as Date.");
    }

    // Creates a User and adds it to the billing info list
    User user = new Client(formattedLine[DataConstants.FIRST_NAME_INDEX],
        formattedLine[DataConstants.LAST_NAME_INDEX], formattedLine[DataConstants.EMAIL_INDEX],
        formattedLine[DataConstants.ADDRESS_INDEX], formattedLine[DataConstants.CREDIT_CARD_INDEX],
        expiryDate);
    UserDatabase.addUser(user);

    // While there is more input, keep reading the next line
    while (input.hasNext()) {
      nextLine = input.nextLine();
      formattedLine = nextLine.split(",");

      // Attempts to parse the expiry date string
      try {
        expiryDate = df.parse(formattedLine[DataConstants.EXPIRY_DATE_INDEX]);
      } catch (ParseException e) { // when date isn't formatted as required
        System.err.println("Error parsing the Strings as Date.");
      }

      // Creates a User and adds it to the billing info list
      user = new Client(formattedLine[DataConstants.FIRST_NAME_INDEX],
          formattedLine[DataConstants.LAST_NAME_INDEX], formattedLine[DataConstants.EMAIL_INDEX],
          formattedLine[DataConstants.ADDRESS_INDEX],
          formattedLine[DataConstants.CREDIT_CARD_INDEX], expiryDate);
      UserDatabase.addUser(user);
    }
    input.close();
  }

  /**
   * Parses the flight information given a file name, and adds the flights to the FlightDatabase.
   *
   * @param path the path of the file to be parsed
   * @throws FileNotFoundException when the file name specified cannot be found
   */
  public static void parseFlightInfo(String path) throws FileNotFoundException {
    // Creates a new Scanner instance which reads a file
    Scanner input = new Scanner(new File(path));

    // Sets a Date format which will be useful when parsing the flight dates
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_TIME_FORMAT);

    // Reads the next line
    String nextLine = input.nextLine();
    String[] formattedLine = nextLine.split(",");

    // Initialize depart date and arrival date variables
    Date departDate = null;
    Date arrivalDate = null;

    // Attempts to parse the departure date and arrival date strings
    try {
      departDate = df.parse(formattedLine[DataConstants.DEPARTURE_DATE_INDEX]);
      arrivalDate = df.parse(formattedLine[DataConstants.ARRIVAL_DATE_INDEX]);
    } catch (ParseException e) { // when date isn't formatted as required
      System.err.println("Error parsing the Strings as Date.");
    }

    // Creates a Flight and adds it to the flight info list
    Flight flight = new Flight(formattedLine[DataConstants.FLIGHT_NUMBER_INDEX], departDate,
        arrivalDate, formattedLine[DataConstants.AIRLINE_INDEX],
        formattedLine[DataConstants.ORIGIN_INDEX], formattedLine[DataConstants.DESTINATION_INDEX],
        Double.parseDouble(formattedLine[DataConstants.PRICE_INDEX]),
        Integer.parseInt(formattedLine[DataConstants.NUM_SEATS_INDEX]));
    FlightDatabase.addFlight(flight);

    // While there is more input, keep reading the next line
    while (input.hasNext()) {
      nextLine = input.nextLine();
      formattedLine = nextLine.split(",");

      // Attempts to parse the departure date and arrival date strings
      try {
        departDate = df.parse(formattedLine[DataConstants.DEPARTURE_DATE_INDEX]);
        arrivalDate = df.parse(formattedLine[DataConstants.ARRIVAL_DATE_INDEX]);
      } catch (ParseException e) { // when date isn't formatted as required
        System.err.println("Error parsing the Strings as Date.");
      }

      // Creates a Flight and adds it to the flight info list
      flight = new Flight(formattedLine[DataConstants.FLIGHT_NUMBER_INDEX], departDate, arrivalDate,
          formattedLine[DataConstants.AIRLINE_INDEX], formattedLine[DataConstants.ORIGIN_INDEX],
          formattedLine[DataConstants.DESTINATION_INDEX],
          Double.parseDouble(formattedLine[DataConstants.PRICE_INDEX]),
          Integer.parseInt(formattedLine[DataConstants.NUM_SEATS_INDEX]));
      FlightDatabase.addFlight(flight);
    }
    input.close();
  }
}
