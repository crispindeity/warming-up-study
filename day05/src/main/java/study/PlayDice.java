package study;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PlayDice {

    private final Dice dice;
    private final Scanner scanner;
    private final Map<Integer, Integer> resultStorage;

    public PlayDice(Dice dice, Scanner scanner) {
        this.dice = dice;
        this.scanner = scanner;
        this.resultStorage = new HashMap<>();
    }

    public void play() {
        int numberToThrow = inputNumber();
        rollTheDice(numberToThrow);
        printResult();
    }

    private int inputNumber() {
        System.out.print("숫자를 입력하세요 : ");
        return scanner.nextInt();
    }

    private void rollTheDice(int numberToThrow) {
        for (int i = 0; i < numberToThrow; i++) {
            resultStorage.put(dice.roll(), resultStorage.getOrDefault(dice.roll(), 0) + 1);
        }
    }

    private void printResult() {
        resultStorage.entrySet().stream()
                .map(entry -> String.format("%d은 %d번 나왔습니다.", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);
    }
}
