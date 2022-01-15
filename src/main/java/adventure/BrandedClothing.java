package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Clothing that can be read and implements the Readable interface
*/
public class BrandedClothing extends Clothing implements Readable {

  /**
  * Default Constructor
  */
  public BrandedClothing() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the branded clothing is being made from
  */
  public BrandedClothing(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player reading the branded clothing
  * @return A string containing details about reading the branded clothing
  */
  public String read() {
    return "You read the label on " + super.getName() + ".\n"
    + "It says \"Supreme\"\n";
  }

  /**
  * Returns the branded clothing's name and a short description
  * @return The branded clothing's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

}
