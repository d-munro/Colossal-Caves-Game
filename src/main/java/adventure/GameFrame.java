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
public class GameFrame implements ActionListener {

    private Adventure myAdventure;

    //frame constants
    private static final int MAX_FRAME_SIZE_X = 1000;
    private static final int MAX_FRAME_SIZE_Y = 700;
    private static final int BORDERLAYOUT_SPACING_X = 10;
    private static final int BORDERLAYOUT_SPACING_Y = 5;
    private static final Dimension MAJOR_TEXT_AREA_SIZE = new Dimension(450, 500);

    //Swing instance vars
    private final JFrame frame = new JFrame("Colossal Caves Adventure");
    private Container contentPane;

    //South region
    private final JTextField playerNameText = new JTextField("No name entered");
    private final JTextField changePlayerNameText = new JTextField();
    private final JButton changePlayerNameBtn = new JButton("Change Name");

    //East region
    private final JTextField currentRoomText = new JTextField();
    private final JTextArea commandHistoryText = new JTextArea();
    private final JTextField enterCommandText = new JTextField();
    private final JButton enterCommandBtn = new JButton("Enter command");

    //North region
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem quitGameMenuItem = new JMenuItem("Quit Game");
    private final JMenuItem saveGameMenuItem = new JMenuItem("Save Game");
    private final JMenuItem loadNewJSONFileMenuItem = new JMenuItem("Load New JSON File");
    private final JMenuItem loadDefaultJSONFileMenuItem = new JMenuItem("Load Default JSON File");
    private final JMenuItem loadSavedJSONFileMenuItem = new JMenuItem("Load Saved JSON File");
    private final JFileChooser fileChooser = new JFileChooser();

    //West region
    private final JTextArea inventoryText = new JTextArea();

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

    //-------------------------South Region Initializers---------------
    /**
     * Creates all swing components in the north region of the Container's
     * BorderLayout The north region contains all information relevant to the
     * player's name
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
     * Initializes the JPanel containing the player's current name and a
     * description
     *
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
     *
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

    //---------------------------------------------East Region Initializers----------------------------------------
    /**
     * Creates all swing components in the east region of the Container's
     * BorderLayout The east region contains all information relevant to the
     * user's commands
     */
    public void makeEastRegion() {
        JPanel commandPanel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        commandPanel.setBorder(getCenteredTitledBorder("Commands"));
        commandPanel.add(initializeCurrentRoomPanel(), BorderLayout.NORTH);
        commandPanel.add(initializeCommandHistoryPanel(), BorderLayout.CENTER);
        commandPanel.add(initializeNextCommandPanel(), BorderLayout.SOUTH);
        contentPane.add(commandPanel, BorderLayout.EAST);
    }

    /**
     * Initializes JPanel informing users of the current room in the adventure
     *
     * @return The JPanel containing details about the player's current room
     */
    private JPanel initializeCurrentRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JLabel currentRoomLabel = new JLabel("Current Room: ");
        currentRoomText.setEditable(false);
        currentRoomText.setText("The current room of the adventure will appear here\n");
        panel.add(currentRoomLabel, BorderLayout.WEST);
        panel.add(currentRoomText, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Initializes JPanel describing all historical commands
     *
     * @return The JPanel containing details about the player's command history
     */
    private JPanel initializeCommandHistoryPanel() {
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
     * Initializes and returns JPanel prompting the user for their next command
     * 
     * @return The JPanel prompting the user for their next command
     */
    private JPanel initializeNextCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout(BORDERLAYOUT_SPACING_X, BORDERLAYOUT_SPACING_Y));
        JLabel commandPrompt = new JLabel("What do you want to do next? ");
        enterCommandBtn.addActionListener(this);
        panel.add(commandPrompt, BorderLayout.WEST);
        panel.add(enterCommandText, BorderLayout.CENTER);
        panel.add(enterCommandBtn, BorderLayout.SOUTH);
        return panel;
    }
    //------------------------------End of East region initializers-----------------------------

    //------------------------------North region initializers-----------------------------------
    
