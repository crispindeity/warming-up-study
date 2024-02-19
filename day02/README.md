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
