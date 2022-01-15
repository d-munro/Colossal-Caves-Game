package adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class RoomTest {
  private Game theGame = new Game();

    @Before
    public void setup(){
    }

    @Test
    public void testValidRooms() {
      System.out.println("Testing adventure containing all valid rooms");
      JSONObject validJson = theGame.loadAdventureJson("src/main/resources/haunted_forest.json");
      Adventure validAdventure = new Adventure(validJson);
      /*ArrayList<Room> roomList = validAdventure.getRoomList();
      try {
        for (Room currentRoom : roomList) {
          assertTrue(currentRoom.isValidRoom() == true);
        }
      } catch (InvalidJSONFileException e) {
        fail(e.getMessage());
      }*/
    }

    @Test
    public void testRoomWithNoExits() {
      System.out.println("Testing adventure containing rooms with no exits");
      JSONObject noExitJson = theGame.loadAdventureJson("src/main/resources/no_exits.json");
      Adventure noExitAdventure = new Adventure(noExitJson);
      ArrayList<Room> roomList = noExitAdventure.getRoomList();
      try {
        for (Room currentRoom : roomList) {
          currentRoom.isExitable();
        }
      } catch (InvalidJSONFileException e) {
        assertTrue(e.getMessage().compareTo("Corrupt JSON File - The forest center has no exits.\n") == 0);
      }
    }

    @Test
    public void testRoomWithMismatchedExits() {
      System.out.println("Testing adventure containing rooms where exits do not match");
      JSONObject mismatchedExitJson = theGame.loadAdventureJson("src/main/resources/mismatched_exits.json");
      Adventure mismatchedExitAdventure = new Adventure(mismatchedExitJson);
      ArrayList<Room> roomList = mismatchedExitAdventure.getRoomList();
      try {
        for (Room currentRoom : roomList) {
          currentRoom.hasValidExitDirections();
        }
      } catch (InvalidJSONFileException e) {
        assertTrue(e.getMessage().compareTo("Corrupt JSON File:\n"
        + "The deep forest can not be re-entered when exiting from the E.\n") == 0);
      }
    }

    @Test
    public void testRoomWithInvalidItems() {
      System.out.println("Testing adventure containing items that don't exist in dungeon");
      JSONObject invalidItemsJson = theGame.loadAdventureJson("src/main/resources/invalid_items.json");
      Adventure invalidItemsAdventure = new Adventure(invalidItemsJson);
      ArrayList<Room> roomList = invalidItemsAdventure.getRoomList();
      try {
        for (Room currentRoom : roomList) {
          currentRoom.hasValidLoot();
        }
      } catch (InvalidJSONFileException e) {
        assertTrue(e.getMessage().compareTo("Corrupt JSON File:\n"
        + "A bear trap contains items that do not exist in the adventure.\n") == 0);
      }
    }

    @Test
    public void testRoomWithInvalidId() {
      System.out.println("Testing adventure containing an exit to a room with an invalid ID");
      JSONObject invalidRoomIdJson = theGame.loadAdventureJson("src/main/resources/invalid_room_ids.json");
      Adventure invalidRoomIdAdventure = new Adventure(invalidRoomIdJson);
      ArrayList<Room> roomList = invalidRoomIdAdventure.getRoomList();
      try {
        for (Room currentRoom : roomList) {
          currentRoom.exitsToValidRoom();
        }
      } catch (InvalidJSONFileException e) {
        assertTrue(e.getMessage().compareTo("Corrupt JSON File:\n"
        + "A bush exits to invalid room ID.\n") == 0);
      }
    }

}
