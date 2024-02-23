# 5일차 과제

## 문제

### 클린코드

- 최대한 클린하지 않게 작성된 아래 코드는 다음과 같이 동작합니다.
- 이 코드를 클린하게 리팩토링 해주세요.
  - 주사위를 던질 숫자를 하나 받는다.
  - 해당 숫자만큼 주사위를 던져, 각 숫자가 몇 번 나왔는지 알려준다.

```java
public class Main {
    public static void main(String[] args) {
        System.out.print("숫자를 입력하세요. : ");
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();

        int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0;

        for (int i = 0; i < a; i++) {
            double b = Math.random() * 6;
            if (d >= 0 && b < 1) {
                r1++;
            } else if (b >= 1 && b < 2) {
                r2++;
            } else if (b >= 2 && b < 3) {
                r3++;
            } else if (b >= 3 && b < 4) {
                r4++;
            } else if (b >= 4 && b < 5) {
                r5++;
            } else if (b >= 5 && b < 6) {
                r6++;
            }
        }

        System.out.printf("1은 %d번 나왔습니다.\n", r1);
        System.out.printf("2는 %d번 나왔습니다.\n", r2);
        System.out.printf("3은 %d번 나왔습니다.\n", r3);
        System.out.printf("4는 %d번 나왔습니다.\n", r4);
        System.out.printf("5는 %d번 나왔습니다.\n", r5);
        System.out.printf("6은 %d번 나왔습니다.\n", r6);
    }
}
```

> 📌 한 걸음 더!
>
> 현재 코드는 주사위가 1 ~ 6까지만 있다는 가정으로 작성되어 있습니다.<br>
> 따라서 주사위가 1 ~ 12까지 있거나 1 ~ 20 까지 있다면 코드를 많이 수정해야 하죠!
>
> 위의 코드를 클린하게 개선해 보았다면, 주사위의 숫자 범위가 달라지더라도 코드를 적게 수정할 수 있더록 고민해 봅시다!

## 풀이

```java
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
```

- 먼저 던질 주사위를 객체로 만들었다.
- 주사위를 생성할때 주사위의 면의 수를 받아 주사위를 생성할 수 있도록 했다.

```java
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
```

- 주사위 놀이를 실행하는 main 메서드.
- 주사위 놀이에 필요한 의존성을 주입해 주고 있다.

```java
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
```

- 주사위 놀이를 시행하는 클래스
- 메서드 단위로 분리하여 알아보자.

```java
public void play() {
    int numberToThrow = inputNumber();
    rollTheDice(numberToThrow);
    printResult();
}
```

- 주사위 놀이를 시작하는 메서드로 주사위의 면 개수를 결정하여 주입받은 주사위를 가지고 플레이를 진행한다.

```java
private int inputNumber() {
    System.out.print("숫자를 입력하세요 : ");
    return scanner.nextInt();
}
```

- 주사위를 몇번 던질 것인지에 대한 수를 입력받는 메서드이다.

```java
private void rollTheDice(int numberToThrow) {
    for (int i = 0; i < numberToThrow; i++) {
        resultStorage.put(dice.roll(random), resultStorage.getOrDefault(dice.roll(random), 0) + 1);
    }
}
```

- 입력받은 수 만큼 반복하여 주사위를 던지고, 나온 숫자를 결과 저장소에 저장하는 메서드이다.
- if 문을 통한 결과 분기를 없애고, Map 자료구조의 getOrDefault() 메서드를 사용하여 최초의 값은 0으로 셋팅 후 동일한 값이 나오는 경우 1씩 값을 올려 저장하도록 작성하였다.
- 이렇게 동작하는 경우 주사위 면의 개수에 따라 if 문의 개수를 늘려줘야하던 기존의 코드에서 따로 코드 수정 없이 면의 개수에 맞게 저장되게 되어 면의 개수가 변경되더라도 코드의 수정은 면의 개수를 입력하는 변수외에 변화가 발생하지 않게 된다.

```java
private void printResult() {
    resultStorage.entrySet().stream()
            .map(entry -> String.format("%d은 %d번 나왔습니다.", entry.getKey(), entry.getValue()))
            .forEach(System.out::println);
}
```

- 결과를 출력해주는 메서드
- 저장되어 있는 결과값을 받아서 map() 으로 결과값에 대한 내용을 변경한 후 forEach() 를 통해 출력해주고 있다.

## 한걸음 더
- 리펙터링 이후 주사위의 면의 개수가 변경되었을때 주사위를 생성해주는 generate() 메서드의 파라미터로 들어가는 인자 값만 변경해주면 다른 코드는 전혀 손댈 필요가 없어지게 되었다.
- 이전 코드보다 코드의 양은 늘어났지만, 변경에 있어 최소한의 변경점만 발생하도록 작업되어 유의미한 리펙토링을 진행했다고 생각한다.
