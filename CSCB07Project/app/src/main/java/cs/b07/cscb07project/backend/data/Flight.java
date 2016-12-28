package cs.b07.cscb07project.backend.data;

import cs.b07.cscb07project.backend.constants.Constants.DateConstants;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/** A class representing a flight. */
public class Flight implements Serializable {
  private static final long serialVersionUID = 5603057775927034382L;
  // Initializing all the variables that a normal
  // flight ticket would require
  private String flightNumber;
  private Date departure; // YYYY-MM-DD HH:MM format
  private Date arrival; // YYYY-MM-DD HH:MM format
  private String airline;
  private String origin;
  private String destination;
  private String travelTime; // HH:MM format
  private double cost;
  private int numSeats;


  /**
   * Initializes a flight.
   * 
   * @param flightNumber a flight number
   * @param departure a departure time (in the format YYYY-MM-DD HH:MM)
   * @param arrival an arrival time (in the format YYYY-MM-DD HH:MM)
   * @param airline a flight airline
   * @param origin a flight origin
   * @param destination a flight destination
   * @param cost the cost of the flight
   */
  public Flight(String flightNumber, Date departure, Date arrival, String airline, String origin,
      String destination, double cost, int numSeats) {
    setFlightNumber(flightNumber);
    setDeparture(departure);
    setArrival(arrival);
    setAirline(airline);
    setOrigin(origin);
    setDestination(destination);
    setTravelTime(departure, arrival);
    setCost(cost);
    setNumSeats(numSeats);
  }

  /**
   * Returns the flight number of this flight.
   *
   * @return the number associated with this flight
   */
  public String getFlightNumber() {
    return flightNumber;
  }

  /**
   * Sets a new value to flightNumber.
   *
   * @param flightNumber a flight number to set for this flight
   */
  private void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  /**
   * Sets the departure time of this flight to the date given.
   * 
   * @param departure a departure time for this flight (in the format YYYY-MM-DD HH:MM)
   */
  private void setDeparture(Date departure) {
    this.departure = departure;
  }

  /**
   * Returns the time of departure of this flight.
   * 
   * @return departure a departure date and time for this flight (in the format YYYY-MM-DD HH:MM)
   */
  public Date getDepartureDate() {
    return departure;
  }

