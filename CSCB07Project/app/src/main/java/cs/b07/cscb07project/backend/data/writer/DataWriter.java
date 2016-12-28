package cs.b07.cscb07project.backend.data.writer;

import android.util.JsonWriter;

import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.constants.Constants.JsonConstants;
import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.users.Admin;
import cs.b07.cscb07project.backend.users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;


/**
 * A class used for writing data to JSON files.
 *
 * @author Brian
 */

public class DataWriter {
  /**
   * Creates a JSON file containing user information.
   *
   * @param users the list of users to be written to a file
   * @param path the path of which information is stored
   */
  public static void createUserInfo(Set<User> users, File path) {
    // Tries to create a file
    try {
      // If a file exists, delete it
      if (path.exists()) {
        path.delete();
      }
      // Create a new file
      path.createNewFile();
    } catch (IOException e) { // Prints out the error message if there is a problem making the file
      System.out.println(e.getMessage());
    }

    // Initialize an output stream
    OutputStream out = null;

    // Tries to set the OutputStream
    try {
      out = new FileOutputStream(path);
    } catch (FileNotFoundException e) { // prints the error message and exits function
      System.out.println(e.getMessage());
      return;
    }

    // Creates an instance of JsonWriter which will be used for writing to JSON file
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));

    // Tries to write all the information to the JSON file
    try {

      writer.beginObject();
      writer.setIndent(JsonConstants.INDENT);

      for (User user : users) {
        writer.name(user.getEmail());
        writer.beginObject();
        writer.name("Username").value(user.getUsername());
        writer.name("Password").value(user.getPassword());

        if (user instanceof Admin) {
          writer.name("IsAdmin").value(true);
        } else {
          writer.name("IsAdmin").value(false);
        }

        writer.name("LastName").value(user.getLastName());

        writer.name("FirstName").value(user.getFirstName());
        writer.name("Address").value(user.getAddress());
        writer.name("CreditCardNumber").value(user.getCreditInfo());
        writer.name("ExpiryDate").value(user.getExpiryDate());
        writer.name("BookedItinerary").beginArray();
        for (String bookedItinerary : user.getBookedItinerary()) {
          writer.value(bookedItinerary);
        }
        writer.endArray();
        writer.endObject();
      }
      writer.endObject();
      // Closes readers
      writer.close();
      out.close();
    } catch (IOException e) { // Prints an IOException message if the file can't be accessed
      System.out.println(e.getMessage());
    }
  }

  /**
   * Creates a JSON file containing flight information.
   *
   * @param flights the flights to be written to the file
   * @param path the path of which information is stored
   */
  public static void createFlightInfo(Set<Flight> flights, File path) {

    // Tries to create a file
    try {
      // If a file exists, delete it
      if (path.exists()) {
        path.delete();
      }
      // Create a new file
      path.createNewFile();
    } catch (IOException e) { // Prints out the error message if there is a problem making the file
      System.out.println(e.getMessage());
    }

    // Initialize an output stream
    OutputStream out = null;

    // Tries to set the OutputStream
    try {
      out = new FileOutputStream(path);
    } catch (FileNotFoundException e) { // prints the error message and exits function
      System.out.println(e.getMessage());
      return;
    }

    // Creates an instance of JsonWriter which will be used for writing to JSON file
    JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
    // Creates a DateFormat with the flight date format
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_TIME_FORMAT);

    // Tries to write all the information to the JSON file
    try {

      writer.beginObject();
      writer.setIndent(JsonConstants.INDENT);

      for (Flight flight : flights) {
        writer.name(flight.getFlightNumber());

        writer.beginObject();
        String departDate = df.format(flight.getDepartureDate());
        writer.name("DepartureDateTime").value(departDate);
        String arrivalDate = df.format(flight.getArrival());
        writer.name("ArrivalDateTime").value(arrivalDate);
        writer.name("Airline").value(flight.getAirline());
        writer.name("Origin").value(flight.getOrigin());
        writer.name("Destination").value(flight.getDestination());
        writer.name("Price").value(flight.getCost());
        writer.name("NumSeats").value(flight.getNumSeats());

        writer.endObject();
      }
      writer.endObject();
      // Closes readers
      writer.close();
      out.close();
    } catch (IOException e) { // Prints an IOException message if the file can't be accessed
      System.out.println(e.getMessage());
    }
  }


}
