package cs.b07.cscb07project.backend.data;

import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.databases.FlightDatabase;
import cs.b07.cscb07project.backend.databases.NoSuchFlightException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/** A class representing a Itinerary. */
public class Itinerary implements Serializable {
  private static final long serialVersionUID = -1774235701390778379L;
  // Initializing all the variables needed as well as
  // the list of flights
  private String itineraryId;
  private List<String> listOfFlights = new ArrayList<String>();
  private double costOfFlights;
  private String departureLocation;
  private String finalDestination;
  private String totalTime;
  private Date departureDate = new Date(); // YYYY-MM-DD HH:MM format
  private Date arrivalDate = new Date(); // YYYY-MM-DD HH:MM format

  /**
   * Initializes an Itinerary with one flight.
   *
   * @param flightNumber the flight in this itinerary.
   * @throws NoSuchFlightException when the flight given doesn't exist
   */
  public Itinerary(String flightNumber) throws NoSuchFlightException {
    // adding the flight number to the list of flights initialized
    listOfFlights.add(flightNumber);
    itineraryId = flightNumber;
    Flight flight = FlightDatabase.getFlight(flightNumber);
    updateCost(flight.getCost());
    departureLocation = flight.getOrigin();
    setFinalDestination(flight.getDestination());
    setTotalTime(flight.getTravelTime());
    departureDate = flight.getDepartureDate();
    arrivalDate = flight.getArrival();
  }

  /**
   * Initializes an Itinerary with a sequence of flights.
   *
   * @param flightNumbers a list of flights numbers making this Itinerary
   * @throws NoSuchFlightException when any of the flights given do not exist in the Flight Database
   */
  public Itinerary(List<String> flightNumbers) throws NoSuchFlightException {

    // Adds the first Flight in Itinerary
    Flight firstFlight = FlightDatabase.getFlight(flightNumbers.get(0));
    departureLocation = firstFlight.getOrigin();
    departureDate = firstFlight.getDepartureDate();
    itineraryId = itineraryId + firstFlight.getFlightNumber();
    this.addFlight(firstFlight.getFlightNumber());

    // Adds the rest of flights in Itinerary
    for (int num = 1; num < flightNumbers.size(); num++) {
      String flightNum = flightNumbers.get(num);
      this.addFlight(flightNum);
    }

    setTotalTime(findTimeGap(this.getDepartureDate(), this.getArrivalDate()));
  }

  /**
   * Updates the total cost of this Itinerary.
   * 
   * @param cost the cost to add on to current Itinerary
   */
  private void updateCost(double cost) {
    costOfFlights += cost;
  }

  /**
   * Returns the total cost of this Itinerary.
   * 
   * @return the total cost of this Itinerary
   */
  public double getCost() {
    return costOfFlights;
  }

  /**
   * Returns the departure location of this Itinerary.
   * 
   * @return the departure location of this Itinerary
   */
  public String getDepartureLocation() {
    return departureLocation;
  }

  /**
   * Returns the departure date of this Itinerary.
   * 
   * @return the departure date of this Itinerary
   */
  public Date getDepartureDate() {
    return departureDate;
  }

  /**
   * Returns the final destination of this Itinerary.
   *
   * @return the final destination of this Itinerary
   */
  public String getFinalDestination() {
    return finalDestination;
  }

  /**
   * Sets the final destination location of this Itinerary.
   *
   * @param finalDestination the final destination of this Itinerary
   */
  private void setFinalDestination(String finalDestination) {
    this.finalDestination = finalDestination;
  }

  /**
   * Returns the date of arrival for this Itinerary.
   *
   * @return the date of arrival for this Itinerary
   */
  public Date getArrivalDate() {
    return arrivalDate;
  }