  /**
   * Returns the String representation of of the departure day.
   * 
   * @return a departure date for this flight (in the format YYYY-MM-DD HH:mm)
   */
  public String getStringDepartureDate() {
    DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_TIME_FORMAT);
    return dateFormat.format(departure);
  }

  /**
   * Returns the arrival time.
   * 
   * @return arrival an arrival time for this flight (in the format YYYY-MM-DD HH:MM)
   */
  public Date getArrival() {
    return arrival;
  }

  /**
   * Sets the arrival time to time given.
   *
   * @param arrival an arrival time for this flight (in the format YYYY-MM-DD HH:MM)
   */
  private void setArrival(Date arrival) {
    this.arrival = arrival;
  }

  /**
   * Returns the String representation of of the arrival day.
   *
   * @return an arrival date for this flight (in the format YYYY-MM-DD HH:mm)
   */
  public String getStringArrivalDate() {
    DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_TIME_FORMAT);
    return dateFormat.format(arrival);
  }

  /**
   * Returns the airline of the airline.
   *
   * @return the flight airline associated with this flight.
   */
  public String getAirline() {
    return airline;
  }

  /**
   * Sets the airline of this flight to the one given.
   *
   * @param airline a flight airline
   */
  private void setAirline(String airline) {
    this.airline = airline;
  }

  /**
   * Returns the origin of the flight.
   *
   * @return the origin of the flight
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Sets the origin of the flight with the one given.
   *
   * @param origin a flight origin
   */
  private void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * Returns the destination of this flight.
   *
   * @return the destination of this flight
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Sets the destination of the flight with the destination given.
   *
   * @param destination a flight destination
   */
  private void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Sets the travel time of the flight.
   * 
   * @param departure the date and time of departure in format (YYYY-MM-DD HH:MM)
   * @param arrival the date and time of arrival in format (YYYY-MM-DD HH:MM)
   */
  private void setTravelTime(Date departure, Date arrival) {
    long gap = arrival.getTime() - departure.getTime();
    long minutes =
        (gap / (DateConstants.MINUTES_TO_SECONDS * DateConstants.SECONDS_TO_MILLISECONDS))
            % DateConstants.HOURS_TO_MINUTES;
    long hours = (gap / (DateConstants.HOURS_TO_MINUTES * DateConstants.MINUTES_TO_SECONDS
        * DateConstants.SECONDS_TO_MILLISECONDS));
    travelTime = String.format("%02d:%02d", hours, minutes);
  }

  /**
   * Returns the travel time.
   * 
   * @return the travel time of the flight (in the format HH:MM)
   */
  public String getTravelTime() {
    return travelTime;
  }

  /**
   * Returns the cost of this flight.
   *
   * @return the cost of this flight
   */
  public double getCost() {
    return cost;
  }

  /**
   * Changes the cost of the flight.
   *
   * @param cost a flight cost
   */
  private void setCost(double cost) {
    this.cost = cost;
  }

  /**
   * Takes in a flight and compares the flight numbers of both flights.
   * 
   * @param flight a flight to compare with this flight
   * @return a boolean indicating if the two flight numbers are equal
   */

  public boolean equals(Flight flight) {
    return (flightNumber.equals(flight.getFlightNumber()));
  }

  /**
   * Compares the origin and the departure date of this flight and a given flight.
   * 
   * @param flight a flight to compare with this flight
   * @return a boolean indicating if the two flights have equal origin and equal departure date
   */
  public boolean compareTo(Flight flight) {
    return (flight.getOrigin().equals(origin) && flight.getDepartureDate().equals(departure));
  }

  /**
   * Returns a boolean indicating if flight departs from origin given, at the date given, and can
   * successfully booked.
   * 
   * @param date the date of departure
   * @param origin the origin of flight
   * @return a boolean indicating if flight meets all given requirements, and can be booked
   */
  public boolean isValidStartingFlight(String date, String origin) {
    DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_FORMAT);

    return ((this.getOrigin().equals(origin))
        && ((dateFormat.format(getDepartureDate())).equals(date)) && (!(this.isFullyBooked())));
  }

  /**
   * Returns the string representation of the flight data.
   * 
   * @return the string representation of the flight data in the following format: flight number,
   *         departure date, arrival date, airline, origin, destination
   */
  public String getFlightData() {
    // Returns the flight Data as a String
    return String.format("%s,%s,%s,%s,%s,%s", flightNumber, getStringDepartureDate(),
        getStringArrivalDate(), getAirline(), getOrigin(), getDestination());
  }

  /**
   * Returns the string representation of the flight data.
   * 
   * @return the string representation of the flight data in the following format: Flight number:
   *         flight number Origin: flight origin Departure: flight departure date (YYYY-MM-DD HH:mm)
   *         Destination: flight destination Arrival: flight arrival date (YYYY-MM-DD HH:mm)
   *         Airline: flight airline
   */
  public String getFlightAppData() {
    // Returns the flight Data as a String
    return String.format(
        "Flight number: %s%n" + "Origin: %s%n" + "Departure: %s%n" + "Destination: %s%n"
            + "Arrival: %s%n" + "Airline: %s",
        flightNumber, getOrigin(), getStringDepartureDate(), getDestination(),
        getStringArrivalDate(), getAirline());
  }

  /**
   * Returns the String representation of this Flight in the format: (departure date),origin to
   * destination,departureDate-Arrival HH:MM-HH:MM.
   * 
   * @return a String representation of the Flight
   */
  @Override
  public String toString() {
    DateFormat df = new SimpleDateFormat("HH:mm");
    return "(" + this.getStringDepartureDate() + ")" + this.getOrigin() + " to "
        + this.getDestination() + ", " + df.format(this.getDepartureDate()) + " - "
        + df.format(this.getArrival());
  }

  /**
   * Returns whether or not the flight is fully booked (ie. no more seats).
   * 
   * @return true if the flight is fully booked, and false otherwise
   */
  public boolean isFullyBooked() {
    return (getNumSeats() == 0);
  }

  /**
   * Books a seat for this flight.
   */
  public void book() {
    if (!(isFullyBooked())) {
      setNumSeats(getNumSeats() - 1);
    }
  }

  /**
   * Returns the number of seats available on a flight.
   * 
   * @return the number of seats available on a flight
   */
  public int getNumSeats() {
    return numSeats;
  }

  /**
   * Sets the numbers of seats available on a flight.
   * 
   * @param numSeats the number of seats available on a flight
   */
  public void setNumSeats(int numSeats) {
    this.numSeats = numSeats;
  }

}
