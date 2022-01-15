package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Spell that can be read and implements the Readable interface
*/
public class Spell extends Item implements Readable {

  /**
  * Default Constructor
  */
  public Spell() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the spell is being made from
  */
  public Spell(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player reading the spell
  * @return A string containing details about reading the spell
  */
  public String read() {
    return "You read the text to cast " + super.getName() + ".\n";
  }

  /**
  * Returns the Spell's name and a short description
  * @return The Spell's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

}
