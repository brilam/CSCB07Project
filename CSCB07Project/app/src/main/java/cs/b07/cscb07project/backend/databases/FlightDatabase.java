package cs.b07.cscb07project.backend.databases;

import cs.b07.cscb07project.backend.constants.Constants.DataConstants;
import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.data.Flight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A database of Flights.
 *
 * @author Vijanthan
 */
public class FlightDatabase {
  // Maps the ID to a set of IDs
  private static Map<String, HashSet<String>> flightData = new HashMap<String, HashSet<String>>();
  // Maps the IDs to the corresponding flight
  private static Map<String, Flight> idMap = new HashMap<String, Flight>();

  /**
   * Returns a Set of all Flights in this Flight database.
   *
   * @return a Set of Flights in this Flight database
   */
  public static Set<Flight> getFlights() {
    HashSet<Flight> flights = new HashSet<Flight>();
    for (Flight flight : idMap.values()) {
      flights.add(flight);
    }
    return flights;
  }

  /**
   * Returns the Flight from this Flight database with the given ID.
   *
   * @param id the flight number of the Flight used as ID
   * @return a Flight from this database with the given ID
   * @throws NoSuchFlightException if there is no Flight with ID id in this Flight database
   */
  public static Flight getFlight(String id) throws NoSuchFlightException {
    // If id is in the idMap, get the corresponding flight object
    if (idMap.containsKey(id)) {
      return idMap.get(id);
    }
    throw new NoSuchFlightException();
  }

  /**
   * Returns a Set of paths that the given Flight can take.
   *
   * @param flight the Flight to find paths for
   * @return a Set of paths for the Flight
   * @throws NoSuchFlightException if the given Flight in not this Flight database
   */
  public static Set<Flight> getPaths(Flight flight) throws NoSuchFlightException {
    // Find the path IDs of the input flight
    String id = flight.getFlightNumber();
    if (!(flightData.containsKey(id))) {
      throw new NoSuchFlightException();
    }
    HashSet<String> pathIds = flightData.get(id);
    HashSet<Flight> paths = new HashSet<Flight>();
    // Add the flight objects represented by the path IDs to the list
    for (String pathId : pathIds) {
      paths.add(FlightDatabase.getFlight(pathId));
    }
    return paths;

  }

  /**
   * Returns a Set of paths that the Flight represented by the given ID can take.
   *
   * @param id the flight number of the Flight used as ID
   * @return a Set of paths for the Flight represented by the given id
   * @throws NoSuchFlightException if there is no Flight with ID id in this Flight database
   */
  public static Set<Flight> getPaths(String id) throws NoSuchFlightException {
    return getPaths(getFlight(id));
  }

  /**
   * Adds the flight to this Flight database.
   *
   * @param newFlight a flight to add to this database
   */
  public static void addFlight(Flight newFlight) {

    try {
      // Check if the Flight is valid and overwrite if it already exists
      if (FlightDatabase.validFlight(newFlight)) {
        String id = newFlight.getFlightNumber();
        if (FlightDatabase.containsFlight(newFlight)) {
          FlightDatabase.removeFlight(id);
        }
        flightData.put(id, new HashSet<String>());
        idMap.put(id, newFlight);
        String origin = newFlight.getOrigin();
        String destination = newFlight.getDestination();
        Date departure = newFlight.getDepartureDate();
        Date arrival = newFlight.getArrival();
        for (Flight flight : FlightDatabase.getFlights()) {
          String flightId = flight.getFlightNumber();
          // Checks if the input flight is path for this flight
          if ((!(flight.equals(newFlight))) && (flight.getDestination().equals(origin))) {
            long startTime = departure.getTime();
            long earliestTime = startTime - DataConstants.MAXIMUM_TIME_GAP;
            startTime -= DataConstants.MINIMUM_TIME_GAP;
            // Arrival time of the current Flight object
            long arrivalTime = flight.getArrival().getTime();
            // Add the input Flight as a path for the current Flight object if
            // the current Flight's arrival time is within 6 hours of the input Flight's
            // departure time
            if ((arrivalTime >= earliestTime) && (arrivalTime <= startTime)) {
              // Adds input Flight (ID) as a path to the current Flight object
              flightData.get(flightId).add(id);
            }
            // Checks if this Flight is a path for the input Flight
          } else if ((!(flight.equals(newFlight))) && (flight.getOrigin().equals(destination))) {
            long startTime = arrival.getTime();
            long latestTime = startTime + DataConstants.MAXIMUM_TIME_GAP;
            startTime += DataConstants.MINIMUM_TIME_GAP;
            // Departure time of the current Flight object
            long departureTime = flight.getDepartureDate().getTime();
            if ((departureTime <= latestTime) && (departureTime >= startTime)) {
              // Adds Flight (flight) as a path for the input Flight
              flightData.get(id).add(flightId);
            }
          }
        }
      }
    } catch (NoSuchFlightException e) {
      // Will not reach
      System.out.println(e.getMessage());
    }
  }

