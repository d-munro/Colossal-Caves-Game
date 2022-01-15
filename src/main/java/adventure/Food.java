package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates a subclass of Item that can be eaten and implements the Edible interface
*/
public class Food extends Item implements Edible {

  /**
  * Default Constructor
  */
  public Food() {
    super(null);
  }

  /**
  * One parameter constructor
  * @param obj The JSONObject that the Food is being made from
  */
  public Food(JSONObject obj) {
    super(obj);
  }

  /**
  * Returns text describing the player eating the Food
  * @return A string containing details about eating the Food
  */
  public String eat() {
    return "You eat " + super.getName() + ". Delicious!\n";
  }

  /**
  * Returns the Food's name and a short description
  * @return The Food's name and a short description
  * @Override
  */
  public String toString() {
    return super.toString();
  }

}
