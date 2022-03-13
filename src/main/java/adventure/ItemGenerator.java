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
    * Creates an item from a JSONObject based on the interfaces it will implement
    * @param obj The JSONObject in question
    * @return The name of the class the object will become
    */
    public Item generateItem(JSONObject obj) {
      if (getFood(obj) != null) {
        return getFood(obj);
      } else if (getClothing(obj) != null) {
        return getClothing(obj);
      } else if (obj.get("readable") != null) { //Spell
        return new Spell(obj);
      } else if (obj.get("tossable") != null) { //Weapon
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
      if (obj.get("edible") != null) { //Food or SmallFood
        if (obj.get("tossable") != null) { //SmallFood
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
      if (obj.get("wearable") != null) { //Clothing or BrandedClothing
        if (obj.get("readable") != null) { //BrandedClothing
          return new BrandedClothing(obj);
        }
        return new Clothing(obj);
      }
      return null;
    }
}
