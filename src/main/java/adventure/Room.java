package adventure;

//imports
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.Serializable;

/**
* Creates a Room object which contains various items
* @author Dylan Munro
* @version 2.0
*/
public class Room implements Serializable {

    //instance var declarations
    private String name;
    private String shortDescription;
    private String longDescription;
    private int id;
    private boolean startingRoom;
    private ArrayList<Item> loot = new ArrayList<Item>();
    private HashMap<String, Integer> adjacentRoomIds = new HashMap<String, Integer>();
    private static final long serialVersionUID = 2L;

    /**
    * Default constructor for the room class
    */
    public Room() {
        this.name = "";
        this.shortDescription = "";
        this.longDescription = "";
        this.id = 0;
        this.startingRoom = false;
        this.loot = null;
        this.adjacentRoomIds = new HashMap<String, Integer>();
    }

    /**
    *Constructor for the Room class
    * @param obj The JSON object containing details about the current room
    */
    public Room(JSONObject obj) {
      String temp;
      this.name = obj.get("name").toString();
      this.shortDescription = obj.get("short_description").toString();
      this.longDescription = obj.get("long_description").toString();
      checkIfStartingRoom(obj);
      temp = obj.get("id").toString();
      this.id = Integer.parseInt(temp);
      initializeLoot(obj);
      mapAdjacentRooms(obj);
    }

    //private methods

    /**
    * Checks if a room is the starting room
    * @param obj The JSONObject representation of the room
    */
    private void checkIfStartingRoom(JSONObject obj) {
      if (obj.get("start") != null) { //current room has a start
        this.startingRoom = true;
      } else {
        this.startingRoom = false;
      }
    }

    /**
    * Initializes all items present in the current room
    * @param obj The JSON Object containing details about the current room
    */
    private void initializeLoot(JSONObject obj) { //Initializes all items in room
      JSONArray lootJson = (JSONArray) obj.get("loot");
      if (lootJson == null) { //room has no loot
        return;
      }
      for (int i = 0; i < lootJson.size(); i++) {
        addLoot((JSONObject) lootJson.get(i));
      }
    }

    /**
    * Adds a JSON object to a room's loot
    * @param obj The JSONObject representation of the item
    */
    public void addLoot(JSONObject obj) {
      String temp = obj.get("id").toString();
      Item currentItem = Adventure.ID_TO_ITEM_MAP.get(Integer.parseInt(temp));
      if (currentItem != null) {
        currentItem.setContainingRoom(this);
      }
      loot.add(currentItem);
    }

    /**
    * Creates a map of adjacent rooms in all directions relevant to the current room
    * @param obj The JSON Object containing details about the current room
    */
    private void mapAdjacentRooms(JSONObject obj){
      JSONArray entranceJson = (JSONArray) obj.get("entrance");
      if (entranceJson != null) {
        for (int i = 0; i < entranceJson.size(); i++) {
          addAdjacantRoom((JSONObject) entranceJson.get(i));
        }
      }
    }

    private void addAdjacantRoom(JSONObject jsonRoom) {
      String currentDirection = jsonRoom.get("dir").toString();
      String temp = jsonRoom.get("id").toString(); //string for ID at entrance i
      Integer currentId = Integer.parseInt(temp);
      if (isValidDirection(currentDirection)) {
        adjacentRoomIds.put(currentDirection, currentId); //replace 1 with currentID
      }
    }

    //-----------------------Methods checking if rooms are legitimate------------------------

    /**
    * Adds an item to room and checks that various room aspects are valid
    * Checks that the direction is valid, item ID is valid, the room exists in the dungeon,
    * and that the room has an exit
    * If room is invalid, throws InvalidJSONFileException
    * @return True if the room is a valid room for the dungeon
    */
    public boolean isValidRoom() throws InvalidJSONFileException {
      isExitable();
      exitsToValidRoom();
      hasValidExitDirections();
      hasValidLoot();
      return true;
    }

    /**
    * Verifies that a room has at least 1 exit
    * @return true if the room has at least 1 exit, false otherwise
    */
    public boolean isExitable() throws InvalidJSONFileException {
      if (adjacentRoomIds.isEmpty()) {
        throw new InvalidJSONFileException("Corrupt JSON File - " + name + " has no exits.\n");
      }
      return true;
    }

