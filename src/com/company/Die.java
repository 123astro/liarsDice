package com.company;

public class Die {
    public int sides;
    public int faceUpValue;

    public Die() {
        this.sides = 6;
    }

    public Die(int sides) {
        this.sides = sides;
    }

    public void roll() {
        faceUpValue = (((int) (Math.random() * this.sides) + 1));
    }


}
