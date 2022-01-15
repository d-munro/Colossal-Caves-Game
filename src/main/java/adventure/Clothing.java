package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item that can be worn and implements the Wearable interface
*/
public class Clothing extends Item implements Wearable {

  private boolean wornStatus = false;

  /**
  * Default Constructor
  */
  public Clothing() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the clothing is being made from
  */
  public Clothing(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player wearing the clothing
  * @return A string containing details about wearing the clothing
  */
  public String wear() {
    return "You wear " + super.getName() + ". It fits quite well.\n";
  }

  /**
  * Returns the clothing's name and a short description
  * @return The clothing's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

  /**
  * Changes an objects worn status
  * @param status The new status of the item (true if worn, false if not)
  */
  public void setWornStatus(boolean status) {
    this.wornStatus = status;
  }

  /**
  * Returns the worn status of a clothing article
  * @return The status of the clothing article
  */
  public boolean getWornStatus() {
    return wornStatus;
  }

}
