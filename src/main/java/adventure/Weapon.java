package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item which can be worn
*/
public class Weapon extends Item implements Tossable {

  /**
  * Creates a Weapon from its JSONObject representation
  * 
  * @param obj JSONObject representation of the item
  */
  public Weapon(JSONObject obj) {
    super(obj);
  }

  /**
  * 
  * @return A string containing details about tossing the item
  */
  @Override
  public String toss() {
    return "You toss " + super.getName() + ". You didn't need it anyway.\n";
  }
}
