package com.company;


import java.util.*;

public class Cup {

    public  List<Die> dice = new ArrayList<>();
    static int numberOfDice = 0 ;

    public Cup() {
        while (dice.size() < numberOfDice) {  //you need size of dice
            dice.add(new Die());      // remember .add
        }
    }

    public void removeDie() {
        dice.remove(0);
    }

    public void roll() {
        for (Die die : dice) {
            die.roll();
        }
    }

    public String displayCup() {  // display used for end user
        String output = ""; // assign output a blank string
        for (Die die : dice) {
            output += die.faceUpValue + " ";
        }
        return output.trim();
    }

}


