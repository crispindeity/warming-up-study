# 3일차 과제


## 질문

> 💡키워드: 익명 클래스 / 람다 / 함수형 프로그래밍 / @FunctionalInterface / 스트림 API / 메소드 레퍼런스

### 자바의 람다식은 왜 등장했을까?

#### 람다식이란??

- 자바에서 함수형 프로그래밍을 구현하는 방식
- 클래스를 생성하지 않고 함수의 호출만으로 기능을 수행
- 자바 8부터 지원되는 기능
- 순수 함수를 구현하고 호출
- 매개 변수만을 사용하도록 만든 함수로 외부 자료에 부수적인 영향(side effect)가 발생하지 않도록 함
- 입력 받은 자료를 기반으로 수행되고 외부에 영향을 미치지 않으므로 병렬처리 등이 가능
- 안정적인 확장성 있는 프로그래밍 방식

### 람다식과 익명 클래스는 어떤 관계가 있을까?

#### 익명 클래스란?

- 말 그대로 이름이 없는 클래스를 뜻한다.
- 프로그램 내부에서 반복적으로 사용되는것이 아니라 일회용으로 사용할 목적으로 한번만 사용되고 버려지는 클래스라 생각하면 된다.
- 일회용으로 사용될 클래스를 정의하고 생성하는 것은 비효율적이기 때문에 이런 비효율적인 과정을 해소하고자 만들어지 일종의 코드를 줄이는 기법이라 할 수 있다.

#### 익명 클래스와 람다의 사용

- 익명 클래스로 선언하고 사용하는 부분을 람다식과 함께 사용하면 더욱 획기적으로 코드의 양을 줄일 수 있다.

```java
public class Main {

    public static void main(String[] args) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        };
    }
}
```

- 매우 간단한 예제를 살펴보자. 자바에서 어떤한 값을 정렬할때 매우 자주 사용되는 Comparator 를 익명 클래스로 선언한 부분이다.
- 이렇게만 사용해도 매우 편리하게 정렬에 대한 기준을 만들어줄수 있는데, 여기서 람다를 사용하면 코드의 양이 더욱 줄게 된다.

```java
public class Main {

    public static void main(String[] args) {
        Comparator<String> comparator = (o1, o2) -> 0;
    }
}
```

- 위에 보이는 것 처럼 좀 더 간단하게 사용할 수 있다.
- 단, 람다식을 잘 모르는 경우 오히려 코드를 읽는데 방해가 되기 때문에 이 부분은 팀 또는 회사 내에서 기준을 정해 사용하면 될 것 같다.

### 람다식의 문법은 어떻게 될까?

```java
interface PrintString {
    void showString(String str);
}

public class TestLambda {
    public static void main(String[] args) {

        PrintString lambdaStr = System.out::println; // 메서드 레퍼런스
        lambdaStr.showString("Test");

        showMyString(lambdaStr);

        PrintString test = returnString();
        test.showString("Test3");
    }

    private static void showMyString(PrintString p) {
        p.showString("Test2");
    }

    public static PrintString returnString() {
        return s -> System.out.println(s + "!!!");
    }
}
```

- 매개 변수가 하나인 경우 괄호 생략가능(두 개인경우는 괄호를 생략할 수 없다.)
    - str → {System.out.println(str);}
- 중괄호 안의 구현부가 한 문장인 경우 중괄호 생략
    - str → System.out.println(str)
- 중괄호 안의 구현부가 한 문장이라도 return문은 중괄호를 생략할 수 없다.
    - str → return str.length(); //오류 발생
- 중괄호 안의 구현부가 반환문 하나라면 return과 중괄호를 모두 생략할 수 있다.
    - (x, y) → x + y     //두 값을 더하여 반환
    - str → str.length() //문자열 길이 반환

### 함수형 인터페이스

- 자주 사용되는 함수형 인터페이스


| 함수형 인터페이스          | 메서드                             | 설명                                     |
|--------------------|---------------------------------|----------------------------------------|
| java.lang.Runnable | void run()                      | 매개변수도 없고, 반환값도 없다.                     |
| Supplier<T>        | T get()                         | 매개변수는 없고, 반환값만 있다.                     |
| Consumer<T>        | T → void accept(T t)            | Supplier와 반대로 매개변수만 있고 반환값이 없다.        |
| Function<T, R>     | T → R apply(T t) → R            | 일반적인 함수, 하나의 매개변수를 받아서 결과를 반환          |
| Predicate<T>       | T → boolean test(T t) → boolean | 조건식을 표현하는데 사용됨, 매개변수는 하나 반환타입은 boolean |
- 매개변수가 2개인 함수형 인터페이스


