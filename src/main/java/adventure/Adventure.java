package adventure;

//imports
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.Serializable;

/**
* Creates an adventure object which holds various information about the game
* @author Dylan Munro
* @version 2.0
*/
public class Adventure implements Serializable{

    //instance var declarations
    private ArrayList<Room> roomList = new ArrayList<Room>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private Room currentRoom;
    private Player currentPlayer = new Player();
    private static final long serialVersionUID = 2L;

    //Constants
    public static final HashMap<Integer, Item> ID_TO_ITEM_MAP = new HashMap<Integer, Item>();
    public static final HashMap<Integer, Room> ID_TO_ROOM_MAP = new HashMap<Integer, Room>();
    public static final HashMap<String, Item> STRING_TO_ITEM_MAP = new HashMap<String, Item>();
    public static final HashMap<String, Room> STRING_TO_ROOM_MAP = new HashMap<String, Room>();
    public static final HashMap<String, Boolean> VALID_DIRECTIONS_MAP = new HashMap<String, Boolean>();

    /**
    * Default Constructor
    */
    public Adventure() {
      roomList = null;
      itemList = null;
      currentRoom = null;
    }

    /**
    * Constructor
    * @param adventureJson The JSON object containing all relevant information about the adventure
    */
    public Adventure(JSONObject adventureJson) {
      setValidDirectionsMap();

      //load item settings - IMPORTANT: Must initialize before rooms to prevent NPE when initializing loot
      JSONArray jsonItems = (JSONArray) adventureJson.get("item");
      generateItems(jsonItems);
      initializeIDToItemMap();
      initializeStringToItemMap();

      //load room settings
      JSONArray jsonRooms = (JSONArray) adventureJson.get("room");
      generateRooms(jsonRooms);
      initializeIDToRoomMap();
      initializeStringToRoomMap();
    }

    /**
    * Checks that the all entries in the adventure JSON file are valid
    * @return True if all entires are valid, false otherwise
    */
    public boolean isValidAdventure() throws InvalidJSONFileException {
      for (Room current : roomList) {
        if (! current.isValidRoom()) {
          return false;
        }
      }
      return true;
    }

    /**
    * Creates and returns a HashMap containing all valid directions for the game
    */
    private void setValidDirectionsMap() {
      VALID_DIRECTIONS_MAP.put("N", true);
      VALID_DIRECTIONS_MAP.put("E", true);
      VALID_DIRECTIONS_MAP.put("S", true);
      VALID_DIRECTIONS_MAP.put("W", true);
      VALID_DIRECTIONS_MAP.put("up", true);
      VALID_DIRECTIONS_MAP.put("down", true);
    }

    //Map initializers

    /**
    * Initializes the constant ID_TO_ITEM_MAP
    */
    private void initializeIDToItemMap() {
      int currentId;
      Item currentItem;
      for (int i = 0; i < itemList.size(); i++) { //map int values to their items
        currentItem = itemList.get(i);
        currentId = currentItem.getId();
        ID_TO_ITEM_MAP.put(currentId, currentItem);
      }
    }

    /**
    * Initializes the constant STRING_TO_ITEM_MAP
    */
    private void initializeStringToItemMap() {
      Item currentItem;
      String itemName;
      for (int i = 0; i < itemList.size(); i++) {
        currentItem = itemList.get(i);
        itemName = currentItem.getName();
        STRING_TO_ITEM_MAP.put(itemName, currentItem);
      }
    }

    /**
    * Initializes the constant ID_TO_ROOM_MAP
    */
    private void initializeIDToRoomMap() {
      int currentId;
      Room currRoom;
      for (int i = 0; i < roomList.size(); i++) { //maps int values to their rooms
        currRoom = roomList.get(i);
        currentId = currRoom.getId();
        ID_TO_ROOM_MAP.put(currentId, currRoom);
      }
    }

