package cs.b07.cscb07project.backend.databases;


import cs.b07.cscb07project.backend.data.Flight;
import cs.b07.cscb07project.backend.data.Itinerary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;



/**
 * Class representing a database of all Itineraries.
 * 
 * @author Shadman
 */
public class ItineraryDatabase {

  /**
   * Returns all itineraries that depart from origin on the given date and arrive at destination. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. If there are more than 6 hours or less than 30 minutes between the
   * arrival of F1 and the departure of F2, then we do not consider this sequence for a possible
   * itinerary.
   * 
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a flight original
   * @param destination a flight destination
   * @return list of valid itineraries that depart from origin on the date and end at destination
   */
  public static ArrayList<Itinerary> getItineraries(String date, String origin,
      String destination) {
    return ItineraryDatabase.searchItineraries(date, origin, destination);
  }


  /**
   * Returns all itineraries sorted according to total itinerary cost, in non-decreasing order that
   * depart from origin on the given date and arrive at destination. If an itinerary contains two
   * consecutive flights F1 and F2, then the destination of F1 should match the origin of F2. If
   * there are more than 6 hours or less than 30 minutes between the arrival of F1 and the departure
   * of F2, then we do not consider this sequence for a possible itinerary.
   * 
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a flight original
   * @param destination a flight destination
   * @return list of valid itineraries that depart from origin on the date and end at destination
   *         (sorted in non-decreasing order by total itinerary cost)
   *
   */
  public static ArrayList<Itinerary> getItinerariesSortedByCost(String date, String origin,
      String destination) {
    ArrayList<Itinerary> itineraries;
    itineraries = ItineraryDatabase.getItineraries(date, origin, destination);
    // Sort itineraries by cost
    Collections.sort(itineraries, new CostComparator());
    // get and return the string representation of all itineraries
    return itineraries;
  }


  /**
   * Returns all itineraries sorted according to itinerary travel time, in non-decreasing order that
   * depart from origin on the given date and arrive at destination. If an itinerary contains two
   * consecutive flights F1 and F2, then the destination of F1 should match the origin of F2. If
   * there are more than 6 hours or less than 30 minutes between the arrival of F1 and the departure
   * of F2, then we do not consider this sequence for a possible itinerary.
   * 
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a flight origin
   * @param destination a flight destination
   * @return list of valid itineraries that depart from origin on the date and end at destination
   *         (sorted in non-decreasing order of travel itinerary travel time)
   *
   */
  public static ArrayList<Itinerary> getItinerariesSortedByTime(String date, String origin,
      String destination) {
    ArrayList<Itinerary> itineraries;
    itineraries = ItineraryDatabase.getItineraries(date, origin, destination);

    // Sort itineraries by time
    Collections.sort(itineraries, new TimeComparator());
    // get and return the string representation of all itineraries
    return itineraries;
  }

  /**
   * Returns a string representation of the given list of itineraries.
   * 
   * @param itineraries a list of itineraries
   * @return the string representation of the given list of itineraries
   */
  public static String getStringRep(ArrayList<Itinerary> itineraries) {
    String result = "";
    for (Itinerary itinerary : itineraries) {

      try {
        result += String.format("%s%n%.2f%n%s%n", itinerary.getItineraryData(), itinerary.getCost(),
            itinerary.getTotalTime());
      } catch (NoSuchFlightException e) {
        continue;
      }
    }
    // Return the full string representation
    return result;
  }


  /////////////////////// Private Methods///////////////////////

