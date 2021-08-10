package com.company;

public class Validate {

    public  void scanForIntOnly() {
        while (!LiarsDice.scanner.hasNextInt()) {  // while loop = prompt user if an int wasn't entered
            System.out.println("Input is not a number!!!!");
            LiarsDice.scanner.nextLine();
        }
    }

    public void scanForStringOnly() {
        while (LiarsDice.scanner.hasNextInt()) {
            System.out.println("Please input yes or no.");
            LiarsDice.scanner.nextLine();
        }
    }

    public void isValidBid(Player activePlayer, int currentValue, int currentQty) {
        int previousValue = LiarsDice.currentBid[1] ;
        int previousQty = LiarsDice.currentBid[1];
        if (currentQty > previousQty) {
            System.out.println("valid quantity");
            LiarsDice.currentBid[0] = currentValue;
            LiarsDice.currentBid[1] = currentQty;
        } else if (currentQty == previousQty && currentValue > previousValue) {
            System.out.println("valid value");
            LiarsDice.currentBid[0]= currentValue;

        } else {
            System.out.println("Please try a valid bid");
            LiarsDice.getSelections(activePlayer);
        }
    }

}
