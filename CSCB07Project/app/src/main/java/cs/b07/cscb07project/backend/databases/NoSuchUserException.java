package cs.b07.cscb07project.backend.databases;


/**
 * Exception for when no such user exists.
 */
public class NoSuchUserException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchUserException(String message) {
    super(message);
  }

  public NoSuchUserException() {
    super();
  }
}
