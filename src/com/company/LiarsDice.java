package com.company;

import java.util.*;

public class LiarsDice {
    public List<Player> players = new ArrayList<>();
    public Scanner scanner = new Scanner(System.in);
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 6;
    public int[] currentBid = new int[2]; // compare the scanned in values to the previous scanned in values
    public String wasLiar;
    public int numberOfPlayers;
    public static Map<Integer, Integer> freq = new HashMap<>();
    public int activePlayerIndex = 0; // starts at index 0 bc first player to sign up

    public LiarsDice() {
        do {
            System.out.println("How many players (2 through 6)?");
            scanForIntOnly();
            numberOfPlayers = scanner.nextInt();
            scanner.nextLine();
        } while (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS);
        System.out.println("How many dice would you like to play with?");
        do {
            scanForIntOnly();
            Cup.numberOfDice = scanner.nextInt();
            scanner.nextLine();
        } while (Cup.numberOfDice == 0);
        // ADD PLAYER TO LIST WITH NAME
        while (players.size() < numberOfPlayers) {
            System.out.println("What is your name?");
            players.add(new Player((scanner.nextLine().trim())));
        }
    }

    public void play() {
        while (players.size() > 1) {  // game doesn't end until player list size is no longer
            // greater than 1 => rounds continue => loops through game
            round();
        }
        System.out.println(players.get(0).name + "'s the Winner,\n*******Winner, Winner, Chicken dinner!!!!*******");
        // end the game by grabbing the last player in players
    }

    public void round() {  // the while loop transitions from player to player's turn until liar is called.
        rollAll();
        boolean isTurnOne = true;  // safeguard => no prompt if liar prior to bidding. First turn of round only!
        boolean isRoundOver = false; // callLiar() returns true when liar has been selected
        while (!isRoundOver) { // while loop continues until liar method returns True.
            System.out.println(getActivePlayer().name + "'s turn"); // getActivePlayer default is 0 index
            System.out.println(getActivePlayer().cup.displayCup()); //display their cup
            if (!isTurnOne) {    // turn one is true not prompting the user if they lied.
                isRoundOver = callLiar(getActivePlayer()); // after turn one => callLiar() method is invoked
            } else {
                isTurnOne = false; // sets isTurn to false => next loop => step into   if (!isTurnOne).
            }
            if (isRoundOver) {
                break;
            }
            turnController(getActivePlayer()); // invoke turn and need to pass getActivePlayer() method as the argument.
        }
        zeroOutCurrentBid();  // need to start round over and this includes setting the current bids back to zeros
        clearFreq();  // clear the freq map because a new roll and dice amounts will happen if no winner is declared.
        removePlayer(); // make sure all players still have die. If they do not, remove the player from the list!
    }

    public void rollAll() {
        for (Player activePlayer : players) { //assign the activePlayer to each player in players list.
            activePlayer.cup.roll(); // roll player's dice
            diceFreqMap(activePlayer.cup.dice);//  take the active players dice and add to freq map
        }
        System.out.println("New Roll!!!!"); // print out new roll
        System.out.println("There are " + numberOfDiceInPlay() + " dice in play now."); // announce the number of
        // dice in play.
    }

    public Player getActivePlayer() { // returns active players index pointer out of players list. **Player is the data
        // type.
        return players.get(activePlayerIndex); // getting players index that has players reference pointer.
        // activePlayerIndex is a static int that is set to 0 to start. You need this to track this player through
        // turn.
    }

    public void turnController(Player activePlayer) {
        getSelections(activePlayer); // invoke getSelections for the active player
        setNextPlayersTurn(); // invoke this method to increment the activePlayerIndex variable.
        clearScreen(); // add lines to the screen to clear.
    }

    public void getSelections(Player activePlayer) { // logic for player entering bid
        int valueBid0 = 0; //  initial value
        int qtyBid1 = 0;  // initial value

        System.out.println(activePlayer.name + " select a die value.");
        scanForIntOnly(); //check if the player entered an integer
        valueBid0 = scanner.nextInt();

        System.out.println(activePlayer.name + " please enter the quantity of that die value");
        scanForIntOnly(); //check if the player entered an integer
        qtyBid1 = scanner.nextInt();

        if (currentBid[0] == 0) {  // first bid or new ROUND
            currentBid[0] = valueBid0; // WHEN YOU START GAME current bid is  = 0
            currentBid[1] = qtyBid1;// WHEN YOU START GAME current bid is  = 0
        } else {
            isValidSelection(activePlayer, qtyBid1, valueBid0); // After first round => checking if valid bids
        }
    }

