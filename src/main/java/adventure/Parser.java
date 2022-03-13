package adventure;

import java.io.Serializable;
import java.util.Set;

/**
* Creates a Parser object which handles all parsing of user commands
* @author Dylan Munro
* @version 2.0
*/
public class Parser implements Serializable{

  private static final long serialVersionUID = 2L;

  //Mandatory methods - DO NOT MODIFY SIGNATURES

  /**
  * Constructor
  */
  public Parser() {

  }

  /**
  * Parses a string to turn it into a command object
  * @param userCommand A string form of the command
  * @return A command object containing relevant info for executing a command
  */
  public Command parseUserCommand(String userCommand) throws InvalidCommandException {
    String[] words = userCommand.split(" ");
    String secondArg;
    if (words.length == 0) {
      throw new InvalidCommandException("You must include a command");
    }
    if (words.length == 1) {
      return new Command(words[0]);
    }
    secondArg = userCommand.substring(userCommand.indexOf(" ") + 1, userCommand.length());
    return new Command(words[0], secondArg);
  }

  /**
  * Returns a list of all commands
  * @return A string containing all possible commands
  */
  public String allCommands() {
    Set<String> commandsSet = Command.VALID_COMMANDS.keySet();
    String returnedString = "";
    for (String current : commandsSet) {
      returnedString += current + "\n";
    }
    return returnedString;
  }

  /**
  * Returns the name of the class (Parser)
  * @return A string of the class' name
  * @Override
  */
  public String toString() {
    return "Class: Parser";
  }
  //End of Mandatory methods

}