  /**
   * Returns all itineraries that depart from origin and arrive at destination on the given date. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. To simplify our task, if there are more than 6 hours between the
   * arrival of F1 and the departure of F2, then we do not consider this sequence for a possible
   * itinerary (we judge that the stopover is too long).
   * 
   * @param date a departure date (in the format YYYY-MM-DD)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries that depart from origin and arrive at destination on the given date with
   *         stopovers at or under 6 hours.
   */
  private static ArrayList<Itinerary> searchItineraries(String date, String origin,
      String destination) {
    Set<Flight> allFlights = FlightDatabase.getFlights();
    ArrayList<Itinerary> validItineraries = new ArrayList<Itinerary>();

    // Go through every flight
    for (Flight myFlight : allFlights) {
      // flight is the appropriate start and has capacity
      if (myFlight.isValidStartingFlight(date, origin)) {
        validItineraries
            .addAll(ItineraryDatabase.getValidItineraries(myFlight, origin, destination));
      }
    }
    return validItineraries;
  }

  /**
   * Returns all itineraries that depart from myFlight's origin and end at destination.
   * 
   * @param destination the final destination of the itinerary needed
   * @param origin the origin of the itinerary
   * @param myFlight the initial flight of a possible itinerary
   * @return list of valid itineraries that depart from myFlight's origin and end at destination
   */
  private static ArrayList<Itinerary> getValidItineraries(Flight myFlight, String origin,
      String destination) {
    Set<Flight> connectingFlights;
    ArrayList<Itinerary> result = new ArrayList<>();

    try {
      connectingFlights = FlightDatabase.getPaths(myFlight);

      // Itinerary of single flight
      if (myFlight.getDestination().equals(destination) && (!(myFlight.isFullyBooked()))) {
        result.add(new Itinerary(myFlight.getFlightNumber()));
      } else {

        // Go through every connecting flight
        for (Flight nextFlight : connectingFlights) {
          // Add flight and its connection if its not fully booked
          ArrayList<String> flightPath = new ArrayList<>();
          ArrayList<String> alreadyVisited = new ArrayList<>();
          if (!(nextFlight.isFullyBooked())) {
            flightPath.add(myFlight.getFlightNumber());
            flightPath.add(nextFlight.getFlightNumber());
            alreadyVisited.add(origin);
            alreadyVisited.add(nextFlight.getOrigin());

            // Add all possible itineraries from
            // connecting flight to destination
            ItineraryDatabase.addValidItineraries(destination, result, flightPath, nextFlight,
                alreadyVisited);
          }
        }
      }
    } catch (NoSuchFlightException e) {
      return result;
    }
    return result;
  }

  /**
   * Adds Itineraries to result if the given flight path is valid.
   * 
   * @param destination the end location of the flight path
   * @param result a list of valid itineraries
   * @param flightPath the list of possible flights
   * @param myFlight the next flight to check in the flight path
   * @param alreadyVisited a list of places to avoid
   * @throws NoSuchFlightException when the next flight can't be found
   */
  private static void addValidItineraries(String destination, ArrayList<Itinerary> result,
      ArrayList<String> flightPath, Flight myFlight, ArrayList<String> alreadyVisited)
          throws NoSuchFlightException {

    // Base case where flightPath is already valid
    if ((myFlight.getDestination().equals(destination) && (!(myFlight.isFullyBooked())))) {
      result.add(new Itinerary(flightPath));
    } else {

      // Go through every valid connection and recursively
      // add and check routes
      Set<Flight> connectingFlights = FlightDatabase.getPaths(myFlight);
      if (!connectingFlights.isEmpty()) {
        for (Flight nextFlight : connectingFlights) {
          if (!(alreadyVisited.contains(nextFlight.getOrigin()))
              && (!(nextFlight.isFullyBooked()))) {

            // Add onto data for every valid neighboring flight
            ArrayList<String> updatedFlightPath = new ArrayList<>(flightPath);
            ArrayList<String> alreadyVisitedPath = new ArrayList<>(alreadyVisited);
            updatedFlightPath.add(nextFlight.getFlightNumber());
            alreadyVisitedPath.add(nextFlight.getOrigin());

            ItineraryDatabase.addValidItineraries(destination, result, updatedFlightPath,
                nextFlight, alreadyVisitedPath);
          }
        }
      }
    }
  }
}
