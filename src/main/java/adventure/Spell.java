package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item which can be read
*/
public class Spell extends Item implements Readable {

  /**
  * Creates a Spell from its JSONObject representation
  * @param obj JSONObject representation of the spell
  */
  public Spell(JSONObject obj) {
    super(obj);
  }

  /**
  * @return String informing users that they have read the spell
  */
  @Override
  public String read() {
    return "You read the text to cast " + super.getName() + ".\n";
  }
}