| 함수형 인터페이스           | 메서드                               | 설명                                   |
|---------------------|-----------------------------------|--------------------------------------|
| BiConsumer<T, U>    | T, U → void accept(T t, U u)      | 두 개의 매개변수만 있고, 반환값이 없다               |
| BiPredicate<T, U>   | T, U → boolean test(T t, U u) → B | 조건식을 표현하는데 사용됨 매개변수는 둘, 반환값은 boolean |
| BiFunction<T, U, R> | T, U → R apply(T t, U u) → R      | 두 개의 매개변수를 받아서 하나의 결과를 반환            |

- 매개변수의 타입과 반환타입이 일치하는 함수형 인터페이스

| 함수형 인터페이스                  | 메서드                          | 설명                                  |
|----------------------------|------------------------------|-------------------------------------|
| UnaryOperator<T> (단항 연산자)  | T → T apply(T t) → T         | Function의 자손, 매개변수의 타입과 반환타입이 같다.   |
| BinaryOperator<T> (이항 연산자) | T, T → T apply(T t, T t) → T | BiFunction의 자손, 매개변수의 타입과 반환타입이 같다. |


#### 함수형 인터페이스의 결합

- f.andThen(g); → f를 실행 후 g 실행

#### Predicate의 결합

```java
Predicate<Integer> p = i -> i < 100;
Predicate<Integer> q = i -> i < 200;
Predicate<Integer> r = i % 2 == 0;

Predicate<Integer> notp = p.negate(); // i >= 100
Predicate<Integer> all = notp.and(q).or(r); // 100 <= i && i < 200 || i % 2 == 0
Predicate<Integer> all2 = notp.and(q.or(r)); // 100 <= i && (i < 200 || i % 2 == 0)
```

- and(), or(), negate()로 두 Predicate를 하나로 결합(defult메서드)

```java
Predicate<String> p = Predicate.isEqual(str1); // isEqual()는 static 메서드
Boolean result = p.test(str2);
// 위에 코드를 이런식으로 축약 가능
boolean result = Predicate.isEqual(str1).test(str2);
```

- isEqual() 사용

#### 컬렉션 프레임웍과 함수형 인터페이스

- 함수형 인터페이스를 사용하는 컬렉션 프레임웍의 메서드


| 함수형 인터페이스  | 메서드                                              | 설명                   |
|------------|--------------------------------------------------|----------------------|
| Collection | boolean removeIf(Predicate<E> filter)            | 조건에 맞는 요소를 삭제        |
| List       | void replaceAll(UnaryOperator<E> operator)       | 모든 요소를 변환하여 대체       |
| Iterable   | void forEach(Consumer<T> action)                 | 모든 요소에 작업 action을 수행 |
| Map        | V compute(K key, BiFunction<K, V, V> f)          | 지정된 키의 값에 작업 f를 수행   |
| Map        | V computeifAbsent(K key, Finction<k, V> f)       | 키가 없으면, 작업 f 수행 후 추가 |
| Map        | V computeIfPersent(K key, BiFunction<K, V, V> f) | 지정된 키가 있을때, 작업 f 수행  |
| Map        | V merge(K key, V value, BiFunction<V, V, V> f)   | 모든 요소에 병합작업 f를 수행    |
| Map        | void forEach(BiConsumer<K ,V> action)            | 모든 요소에 작업 action을 수행 |
| Map        | void replaceAll(BiFunction<K, V, V> f)           | 모든 요소에 치환작업 f를 수행    |


```java
list.forEach(i -> System.out.println(i + ","));
list.removeIf(x -> x % 2 == 0 || x % 3 == 0);
list.replaceAll(i -> i * 10);
map.forEach((k, v) -> System.out.println("k:" + k + "," + "v:" + v);
```

---

## REFERENCE

- [자바의정석-람다식](https://www.youtube.com/watch?v=3wnmgM4qK30)
