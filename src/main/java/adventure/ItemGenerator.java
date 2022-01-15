package adventure;

//imports
import org.json.simple.JSONObject;

/**
* Creates an ItemGenerator object which creates Items of the correct class type
* based on the interfaces that they implement
* @author Dylan Munro
* @version 1.0
*/
public class ItemGenerator {

    /**
    * Default Constructor
    */
    public ItemGenerator() {

    }

    /**
    * Creates an item from a JSONObject based on the interfaces it will implement
    * @param obj The JSONObject in question
    * @return The name of the class the object will become
    */
    public Item generateItem(JSONObject obj) {
      if (getFood(obj) != null) {
        return getFood(obj);
      } else if (getClothing(obj) != null) {
        return getClothing(obj);
      } else if (willImplementReadable(obj)) { //Spell
        return new Spell(obj);
      } else if (willImplementTossable(obj)) { //Weapon
        return new Weapon(obj);
      }
      return new Item(obj);
    }

    /**
    * Determines if a JSON object will be of type food
    * @param obj The JSON object in question
    * @return The reference to the food item if the JSON object will be food, null otherwise
    */
    private Item getFood(JSONObject obj) {
      if (willImplementEdible(obj)) { //Food or SmallFood
        if (willImplementTossable(obj)) { //SmallFood
          return new SmallFood(obj);
        }
        return new Food(obj);
      }
      return null;
    }

    /**
    * Determines if a JSON object will be of type Clothing
    * @param obj The JSON object in question
    * @return The reference to the clothing item if the JSON object will be clothing, null otherwise
    */
    private Item getClothing(JSONObject obj) {
      if (willImplementWearable(obj)) { //Clothing or BrandedClothing
        if (willImplementReadable(obj)) { //BrandedClothing
          return new BrandedClothing(obj);
        }
        return new Clothing(obj);
      }
      return null;
    }

    /**
    * Determines if the item created from a JSONObject will implement the edible interface
    * @param obj The JSONObject
    * @return true if the JSONObject will implement the edible interface, false otherwise
    */
    private boolean willImplementEdible(JSONObject obj) {
      if (obj.get("edible") == null) {
        return false;
      }
      return true;
    }

    /**
    * Determines if the item created from a JSONObject will implement the tossable interface
    * @param obj The JSONObject
    * @return true if the JSONObject will implement the tossable interface, false otherwise
    */
    private boolean willImplementTossable(JSONObject obj) {
      if (obj.get("tossable") == null) {
        return false;
      }
      return true;
    }

    /**
    * Determines if the item created from a JSONObject will implement the readable interface
    * @param obj The JSONObject
    * @return true if the JSONObject will implement the readable interface, false otherwise
    */
    private boolean willImplementReadable(JSONObject obj) {
      if (obj.get("readable") == null) {
        return false;
      }
      return true;
    }

    /**
    * Determines if the item created from a JSONObject will implement the wearable interface
    * @param obj The JSONObject
    * @return true if the JSONObject will implement the wearable interface, false otherwise
    */
    private boolean willImplementWearable(JSONObject obj) {
      if (obj.get("wearable") == null) {
        return false;
      }
      return true;
    }
}
