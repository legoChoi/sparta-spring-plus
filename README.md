## 프로젝트 요구사항

### 필수 요구사항

1. 퀴즈 - 코드 개선
2. 퀴즈 - JWT의 이해 
   - JWT에 User의 nickname 저장
4. 퀴즈 - AOP의 이해
5. 퀴즈 - JPA의 이해 
   - 검색 조건 수정 (JPQL 사용 페이지네이션 적용)
6. JPA Cascade 
   - CascadeType.PERSIST : Todo 저장 시 Manager 함께 영속화
7. N + 1 문제 해결
    - @EntityGraph 사용
8. QueryDSL 적용
   - 단순 조회
9. Spring Security 적용


### 도전 과제

1. QueryDSL을 활용한 여러 조건으로 페이지네이션 적용
   - 동적 검색 조건(BooleanExpression) 활용
   - 역정규화를 통해 불필요한 조인 삭제
2. @Transactional 전파 설정
   - RequiresNew
   
3. User nickname 조회 시 성능 최적화
   - index 적용 (unique index X)

### 미완료 목록
- [ ] Docker & AWS 
- [ ] Kotlin 마이그레이션
- [ ] 리팩토링
- [ ] 추가 성능 개선

---

### Refresh Token 추가

1. Refresh Token 저장 로직들 
   - 회원가입 or 로그인 시 클라이언트에 Access Token과 Refresh Token을 응답해주고 Refresh Token을 Redis에 저장한다. 
   - 사용자가 Access Token 재발급 API를 보내면 Refresh Token을 확인하고 해당 user_id를 key로 가지는 refresh_token과 요청으로 받은 Refresh Token을 비교해서 동일하면 Redis에 새로운 Refresh Token을 저장하고 클라이언트에 응답한다.

### User 테이블 검색 성능 개선 - nickname 컬럼 인덱싱

1. 시간 비교
   - 인덱싱 전
      ![인덱싱 전 시간 1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F9GE4O%2FbtsL11SNjrt%2FBxAw91e8xkS9NR8Pzdntg0%2Fimg.png)
      ![인덱싱 전 시간 2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbJ9RRl%2FbtsL1W5dj88%2FkjgsM8Zh1AykaQIBxQUAzK%2Fimg.png)
   - 인덱싱 후
      ![인덱싱 후 시간 1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FxkYDR%2FbtsL3ilJmg5%2FaUiEiPseIytok1CYG8rVM0%2Fimg.png)
2. 실행 계획 비교
   - 인덱싱 전
     ![인덱싱 전 실행 계획](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FG4XdO%2FbtsL1Wc6xF0%2FsdJw8keAMA8QBzHhmCBvU1%2Fimg.png)
   - 인덱싱 후
     ![인덱싱 전 실행 계획](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fpm8fD%2FbtsL1GaywwN%2FMq5jusbPepOISsRItbMA8k%2Fimg.png)

---
> https://itak.tistory.com/19