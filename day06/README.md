# 6일차 과제

## 문제

### 문제1

- 4일차 과제에서 만들었던 API 를 강의 내용 처럼 Controller - Service - Repository 로 분리해보세요.

### 문제2

- 문제1 에서 코드가 분리되면 `FruitController` / `FruitService`/ `FruitRepository` 가 생겼을겁니다.
- 기존에 작성했던 `FruitRepository` 를 `FruitMemoryRepository` 와 `FruitMysqlRepository` 로 나누고 `@Primary` 어노테이션을 활용해 두 Repository 를 바꿔가며 동작시킬 수 있도록 코드를 변경해보세요.
> 📌 @Qualifier 어노테이션을 사용해도 좋습니다!


## 풀이

### 문제1

- 이미 4일차 과제를 진행할때 레이어별로 분리를 해놔서. 따로 추가 작업을 할 필요가 없었다.

### 문제2

- 먼저 FruitMemoryRepository 를 만들자.

```java
import org.springframework.stereotype.Repository;
import study.day06.fruit.domain.Fruit;
import study.day06.fruit.domain.Status;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FruitMemoryRepository implements FruitRepository {

    private final Map<Long, Fruit> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Fruit save(Fruit fruit) {
        if (fruit.id() == null || storage.get(fruit.id()) == null) {
            long currentSequence = sequence.incrementAndGet();
            Fruit newFruit = Fruit.of(
                    currentSequence,
                    fruit.name(),
                    fruit.warehousingDate(),
                    fruit.price()
            );
            storage.put(currentSequence, newFruit);
            return storage.get(currentSequence);
        } else {
            storage.put(fruit.id(), fruit);
            return storage.get(fruit.id());
        }
    }

    @Override
    public Long update(Long id) {
        Fruit fruit = storage.get(id);

        if (fruit == null) {
            throw new IllegalArgumentException("해당하는 과일이 없습니다.");
        }

        save(fruit.updateStat());
        return id;
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        return storage.values()
                .stream()
                .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SOLD))
                .map(Fruit::price)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        return storage.values()
                .stream()
                .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SALE))
                .map(Fruit::price)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        return Optional.of(storage.get(id));
    }
}

```

- Map 을 사용하여, 메모리에 저장하는 Repository 를 만들었다, 간단 하지만 메서드 별로 조금씩 나눠서 알아보자.

```java
@Repository
public class FruitMemoryRepository implements FruitRepository {

    private final Map<Long, Fruit> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Fruit save(Fruit fruit) {
        if (fruit.id() == null || storage.get(fruit.id()) == null) {
            long currentSequence = sequence.incrementAndGet();
            Fruit newFruit = Fruit.of(
                    currentSequence,
                    fruit.name(),
                    fruit.warehousingDate(),
                    fruit.price()
            );
            storage.put(currentSequence, newFruit);
            return storage.get(currentSequence);
        } else {
            storage.put(fruit.id(), fruit);
            return storage.get(fruit.id());
        }
    }
}
```

- 과일을 저장할때 사용하는 메서드이다.
- 테스트를 사용할때 자주 만들었던 `FakeRepository` 와 매우 유사한 형태이다.
- 테스트에서 사용할때는 별 생각이 없었지만, 그래도 서비스에서 사용하는걸로 가정해서 만들었으니 조금은 동시성에 대해 생각해 코드를 작성했다.
    - `ConcurrentHashMap` 을 사용해서 멀티쓰레드에서 동시적으로 호출을 했을때 안전성을 조금 더해줬다.
    - 유일한 값을 지정하는 `sequence` 의 경우도 `AtomicLong` 을 사용하여 동시 호출에 안전성을 더해줬다.

```java
@Override
public Long update(Long id) {
    Fruit fruit = storage.get(id);

    if (fruit == null) {
        throw new IllegalArgumentException("해당하는 과일이 없습니다.");
    }

    save(fruit.updateStat());
    return id;
}
```

- 저장소에서 과일을 꺼내 상태를 변경시키는 메서드이다.
- 4단계에서 updateStat() 메서드를 static 으로 선언하는 실수를 해서 수정했다.
- 로직에는 문제가 없지만, 굳이 static 메서드로 사용할 이유가 없는데 static 으로 선언하여 수정


```java
@Override
public Optional<Long> findSoldsAmountByFruitName(String name) {
    return storage.values()
            .stream()
            .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SOLD))
            .map(Fruit::price)
            .reduce(Long::sum);
}

@Override
public Optional<Long> findSalesAmountByFruitName(String name) {
    return storage.values()
            .stream()
            .filter(fruit -> fruit.name().equals(name) && fruit.status().equals(Status.SALE))
            .map(Fruit::price)
            .reduce(Long::sum);
}
```

- 스트림을 사용해서 이름과 상태를 기준으로 값을 계산하는 로직을 작성했다.

```java
@Override
public Optional<Fruit> findById(Long id) {
    return Optional.of(storage.get(id));
}
```

- 아이디를 기반으로 저장된 과일을 조회하는 로직.

```java
@Primary
@Repository
public class FruitMemoryRepository implements FruitRepository {
    // ...
}

@Repository
public class FruitJdbcRepository implements FruitRepository {
    // ...
}
```

- JdbcRepository 와 MemoryRepository 를 FruitRepository 인터페이스를 구현하도록 만든 후 @Repository 어노테이션을 사용하여 Component 로 등록하여 사용하고 있다.
- Service 에서는 FruitRepository 로 주입을 받고 있으며, 어떤 Repository 를 주입 받을지 @Primary 어노테이션을 사용하여, 알려주고있다.

```java
@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(@Qualifier("fruitMemoryRepository") FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }
}
```

- @Primary 어노테이션 말고 @Qualifier 어노테이션을 사용하여, 어떤 Component 를 주입 받을지 지정할 수도 있다.
