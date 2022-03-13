package adventure;

//imports
import java.util.HashMap;

/**
 * Creates a Command object which ensures that entered user commands are syntactically valid
 * 
 * @author Dylan Munro
 */
public class Command {

    //Instance vars
    private final String action;
    private final String noun;

    //Constants
    public static final HashMap<String, String> VALID_COMMANDS = new HashMap<String, String>();

    static {
        VALID_COMMANDS.put("eat", "eat an item in your inventory");
        VALID_COMMANDS.put("go", "travel in a specified direction");
        VALID_COMMANDS.put("inventory", "view all items currently held in your inventory");
        VALID_COMMANDS.put("look", "look at an object");
        VALID_COMMANDS.put("quit", "exit the game");
        VALID_COMMANDS.put("read", "read an item in your inventory");
        VALID_COMMANDS.put("take", "take an object from the room into your inventory");
        VALID_COMMANDS.put("toss", "toss an item from your inventory");
        VALID_COMMANDS.put("wear", "wear an item in your inventory");
    }

    /**
     * Create a command object given only an action.
     *
     * @param action The first word of the command.
     *
     * @throws InvalidCommandException
     */
    public Command(String action) throws InvalidCommandException {
        this(action, null);
    }

    /**
     * Creates a command object given both an action and a noun.
     *
     * @param action The first word of the command.
     * @param noun The second word of the command.
     *
     * @throws InvalidCommandException
     */
    public Command(String action, String noun) throws InvalidCommandException {
        if (action == null || !VALID_COMMANDS.containsKey(action)) { //command DNE
            throw new InvalidCommandException();
        }
        this.action = action;
        this.noun = noun;
    }

    /**
     * @return Action describing the functionality of the command
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
}
