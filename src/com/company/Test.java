package com.company;

import java.util.List;

public class Test {
    public static void main(String[] args) {

        LiarsDice game = new LiarsDice();

        game.numberOfPlayers = 3;
        Cup.numberOfDice = 1;
        game.players.add( new Player("Cliff"));
        game.players.add( new Player("Keith"));
        game.players.add( new Player("Sally"));
        //game.players.add( new Player("Jim"));
        game.play();

    }
}
