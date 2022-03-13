package adventure;

//imports
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Creates a Room object which contains various items
 *
 * @author Dylan Munro
 * @version 2.0
 */
public class Room {

    private final String name;
    private final String shortDescription;
    private final String longDescription;
    private final int id;
    private final boolean isStartingRoom;
    private final ArrayList<Item> loot = new ArrayList<Item>();
    private final HashMap<String, Integer> adjacentRoomIds = new HashMap<String, Integer>();

    /**
     * Generates a Room from its JSONObject representation
     *
     * @param obj JSONObject representation of the Room
     */
    public Room(JSONObject obj) {
        String temp;
        this.name = obj.get("name").toString();
        this.shortDescription = obj.get("short_description").toString();
        this.longDescription = obj.get("long_description").toString();
        this.isStartingRoom = obj.get("start") != null;
        temp = obj.get("id").toString();
        this.id = Integer.parseInt(temp);
        initializeLoot(obj);
        mapAdjacentRooms(obj);
    }

    /**
     * Initializes all items present in the current room
     *
     * @param obj JSONObject representation of the Room
     */
    private void initializeLoot(JSONObject obj) { //Initializes all items in room
        JSONArray lootJson = (JSONArray) obj.get("loot");
        if (lootJson == null) { //room has no loot
            return;
        }
        JSONObject lootObject;
        for (int i = 0; i < lootJson.size(); i++) {
            lootObject = (JSONObject) lootJson.get(i);
            String temp = lootObject.get("id").toString();
            Item currentItem = Adventure.ID_TO_ITEM_MAP.get(Integer.parseInt(temp));
            if (currentItem != null) {
                currentItem.setContainingRoom(this);
            }
            loot.add(currentItem);
        }
    }

    /**
     * Creates a map of adjacent rooms in all directions relevant to the current
     * room
     *
     * @param obj JSONObject representation of the current room
     */
    private void mapAdjacentRooms(JSONObject obj) {
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

    /**
     * Adds an item to room and checks that various room aspects are valid
     * Checks that the direction is valid, item ID is valid, the room exists in
     * the dungeon, and that the room has an exit If room is invalid, throws
     * InvalidJSONFileException
     *
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
     *
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
     *
     * @return true if exit directions are valid, throws
     * InvalidJSONFileException otherwise
     */
    public boolean hasValidExitDirections() throws InvalidJSONFileException {
        for (String currentDirection : adjacentRoomIds.keySet()) {
            checkRoomAccessibility(currentDirection);
        }
        return true;
    }

    /**
     * Checks if a room can be accessed from its opposite direction Throws
     * InvalidJSONFileException if not accessible
     *
     * @param currentDirection The direction being checked
     */
    private void checkRoomAccessibility(String currentDirection) throws InvalidJSONFileException {
        Room connectedRoom = Adventure.ID_TO_ROOM_MAP.get(adjacentRoomIds.get(currentDirection));
        String oppositeDirection = getOppositeDirectionMap().get(currentDirection);
        if (connectedRoom.adjacentRoomIds.isEmpty()) {
            throw new InvalidJSONFileException("Corrupt JSON File - " + name + " has no exits.\n");
        } else if (connectedRoom.getAdjacentRoomIds().get(oppositeDirection) == null) { //Invalid direction
            throw new InvalidJSONFileException("Corrupt JSON File:\n"
                    + name + " can not be re-entered when exiting from the " + currentDirection + ".\n");
        } else if (!(connectedRoom.getAdjacentRoomIds().get(oppositeDirection) == id)) { //Invalid direction
            throw new InvalidJSONFileException("Corrupt JSON File:\n"
                    + name + " can not be re-entered when exiting from the " + currentDirection + ".\n");
        }
    }

    /**
     * Returns a map containing the opposite of a direction
     *
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
     *
     * @return true if room contains valid items, throws
     * InvalidJSONFileException otherwise
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
     *
     * @return true if room exits to valid rooms, throws
     * InvalidJSONFileException otherwise
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
     * Creates a map of adjacent rooms in all directions relevant to the current
     * room
     *
     * @param i The id of the room being added to the map
     * @param direction The direction needed to travel to reach the adjacent
     * room
     */
    public void mapAdjacentRooms(String direction, int i) {
        if (isValidDirection(direction)) {
            adjacentRoomIds.put(direction, i);
        }
    }

    /**
     * Determines if a direction is valid
     *
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
     * @return An item ArrayList containing all items in the room
     */
    public ArrayList<Item> getLoot() {
        return loot;
    }

    /**
     * @return true if the room is the starting room in the adventure, false otherwise
     */
    public boolean isStartingRoom() {
        return isStartingRoom;
    }

    /**
     * @return The name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * @return A short description of the current room
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @return A long description of the current room
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * The id is the unique identifier used to reference the room
     *
     * @return The id of the room
     */
    public int getId() {
        return id;
    }

    /**
     * @return A map containing the ids of all adjacent rooms
     */
    public HashMap<String, Integer> getAdjacentRoomIds() {
        return adjacentRoomIds;
    }

    /**
     * Returns the room adjacent to current room in a specified direction
     *
     * @param direction The requested direction
     * @return The adjacent room, or null if there is no room in the specified
     * direction
     */
    public Room getConnectedRoom(String direction) {
        if (adjacentRoomIds.containsKey(direction)) {
            return Adventure.ID_TO_ROOM_MAP.get(adjacentRoomIds.get(direction));
        }
        return null;
    }

    /**
     * @return String describing all items found in the room
     */
    private String getLootString() {
        if (loot.isEmpty()) {
            return "There are no items in this area.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("You have found the following items in this area:\n");
        for (int i = 0; i < loot.size(); i++) {
            sb.append(loot.get(i)).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Determines if an item is present in a room
     *
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
     * @param item The item to be removed
     * @return true if item was successfully removed, false otherwise
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
     * @return A short description of the room and the items it contains
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are now in: ").append(name).append("\n");
        sb.append(shortDescription).append("\n");
        sb.append(getLootString());
        return sb.toString();
    }

    /**
     * Adds an item to the loot contained within the room
     *
     * @param item The item to be added to the room's loot table
     */
    public void addLoot(Item item) {
        loot.add(item);
    }
}