    /**
     * Creates all swing components in the south region of the Container's
     * BorderLayout The south region contains all information relevant to the
     * game's settings
     */
    public void makeNorthRegion() {
        
        //Initialize Menu Bar
        fileMenu.add(quitGameMenuItem);
        fileMenu.add(saveGameMenuItem);
        fileMenu.add(loadNewJSONFileMenuItem);
        fileMenu.add(loadDefaultJSONFileMenuItem);
        fileMenu.add(loadSavedJSONFileMenuItem);
        menuBar.add(fileMenu);
        
        //Add action listeners to menu bar
        quitGameMenuItem.addActionListener(e
                -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        saveGameMenuItem.addActionListener(e -> fileChooser.showOpenDialog(null));
        loadNewJSONFileMenuItem.addActionListener(e -> loadNewAdventure());
        loadDefaultJSONFileMenuItem.addActionListener(e -> loadDefaultAdventure());
        loadSavedJSONFileMenuItem.addActionListener(e -> fileChooser.showOpenDialog(null));
        
        contentPane.add(menuBar, BorderLayout.NORTH);
    }

    /**
     * Creates all swing components in the west region of the Container's
     * BorderLayout The west region contains all information relevant to the
     * player's inventory
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
     * @param title The title of the border
     * @return A centered, titled border for the specified title
     */
    private TitledBorder getCenteredTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleJustification(TitledBorder.CENTER); //centers title in middle of border
        return border;
    }

    //---------------------------Event Handling--------------------------
    /**
     * Handles all actions performed on objects
     *
     * @param e The current event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changePlayerNameBtn && myAdventure == null) {
            displayErrorMessage("Please load an adventure before changing your name");
        } else if (e.getSource() == changePlayerNameBtn) {
            playerNameText.setText(changePlayerNameText.getText());
            myAdventure.getCurrentPlayer().setName(changePlayerNameText.getText());
            changePlayerNameText.setText("");
        } else if (e.getSource() == enterCommandBtn) {
            processCommand(enterCommandText.getText());
            enterCommandText.setText("");
        }
    }
    
    /**
     * Creates a Command from a String
     *
     * @param userInput The string representation of the command
     */
    private void processCommand(String userInput) {
        Parser commandParser = new Parser();
        Command currentCommandObject;
        try {
            currentCommandObject = commandParser.parseUserCommand(userInput);
            setTextFields(currentCommandObject);
            if (currentCommandObject.getActionWord().compareTo("quit") == 0 && myAdventure != null) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //Closes the frame
            }
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Sets various text fields according to a commands output
     *
     * @param currentCommandObject The command being executed
     */
    private void setTextFields(Command currentCommandObject) throws ItemNotFoundException, InvalidCommandException {
        Player currentPlayer = myAdventure.getCurrentPlayer();
        commandHistoryText.setText(myAdventure.executeCommand(currentCommandObject));
        inventoryText.setText(currentPlayer.getInventoryString());
        playerNameText.setText(currentPlayer.getName());
        currentRoomText.setText(myAdventure.getCurrentRoom().getName());
    }

    /**
     * Sets various text fields based on details about the adventures state
     */
    private void initializeTextFields() {
        Player currentPlayer = myAdventure.getCurrentPlayer();
        commandHistoryText.setText(myAdventure.getCurrentRoom().toString());
        inventoryText.setText(currentPlayer.getInventoryString());
        playerNameText.setText(currentPlayer.getName());
        currentRoomText.setText(myAdventure.getCurrentRoom().getName());
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
            initializeTextFields();
            commandHistoryText.setText("You have loaded the default adventure file, haunted_forest.json");
        } catch (InvalidJSONFileException e) {
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
            initializeTextFields();
            commandHistoryText.setText("You have loaded a new adventure file");
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Creates a JSONObject with all information relevant to the adventure
     *
     * @return A JSON object containing all information relevant to the
     * adventure
     */
    public JSONObject loadAdventureJson() {
        InputStream fileStream = Game.class.getClassLoader().getResourceAsStream("haunted_forest.json");
        return getJSONFromInputStream(fileStream);
    }

    /**
     * Creates a JSONObject with all information relevant to the adventure
     *
     * @param file The file being loaded
     * @return A JSON object containing all information relevant to the
     * adventure
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
     * Obtains JSONObject containing the necessary information to load an
     * adventure from an InputStream
     *
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
     * @param message The message describing the error
     */
    public void displayErrorMessage(String message) {
        if (myAdventure == null) {
            JOptionPane.showMessageDialog(frame,
                    "Please load an adventure file before interacting with the game", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
