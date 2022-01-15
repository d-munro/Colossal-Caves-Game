package adventure;

//imports
import java.util.HashMap;
import java.io.Serializable;

public class Command implements Serializable {

    //instance vars
    private String action;
    private String noun;
    private static final long serialVersionUID = 2L;

    //Constants
    public static final HashMap<String, Boolean> VALID_COMMANDS_MAP = new HashMap<String, Boolean>();

    //---------Manditory methods - DO NOT CHANGE SIGNATURES----------------------

    public Command() throws InvalidCommandException {
        this(null);
    }

  /**
     * Create a command object given only an action.  this.noun is set to null
     *
     * @param command The first word of the command.
     *
     */
    public Command(String command) throws InvalidCommandException{
        this(command, null);
    }

    /**
     * Creates a command object given both an action and a noun.
     * @param command The first word of the command.
     * @param what      The second word of the command.
     */
    public Command(String command, String what) throws InvalidCommandException{
        setValidCommands();
        setCurrentCommand(command, what);
    }

    /**
    * Initializes the command instructions and ensures that it is valid
    * @param command The first word of the command
    * @param what The second word of the command
    */
    private void setCurrentCommand(String command, String what) throws InvalidCommandException{
      if (command == null || !VALID_COMMANDS_MAP.containsKey(command)) { //command DNE
        throw new InvalidCommandException();
      }
      this.action = command;
      this.noun = what;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     *
     * @return The command word.
     */
    public String getActionWord() {
        return this.action;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getNoun() {
        return this.noun;
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (noun != null);
    }

    /**
    * Returns the name of the current class
    * @return A String containing the name of the current class (Commands)
    * @Override
    */
    public String toString() {
      return "Commands";
    }
    //------------------------End of mandatory methods---------------------------

    /**
    * Creates a map which contains all valid commands for the program
    */
    public static void setValidCommands() {
      VALID_COMMANDS_MAP.put("go", true);
      VALID_COMMANDS_MAP.put("look", true);
      VALID_COMMANDS_MAP.put("take", true);
      VALID_COMMANDS_MAP.put("inventory", true);
      VALID_COMMANDS_MAP.put("quit", true);
      VALID_COMMANDS_MAP.put("eat", true);
      VALID_COMMANDS_MAP.put("read", true);
      VALID_COMMANDS_MAP.put("toss", true);
      VALID_COMMANDS_MAP.put("wear", true);
    }

    /**
    * Sets the action instance variable
    * @param a The action being performed
    */
    public void setAction(String a) {
      this.action = a;
    }

    /**
    * Sets the noun instance variable
    * @param n The noun being acted upon
    */
    public void setNoun(String n) {
      this.noun = n;
    }
}
