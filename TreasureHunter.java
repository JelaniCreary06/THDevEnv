/**
* This class is responsible for controlling the Treasure Hunter game.<p>
* It handles the display of the menu and the processing of the player's choices.<p>
* It handles all of the display based on the messages it receives from the Town object.
*
*/
import java.util.Scanner;



public class TreasureHunter
{

  private static String[] treasureList = new String[3];
  //Instance variables
  private Town currentTown;
  private Hunter hunter;
  private boolean hardMode, cheatMode, isGameOver;
  private Scanner scanner;
  
  //Constructor
  /**
  * Constructs the Treasure Hunter game.
  */
  public TreasureHunter()
  {
    // these will be initialized in the play method
    currentTown = null;
    hunter = null;
    hardMode = false;
    isGameOver = false;
   treasureList[0] = "Excalibur";
    treasureList[1] = "Demon Spear";
    treasureList[2] = "Treasure 3";

    scanner = new Scanner(System.in);
  }

  /** 
  * Returns the array of treasures.
  */
  public static String[] getTreasureList() { return treasureList; }

  /**
* This returns a random treasure from the treasure list.
*/
  private String getRandomTreasure() {
    return treasureList[(int) (Math.random() * treasureList.length)];
  }

  
  // starts the game; this is the only public method
  public void play ()
  {
    
    welcomePlayer();
    enterTown();
    showMenu();
  }
  
  /**
  * Creates a hunter object at the beginning of the game and populates the class member variable with it.
  */
  private void welcomePlayer()
  {
    System.out.println("Welcome to TREASURE HUNTER!");
    System.out.println("Going hunting for the big treasure, eh?");
    System.out.print("What's your name, Hunter? ");
    String name = scanner.nextLine();
    
    // set hunter instance variable
    hunter = new Hunter(name, 10);
    
    System.out.print("Hard mode? (y/n): ");
    String hard = scanner.nextLine();
    if (hard.equals("y") || hard.equals("Y")) 
    {
      hardMode = true;
    } else if (hard.equals("WASD")) cheatMode = true;

    scanner.close();
  }

  /**
* Sets the boolean instance variable isGameOver to true
*/
  public void gameOver() { this.isGameOver = true;}
  
  /**
  * Creates a new town and adds the Hunter to it.
  */
  private void enterTown()
  {
    double markdown = 0.25;
    double toughness = 0.4;
    if (hardMode)
    {
      // in hard mode, you get less money back when you sell items
      markdown = 0.5;
      
      // and the town is "tougher"
      toughness = 0.75;
    }
    
    // note that we don't need to access the Shop object
    // outside of this method, so it isn't necessary to store it as an instance
    // variable; we can leave it as a local variable

    Shop shop;
    if (cheatMode) {
      shop = new Shop(markdown, cheatMode);
      currentTown = new Town(this, shop, toughness, cheatMode);
    } else {
      shop = new Shop(markdown);
      currentTown = new Town(this, shop, toughness);
    }
        
    // creating the new Town -- which we need to store as an instance
    // variable in this class, since we need to access the Town
    // object in other methods of this class
  
    
    // calling the hunterArrives method, which takes the Hunter
    // as a parameter; note this also could have been done in the
    // constructor for Town, but this illustrates another way to associate
    // an object with an object of a different class
    currentTown.hunterArrives(hunter);
  }

  /**
* Checks whether the hunter has all 3 treasures to end the game.
*/
  private boolean checkTreasures() {
    return (hunter.hasTreasure(treasureList[0]) &&hunter.hasTreasure(treasureList[1])&&hunter.hasTreasure(treasureList[2]));
  }
   
  /**
   * Displays the menu and receives the choice from the user.<p>
   * The choice is sent to the processChoice() method for parsing.<p>
   * This method will loop until the user chooses to exit.
   */
  private void showMenu()
  {
    
    if (!isGameOver) {
       String choice = "";
      
      while (!(choice.equals("X") || choice.equals("x")))
    {
       
      System.out.println();
      System.out.println(currentTown.getLatestNews());
      System.out.println("***");
      System.out.println(hunter);
      System.out.println(currentTown);
      System.out.println("(B)uy something at the shop.");
      System.out.println("(S)ell something at the shop.");
      System.out.println("(M)ove on to a different town.");
      System.out.println("(L)ook for trouble!");
      System.out.println("Give up the hunt and e(X)it.");
      choice = scanner.nextLine();
      System.out.println("input: " + choice);
      if (!currentTown.treasureFound()) System.out.println("(H)unt for treasure!");
      System.out.println();
      System.out.print("What's your next move? ");
      choice = scanner.nextLine();
      choice = choice.toUpperCase();
      processChoice(choice);
      if (!choice.equals("L")) {
        System.out.print("Hit Enter to continue! ");
        scanner.nextLine();
      }
      TreasureHunter.clearScreen();
       scanner.nextLine();
    }
     
    }
     else {
      if (!hunter.checkGold()) {
       
        System.out.println("You lost a brawl and have 0 gear, game over!");
      } else if (checkTreasures()) {
        
        System.out.println("You found all 3 treasures!\nYou win!");
      }
    }
  }
   
  /**
  * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
  * @param choice The action to process.
  */
  private void processChoice(String choice)
  {
    if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s"))
    {
      currentTown.enterShop(choice);
    }
    else if (choice.equals("M") || choice.equals("m"))
    {
      if (currentTown.leaveTown())
      {
      //This town is going away so print its news ahead of time.
        System.out.println(currentTown.getLatestNews());
        enterTown();
      }
    }
    else if (choice.equals("L") || choice.equals("l"))
    {
      currentTown.lookForTrouble();
    }
    else if (choice.equals("X") || choice.equals("x"))
    {
      System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
    }
    else if (choice.equals("H") || choice.equals("h")) {
        if (!currentTown.treasureFound()) {
          int chance = (int) (Math.random() * 2);

          if (chance == 0) System.out.println("You found nothing!");
          else {
            String t = getRandomTreasure();
            currentTown.treasureFound(true);
            hunter.addTreasure(t);

            System.out.println("You found the treasure \"" + t + "\" !");
          }
        } else System.out.print("You can only find 1 treasure per town!");
      }
    else
    {
      System.out.println("Yikes! That's an invalid option! Try again.");
    }
  }

  public static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
  
}