package cs.b07.cscb07project.backend.databases;

import cs.b07.cscb07project.backend.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;




/**
 * Class representing a database of all users.
 *
 * @author Shadman
 */
public class UserDatabase {
  private static HashMap<String, User> emailMap = new HashMap<>();
  private static HashMap<String, String> usernameMap = new HashMap<>();

  /**
   * Adds a set of users to this user database.
   * 
   * @param userData a list of users to be added to this database
   */
  public static void addUser(ArrayList<User> userData) {
    for (User nextUser : userData) {
      UserDatabase.addUser(nextUser.getEmail(), nextUser.getUsername(), nextUser);
    }
  }

  /**
   * Adds the user given to the database; returns a boolean indicating if adding user was
   * successful.
   * 
   * @param userEmail the email of the user
   * @param username the username of the user
   * @param user the user to be added
   * @return a boolean indicating if the user was added successfully
   */
  public static boolean addUser(String userEmail, String username, User user) {
    if (!(UserDatabase.containsEmail(userEmail))) {
      UserDatabase.emailMap.put(userEmail, user);
      UserDatabase.usernameMap.put(username, userEmail);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Adds and returns a boolean indicating if the addition of given user to the database was
   * successful.
   * 
   * @param user the user to add to the database
   * @return boolean indicating if addition to database was useful
   */
  public static boolean addUser(User user) {
    return addUser(user.getEmail(), user.getUsername(), user);

  }

  /**
   * Returns a set of users currently stored inside the database.
   * 
   * @return a Set of users in the database
   */
  public static Set<User> getUsers() {
    HashSet<User> users = new HashSet<>();
    for (User user : emailMap.values()) {
      users.add(user);
    }
    return users;
  }

  /**
   * Returns the user associated with the given email-address.
   * 
   * @param userEmail email address of the needed user
   * @return the user with the given email
   * @throws NoSuchUserException when user with email given doesn't exist inside this database
   */
  public static User getUserByEmail(String userEmail) throws NoSuchUserException {

    // Check all user email, throw exception if no match is found
    if (UserDatabase.containsEmail(userEmail)) {
      return UserDatabase.emailMap.get(userEmail);
    }
    throw new NoSuchUserException(String.format("no user with email address %s exists", userEmail));
  }

  /**
   * Returns the user associated with the given username.
   * 
   * @param username username of the needed user
   * @return the user with the given username
   * @throws NoSuchUserException when user with username given doesn't exist inside this database
   */
  public static User getUserByUsername(String username) throws NoSuchUserException {
    // Check all user email, throw exception if no match is found
    if (UserDatabase.containsUsername(username)) {
      String userEmail = UserDatabase.usernameMap.get(username);
      return UserDatabase.getUserByEmail(userEmail);
    }
    throw new NoSuchUserException(String.format("no user with username %s exists", username));
  }

  /**
   * Returns a boolean indicating if the given username is contained in this database.
   * 
   * @param username the username of the user
   * @return a boolean indicating if the username is contained in this database
   */
  public static boolean containsUsername(String username) {
    return usernameMap.containsKey(username);
  }


  /**
   * Returns a boolean indicating if the given email is contained in this database.
   * 
   * @param userEmail the email of the user
   * @return a boolean indicating if the email is contained in this database
   */
  public static boolean containsEmail(String userEmail) {
    return emailMap.containsKey(userEmail);
  }


  /**
   * Returns a string representation of every user in this database.
   * 
   * @return a string representation of every user in this database
   */
  public static String getStringRep() {
    String result = "";
    for (User myUser : UserDatabase.emailMap.values()) {
      if (myUser.getAccessKey()) {
        result += "Admin: " + myUser.getPersonalInformation() + "\n";
      } else {
        result += "User: " + myUser.getPersonalInformation() + "\n";
      }
    }
    return result;
  }
}
