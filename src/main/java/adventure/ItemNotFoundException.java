package adventure;

import java.io.Serializable;
/**
*Handles all error checking for invalid commands
* @author Dylan Munro
* @version 2.0
*/
public class ItemNotFoundException extends Exception implements Serializable {

  private static final long serialVersionUID = 2L;

  /**
  * Constructor for handling default command
  */
  public ItemNotFoundException() {
    this("I can not seem to find that item at this time.\n"
    + "Maybe it exists, but you just haven't found it yet?\n");
  }

  /**
  * Constructor for handling a custom command message
  * @param message The message describing the command
  */
  public ItemNotFoundException(String message) {
    super(message);
  }

  /**
  * Returns the exception's error message
  * @param e The exception
  * @return The exception's error message
  * @Override
  */
  public String getMessage(ItemNotFoundException e) {
    return super.getMessage();
  }

  /**
  * Returns the default command message
  * @return The name of the class (ItemNotFoundException)
  * @Override
  */
  public String toString() {
    return "ItemNotFoundException\n";
  }

}