    /**
    * Initializes the constant STRING_TO_ROOM_MAP
    */
    private void initializeStringToRoomMap() {
      Room currRoom;
      String roomName;
      for (int i = 0; i < roomList.size(); i++) {
        currRoom = roomList.get(i);
        roomName = currRoom.getName();
        STRING_TO_ROOM_MAP.put(roomName, currRoom);
      }
    }

    //private methods

    /**
    * Generates all rooms in the adventure
    * @param jsonRooms An array of all rooms in the adventure
    */
    private void generateRooms(JSONArray jsonRooms) {
      JSONObject current;
      for (int i = 0; i < jsonRooms.size(); i++) {
        current = (JSONObject) jsonRooms.get(i);
        Room newRoom = new Room(current);
        if (newRoom.isStartingRoom()) {
          currentRoom = newRoom;
          currentPlayer.setCurrentRoom(currentRoom);
        }
        roomList.add(newRoom);
      }
    }

    /**
    * Generates all items in the adventure
    * @param jsonItems An array of all items in the adventure
    */
    private void generateItems(JSONArray jsonItems) {
      JSONObject current;
      Item newItem;
      ItemGenerator generator = new ItemGenerator();
      for (int i = 0; i < jsonItems.size(); i++) {
        current = (JSONObject) jsonItems.get(i);
        newItem = generator.generateItem(current);
        itemList.add(newItem);
      }
    }

    //-----------------------------------output methods and getters--------------------------------

    /**
    * Returns a list of all rooms in the adventure game
    * @return An ArrayList of all rooms in the adventure
    */
    public ArrayList<Room> listAllRooms() {
      return roomList;
    }

    /**
    * Returns a list of all items in the adventure game
    * @return An ArrayList of all items in the adventure
    */
    public ArrayList<Item> listAllItems() {
      return itemList;
    }

    /**
    * Returns a player object containing the current player
    * @return A player object containing the current player
    */
    public Player getCurrentPlayer() {
      return currentPlayer;
    }

    /**
    * Returns a room object containing the current room in the adventure
    * @return The current room in the adventure
    */
    public Room getCurrentRoom() {
      return currentRoom;
    }

    /**
    * Returns a short description of the current room in the adventure
    * @return A short description of the current room in the adventure
    */
    public String getCurrentRoomShortDescription() {
      return currentRoom.getShortDescription();
    }

    /**
    * Returns a long description of the current room in the adventure
    * @return A long description of the current room in the adventure
    */
    public String getCurrentRoomDescription() {
      return currentRoom.getLongDescription();
    }

    /**
    * Obtains the string format of the starting room
    * @return Starting room name, items contained, and a short description about the room
    */
    public String loadDefaultRoom() {
      return currentRoom.toString();
    }

    //----------------------------------game command execution------------------------------

