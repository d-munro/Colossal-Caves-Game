package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Clothing which can be read
*/
public class BrandedClothing extends Clothing implements Readable {

  /**
  * Creates a BrandedClothing Item from its JSONObject representation
  * @param obj JSONObject representation of BrandedClothing
  */
  public BrandedClothing(JSONObject obj) {
    super(obj);
  }

  /**
  * @return Message informing the user of the clothing's brand
  */
  @Override
  public String read() {
    return "You read the label on " + super.getName() + ".\n"
    + "It says \"Supreme\"\n";
  }
}
