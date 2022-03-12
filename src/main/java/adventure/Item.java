package adventure;

//imports
import org.json.simple.JSONObject;

/**
 * Contains necessary information for each item in the adventure
 *
 * @author Dylan Munro
 * @version 2.0
 */
public class Item {

    private final String name;
    private final String description;
    private final int id;
    private Room containingRoom = null; //Room where item is initially located

    /**
     * Creates an Item from its JSONObject representation
     *
     * @param obj JSONObject representation of the item
     */
    public Item(JSONObject obj) {
        this.name = obj.get("name").toString();
        this.description = obj.get("desc").toString();
        String temp = obj.get("id").toString();
        this.id = Integer.parseInt(temp);
    }

    /**
     * Sets the room which contains the current item as loot
     *
     * @param room The room containing the item as loot
     */
    public void setContainingRoom(Room room) {
        this.containingRoom = room;
    }

    /**
     * @return The name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * @return A long description of the item
     */
    public String getLongDescription() {
        return description;
    }

    /**
     * The id of the item is a unique identifier used to reference it
     * 
     * @return The id of the item
     */
    public int getId() {
        return id;
    }

    /**
     * @return The room where the item is located
     */
    public Room getContainingRoom() {
        return containingRoom;
    }

    /**
     * @return The item's name and a short description of it
     */
    @Override
    public String toString() {
        return "Item Name: " + name + "\nItem Description: " + description;
    }

}
