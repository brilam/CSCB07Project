package cs.b07.cscb07project.backend.constants;

/**
 * A class containing inner classes with constants for various usages like data, date, JSON, etc.
 */
public class Constants {
  /**
   * A class that contains only constants related to the data package.
   */
  public class DataConstants {
    // ----- Constants for personal info indices -----
    /** The index for first name when parsing a CSV file. */
    public static final int FIRST_NAME_INDEX = 0;
    /** The index for last name when parsing a CSV file. */
    public static final int LAST_NAME_INDEX = 1;
    /** The index for email when parsing a CSV file. */
    public static final int EMAIL_INDEX = 2;
    /** The index for address when parsing a CSV file. */
    public static final int ADDRESS_INDEX = 3;
    /** The index for credit card when parsing a CSV file. */
    public static final int CREDIT_CARD_INDEX = 4;
    /** The index for expiry date when parsing a CSV file. */
    public static final int EXPIRY_DATE_INDEX = 5;

    // ----- Constants for flight info indices -----
    /** The index for flight number when parsing a CSV file. */
    public static final int FLIGHT_NUMBER_INDEX = 0;
    /** The index for departure date when parsing a CSV file. */
    public static final int DEPARTURE_DATE_INDEX = 1;
    /** The index for arrival date when parsing a CSV file. */
    public static final int ARRIVAL_DATE_INDEX = 2;
    /** The index for airline when parsing a CSV file. */
    public static final int AIRLINE_INDEX = 3;
    /** The index for origin when parsing a CSV file. */
    public static final int ORIGIN_INDEX = 4;
    /** The index for destination when parsing a CSV file. */
    public static final int DESTINATION_INDEX = 5;
    /** The index for price when parsing a CSV file. */
    public static final int PRICE_INDEX = 6;
    /** The index for the number of seats when parsing a CSV file. */
    public static final int NUM_SEATS_INDEX = 7;
    /** The index for the travel time when parsing a CSV file. */
    public static final int TRAVEL_TIME_INDEX = 8;

    // -- Constants for maximum/minimum time gap in milliseconds between flights in an Itinerary --
    /** The maximum time gap allowed from one flight to the next. */
    public static final int MAXIMUM_TIME_GAP = 21600000;
    /** The minimum time gap allowed from one flight to the next. */
    public static final int MINIMUM_TIME_GAP = 1800000;

    // ----- Constants for Comparator -----
    /** The value returned when greater than value. */
    public static final int GREATER_THAN_VALUE = 1;
    /** The value returned when equal to value. */
    public static final int EQUAL_VALUE = 0;
    /** The value returned when less than value. */
    public static final int LESS_THAN_VALUE = -1;
  }

  /**
   * A class that represents only constants related to date.
   */
  public class DateConstants {
    /** Constant for Flight date format. */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    /** Expiry Date for Credit Card Format. */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /** The number of minutes in an hour. */
    public static final int HOURS_TO_MINUTES = 60;
    /** The number of seconds in an minute. */
    public static final int MINUTES_TO_SECONDS = 60;
    /** The number of milliseconds in a second. */
    public static final int SECONDS_TO_MILLISECONDS = 1000;
    /** The index for hour. */
    public static final int HOUR_INDEX = 0;
    /** The index for minute. */
    public static final int MINUTE_INDEX = 1;
  }

  /**
   * A class that represents only constants related to JSON files.
   */
  public class JsonConstants {
    /** The number of objects in a user info JSON file. */
    public static final int USER_OBJECTS = 9;
    /** The number of objects in a flight info JSON file. */
    public static final int FLIGHT_OBJECTS = 7;
    /** Used for indent characters. */
    public static final String INDENT = "  ";
  }

  /**
   * A class that represents only file names (uploaded files, and loaded files).
   */
  public class FileNameConstants {
    /** The file name for user information. */
    public static final String SAVED_USER_INFO_FILE = "user_info.json";
    /** The file name for flight information. */
    public static final String FLIGHT_INFO_FILE = "flight_info.json";
    /** The user data directory. */
    public static final String USER_DATA_DIR = "userdata";
    /** The upload data directory. */
    public static final String UPLOAD_DATA_DIR = "upload";
  }

  /**
   * A class that represents only constants to be used for checking fields.
   */
  public class FieldConstants {
    /** The minimum length of an allowable password. */
    public static final int MINIMUM_PASSWORD_LENGTH = 6;
    /** The minimum length of a name (first name and last name). */
    public static final int MINIMUM_NAME_LENGTH = 2;
    /** The minimum length of a username. */
    public static final int MINIMUM_USERNAME_LENGTH = 4;
    /** Credit card length. */
    public static final int CREDIT_CARD_LENGTH = 16;
    /** Minimum email length. */
    public static final int MINIMUM_EMAIL_LENGTH = 7;

  }
}
