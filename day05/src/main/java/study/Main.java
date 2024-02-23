package study;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int numberOfSides = 20;
        Dice dice = new Dice(random, numberOfSides);

        PlayDice playDice = new PlayDice(dice, scanner);

        playDice.play();
        scanner.close();
    }
}
