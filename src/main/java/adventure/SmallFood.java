package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Food that can be tossed and implements the Tossable interface
*/
public class SmallFood extends Food implements Tossable {

  /**
  * Default Constructor
  */
  public SmallFood() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the SmallFood is being made from
  */
  public SmallFood(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player tossing the SmallFood
  * @return A string containing details about tossing the SmallFood
  */
  public String toss() {
    return "You toss " + super.getName() + " onto the ground.\n";
  }

  /**
  * Returns the SmallFood's name and a short description
  * @return The SmallFood's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

}
