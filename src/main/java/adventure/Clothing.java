package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item which can be worn
*/
public class Clothing extends Item implements Wearable {

  private boolean wornStatus = false;

  /**
  * Creates a Clothing Item from its JSONObject representation
  * @param obj JSONObject representation of Clothing Item
  */
  public Clothing(JSONObject obj) {
    super(obj);
  }

  /**
  * @return Message informing the user that they successfully equipped the clothing
  */
  @Override
  public String wear() {
    return "You wear " + super.getName() + ". It fits quite well.\n";
  }
  
  /**
  * Changes an objects worn status
  * @param status The new status of the item (true if worn, false if not)
  */
  public void setWornStatus(boolean status) {
    this.wornStatus = status;
  }

  /**
  * @return The status of the clothing article
  */
  public boolean getWornStatus() {
    return wornStatus;
  }
}