  /**
   * Returns True iff there does exists a flight in this database with the same flight number.
   *
   * @param inputFlight a flight to compare
   * @return a boolean of if the Flight exists in this database
   */
  public static boolean containsFlight(Flight inputFlight) {
    return idMap.containsKey(inputFlight.getFlightNumber());
  }

  /**
   * Returns True iff the Flight is a valid Flight. A valid flight does not have the same origin and
   * destination, and the arrival time of a valid flight will be after the departure time of it.
   *
   * @param inputFlight a flight to compare
   * @return a boolean of if the Flight is valid
   */
  public static boolean validFlight(Flight inputFlight) {
    return (!((inputFlight.getOrigin().equals(inputFlight.getDestination()))
        || (inputFlight.getArrival().before(inputFlight.getDepartureDate()))));
  }

  /**
   * Returns a String representation of flights with the input departure date, origin, and
   * destination. In the format (with each line being a different flight):
   * FlightNum,DepartureDate,ArrivalDate,Airline,Origin,Destination,Cost, NumSeats, TravelTime
   * FlightNum,DepartureDate,ArrivalDate,Airline,Origin,Destination,Cost, NumSeats, TravelTime
   *
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a Flight origin
   * @param destination a Flight destination
   * @return a String representation of flights
   */
  public static String searchForDisplay(String date, String origin, String destination) {
    String result = "";
    // Creates a new Dateformat with the only the date (no time included)
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);

    // Compares input departure date to every flight in this Flight database's departure date
    for (Flight flight : FlightDatabase.getFlights()) {
      String flightDate = df.format(flight.getDepartureDate());
      if ((flightDate.equals(date)) && (flight.getOrigin().equals(origin))
          && (flight.getDestination().equals(destination)) && (!(flight.isFullyBooked()))) {

        result += String.format("%s,%.2f,%s,%s/n", flight.getFlightData(), flight.getCost(),
            flight.getNumSeats(), flight.getTravelTime());
      }
    }
    return result;
  }

  /**
   * Returns a String representation of flights with the input departure date, origin, and
   * destination. In the format (with each line being a different flight):
   * FlightNum,DepartureDate,ArrivalDate,Airline,Origin,Destination,Cost
   * FlightNum,DepartureDate,ArrivalDate,Airline,Origin,Destination,Cost
   *
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a Flight origin
   * @param destination a Flight destination
   * @return a String representation of flights
   */
  public static String search(String date, String origin, String destination) {
    String result = "";
    // Creates a new Dateformat with the only the date (no time included)
    DateFormat df = new SimpleDateFormat(DateConstants.DATE_FORMAT);

    // Compares input departure date to every flight in this Flight database's departure date
    for (Flight flight : FlightDatabase.getFlights()) {
      String flightDate = df.format(flight.getDepartureDate());
      if ((flightDate.equals(date)) && (flight.getOrigin().equals(origin))
          && (flight.getDestination().equals(destination)) && (!(flight.isFullyBooked()))) {

        result += String.format("%s,%.2f%n", flight.getFlightData(), flight.getCost());
      }
    }
    return result;
  }

  /**
   * Removes the Flight in this database provided that the Flight does exist.
   *
   * @param id a flight ID
   * @throws NoSuchFlightException if flight is not in this database
   */
  public static void removeFlight(String id) throws NoSuchFlightException {
    if (flightData.containsKey(id)) {
      // Removes the Flight from the set of keys in
      flightData.remove(id);
      idMap.remove(id);
      // Removes the Flight from possible paths of the flights in this database
      for (String currId : flightData.keySet()) {
        HashSet<String> paths = flightData.get(currId);
        if (paths.contains(id)) {
          paths.remove(id);
        }
      }
    } else {
      throw new NoSuchFlightException();
    }
  }

  /**
   * Clears the saved contents of this Flight database.
   */
  public static void clear() {
    // Used for testing purposes
    flightData.clear();
    idMap.clear();
  }

  /**
   * Returns a String representation of this database with the format: Flight1 has paths to:
   * Flight2, Flight3.
   *
   * @return a String representation of this database
   */
  public static String getToString() {
    // Method to get a string representation used to view mapping.
    String result = "";
    for (String flightId : flightData.keySet()) {
      result += idMap.get(flightId) + " has paths to: ";
      for (String pathId : flightData.get(flightId)) {
        result += idMap.get(pathId) + ", ";
      }
      result = result.substring(0, result.length() - 2);
      result += "\n";
    }

    return result;
  }
}
