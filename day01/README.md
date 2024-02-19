# 1일차 과제

## 질문

### 어노테이션을 사용하는 이유 (효과) 는 무엇일까?

#### 어노테이션이 뭘까?

- 자바 어노테이션(Java Annotation)은 자바 소스 코드에 추가하여 사용할 수 있는 메타데이터의 일종이다. 보통 @ 기호를 앞에 붙여서 사용한다. JDK 1.5 버전 이상에서 사용 가능하다. 자바 어노테이션은 클래스 파일에 임베디드되어 컴파일러에 의해 생성된 후 자바 가상머신에 포함되어 작동한다.
  - 참조: [위키백과](https://ko.wikipedia.org/wiki/자바_애너테이션)
- 위키백과에 적혀있는 설명을 봤을때 기존 코드에 뭔가를 추가할때 사용하는 것 이라는 대략적인 감이 온다.
- 자바 어노테이션이 없을 당시 자바 소스코드에 데이터 또는 지침을 추가할때 기존코드에 함께 작성을 하거나 XML 파일을 가지고 작성을 해야만 했다. 직접적인 코드에 작성을 하는 경우 코드량이 매우 방대해지고, XML 을 사용해 작성하는 경우에는
  XML 문법을 알아야 하는 등의 문제가 있었다.
- 단 1줄의 간단한 코드를 추가하여, 위에 문제를 해결할 수 있도록 만들어진것이 자바의 어노테이션이다.
- 사실 2004년 이전에 코드를 작성해 본적이 없어, 그 전에 비해 얼마나 많은 편리함이 생겼는지 알기는 쉽지 않지만 당장 스프링을 사용할때 만 하더라도 AOP 를 만들어 사용할때 어노테이션 코드 몇줄 만 추가해주면 되니 많이 편리한것 같기는 하다.

#### 왜 만들어졌나?

- 사용하는 이유를 알아보기 전에 어떤 수요에 의해 어노테이션이라는 기술이 자바에 적용이 되게 되었는지 먼저 알아보자.
  - 적용 시기: 2004년 에 추가된 jdk 1.5(java5) 부터 적용
  - 수요: 코드의 유지보수 및 유연성을 향상시키고자 하는 요구
  - 결과: 과거를 비롯 현재까지도 많은 라이브러리와 프레임워크에서 사용하고 있으며, 앞으로도 많이 사용될것 같다.
- 소스 코드에 추가적으로 필요한 데이터 또는 지침을 손 쉽게 추가하여, 코드양을 줄이고 그로 인한 유지보수성 및 유연성을 향상시키고자 하는 요구를 충족시키기 위해

#### 왜 사용할까?

- 한번 만들어 놓으면, 반복적으로 사용하기에 매우 쉬우면서도 편리하다.
- 어노테이션을 사용하면 작성해야하는 코드의 양이 줄어 유지보수성이 높아진다.

### 나만의 어노테이션은 어떻게 만들 수 있을까?

#### 메타 어노테이션

- 어노테이션을 위한 어노테이션이며, 어노테이션 생성 및 확장에 쓰인다.
- 어노테이션을 생성할때 사용하는 `@Target` , `@Retention` 등등 이에 해당된다.

#### 마커 어노테이션

- `@Override` 어노테이션 처럼 별 다른 기능 없이, 어떠한 표기를 위해 사용하는 어노테이션

#### 사용자 정의 어노테이션 생성 방법

- 사용자 정의 어노테이션을 만드는 방법은 매우 간단하다.

```java
public @interface MyAnnotation {

}
```

- Java 클래스를 생성하는 것 처럼 생성을 해주는데 class 앞에 클래스 대신 `@interface` 만 붙여주면 된다.
- 생성을 했으면 이제 이 어노테이션을 어디서 어떻게 사용할 것인지 지정해줘야 한다. 지정을 위해서는 메타 어노테이션을 사용하는데, 우선 지정에 사용되는 메타 어노테이션을 알아보자.
  - @Target(ElementType[] value()): 어노테이션을 사용할 수 있는 대상을 지정할 수 있게 해주는 메타 어노테이션.
    - `ElementType` 에 대해서는 @Target 내부 코드를 보면 자세하게 나와있다.
  - @Retention(RetentionPolicy value())
    - SOURCE: 기본값이며, 컴파일러에 의해 무시된다.
    - CLASS: 컴파일러에 의해 처리되며 런타임에서는 사용이 안되며, 클래스 파일에서 유지된다.
    - RUNTIME: 컴파일러에 의해 처리되며 Reflection API 를 사용하여 런타임에 사용이 가능하다.

#### 사용자 정의 어노테이션 생성

- 앞서 학습한 내용을 바탕으로, 입력된 `count` 를 기반으로 메서드를 `count` 만큼 실행하도록 하는 어노테이션을 만들어보자.

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    int count() default 1; // 1을 기본 값으로 가지는 count
}
```

- 메서드 레벨에서 동작하며, 런타임에 사용될 사용자 정의 어노테이션

```java
class Printer {

    @MyAnnotation()
    public void printName() {
        System.out.println("name");
    }

    @MyAnnotation(count = 3)
    public void printAge() {
        System.out.println("age");
    }
}
```

- `@MyAnnotation` 을 사용할 `Printer` 클래스

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Printer printer = new Printer();

        Class<? extends Printer> cls = printer.getClass();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyAnnotation.class)) {
                MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
                int count = annotation.count();
                for (int i = 0; i < count; i++) {
                    method.invoke(printer);
                }
                System.out.println();
            }
        }
    }
}

/* 결과 출력
name

age
age
age
*/
```

- `Reflection` 기술을 사용해서 방금 만든 사용자 정의 어노테이션을 사용해 보자.
- `Printer` 인스턴스를 생성했을뿐 메서드를 전혀 호출하지 않았지만, 결과를 보면 `@MyAnnotation` 을 사용하여 지정해준 만큼 메서드가 호출되는것을 볼 수 있다.

---
## REFERENCE

- [JDK 17 Annotation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/annotation/package-summary.html)
- [Java Annotation: What, Why & How?](https://www.linkedin.com/pulse/java-annotation-what-why-how-shivangam-soni)
- [더 자바, 코드를 조작하는 다양한 방법](https://www.inflearn.com/course/the-java-code-manipulation)