    /**
    * Calls appropriate method to handle execution of a command
    * @param currentCommand The current command to be executed
    * @return The output text from executing the command
    */
    public String executeCommand(Command currentCommand) throws ItemNotFoundException, InvalidCommandException {
      if (currentCommand.getActionWord().compareTo("go") == 0) { //go
        return go(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("look") == 0) { //look
        return look(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("take") == 0) {
        return take(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("inventory") == 0) {
        return inventory();
      }
      return executeSpecificItemCommand(currentCommand);
    }

    /**
    * Executes commands related to the item subclasses
    * @param currentCommand The command being executed
    * @return The output text for executing the command
    */
    public String executeSpecificItemCommand(Command currentCommand)
    throws ItemNotFoundException, InvalidCommandException {
      if (currentCommand.getActionWord().compareTo("eat") == 0) {
        return eat(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("toss") == 0) {
        return toss(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("wear") == 0) {
        return wear(currentCommand.getNoun());
      } else if (currentCommand.getActionWord().compareTo("read") == 0) {
        return read(currentCommand.getNoun());
      }
      throw new InvalidCommandException();
    }

    /**
    * Handles execution of the go command
    * @param direction The direction to travel in
    * @return The new room's name, short description, and items contained
    */
    public String go(String direction) throws InvalidCommandException {
      if (direction == null || VALID_DIRECTIONS_MAP.get(direction) == null) {
        throw new InvalidCommandException("Please enter a valid direction.\n");
      }
      if (currentRoom.getConnectedRoom(direction) == null) { //Room doesn't exist
        return "There is no room in that direction.\n";
      }
      currentRoom = currentRoom.getConnectedRoom(direction); //update currentRoom to new room
      currentPlayer.setCurrentRoom(currentRoom);
      return currentRoom.toString();
    }

    /**
    * Handles execution of the look command
    * @param object The object being looked at
    * @return A long description of the object
    */
    public String look(String object) throws ItemNotFoundException, InvalidCommandException{
      if (object == null) {
        throw new InvalidCommandException();
      } else if (STRING_TO_ITEM_MAP.containsKey(object)) { //object is an item
        return lookAtItem(STRING_TO_ITEM_MAP.get(object));
      } else if (STRING_TO_ROOM_MAP.containsKey(object)) { //object is a room
        return lookAtRoom(STRING_TO_ROOM_MAP.get(object));
      }
      throw new ItemNotFoundException("I do not recognize that object.\n"
      + "Ensure that the object is a valid room or item!\n");
    }

    /**
    * Handles execution of the look command for items
    * Also checks that the current item is present in the player's inventory or the current room
    * @param item The item being looked at
    * @return A long description of the item
    */
    private String lookAtItem(Item item) throws ItemNotFoundException {
      if (!(currentPlayer.hasItem(item) || currentRoom.containsItem(item))) {
        throw new ItemNotFoundException(item.getName() + " is not present in your inventory or the current room.\n");
      }
      return item.getLongDescription() + "\n";
    }

    /**
    * Handles execution of the look command for rooms
    * Also ensures that the player is currently in the room that they're trying to look at
    * @param room The room being looked at
    * @return A long description of the room
    */
    private String lookAtRoom(Room room) throws ItemNotFoundException {
      if (room.getId() != currentRoom.getId()) {
        throw new ItemNotFoundException("You must be present in " + room.getName() + " to look at it.\n");
      }
      return room.getLongDescription() + "\n";
    }

    /**
    * Handles execution of the take command
    * @param object The object being taken
    * @return A message telling the player what item was taken and a description of the item
    */
    public String take(String object) throws ItemNotFoundException, InvalidCommandException {
      if (object == null) {
        throw new InvalidCommandException();
      }
      if (!STRING_TO_ITEM_MAP.containsKey(object) || !currentRoom.containsItem(STRING_TO_ITEM_MAP.get(object))) {
        throw new ItemNotFoundException("A " + object + " is not present in the current room.\n");
      }
      currentPlayer.addToInventory(STRING_TO_ITEM_MAP.get(object));
      currentRoom.removeItem(STRING_TO_ITEM_MAP.get(object));
      return "You have added a " + object + " to your inventory.\n";
    }

    /**
    * Handles execution of the inventory command
    * @return A string containing details about all items in the player's inventory
    */
    public String inventory() {
      String returnedString = "";
      ArrayList<Item> inv = currentPlayer.getInventory();
      if (inv.size() == 0) {
        return "Your inventory is empty.\n";
      }
      return currentPlayer.getInventoryString();
    }

    /**
    * Handles execution of the eat command
    * @param object The name of the item
    * @return A string containing details about the item eaten
    */
    public String eat(String object) throws ItemNotFoundException, InvalidCommandException {
      Item eatenItem = STRING_TO_ITEM_MAP.get(object);
      if (eatenItem == null || ! currentPlayer.hasItem(eatenItem)) {
        throw new ItemNotFoundException(object + " is not present in your inventory.\n");
      }
      if (eatenItem instanceof Edible) {
        return removeEdibleItem(eatenItem);
      }
      throw new InvalidCommandException(object + " is not edible.\n");
    }

    /**
    * Removes an edible item from the game
    * @param eatenItem The edible item to be removed
    * @return A string describing eating the item
    */
    private String removeEdibleItem(Item eatenItem) {
      eatenItem.setContainingRoom(null);
      currentPlayer.removeFromInventory(eatenItem);
      ID_TO_ITEM_MAP.remove(eatenItem.getId());
      STRING_TO_ROOM_MAP.remove(eatenItem.getName());
      return ((Food) eatenItem).eat();
    }

    /**
    * Handles execution of the toss command
    * @param object The name of the item being tossed
    * @return A string containing details about the item tossed
    */
    public String toss(String object) throws ItemNotFoundException, InvalidCommandException {
      Item tossedItem = STRING_TO_ITEM_MAP.get(object);
      if (tossedItem == null || ! currentPlayer.hasItem(tossedItem)) {
        throw new ItemNotFoundException(object + " is not present in your inventory.\n");
      } else if (tossedItem instanceof SmallFood) {
        tossedItem.setContainingRoom(currentRoom);
        currentRoom.addLoot(tossedItem);
        currentPlayer.removeFromInventory(tossedItem);
        return ((SmallFood) tossedItem).toss();
      } else if (tossedItem instanceof Weapon) {
        tossedItem.setContainingRoom(currentRoom);
        currentRoom.addLoot(tossedItem);
        currentPlayer.removeFromInventory(tossedItem);
        return ((Weapon) tossedItem).toss();
      }
      throw new InvalidCommandException(object + " is not tossable.\n");
    }

    /**
    * Handles execution of the wear command
    * @param object The item being worn
    * @return A string containing details about the item worn
    */
    public String wear(String object) throws ItemNotFoundException, InvalidCommandException {
      Item wornItem = STRING_TO_ITEM_MAP.get(object);
      if (wornItem == null || ! currentPlayer.hasItem(wornItem)) {
        throw new ItemNotFoundException(object + " is not present in your inventory.\n");
      }
      if (wornItem instanceof Wearable && wornItem instanceof Clothing) {
        ((Clothing) wornItem).setWornStatus(true);
        return ((Clothing) wornItem).wear();
      }
      throw new InvalidCommandException(object + " is not wearable.\n");
    }

    /**
    * Handles execution of the read command
    * @param object The item being read
    * @return A string containing details about the item read
    */
    public String read(String object) throws ItemNotFoundException, InvalidCommandException {
      Item readItem = STRING_TO_ITEM_MAP.get(object);
      if (readItem == null || ! currentPlayer.hasItem(readItem)) {
        throw new ItemNotFoundException(object + " is not present in your inventory.\n");
      }
      if (readItem instanceof BrandedClothing) {
        return ((BrandedClothing) readItem).read();
      } else if (readItem instanceof Spell) {
        return ((Spell) readItem).read();
      }
      throw new InvalidCommandException(object + " is not readable.\n");
    }

    /**
    * Returns the name of the class (Adventure)
    * @return The name of the class (Adventure)
    * @Override
    */
    public String toString() {
      return "Adventure";
    }

    //Setters
    /**
    * Sets the roomList which contains all rooms in the adventure
    * @param list The list of all rooms in the adventure
    */
    public void setRoomList(ArrayList<Room> list) {
      this.roomList = list;
    }

    /**
    * Sets the itemList which contains all items in the adventure
    * @param list The list of all items in the adventure
    */
    public void setItemList(ArrayList<Item> list) {
      this.itemList = list;
    }

    /**
    * Sets the current room in the adventure
    * @param room The current room in the adventure
    */
    public void setCurrentRoom(Room room) {
      this.currentRoom = room;
    }

    /**
    * Sets the current player in the adventure
    * @param player The current player
    */
    public void setCurrentPlayer(Player player) {
      this.currentPlayer = player;
    }

    /**
    * Returns an ArrayList containing all rooms in the adventure
    * @return All rooms in the adventure
    */
    public ArrayList<Room> getRoomList() {
      return roomList;
    }

}
