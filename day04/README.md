# 4일차 과제

## 문제

<details>
<summary>문제 1</summary>
우리는 작은 과일 가게를 운영하고 있습니다. 과일 가게에 입고된 "과일 정보"를 저장하는 API 를 만들어 봅시다. 스펙은 다음과 같습니다.

- HTTP Method: `POST`
- HTTP Path: `/api/v1/fruit`

```json
{
    "name": "String",
    "warehousingDate": "LocalDate",
    "price": "Long"
}
```

- HTTP 요청 Body

```json
{
    "name": "String",
    "warehousingDate": "2024-02-01",
    "price": 5000
}
```

- HTTP 요청 Body 예시
- 응답 성공 시 200 OK

> 📌 한 걸음 더!
>
> 자바에서 정수를 다루는 대표적인 두 가지 방법은 `int` 와 `long` 입니다.
>
> 이 두 가지 방법 중 위 API 에서 `long` 을 사용한 이유는 무엇일까요?

</details>


<details>
<summary>문제 2</summary>
과일이 팔리게 되면, 우리 시스템에 팔린 과일 정보를 기록해야 합니다. 스펙은 다음과 같습니다.

- HTTP Method: `PUT`
- HTTP Path: `/api/v1/fruit`

```json
{
    "id": "long"
}
```

- HTTP 요청 Body

```json
{
    "id": 3
}
```

- HTTP 요청 Body 예시
- 응답 성공 시 200 OK

</details>

<details>
<summary>문제 3</summary>
우리는 특정 과일을 기준으로 팔린 금액, 팔리지 않은 금액을 조회하고 싶습니다.<br>예를 들어

1. (1, 사과, 3000원, 판매 O)
2. (2, 사과, 4000원, 판매 X)
3. (3, 사과, 3000원, 판매 O)

와 같은 세 데이터가 있다면 우리의 API 는 판매된 금액 6000원, 판매되지 않은 금액: 4000원 이라고 응답해야 합니다.<br>
구체적인 스펙은 다음과 같습니다.

- HTTP Method: `GET`
- HTTP Path: `/api/v1/fruit/stat`
- HTTP Query
  - name: 과일 이름
- 예시: `GET /api/v1/fruit/stat?name=사과`

```json
{
    "salesAmount": "long",
    "notSalesAmount": "long"
}
```

- HTTP 응답 Body

```json
{
    "salesAmount": 6000,
    "notSalesAmount": 4000
}
```

- HTTP 응답 Body 예시

> 📌 한 걸음 더!
>
> 문제 3번을 모두 푸셨다면 SQL 의 sum, group by 키워드를 검색해서 적용해보세요!

</details>

## 풀이

- 4일차 과제는 2일차 과제보다는 복잡도가 있다고 생각하여 Layer 를 나누어 구현하였다.
- `Controller` 는 `Service` 에게 요청을 위임하고, `Service` 는 `Repository` 에게 요청을 위임한다.
- `Repository` 는 `NamedParameterJdbcTemplate` 를 사용하였다.

### 문제 1

- 과일 정보를 `Request Body` 로 받아서 `DB` 에 저장하면 되는 문제이다.
- 과일 정보가 처음 등록되었을때 과일의 판매 상태는 Sale 이다.

```java
@RestController
@RequestMapping("/api/v1")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping("/fruit")
    public FruitSaveResponse save(FruitSaveRequest request) {
        return fruitService.save(request);
    }
}
```

- `Controller` 의 경우는 매우 단순하다.
- `Request Body` 를 통해 받은 정보를 `FruitSaveRequest` 로 매핑하고, `FruitService` 에게 위임한다.

```java
@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    public FruitSaveResponse save(FruitSaveRequest request) {
        Fruit savedFruit = fruitRepository.save(Fruit.of(request.name(), request.warehousingDate(), request.price()));
        return new FruitSaveResponse(savedFruit.name(), savedFruit.warehousingDate(), savedFruit.price());
    }
}
```

- `Service` 의 경우는 `Repository` 에게 요청을 위임하고, `Repository` 에서 받은 결과를 `Response` 로 매핑하여 반환한다.

```java
@Repository
public class FruitJdbcRepository implements FruitRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FruitJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("fruit")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "warehousingDate", "price", "status");
    }

    @Override
    public Fruit save(Fruit fruit) {
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(fruit) {
            @Override
            public Object getValue(String paramName) throws IllegalArgumentException {
                Object value = super.getValue(paramName);
                if (value instanceof Enum) {
                    return value.toString();
                }
                return value;
            }
        };
        Number key = jdbcInsert.executeAndReturnKey(sqlParameterSource);
        return Fruit.of(key.longValue(), fruit.name(), fruit.warehousingDate(), fruit.price());
    }
}
```

