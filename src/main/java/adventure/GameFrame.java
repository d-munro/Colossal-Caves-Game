package adventure;

//imports
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//Game imports

import java.io.InputStreamReader;

/**
 *
 * @author Dylan Munro
 */
public class GameFrame implements ActionListener{

    private Adventure myAdventure;
    private String playerName;

    //frame constants
    private static final int MAX_FRAME_SIZE_X = 1000;
    private static final int MAX_FRAME_SIZE_Y = 700;
    private static final int BORDERLAYOUT_SPACING_X = 10;
    private static final int BORDERLAYOUT_SPACING_Y = 5;
    private static final Dimension MAJOR_TEXT_AREA_SIZE = new Dimension(450, 500);


    //Swing instance vars
    private JFrame frame = new JFrame("Colossal Caves Adventure");
    private Container contentPane;

    //South region
    private JTextField playerNameText = new JTextField("No name entered");
    private JTextField changePlayerNameText = new JTextField();
    private JButton changePlayerNameBtn = new JButton("Change Name");

    //East region
    private JTextField currentRoomText = new JTextField();
    private JTextArea commandHistoryText = new JTextArea();
    private JTextField enterCommandText = new JTextField();
    private JButton enterCommandBtn = new JButton("Enter command");

    //North region
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem quitGameMenuItem = new JMenuItem("Quit Game");
    private JMenuItem saveGameMenuItem = new JMenuItem("Save Game");
    private JMenuItem loadNewJSONFileMenuItem = new JMenuItem("Load New JSON File");
    private JMenuItem loadDefaultJSONFileMenuItem = new JMenuItem("Load Default JSON File");
    private JMenuItem loadSavedJSONFileMenuItem = new JMenuItem("Load Saved JSON File");
    private JFileChooser fileChooser = new JFileChooser();

    //West region
    private JTextArea inventoryText = new JTextArea();

    //----------------------------------Constructors-----------------------

    /**
     * Default constructor
     */
    public GameFrame() {
        myAdventure = null;
        playerName = null;
    }

    public static void main(String[] args) {
      GameFrame frame = new GameFrame();
      frame.run();
    }

