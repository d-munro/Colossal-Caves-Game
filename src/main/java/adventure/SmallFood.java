package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Food which can be tossed
*/
public class SmallFood extends Food implements Tossable {

  /**
  * Creates a small piece of food from its JSONObject representation
  * @param obj JSONObject representation of small food object
  */
  public SmallFood(JSONObject obj) {
    super(obj);
  }

  /**
  * @return Message informing users that they have tossed the food
  */
  @Override
  public String toss() {
    return "You toss " + super.getName() + " onto the ground.\n";
  }
}