    public void scanForIntOnly() {
        while (!scanner.hasNextInt()) {  // while loop = prompt user if an int wasn't entered
            System.out.println("Input is not a number!!!!");
            scanner.nextLine();
        }
    }


    public void setNextPlayersTurn() {
        if (activePlayerIndex == (players.size() - 1)) { // testing if activePlayer index is equal to the size of the
            // list and - 1 for that index. This will set the new activePlayer to zero => start at the beginning again.
            activePlayerIndex = 0;
        } else {
            activePlayerIndex += 1; //activePlayer is increment to the next player in the list.
        }
    }

    public int numberOfDiceInPlay() { // counts the values is the hash map.
        int num2 = 0;
        for (Integer num : freq.values()) {
            num2 = num + num2;
        }
        return num2;
    }

    public void removePlayer() { //
        for (int i = 0; i < players.size(); i++) { // setup for loop.
            if (players.get(i).cup.dice.size() < 1) { //go through the list of players and verify they have more than
                // one die.
            if (i == players.size() - 1) { // if i (index of that player) is equal to the last players index (using
                // player.size - 1 for the index)  - you need to setNextPlayerTurn
                setNextPlayersTurn();
            }
                players.remove(i); // remove that player i
                break;
            }
        }

    }


    public void clearScreen() {
        for (int i = 0; i < 4; i++) {
            System.out.println("\n");
        }
    }

    public void zeroOutCurrentBid() {  //used after liar has need called and the while loop in round is exited.
        currentBid[0] = 0;
        currentBid[1] = 0;
    }

    public boolean callLiar(Player activePlayer) {  // call liar logic
        System.out.println("Take a guess - Did " + getPreviousPlayer().name + " lie (y/n)? ");
        wasLiar = scanner.next();
        if (wasLiar.equals("y") || wasLiar.equals("yes")) {
            System.out.println(activePlayer.name + " guessed yes that " + getPreviousPlayer().name + " lied!");

            if ((freq.get(currentBid[0]) == null) || (freq.get(currentBid[0]) < currentBid[1]))//qty check => to
            // retrieve qty in freq map, you need to pass the value and the get() will return the qty of the
            //  key. The null test condition needed to be tested first!
            {
                System.out.println(getPreviousPlayer().name + " LIED and loses a die! \nBelow has the list of " +
                        "dice values and quantities for that round.");
                System.out.println(freq);
                System.out.println(getPreviousPlayer().name + " lost a die.");
                getPreviousPlayer().cup.removeDie(); // need to use the getPrevious method to shift the die removal.
                return true; // get out of round() loop
            } else {
                System.out.println(activePlayer.name + " is so wrong! " + activePlayer.name + " shall lose a die. " +
                        " \nBelow has the list of dice values and quantities for that round.");
                System.out.println(freq);
                System.out.println(activePlayer.name + " lost a die.");
                activePlayer.cup.removeDie();
                return true; // get out of round() loop
            }
        }
        return false;
    }

    public Player getPreviousPlayer() {  // need to remove a die in callLiar.
        if (activePlayerIndex == 0) { // if their index is 0 => you need to get to the largest index
            return players.get(players.size() - 1);
        } else {
            return players.get(activePlayerIndex - 1);
        }
    }

    public void isValidSelection(Player activePlayer, int currentQty, int currentValue) {
        int previousValue = currentBid[0];
        int previousQty = currentBid[1];

        if (currentQty > previousQty) {
            // below; needed to set bc if bid was accepted - the next bid need to compare to
            // that bid.
            System.out.println("valid quantity");
            currentBid[0] = currentValue;
            currentBid[1] = currentQty;

        } else if (previousQty == currentQty && currentValue > previousValue) {
            System.out.println("valid value");
            currentBid[0] = currentValue;

        } else {
            System.out.println("Try again - invalid bid!");
            getSelections(activePlayer);
        }
    }

    public void diceFreqMap(List<Die> dice) {  // frequency of dice OR quantities of each die.
        for (Die die : dice) {
            if (!freq.containsKey(die.faceUpValue)) {
                freq.put(die.faceUpValue, 1);
            } else {
                freq.put(die.faceUpValue, freq.get(die.faceUpValue) + 1);
            }
        }
    }

    public void clearFreq() {  // clear the map bc a die has left the game AND a new round will start with a new freq
        // map reflecting the new freq map.
        freq.clear();
    }
}







