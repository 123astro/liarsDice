package com.company;

import java.util.List;

public class Test {
    public static void main(String[] args) {

        LiarsDice game = new LiarsDice();

        game.numberOfPlayers =3;
        Cup.numberOfDice = 1;
        game.players.add( new Player("Jon"));
        game.players.add( new Player("Sam"));
        game.players.add( new Player("Sally"));
        game.play();

    }
}
