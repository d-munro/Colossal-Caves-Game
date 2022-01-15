package adventure;

/**
* Throws various exceptions informing the user that the JSON file is corrupted
* @author Dylan Munro
* @version 1.0
*/
public class InvalidJSONFileException extends Exception{

  /**
  * Constructor for handling default command
  */
  public InvalidJSONFileException() {
    this("Corrupted JSON File");
  }

  /**
  * Constructor for handling a custom command message
  * @param message The message describing the command
  */
  public InvalidJSONFileException(String message) {
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
  * @return The name of the class (InvalidJSONFileException)
  * @Override
  */
  public String toString() {
    return "InvalidJSONFileException\n";
  }

}
