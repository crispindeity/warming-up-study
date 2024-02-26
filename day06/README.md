# 6일차 과제

## 문제

### 문제1

- 4일차 과제에서 만들었던 API 를 강의 내용 처럼 Controller - Service - Repository 로 분리해보세요.

### 문제2

- 문제1 에서 코드가 분리되면 `FruitController` / `FruitService`/ `FruitRepository` 가 생겼을겁니다.
- 기존에 작성했던 `FruitRepository` 를 `FruitMemoryRepository` 와 `FruitMysqlRepository` 로 나누고 `@Primary` 어노테이션을 활용해 두 Repository 를 바꿔가며 동작시킬 수 있도록 코드를 변경해보세요.
> 📌 @Qualifier 어노테이션을 사용해도 좋습니다!