    /**
     * Main method which executes the frame
     */
    public void run() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(MAX_FRAME_SIZE_X, MAX_FRAME_SIZE_Y);
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        makeNorthRegion();
        makeEastRegion();
        makeSouthRegion();
        makeWestRegion();
        frame.setVisible(true);
    }

    //-------------------------North Region Initializers---------------

    /**
     * Creates all swing components in the north region of the Container's BorderLayout
     * The north region contains all information relevant to the player's name
     */
    public void makeSouthRegion() {
        JPanel panel = new JPanel();
        JPanel playerNamePanel = initializePlayerNamePanel();
        JPanel changePlayerNamePanel = initializeChangePlayerNamePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(getCenteredTitledBorder("Name Settings"));
        panel.add(playerNamePanel);
        panel.add(changePlayerNamePanel);
        contentPane.add(panel, BorderLayout.SOUTH);
    }

    /**
     * Initializes the JPanel containing the player's current name and a description
     * @return The JPanel containing the player's name and a description
     */
    private JPanel initializePlayerNamePanel() {
        JPanel panel = new JPanel();
        JLabel playerNameLabel = new JLabel("Current Player Name:   ");
        panel.setLayout(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        playerNameText.setEditable(false);
        panel.add(playerNameLabel, BorderLayout.WEST);
        panel.add(playerNameText, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Initializes the JPanel allowing the player to change their current name
     * @return The JPanel allowing the player to change their current name
     */
    private JPanel initializeChangePlayerNamePanel() {
        JPanel panel = new JPanel();
        JLabel playerNameLabel = new JLabel("Change player name to:");
        panel.setLayout(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        panel.add(playerNameLabel, BorderLayout.WEST);
        panel.add(changePlayerNameText, BorderLayout.CENTER);
        changePlayerNameBtn.addActionListener(this);
        changePlayerNameBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(changePlayerNameBtn, BorderLayout.SOUTH);
        return panel;
    }

    //--------------------------East Region Initializers------------------

    /**
     * Creates all swing components in the east region of the Container's BorderLayout
     * The east region contains all information relevant to the user's commands
     */
    public void makeEastRegion() {
        JPanel commandPanel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        commandPanel.setBorder(getCenteredTitledBorder("Commands"));
        commandPanel.add(initializeNorthCommandPanel(), BorderLayout.NORTH);
        commandPanel.add(initializeCenterCommandPanel(), BorderLayout.CENTER);
        commandPanel.add(initializeSouthCommandPanel(), BorderLayout.SOUTH);
        contentPane.add(commandPanel, BorderLayout.EAST);
    }

    /**
     * Initializes the JPanel in the north region of the commands section
     * This JPanel Contains the player's current room
     * @return The JPanel containing details about the player's current room
     */
    private JPanel initializeNorthCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JLabel currentRoomLabel = new JLabel("Current Room: ");
        currentRoomText.setEditable(false);
        currentRoomText.setText("The current room of the adventure will appear here\n");
        panel.add(currentRoomLabel, BorderLayout.WEST);
        panel.add(currentRoomText, BorderLayout.CENTER);
        return panel;
    }


    /**
     * Initializes the JPanel in the center region of the commands section
     * This JPanel Contains the player's command history
     * @return The JPanel containing details about the player's command history
     */
    private JPanel initializeCenterCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JScrollPane scrollPane = new JScrollPane(commandHistoryText);
        commandHistoryText.setPreferredSize(MAJOR_TEXT_AREA_SIZE);
        commandHistoryText.setEditable(false);
        commandHistoryText.setText("The game output will appear here\n");
        panel.add(commandHistoryText, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.EAST);
        return panel;
    }


    /**
     * Initializes the JPanel in the south region of the commands section
     * This JPanel Contains prompts for the user's next command
     * @return The JPanel containing prompts for the user's next command
     */
    private JPanel initializeSouthCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JLabel commandPrompt = new JLabel("What do you want to do next? ");
        enterCommandBtn.addActionListener(this);
        panel.add(commandPrompt, BorderLayout.WEST);
        panel.add(enterCommandText, BorderLayout.CENTER);
        panel.add(enterCommandBtn, BorderLayout.SOUTH);
        return panel;
    }

    /**
    * Creates all swing components in the south region of the Container's BorderLayout
    * The south region contains all information relevant to the game's settings
    */
    public void makeNorthRegion() {
        initializeNorththMenu();
        contentPane.add(menuBar, BorderLayout.NORTH);
    }

    /**
    * Initializes the buttons in the south region of the contentPane
    */
    private void initializeNorththMenu() {
      addSouthMenuItemsToGroup();
      initializeSouthMenuItemListeners();
    }

    /**
    * Adds action listeners to all buttons in south region
    */
    private void initializeSouthMenuItemListeners() {
        quitGameMenuItem.addActionListener(e ->
          frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        saveGameMenuItem.addActionListener(e -> fileChooser.showOpenDialog(null));
        loadNewJSONFileMenuItem.addActionListener(e -> loadNewAdventure());
        loadDefaultJSONFileMenuItem.addActionListener(e -> loadDefaultAdventure());
        loadSavedJSONFileMenuItem.addActionListener(e -> fileChooser.showOpenDialog(null));
    }

    /**
    * Adds all radio buttons in the south region to a group
    */
    private void addSouthMenuItemsToGroup() {
        fileMenu.add(quitGameMenuItem);
        fileMenu.add(saveGameMenuItem);
        fileMenu.add(loadNewJSONFileMenuItem);
        fileMenu.add(loadDefaultJSONFileMenuItem);
        fileMenu.add(loadSavedJSONFileMenuItem);
        menuBar.add(fileMenu);
    }

    /**
    * Creates a JPanel containing all JRadioButtons in the south region of the frame
    * @return A JPanel containing all Radio Buttons in the south region
    */
    public JPanel getRadioButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(quitGameMenuItem);
        panel.add(saveGameMenuItem);
        panel.add(loadNewJSONFileMenuItem);
        panel.add(loadDefaultJSONFileMenuItem);
        panel.add(loadSavedJSONFileMenuItem);
        return panel;
    }
    /**
    * Creates all swing components in the west region of the Container's BorderLayout
    * The west region contains all information relevant to the player's inventory
    */
    public void makeWestRegion() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryText);
        inventoryText.setEditable(false);
        inventoryText.setPreferredSize(MAJOR_TEXT_AREA_SIZE);
        inventoryText.setText("Items in your inventory will appear here\n");
        panel.setBorder(getCenteredTitledBorder("Inventory"));
        panel.add(inventoryText, BorderLayout.CENTER);
        panel.add(inventoryScrollPane, BorderLayout.EAST);
        contentPane.add(panel, BorderLayout.WEST);
    }

    /**
     * Returns a centered, titled border
     * @param title The title of the border
     * @return A centered, titled border with the title param as its name
     */
    private TitledBorder getCenteredTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleJustification(TitledBorder.CENTER); //centers title in middle of border
        return border;
    }

    //---------------------------Event Handling--------------------------
    /**
     * Handles all actions performed on objects
     * @Override
     * @param e The current event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changePlayerNameBtn) {
          changePlayerNameFields();
          changePlayerNameText.setText("");
        } else if (e.getSource() == enterCommandBtn) {
          processCommand(enterCommandText.getText());
          enterCommandText.setText("");
        }
    }

    /**
    * Changes all Strings in the game containing the player's name
    */
    private void changePlayerNameFields() {
      if (myAdventure == null) {
        displayErrorMessage("Please Load an adventure before changing your name");
        return;
      }
      setPlayerName(changePlayerNameText.getText());
      myAdventure.getCurrentPlayer().setName(changePlayerNameText.getText());
    }

    /**
    * Creates a Command from a String
    * @param userInput The string representation of the command
    */
    private void processCommand(String userInput) {
      Parser commandParser = new Parser();
      Command currentCommandObject;
      try {
        currentCommandObject = commandParser.parseUserCommand(userInput);
        setTextFields(currentCommandObject);
        checkForQuitCommand(currentCommandObject);
      } catch (Exception e) {
        displayErrorMessage(e.getMessage());
      }
    }

    /**
    * Checks for the command to quit the GUI
    * @param currentCommandObject The command being checked
    */
    private void checkForQuitCommand(Command currentCommandObject) {
      if (currentCommandObject.getActionWord().compareTo("quit") == 0 && myAdventure != null) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //Closes the frame
      }
    }

    /**
    * Sets various text fields according to a commands output
    * @param currentCommandObject The command being executed
    */
    private void setTextFields(Command currentCommandObject) throws ItemNotFoundException, InvalidCommandException{
      Player currentPlayer = myAdventure.getCurrentPlayer();
      commandHistoryText.setText(myAdventure.executeCommand(currentCommandObject));
      inventoryText.setText(currentPlayer.getInventoryString());
      playerNameText.setText(currentPlayer.getName());
      currentRoomText.setText(myAdventure.getCurrentRoom().getName());
    }

    /**
    * Sets various text fields based on details about the adventures state
    */
    private void setTextFields() {
      Player currentPlayer = myAdventure.getCurrentPlayer();
      commandHistoryText.setText(myAdventure.getCurrentRoom().toString());
      inventoryText.setText(currentPlayer.getInventoryString());
      playerNameText.setText(currentPlayer.getName());
      currentRoomText.setText(myAdventure.getCurrentRoom().getName());
    }

    /**
    * Loads a new JSON file to become the current adventure
    */
    private void loadJSONFile() {
      if (loadNewJSONFileMenuItem.isSelected()) {
        loadNewAdventure();
      } else if (loadDefaultJSONFileMenuItem.isSelected()) {
        loadDefaultAdventure();
      } else if (loadSavedJSONFileMenuItem.isSelected()) {
        fileChooser.showOpenDialog(null);
      }
    }

    /**
    * Loads the default Adventure
    */
    private void loadDefaultAdventure() {
      JSONObject obj;
      try {
        obj = loadAdventureJson();
        myAdventure = new Adventure(obj);
        myAdventure.isValidAdventure();
        setTextFields();
        commandHistoryText.setText("You have loaded the default adventure file, haunted_forest.json");
      } catch (Exception e) {
        displayErrorMessage(e.getMessage());
      }
    }

    /**
    * Loads a new adventure
    */
    private void loadNewAdventure() {
      JSONObject adventureJson;
      try {
        fileChooser.showOpenDialog(null);
        adventureJson = loadAdventureJson(fileChooser.getSelectedFile());
        myAdventure = new Adventure(adventureJson);
        setTextFields();
        commandHistoryText.setText("You have loaded a new adventure file");
      } catch (Exception e) {
        displayErrorMessage(e.getMessage());
      }
    }

    /**
    * Creates a JSONObject with all information relevant to the adventure
    * @return A JSON object containing all information relevant to the adventure
    */
    public JSONObject loadAdventureJson() {
      InputStream fileStream = Game.class.getClassLoader().getResourceAsStream("haunted_forest.json");
      return getJSONFromInputStream(fileStream);
    }

    /**
    * Creates a JSONObject with all information relevant to the adventure
    * @param file The file being loaded
    * @return A JSON object containing all information relevant to the adventure
    */
    public JSONObject loadAdventureJson(File file) {
      try {
        InputStream fileStream = new FileInputStream(file);
        return getJSONFromInputStream(fileStream);
      } catch (Exception e) {
        displayErrorMessage(e.getMessage());
      }
      return null;
    }

    /**
    * Obtains a JSON object from an inputStream
    * @param fileStream The InputStream being read
    * @return A JSONObject containing the file's contents
    */
    public JSONObject getJSONFromInputStream(InputStream fileStream) {
      JSONObject adventureJson = null;
      JSONParser parser = new JSONParser();
      try {
        InputStreamReader reader = new InputStreamReader(fileStream);
        adventureJson = (JSONObject) parser.parse(reader);
        adventureJson = (JSONObject) adventureJson.get("adventure");
      } catch (Exception e) {
        displayErrorMessage(e.getMessage());
      }
      return adventureJson;
    }

    /**
     * Creates a dialog displaying an error message
     * @param message The text of the error message
     */
    public void displayErrorMessage(String message) {
      if (myAdventure == null) {
        JOptionPane.showMessageDialog(frame,
        "Please load an adventure file before interacting with the game", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //-------------------------Setters---------------------------

    /**
     * Sets the myAdventure instance variable to a different adventure
     * @param adv The new adventure being played through
     */
    public void setMyAdventure(Adventure adv) {
      this.myAdventure = adv;
    }

    /**
     * Sets the player name to a new name
     * @param name The player's new name
     */
    public void setPlayerName(String name) {
      this.playerName = name;
      setPlayerNameText(name);
    }

    /**
     * Changes the player's name in the text field
     * @param name The player's new name
     */
    public void setPlayerNameText(String name) {
        playerNameText.setText(name);
    }

    /**
     * Changes the changePlayerNameTextField text
     * @param text The new text of the field
     */
   public void setChangePlayerNameText(String text) {
       changePlayerNameText.setText(text);
   }

    /**
     * Changes the text in the current room text field
     * @param text The new text of the field
     */
   public void setCurrentRoomText(String text) {
       currentRoomText.setText(text);
   }

    /**
     * Changes the text of the command history text area
     * @param text The new text of the area
     */
   public void setCommandHistoryText(String text) {
       commandHistoryText.setText(text);
   }

    /**
     * Changes the text of the enter command text field
     * @param text The new text of the field
     */
   public void setEnterCommandText(String text) {
       enterCommandText.setText(text);
   }

    /**
     * Changes the text of the game settings text area
     * @param text The new text of the area
     */
   public void setGameSettingsText(String text) {
       changePlayerNameText.setText(text);
   }

    /**
     * Changes the changePlayerNameTextField text
     * @param text The new text of the area
     */
   public void setInventoryText(String text) {
       inventoryText.setText(text);
   }

    //------------------------------Getters----------------------

    /**
     * Returns current Adventure object being played through
     * @return The adventure object being played through
     */
    public Adventure getMyAdventure() {
        return myAdventure;
    }

    /**
     * Returns the player's name
     * @return The player's new name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the text in the playerNameText text field
     * @return The text in the playerName text field
     */
    public String getPlayerNameText() {
        return playerNameText.getText();
    }

    /**
     * Returns the text in the changePlayerNameText text field
     * @return The text in the changePlayerName text field
     */
   public String getChangePlayerNameText() {
       return changePlayerNameText.getText();
   }

    /**
     * Returns the text in the getCurrentRoomText text field
     * @return The text in the getCurrentRoomText text field
     */
   public String getCurrentRoomText() {
       return currentRoomText.getText();
   }

    /**
     * Returns the text in the command history text area
     * @return The text in the getCommandHistoryText text are
     */
   public String getCommandHistoryText() {
       return commandHistoryText.getText();
   }

    /**
     * Returns the text in the enter command text field
     * @return The text in the getEnterCommandText text field
     */
   public String getEnterCommandText() {
       return enterCommandText.getText();
   }

    /**
     * Returns the text in the game settings text area
     * @return The text in the game settings text area
     */
   public String getGameSettingsText() {
       return changePlayerNameText.getText();
   }

    /**
     * Returns the text in the inventoryText text area
     * @return The text in the inventoryText text area
     */
   public String setInventoryText() {
       return inventoryText.getText();
   }

   //------------------------Text Management for Game Settings Buttons--------------

   /**
    * Provides instructions to tell the user what happens if they quit the game
    * @return Instructions describing what happens when the user quits the game
    */
   public String getQuitGameInstructions() {
       return "Selecting this option will terminate the program.\n"
       + "It is recommended that you save the game before exiting!\n";
   }

   /**
    * Provides instructions to tell the user what happens if they save the game
    * @return Instructions describing what happens when the user saves the game
    */
   public String getSaveGameInstructions() {
       return "Selecting this option will save the current program.\n"
       + "It will be saved to the target folder, and have the name that you give it.\n";
   }

   /**
    * Provides instructions to tell the user what happens if they load a new JSON file
    * @return Instructions describing what happens when the user loads a new JSON file
    */
   public String getLoadNewJSONFileInstructions() {
       return "Selecting this option will load a JSON file from the path selected.\n"
       + "You will be able to choose the path after submitting this option.\n";
   }

   /**
    * Provides instructions to tell the user what happens if they load a saved JSON file
    * @return Instructions describing what happens when the user loads a saved JSON file
    */
   public String getLoadSavedJSONFileInstructions() {
       return "Selecting this option will load a saved JSON file from the path selected.\n"
       + "You will be able to choose the path after submitting this option.\n";
   }

   /**
    * Provides instructions to tell the user what happens if they load the default JSON file
    * @return Instructions describing what happens when the user loads the default JSON file
    */
   public String getLoadDefaultJSONFileInstructions() {
       return "Selecting this option will load the default JSON file.\n";
   }

}
