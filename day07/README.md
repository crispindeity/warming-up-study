# 6일차 과제

## 문제

<details>
<summary>문제 1</summary>

- 6일차 과제에 JPA 를 적용 시키자.

</details>

<details>
<summary>문제 2</summary>

- 우리는 특정 과일을 기준으로 지금까지 우리 가게를 거쳐갔던 과일 개수를 세고 싶습니다.
- <문제 1> 에서 만들었던 과일 Entity Class 를 이용해 기능을 만들어 보세요!

- 예를 들어
  1. (1, 사콰, 3000원, 판매 O)
  2. (2, 바나나, 4000원, 판매 X)
  3. (3, 사과, 3000원, 판매 O)
  - 위와 같은 세 데이터가 있고, 사과를 기준으로 과일 개수를 센다면, 우리의 API 는 2를 반환할 것입니다.

- 구체적인 스펙은 다음과 같습니다.
  - HTTP method: `GET`
  - HTTP path: `/api/v1/fruit/count`
  - HTTP query
    - name: 과일 이름
  - 예시 `GET /api/v1/fruit/count?name=사과`

```json
{
    "count": "Long"
}
```

- HTTP 응답 Body

```json
{
    "count": 2
}
```

- HTTP 응답 Body 예시

</details>

<details>
<summary>문제 3</summary>

- 우리는 아직 판매되지 않은 특정 금액 이상 혹은 특정 금액 이하의 과일 목록을 받아보고 싶습니다.

</details>

## 풀이

### 문제 1

- 기존에 사용하던 저장소를 `JPA` 저장소로 변경하는 문제

```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import study.day07.fruit.domain.Fruit;
import study.day07.fruit.domain.Status;
import study.day07.fruit.repository.entity.FruitEntity;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class FruitJpaRepositoryImpl implements FruitRepository {

    private final JpaFruitRepository jpaFruitRepository;

    public FruitJpaRepositoryImpl(JpaFruitRepository jpaFruitRepository) {
        this.jpaFruitRepository = jpaFruitRepository;
    }

    @Override
    public Fruit save(Fruit fruit) {
        FruitEntity fruitEntity = FruitEntity.fromModel(fruit);
        return jpaFruitRepository.save(fruitEntity).toModel();
    }

    @Override
    public Long update(Long id) {
        Fruit findedFruit = jpaFruitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 과일이 없습니다."))
                .toModel();
        Fruit updatedFruit = findedFruit.updateStat();
        jpaFruitRepository.save(FruitEntity.fromModel(updatedFruit));
        return id;
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        return jpaFruitRepository.findAllByStatusAndName(Status.SOLD, name)
                .stream()
                .map(FruitEntity::getPrice)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        return jpaFruitRepository.findAllByStatusAndName(Status.SALE, name)
                .stream()
                .map(FruitEntity::getPrice)
                .reduce(Long::sum);
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        return jpaFruitRepository.findById(id)
                .map(FruitEntity::toModel);
    }

    @Override
    public Long count(String name) {
        return jpaFruitRepository.countByName(name);
    }

    @Override
    public List<Fruit> findAllByStatusAndPriceGreaterThanEqual(Long price) {
        return jpaFruitRepository.findAllByStatusAndPriceGreaterThanEqual(Status.SALE, price)
                .stream()
                .map(FruitEntity::toModel)
                .toList();
    }

    @Override
    public List<Fruit> findAllByStatusAndPriceLessThanEqual(Long price) {
        return jpaFruitRepository.findAllByStatusAndPriceLessThanEqual(Status.SALE, price)
                .stream()
                .map(FruitEntity::toModel)
                .toList();
    }
}
```

- `JpaFruitRepository` 를 주입받아 `FruitRepository` 를 구현하는 `Repository` 클래스


```java
import org.springframework.data.jpa.repository.JpaRepository;
import study.day07.fruit.domain.Status;
import study.day07.fruit.repository.entity.FruitEntity;

import java.util.List;

public interface JpaFruitRepository extends JpaRepository<FruitEntity, Long> {

    Long countByName(String name);
    List<FruitEntity> findAllByStatusAndPriceGreaterThanEqual(Status status, Long price);
    List<FruitEntity> findAllByStatusAndPriceLessThanEqual(Status status, Long price);
    List<FruitEntity> findAllByStatusAndName(Status status, String name);
}
```

- `JpaRepository` 를 구현하는 `JpaFruitRepository` 클래스
- 처음에는 `FruitJpaRepository` 라는 이름으로 클래스를 생성했는데, `FruitJpaRepositoryImpl` 클래스에서 주입받는 과정에 순환참조가 발생하여 이름을 변경했다.
- `SQL` 을 직접 사용하는 대신 쿼리 메서드를 사용하여, 생성된 `JPQL` 로 `DB` 요청을 하고 있다.

### 문제2

- 간단하게 카운트 질의를 통해 `DB` 에서 원하는 정보를 얻어 해결하면 되는 문제

```java
public interface JpaFruitRepository extends JpaRepository<FruitEntity, Long> {
    Long countByName(String name);
}
```

- 쿼리 메서드를 사용해서 `count` 를 조회하고 있다.

### 문제3

- 금액과 옵션을 받아 해당 조건에 맞는 정보를 `DB` 에서 조회하면 해결되는 문제

```java
public interface JpaFruitRepository extends JpaRepository<FruitEntity, Long> {
    List<FruitEntity> findAllByStatusAndPriceGreaterThanEqual(Status status, Long price);
    List<FruitEntity> findAllByStatusAndPriceLessThanEqual(Status status, Long price);
}
```

- 문제2번과 마찬가지로 쿼리 메서드를 사용하여 `DB` 에서 정보를 조회하고 있다.
- 복잡한 쿼리의 경우(Join 이나 SubQuery) 가 아닌 경우 매우 편리하게 `DB` 질의가 가능해서 좋다.