    /**
    * Verifies that a room has exit directions of N, E, S, W, up, or down
    * @return true if exit directions are valid, throws InvalidJSONFileException otherwise
    */
    public boolean hasValidExitDirections() throws InvalidJSONFileException {
      for (String currentDirection : adjacentRoomIds.keySet()) {
        checkRoomAccessibility(currentDirection);
      }
      return true;
    }

    /**
    * Checks if a room can be accessed from its opposite direction
    * Throws InvalidJSONFileException if not accessible
    * @param currentDirection The direction being checked
    */
    private void checkRoomAccessibility(String currentDirection) throws InvalidJSONFileException {
      Room connectedRoom = Adventure.ID_TO_ROOM_MAP.get(adjacentRoomIds.get(currentDirection));
      String oppositeDirection = getOppositeDirectionMap().get(currentDirection);
      if (connectedRoom.adjacentRoomIds.isEmpty()) {
        throw new InvalidJSONFileException("Corrupt JSON File - " + name + " has no exits.\n");
      } else if (connectedRoom.getAdjacentRoomIds().get(oppositeDirection) == null) { //Invalid direction
        throw new InvalidJSONFileException(getRoomAccessibilityErrorString(currentDirection));
      } else if (! (connectedRoom.getAdjacentRoomIds().get(oppositeDirection) == id)) { //Invalid direction
        throw new InvalidJSONFileException(getRoomAccessibilityErrorString(currentDirection));
      }
    }

    /**
    * Returns the String describing a failed room accessibility test
    * @param currentDirection Leaving the room in this direction causes the error
    * @return A string describing the error
    */
    private String getRoomAccessibilityErrorString(String currentDirection) {
      return "Corrupt JSON File:\n"
      + name + " can not be re-entered when exiting from the " + currentDirection + ".\n";
    }

    /**
    * Returns a map containing the opposite of a direction
    * @return A map containing opposite directions
    */
    private HashMap<String, String> getOppositeDirectionMap() {
      HashMap<String, String> map = new HashMap<String, String>();
      map.put("N", "S");
      map.put("S", "N");
      map.put("W", "E");
      map.put("E", "W");
      map.put("up", "down");
      map.put("down", "up");
      return map;
    }

    /**
    * Verifies that a room contains items which exist in the adventure
    * @return true if room contains valid items, throws InvalidJSONFileException otherwise
    */
    public boolean hasValidLoot() throws InvalidJSONFileException {
      for (Item currentItem : loot) {
        if (currentItem == null || Adventure.ID_TO_ITEM_MAP.get(currentItem.getId()) == null) { //Item DNE
          throw new InvalidJSONFileException("Corrupt JSON File:\n"
          + name + " contains items that do not exist in the adventure.\n");
        }
      }
      return true;
    }

    /**
    * Verifies that a room exits to a room which exists in the adventure
    * @return true if room exits to valid rooms, throws InvalidJSONFileException otherwise
    */
    public boolean exitsToValidRoom() throws InvalidJSONFileException {
      for (String key : adjacentRoomIds.keySet()) {
        if (Adventure.ID_TO_ROOM_MAP.get(adjacentRoomIds.get(key)) == null) { //Room DNE
          throw new InvalidJSONFileException("Corrupt JSON File:\n"
          + name + " exits to invalid room ID.\n");
        }
      }
      return true;
    }

    /**
    * Creates a map of adjacent rooms in all directions relevant to the current room
    * @param i The id of the room being added to the map
    * @param direction The direction needed to travel to reach the adjacent room
    */
    public void mapAdjacentRooms(String direction, int i) {
      if (isValidDirection(direction)) {
        adjacentRoomIds.put(direction, i);
      }
    }

    /**
    * Determines if a direction is valid
    * @param direction The direction in question
    * @return True if the direction is valid, false if invalid
    */
    private boolean isValidDirection(String direction) {
      if (direction == null || Adventure.VALID_DIRECTIONS_MAP.get(direction) == null) {
        return false;
      }
      return true;
    }

    /**
    * Returns all items present in the room
    * @return An item ArrayList containing all items in the room
    */
    public ArrayList<Item> listItems(){
      return loot;
    }

