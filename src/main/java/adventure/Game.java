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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.Serializable;

/**
* Provides methods necessary for all user Input/Output for the game
* @author Dylan Munro
* @version 2.0
*/
public class Game implements Serializable{

    private Adventure myAdventure;
    private static final long serialVersionUID = 2L;

    /**
    * Main method for executing the program
    * @param args The command line arguments
    */
    public static void main(String[] args){
        Game theGame = new Game();
        theGame.load(args);
    }

    /**
    * Loads the main game settings
    * @param args The command line arguments
    */
    public void load(String[] args) { //loads the game
      JSONObject adventureJson = getAdventureJson(args);
      if (adventureJson == null) {
        return;
      }
      myAdventure = new Adventure(adventureJson);
      if (! containsValidRooms()) { //Terminate program
        return;
      }
      run();
    }

    /**
    * Returns myAdventure as a JSON file
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
    * @param filePath The path of the file
    */
    public void deserializeFile(String filePath) {
      try  {
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
      finishGame(input);
    }

    /**
    * Processes user input into commands
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
    * Saves file for user if desired, and writes closing text
    * @param input The scanner which receives input from the user
    */
    public void finishGame(Scanner input) {
      String userChoice = "";
      System.out.println("Would you like to save your game? (Y/N, case sensitive).\n");
      userChoice = obtainUserChoice(input);
      if (userChoice.compareTo("Y") == 0) {
        System.out.println("Enter the name of your save file:\n");
        userChoice = input.nextLine();
        saveAdventure(userChoice);
      }
      System.out.println("Thank you for playing Colossal Cave Adventure.");
    }

    /**
    * Saves the state of the current adventure via serialization
    * @param fileName The name of the save file
    */
    public void saveAdventure(String fileName) {
      try {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        ObjectOutputStream destination = new ObjectOutputStream(outputStream);
        destination.writeObject(myAdventure);
        destination.close();
        outputStream.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }

    /**
    * Prompts user for a yes or no answer
    * @param input The scanner which reads user input
    * @return A Yes or no answer in the form of (Y/N)
    */
    private String obtainUserChoice(Scanner input) { //prompts user for a y/n answer
      String userChoice = "";
      while (userChoice.compareTo("Y") != 0 && userChoice.compareTo("N") != 0) {
        userChoice = input.nextLine();
        if (userChoice.compareTo("Y") != 0 && userChoice.compareTo("N") != 0) {
          System.out.println("Invalid input. Please try entering again:\n");
        }
      }
      return userChoice;
    }

    /**
    * Creates a JSONObject with all information relevant to the adventure
    * @param fileName The path to load the JSON file
    * @return A JSON object containing all information relevant to the adventure
    */
    public JSONObject loadAdventureJson(String fileName){
      JSONObject adventureJson = null;
      JSONParser parser = new JSONParser();
      try (Reader reader = new FileReader(fileName)) {
        if (reader != null) {
          adventureJson = (JSONObject) parser.parse(reader);
          adventureJson = (JSONObject) adventureJson.get("adventure");
        }
      } catch (Exception e) {}
      return adventureJson;
    }

    /**
    * Creates a JSONObject with all information relevant to the adventure
    * @param inputStream The stream containing the JSON file
    * @return A JSON object containing all information relevant to the adventure
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

    /**
    * Returns the name of the current class (Game)
    * @return The name of the class
    */
    public String toString() {
      return "Game";
    }

    /**
    * Sets the myAdventure variable to an adventure object
    * @param adventure The adventure being played through
    */
    public void setMyAdventure(Adventure adventure) {
      this.myAdventure = adventure;
    }

    /**
    * Returns the current adventure being played through
    * @return The current adventure being played through
    */
    public Adventure getMyAdventure() {
      return myAdventure;
    }
}
