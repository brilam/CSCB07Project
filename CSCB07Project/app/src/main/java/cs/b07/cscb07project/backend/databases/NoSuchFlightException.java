package cs.b07.cscb07project.backend.databases;

/**
 * Exception for when no such flight exists.
 */
public class NoSuchFlightException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchFlightException(String message) {
    super(message);
  }

  public NoSuchFlightException() {
    super();
  }

  public String getMessage() {
    return "No such Flight was found in database.";
  }
}