  /**
   * Sets the final arrival date of this Itinerary.
   *
   * @param arrivalDate final arrival date of this Itinerary
   */
  private void setArrivalDate(Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  /**
   * Returns the sequence of flights contained in this Itinerary.
   * 
   * @return the list of flights in this Itinerary
   */
  public List<String> sequenceOfFlights() {
    return listOfFlights;
  }

  /**
   * Returns the time gap in between the given departure and arrival of this Itinerary.
   *
   * @param departure the date and time of departure in format (YYYY-MM-DD HH:MM)
   * @param arrival the date and time of arrival in format (YYYY-MM-DD HH:MM)
   * @return the total time between given arrival date and given date in the format (HH:MM)
   */
  private String findTimeGap(Date departure, Date arrival) {
    long gap = arrival.getTime() - departure.getTime();
    long minutes =
        (gap / (DateConstants.MINUTES_TO_SECONDS * DateConstants.SECONDS_TO_MILLISECONDS))
            % DateConstants.HOURS_TO_MINUTES;
    long hours = (gap / (DateConstants.HOURS_TO_MINUTES * DateConstants.MINUTES_TO_SECONDS
        * DateConstants.SECONDS_TO_MILLISECONDS));
    return String.format("%02d:%02d", hours, minutes);
  }

  /**
   * Returning the total time it takes from origin to the destination (the total travel time for
   * this itinerary).
   *
   * @return total duration of the Itinerary (format HH:mm)
   */
  public String getTotalTime() {
    return totalTime;
  }

  /**
   * Sets the total duration of this Itinerary.
   *
   * @param time the duration of this Itinerary
   */
  private void setTotalTime(String time) {
    this.totalTime = time;
  }

  /**
   * Adds given flight to this Itinerary.
   * 
   * @param flightNumber the flight's flight number to be added to the itinerary
   */
  private void addFlight(String flightNumber) {

    Flight flight;
    try {
      flight = FlightDatabase.getFlight(flightNumber);

      // adding to the list of flights
      listOfFlights.add(flightNumber);
      // adding to the itineraryID list
      itineraryId += "," + flightNumber;

      updateCost(flight.getCost());
      setFinalDestination(flight.getDestination());
      setArrivalDate(flight.getArrival());
    } catch (NoSuchFlightException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the String representation of all the flight ID's in this itinerary separated by commas.
   * 
   * @return string representation of the Itinerary
   */
  public String getItineraryId() {
    return itineraryId;
  }


  /**
   * Returns whether or not this itinerary (any of the flights inside it) is fully booked (ie. no
   * more seats).
   * 
   * @return boolean indicating if this itinerary is fully booked
   */
  public boolean isFullyBooked() {
    for (String flightId : this.sequenceOfFlights()) {
      try {
        if (!(FlightDatabase.getFlight(flightId).isFullyBooked())) {
          return false;
        }
      } catch (NoSuchFlightException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  /**
   * Books all flights in this Itinerary.
   * 
   * @return boolean indicating if booking was successful
   */
  public boolean bookItinerary() {
    for (String flightId : this.sequenceOfFlights()) {
      try {
        FlightDatabase.getFlight(flightId).book();
      } catch (NoSuchFlightException e) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a string representation of this Itinerary.
   * 
   * @return this itinerary's data including every flight in the format: Flight
   *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   * @throws NoSuchFlightException when any flight inside list doesn't exist
   */
  public String getItineraryData() throws NoSuchFlightException {
    String resultData = "";
    for (String flightNumber : listOfFlights) {
      resultData += FlightDatabase.getFlight(flightNumber).getFlightData() + "\n";
    }
    return resultData.substring(0, resultData.length() - 1);
  }

  /**
   * Returns a string representation of this Itinerary.
   * 
   * @return this itinerary's data including every flight in the format: Flight
   *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
   * @throws NoSuchFlightException when any flight inside list doesn't exist
   */
  public String getItineraryAppData() throws NoSuchFlightException {
    String resultData = "";
    for (String flightNumber : listOfFlights) {
      resultData += FlightDatabase.getFlight(flightNumber).getFlightAppData() + "\n\n";
    }
    return resultData.substring(0, resultData.length() - 1);
  }


}
