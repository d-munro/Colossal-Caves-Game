package adventure;

/**
* Creates a Parser object which handles all parsing of user commands
* @author Dylan Munro
* @version 2.0
*/
public class Parser {

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

}
