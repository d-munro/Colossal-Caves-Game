package adventure;

//imports
import java.util.ArrayList;

/**
* Creates a Player object which stores various information about the player's game state
* @author Dylan Munro
* @version 2.0
*/
public class Player {

  private String name;
  private String saveGameName;
  private Room currentRoom;
  private ArrayList<Item> inventory;

  public Player() {
      this("");
  }
  
  /**
  * Constructor for Player class
  * @param n The player's name
  */
  public Player(String n) {
    this(n, "");
  }

  /**
  * Constructor for Player class
  * @param n The player's name
  * @param saveGameN The name of the file that the game is saved to
  */
  public Player(String n, String saveGameN) {
    this(n, saveGameN, null);
  }

  /**
  * Constructor for Player class
  * @param n The player's name
  * @param saveGameN The name of the file that the game is saved to
  * @param inv All items contained in the room
  */
  public Player(String n, String saveGameN, ArrayList<Item> inv) {
    this(n, saveGameN, inv, null);
  }

  /**
  * Constructor for Player class
  * @param n The player's name
  * @param saveGameN The name of the file that the game is saved to
  * @param inv All items contained in the room
  * @param room The current room of the adventure
  */
  public Player(String n, String saveGameN, ArrayList<Item> inv, Room room) {
    this.name = n;
    this.saveGameName = saveGameN;
    if (inv != null) {
      this.inventory = inv;
    } else {
      this.inventory = new ArrayList<Item>();
    }
    this.currentRoom = room;
  }

  //getters

  /**
  * Accessor method for the player's name
  * @return The player's name
  */
  public String getName() {
    return name;
  }

  /**
  * Accessor method for the player's inventory
  * @return All items currently in the player's inventory
  */
  public ArrayList<Item> getInventory() {
    return inventory;
  }

  /**
  * Returns all items in the player's inventory as a String representation
  * @return A string containing all items in the player's inventory
  */
  public String getInventoryString() {
    String returnedString = "";
    for (Item currentItem : inventory) {
      if (currentItem instanceof Wearable) {
        if (((Clothing) currentItem).getWornStatus()) {
          returnedString += "(Currently worn item) ";
        }
      }
      returnedString += currentItem.toString() + "\n\n";
    }
    return returnedString;
  }

  /**
  * @return The current room of the player's adventure
  */
  public Room getCurrentRoom() {
    return currentRoom;
  }

  /**
  * @return The save file name
  */
  public String getSaveGameName() {
    return saveGameName;
  }

  /**
  * @return The player's name, a short description of the current room,
  * and all items contained in the current room
  */
  @Override
  public String toString() {
    return name + currentRoom.toString();
  }
  /*End of mandatory methods*/

  /**
  * @param newItem The item being added to the player's inventory
  */
  public void addToInventory(Item newItem) {
    inventory.add(newItem);
  }

  /**
  * @param item The item being removed from the player's inventory
  */
  public void removeFromInventory(Item item) {
    inventory.remove(item);
  }
  //setters

  /**
  * @param room The current room of the player's adventure
  */
  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  /**
  * @param name The player's name
  */
  public void setName(String name) {
    this.name = name;
  }

  /**
  * @param inv ArrayList containing all items in the players inventory
  */
  public void setInventory(ArrayList<Item> inv) {
    this.inventory = inv;
  }

  /**
  * Sets the name of the file being saved to
  * @param saveGameN The name of the save file
  */
  public void setSaveGameName(String saveGameN) {
    this.saveGameName = saveGameN;
  }

  /**
  * Determines if an item is present in a player's inventory
  * @param item The item in question
  * @return true if the item is present in player's inventory, false otherwise
  */
  public boolean hasItem(Item item) {
      for (Item currentItem : inventory) {
        if (currentItem.getId() == item.getId()) {
          return true;
        }
      }
      return false;
  }

}