    /**
    * Returns whether or not a room is the starting room in the adventure
    * @return true if the room is the starting room in the adventure, false if it isn't
    */
    public boolean isStartingRoom() {
      return startingRoom;
    }

    /**
    * Returns the name of the current room
    * @return The name of the current room
    */
    public String getName(){
      return name;
    }

    /**
    * Returns a short description of the current room
    * @return A short description of the current room
    */
    public String getShortDescription() {
      return shortDescription;
    }

    /**
    * Returns a long description of the current room
    * @return A long description of the current room
    */
    public String getLongDescription(){
      return longDescription;
    }

    /**
    * Returns the id of the current room
    * @return The id of the current room
    */
    public int getId() {
      return id;
    }

    /**
    * Returns all a map containing adjacent room ids in various directions
    * @return The map containing the ids of adjacent rooms
    */
    public HashMap<String, Integer> getAdjacentRoomIds() {
      return adjacentRoomIds;
    }

    /**
    * Returns the room adjacent to current room in a specified direction
    * @param direction The requested direction
    * @return The adjacent room, or null if there is no room in the specified direction
    */
    public Room getConnectedRoom(String direction) {
      if (adjacentRoomIds.containsKey(direction)) {
        return Adventure.ID_TO_ROOM_MAP.get(adjacentRoomIds.get(direction));
      }
      return null;
    }

    /**
    * Returns a short description and the name of the current room
    * @return Short description and name of current room
    */
    public String getRoomString() {
      return "You are now in: " + name + "\n" + shortDescription + "\n";
    }

    /**
    * Returns a string of all loot contained in a room
    * @return All loot contained in the room
    */
    public String getLootString() {
      String returnedString;
      if (loot.size() == 0) {
        return "There are no items in this area.\n";
      } else {
        returnedString = "You have found the following items in this area:\n";
        for (int i = 0; i < loot.size(); i++) {
          returnedString = returnedString + loot.get(i).toString() + "\n";
        }
      }
      return returnedString;
    }

    /**
    * Determines if an item is present in a room
    * @param item The item in question
    * @return true if the item is in the current room, false if not present
    */
    public boolean containsItem(Item item) {
      for (Item currentItem : loot) {
        if (currentItem.getId() == item.getId()) {
          return true;
        }
      }
      return false;
    }

    /**
    * Removes an item from the current room
    * @param item The item to be removed
    * @return true if item was successfully remooved, false if item could not be removed
    */
    public boolean removeItem(Item item) {
      for (Item currentItem : loot) {
        if (item.getId() == currentItem.getId()) {
          loot.remove(currentItem);
          return true;
        }
      }
      return false;
    }

    /**
    * Returns a short description of the current room and the items it contains
    * @return The description of the room and its items
    * @Override
    */
    public String toString() {
      return getRoomString() + getLootString();
    }

    //Setters
    /**
    * Sets the room name
    * @param roomName The new name of the room
    */
    public void setName(String roomName) {
      this.name = roomName;
    }

    /**
    * Sets the short description of the room
    * @param description The short description of the room
    */
    public void setShortDescription(String description) {
      this.shortDescription = description;
    }

    /**
    * Sets the long description of the room
    * @param description The short description of the room
    */
    public void setLongDescription(String description) {
      this.longDescription = description;
    }

    /**
    * Sets the id of the room
    * @param i The room's id
    */
    public void setId(int i) {
      this.id = i;
    }

    /**
    * Changes the room to be the starting room
    * @param room true if the room is the starting room, false otherwise
    */
    public void setStartingRoom(boolean room) {
      this.startingRoom = room;
    }

    /**
    * Sets the loot contained within the room
    * @param items The items contained within the room
    */
    public void setLoot(ArrayList<Item> items) {
      this.loot = items;
    }

    /**
    * Adds an item to the loot contained within the room
    * @param item The item to be added to the room's loot table
    */
    public void addLoot(Item item) {
      loot.add(item);
    }

    /**
    * Sets the map containing the room ids of all adjacent rooms
    * @param roomIds The map containing the adjacent room names mapped to their ids
    */
    public void setAdjacentRoomIds(HashMap<String, Integer> roomIds) {
      this.adjacentRoomIds = roomIds;
    }
}
