package study;

import java.util.Random;

public class Dice {

    private final Random random;
    private final int numberOfSides;

    public Dice(Random random, int numberOfSides) {
        this.random = random;
        this.numberOfSides = numberOfSides;
    }

    public int roll() {
        return random.nextInt(numberOfSides) + 1;
    }
}
