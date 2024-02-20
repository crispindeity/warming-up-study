# 2일차 과제

## 문제

<details>
<summary>문제 1</summary>

두 수를 입력하면, 다음과 같은 결과가 나오는 `GET` API 를 만들어 보자!
- path: `/api/v1/calc` 이다.
- 쿼리 파리미터: num1, num2

```json
{
    "add": "덧셈결과",
    "minus": "뻴셈결과",
    "multiply": "곱셈결과"
}
```

예시
GET /api/v1/calc?num1=10&num2=5

```json
{
    "add": 15,
    "minus": 5,
    "multiply": 15
}
```

</details>

<details>
<summary>문제 2</summary>

날짜를 입력하면, 무슨 요일인지 알려주는 `GET` API 를 만들어 보자!
path 와 쿼리 파라미터는 임의로 만들어도 상관없다.

예시
GET /api/v1/day-of-the-week?date=2023-01-01

```json
{
    "dayOfTheWeek": "MON"
}
```

- 추가 팁: LocalDate 에 대해 찾아보자!

</details>


<details>
<summary>문제 3</summary>

여러 수를 받아 총 합을 반환하는 `POST` API 를 만들어보자!

API 에서 받는 `Body` 는 다음과 같은 형태이다.
(`HINT`: 요청을 받는 DTO 에서 `List` 를 갖고 있으면 JSON 의 배열을 받을 수 있습니다!)

```json
{
    "numbers": [1, 2, 3, 4, 5]
}
```

반환결과

```text
15
```

```text
⚠️ 주의

    반환 결과는 JSON이 아닙니다!
    함수에서 String 혹은 Integer 를 반환하면, API 결과가 JSON 으로 나가지 않고, 단순한 값으로 나가게 됩니다.
    POSTMAN 과 같은 API 테스트 툴을 이용해 한 번 확인해보세요!
```

</details>

## 풀이

### 문제1

- Query Parameter 로 숫자 2개를 받아 각각의 계산을 진행한 뒤 반환해주면 되는 문제이다.
- Numbers Object 를 사용하여 Query Parameter 값을 바인딩 한 후 계산을 진행

```java
public record Numbers(int num1, int num2) { }
```

- java 14 부터 사용 가능한 Record 를 사용하면, DTO 같은 오브젝트를 매우 쉽게 만들 수 있다.

```java
public record Numbers(int num1, int num2) {
    public Numbers(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int num1() {
        return this.num1;
    }

    public int num2() {
        return this.num2;
    }
}
```
- 내가 작성한 코드는 1줄 밖에 안되지만, 컴파일 된 class 파일을 살펴보면 모든 인스턴스 변수를 파라미터로 갖는 생성자와 getter 비슷한 메서드를 만들어 주는걸 볼 수 있다.

```java
@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    @GetMapping("/calc")
    public ResponseCalcResult calc(@ModelAttribute Numbers numbers) {
        int added = numbers.num1() + numbers.num2();
        int minused = numbers.num1() - numbers.num2();
        int multiplied = numbers.num1() * numbers.num2();
        return new ResponseCalcResult(added, minused, multiplied);
    }
}

/* 요청 및 응답 결과
GET http://localhost:8080/api/v1/calc?num1=10&num2=5

HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 20 Feb 2024 13:06:44 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "add": 15,
  "minus": 5,
  "multiply": 50
}
*/
```

- 단순한 로직이기 때문에 따로 Service 레이어를 두지 않고, 컨트롤러에서 처리 후 반환
- Controller 에서 숫자 2개를 가지고 원하는 결과값이 나오도록 계산 후 `json` 형식으로 반환

```java
public record ResponseCalcResult(int add, int minus, int multiply) { }
```

- ResponseCalcResult 오브젝트를 사용하여 json 형식으로 반환
- 오브젝트 형식으로 return 을 해주는데 어떻게 json 형식으로 직렬화가 되어 클라이언트로 전송이 되는걸까?
  - spring web 의존성에 포함된 jackson 라이브러리를 사용하여 직렬화와 역직렬화를 하는것 같다, 좀 더 세부적인 내용은 주말에 보충하자.

### 문제2

- Query Parameter 로 들어온 날짜 정보를 가지고 해당 날짜의 요일을 반환해주면 되는 문제이다.
- Parameter 개수가 1개라서, @RequestParam 을 사용하여 바인딩 진행.

```java
@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    @GetMapping("/day-of-week")
    public ResponseEntity<ResponseDateResult> date(@RequestParam(name = "date") String date) {
        DayOfWeek dayOfWeek = LocalDate.parse(date).getDayOfWeek();
        return new ResponseDateResult(dayOfWeek);
    }
}

/* 요청 및 응답 결과
GET http://localhost:8080/api/v1/day-of-week?date=2023-01-01

HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 20 Feb 2024 13:13:08 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "dayOfTheWeek": "SUNDAY"
}
*/

```

- LocalDate API 를 사용하여 해당 날짜에 대한 요일을 구한 뒤 `json` 형식으로 반환

### 문제3

- Request Body 안 json 리스트 담겨있는 모든 수를 합하여, 그 결과를 반환해주면 되는 문제

```java
public record NumberBundle(List<Integer> numbers) { }
```

- json 리스트를 바인딩할 오브젝트 생성

```java
@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    @PostMapping("/calc")
    public Integer sum(@RequestBody NumberBundle numberBundle) {
        return numberBundle.numbers().stream().reduce(0, Integer::sum);
    }
}

/* 요청 및 응답 결과
POST http://localhost:8080/api/v1/calc

HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 20 Feb 2024 13:14:24 GMT
Keep-Alive: timeout=60
Connection: keep-alive

15

*/
```

- Controller 에서 RequestBody 를 통해 받은 숫자 목록을 stream reduce 로 모두 합하여 단순 값 형태로 반환

## 심화
> ⚠️ 나홀로 필요하다 생각되면 추가 학습 진행(주말 보충)

### Object to Json

### Json to Object

### ModelAttribute 와 RequestParam
- Query Parameter 로 값이 들어올때 스프링에서는 어떻게 그 값을 바인딩하여 사용할 수 있을까?
    - Object 바인딩
    - @RequestParam 을 사용한 바인딩
- @ModelAttribute 를 사용하는것과 사용하지 않는 것의 차이
- @ModelAttribute 와 @RequestParam 차이

### RequestBody 의 동작 원리

### 직렬화와 역직렬화
