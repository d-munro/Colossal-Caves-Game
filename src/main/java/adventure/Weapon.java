package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of item that can be tossed and implements the Tossable interface
*/
public class Weapon extends Item implements Tossable {

  /**
  * Default Constructor
  */
  public Weapon() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the item is being made from
  */
  public Weapon(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player tossing the item
  * @return A string containing details about tossing the item
  */
  public String toss() {
    return "You toss " + super.getName() + ". You didn't need it anyway.\n";
  }

  /**
  * Returns the item's name and a short description
  * @return The item's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

}
