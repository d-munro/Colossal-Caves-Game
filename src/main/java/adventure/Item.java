package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Contains necessary info for each item in the adventure
* @author Dylan Munro
* @version 2.0
*/
public class Item{

    //instance var declarations
    private String name;
    private String description;
    private int id;
    private Room containingRoom = null;

    /**
    * Default constructor
    */
    public Item() {
        this.name = "";
        this.description = "";
        this.id = 0;
        this.containingRoom = null;
    }

    /**
    * Constructor
    * @param obj The JSON object containing all relevant info about the item
    */
    public Item(JSONObject obj) {
      this.name = obj.get("name").toString();
      this.description = obj.get("desc").toString();
      String temp = obj.get("id").toString();
      this.id = Integer.parseInt(temp);
    }

    /**
    * Sets the room which contains the current item as loot
    * @param room The room containing the item as loot
    */
    public void setContainingRoom(Room room) {
      this.containingRoom = room;
    }

    /**
    * Sets the name of the current item
    * @param itemName The name of the item
    */
    public void setName(String itemName) {
      this.name = itemName;
    }
    /* required public methods */

    /**
    * Sets the description of the current item
    * @param itemDescription The description of the item
    */
    public void setDescription(String itemDescription) {
      this.description = itemDescription;
    }

    /* required public methods */
    /**
    * Returns the name of the item
    * @return The name of the item
    */
    public String getName() {
      return name;
    }

    /**
    * Returns a long description of the item
    * @return A long description of the item
    */
    public String getLongDescription() {
      return description;
    }

    /**
    * Returns the id of an item
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
     * The id is a unique identifier used to reference the item
     * 
    * @param id The id associated with the item
    */
    public void setId(int id) {
      this.id = id;
    }

    /**
    * Returns the item's name and a short description of it
    * @return The item's name and a short description of it
    */
    public String toString() {
      return "Item Name: " + name + "\nItem Description: " + description;
    }

}
