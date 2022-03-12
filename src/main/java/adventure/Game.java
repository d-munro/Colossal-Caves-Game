package adventure;

//imports
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/**
 * Provides methods necessary for all user Input/Output for the game
 *
 * @author Dylan Munro
 * @version 2.0
 */
public class Game {

    private Adventure myAdventure;

    /**
     * Main method for executing the program
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.load(args);
    }

    /**
     * Loads the main game settings
     *
     * @param args The command line arguments
     */
    public void load(String[] args) { //loads the game
        JSONObject adventureJson = getAdventureJson(args);
        myAdventure = new Adventure(adventureJson);
        if (!containsValidRooms()) { //Terminate program
            return;
        }
        run();
    }

    /**
     * Returns myAdventure as a JSON file
     *
     * @param args The command line arguments
     * @return A JSON object holding all information about the adventure
     */
    public JSONObject getAdventureJson(String[] args) {
        InputStream fileStream = Game.class.getClassLoader().getResourceAsStream("haunted_forest.json");
        String fileName = processArgs(args);
        if (fileName != null) {
            return loadAdventureJson(fileName);
        } else {
            return loadAdventureJson(fileStream);
        }
    }

    /**
     * Checks the adventure file to see if it contains all valid rooms
     *
     * @return True if the adventure contains all valid rooms, false otherwise
     */
    private boolean containsValidRooms() {
        try {
            myAdventure.isValidAdventure();
        } catch (InvalidJSONFileException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Processes the command line arguments and modifies myAdventure accordingly
     *
     * @param args The command line arguments
     * @return The name of the file path (if -a is an argument)
     */
    public String processArgs(String[] args) {
        if (args.length == 0 || args.length == 1) {
            return null;
        } else if (args[0].compareTo("-l") == 0) { //load next argument
            deserializeFile(args[1]);
        } else if (args[0].compareTo("-a") == 0) {
            return args[1];
        }
        return null;
    }

    /**
     * Deserializes a file
     *
     * @param filePath The path of the file
     */
    public void deserializeFile(String filePath) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            myAdventure = (Adventure) in.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Provides the user interface to interact with the game
     */
    public void run() {
        Scanner input = new Scanner(System.in);
        String commandAction = "";
        System.out.println(myAdventure.loadDefaultRoom());
        do {
            commandAction = processCommand(input.nextLine());
        } while (commandAction.compareTo("quit") != 0);
        System.out.println("Thank you for playing colossal cave adventure");
    }

    /**
     * Processes user input into commands
     *
     * @param userCommand string to be processed into a command
     * @return The action word of the command
     */
    private String processCommand(String userCommand) {
        Parser adventureParser = new Parser();
        Command currentCommandObject;
        try {
            currentCommandObject = adventureParser.parseUserCommand(userCommand);
            System.out.println(myAdventure.executeCommand(currentCommandObject));
            return currentCommandObject.getActionWord();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    /**
     * Creates a JSONObject with all information relevant to the adventure
     *
     * @param fileName The path to load the JSON file
     * @return A JSON object containing all information relevant to the
     * adventure
     */
    public JSONObject loadAdventureJson(String fileName) {
        JSONObject adventureJson = null;
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(fileName)) {
            if (reader != null) {
                adventureJson = (JSONObject) parser.parse(reader);
                adventureJson = (JSONObject) adventureJson.get("adventure");
            }
        } catch (Exception e) {
        }
        return adventureJson;
    }

    /**
     * Creates a JSONObject with all information relevant to the adventure
     *
     * @param inputStream The stream containing the JSON file
     * @return A JSON object containing all information relevant to the
     * adventure
     */
    public JSONObject loadAdventureJson(InputStream inputStream) {
        JSONObject adventureJson = null;
        JSONParser parser = new JSONParser();
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            adventureJson = (JSONObject) parser.parse(reader);
            adventureJson = (JSONObject) adventureJson.get("adventure");
        } catch (Exception e) {
            System.out.println("Error: File not found");
        }
        return adventureJson;
    }
}
