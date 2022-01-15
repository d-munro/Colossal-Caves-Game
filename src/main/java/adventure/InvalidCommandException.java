package adventure;

import java.io.Serializable;

/**
*Handles all error checking for invalid commands
* @author Dylan Munro
* @version 2.0
*/
public class InvalidCommandException extends Exception implements Serializable{

  private static final long serialVersionUID = 2L;

  /**
  * Constructor for handling default command
  */
  public InvalidCommandException() {
    this("I'm afraid that I don't recognize that command.\n"
      + "Ensure that your first word is go, look, quit, inventory, take,\n"
      + "eat, read, wear, or toss.\n"
      + "If using go, ensure that you specify the correct direction.\n"
      + "If using look, take, eat, read, wear, or toss,\n"
      + "Ensure that the item exists and can have the action performed on it.\n"
      + "All commands are case sensitive.\n");
  }

  /**
  * Constructor for handling a custom command message
  * @param message The message describing the command
  */
  public InvalidCommandException(String message) {
    super(message);
  }

  /**
  * Returns the exception's error message
  * @param e The exception
  * @return The exception's error message
  * @Override
  */
  public String getMessage(InvalidCommandException e) {
    return super.getMessage();
  }

  /**
  * Returns the default command message
  * @return The name of the class (InvalidCommandException)
  * @Override
  */
  public String toString() {
    return "InvalidCommandException\n";
  }

}
