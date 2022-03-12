package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item which can be eaten
*/
public class Food extends Item implements Edible {

  /**
  * Creates a Food Item from its JSONObject representation
  * @param obj JSONObject representation of the food item
  */
  public Food(JSONObject obj) {
    super(obj);
  }

  /**
  * @return Message informing the user that they have successfully eaten the food
  */
  @Override
  public String eat() {
    return "You eat " + super.getName() + ". Delicious!\n";
  }
}
