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
