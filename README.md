##Note
This program was an individual assignment completed in CIS*2430 in the Summer 2020 semester. The source code was last updated in June 2020

## Author Information

* Name: Dylan Munro

## Program Operation
To compile the files required for program execution, enter the project folder, in the same level as the pom.xml file
  *Obtain these folders by cloning them from the A3 branch of the socis gitlab

To compile with just using maven, use the following commands:
1) mvn compile
2) mvn exec:java
3) mvn clean

Or, execute the following commands to compile with a jar file:
1) mvn compile
2) mvn assembly:assembly
3) java -jar target/2430_A2-1.0-jar-with-dependencies.jar
  *To load a file different than the default file, execute -a <fileName> after the .jar
  *To load a saved file, execute -l <fileName> after the .jar
    *This file should be in the same directory as the pom.xml file
4) mvn clean (remove unnecessary files from folder after executing)


### Instructions for using the program
Loading the program:
Upon loading the GUI, the full GUI will be visible, but only the game settings button is enabled
*Either select to load a new JSON file or load the default json file (The load saved file and saving the game can't load files)
  *The default adventure file is haunted_forest.json

The main game:
The user can enter 9 commands:
  *go (followed by n, s, e, w, up, or down), to move to a new room. If that room does not exist, the user will stay in the current room. 
	A message will tell the user that there is no room in that direction
  *look (followed by an item or room name), to view a description of an item, or a more detailed description of a room. 
    The look command only works if the user is present in the room being looked at, 
      or the item being looked at is in the users inventory
  *inventory, to view all items in a players inventory
  *take, to take an item from a room. The item must be present in the room to take it
  *quit, to terminate the program
  *eat, to eat an item if it implements the edible interface
    *The only 2 classes that are edible are the Food and SmallFood classes
    *Upon eating an item, it will permanently disappear from the game
  *toss, to throw an item on the ground if the current room if it implements the tossable interface. 
    The only 2 classes which implement the tossable interface are the SmallFood and Weapon classes
  *wear, to equip an item if it implements the wearable inteface. 
    ANY EQUIPMENT WORN WILL SAY "Currently worn item" IN BRACKETS BESIDE THE ITEM NAME 
    The only 2 classes which implement the wearable interface are the clothing and BrandedClothing interfaces 
  *read, to read the text on an item if it implements readable
    The only 2 classes which implement readable are BrandedClothing and spell
*When reading input from the user, the program is case sensitive
*The program assumes that no two objects have the same id


## Statement of Individual Work

By the action of submitting this work for grading, I certify that this assignment is my own work, based on my personal study.  I also certify that no parts of this assignment have previously been submitted for assessment in any other course, except where specific permission has been granted.  Finally, I certify that I have not copied in part or whole  or otherwise plagiarised the work of other persons.