- 이번 과제의 핵심이 되는 부분이다.
- 사실 `JPA` 를 사용했으면 매우 쉽게 과제를 해결할 수 있었겠지만, 강의 진도가 `JPA` 를 다루지 않았기 때문에 `NamedParameterJdbcTemplate` 를 사용하였다.
- `JdbcTemplate` 은 강의에서 간단하게 배웠고, `?` 가 아닌 `parameter` 이름을 사용할 수 있는 방법도 있다하여 `NamedParameterJdbcTemplate` 를 사용하였다.
- 중간에 `BeanPropertySqlParameterSource` 익명 클래스를 사용하였는데, `Enum` 타입 값을 바로 저장하지 못하는 문제가 있어서 이를 해결하기 위해 사용하였다.

### 문제 2

- 저장된 과일의 아이디 값을 받아 판매 상태로 변환하는 문제이다.
- 판매 상태로 변환되면 과일의 상태가 Sale -> Sold 로 변경된다.

```java
@RestController
@RequestMapping("/api/v1")
public class FruitController {
    @Override
    public FruitUpdateResponse update(FruitUpdateRequest request) {
        Long updatedId = fruitRepository.update(request.id());
        return FruitUpdateResponse.of(updatedId);
    }
}
```

- `Request Body` 로 받은 아이디 값을 `FruitUpdateRequest` 로 매핑하고, `FruitService` 에게 위임한다.
- `Service` 는 문제 1번과 마찬가지로 단순하기 때문에 바로 `Repository` 를 보자.

```java
@Repository
public class FruitJdbcRepository implements FruitRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FruitJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("fruit")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "warehousingDate", "price", "status");
    }

    @Override
    public Long update(Long id) {
        String sql = "UPDATE fruit SET fruit.status = :status WHERE id = :id";
        Fruit fruit = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 과일이 없습니다."));

        Fruit updatedFruit = Fruit.updateStat(fruit);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", updatedFruit.name())
                .addValue("warehousingDate", updatedFruit.warehousingDate())
                .addValue("price", updatedFruit.price())
                .addValue("status", updatedFruit.status().name())
                .addValue("id", id);

        jdbcTemplate.update(sql, parameterSource);
        return id;
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        String sql = "SELECT id, name, warehousingDate, price, status FROM fruit WHERE id = :id";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id),
                            (rs, rowNum) -> Fruit.of(
                                    rs.getLong("id"),
                                    rs.getString("name"),
                                    rs.getDate("warehousingDate").toLocalDate(),
                                    rs.getLong("price")
                            )
                    )
            );
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
```

- 과일의 아이디를 통해 저장된 과일을 찾고, 판매 상태로 변경한다.
- `Save` 로직에서 기존에 존재하는 `ID` 의 경우 `Update` 로직을 수행하고, 존재하지 않는 `ID` 의 경우 `Save` 로직을 수행하게 하여, `Update` 로직을 수행할 수 도 있지만 이번 과제에서는 `Update`로 구현하였다.

```java
public record Fruit(Long id, String name, LocalDate warehousingDate, Long price, Status status) {
    public static Fruit of(String name, LocalDate warehousingDate, Long price) {
        return new Fruit(null, name, warehousingDate, price, Status.SALE);
    }

    public static Fruit of(Long id, String name, LocalDate warehousingDate, Long price) {
        return new Fruit(id, name, warehousingDate, price, Status.SALE);
    }

    public static Fruit updateStat(Fruit fruit) {
        return new Fruit(fruit.id(), fruit.name(), fruit.warehousingDate(), fruit.price(), Status.SOLD);
    }
}

```

- `Fruit` 의 상태를 변경하는 로직은 `Fruit` 클래스에 구현하였다.

### 문제 3

- `Controller` 와 `Service` 는 단순하기 때문에 바로 `Repository` 를 보자.

```java
@Repository
public class FruitJdbcRepository implements FruitRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FruitJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("fruit")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "warehousingDate", "price", "status");
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        String sql = "SELECT sum(price) as salesAmount FROM fruit where name = :name group by status having status = 'SOLD'";
        List<Long> price = jdbcTemplate.query(sql, new MapSqlParameterSource("name", name),
                (rs, rowNum) -> rs.getLong("salesAmount"));
        return price.stream().reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        String sql = "SELECT sum(price) as salesAmount FROM fruit where name = :name group by status having status = 'SALE'";
        List<Long> price = jdbcTemplate.query(sql, new MapSqlParameterSource("name", name),
                (rs, rowNum) -> rs.getLong("salesAmount"));
        return price.stream().reduce(Long::sum);
    }
}
```

- `SQL` 의 `sum`, `group by` 를 사용하여 문제를 해결하였다.
- 하나의 쿼리로 문제를 해결 할 수 도 있지만, `SOLD` 와 `SALE` 을 구분하여 조회하였다.
- `Stream` 을 통해 값을 계산할 수 도 있지만, 쿼리가 단순하고, 데이터가 많지 않기 때문에 `sum` 과 `group by` 로 해결하였다.
