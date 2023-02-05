# SimpleLoginTest

JWT와 Spring Security를 활용한 로그인 로직을 공부하기 위한 간단한 프로젝트 입니다.

application-API-KEY.properties파일을 생성해야 합니다.
해당 파일은 MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD, jwt.header, jwt.secret, jwt.token-validity-in-seconds, jwt.auth 정보를 포함하고 있어야 합니다.

## 필터

- `JwtFilter`를 만듦
  - doFilter에서 로직을 실행하고
    1. request Header에서 토큰을 가져옴
    2. 토큰의 유효성 검사
    3. 토큰이 유효하면 securityContextHolder에 authentication 정보 저장
  - 다음 필터를 호출
- `JwtFilter`에서 사용할 토큰과 관련된 메서드를 `TokenProvider`라는 빈에 정의하고 `JwtFilter`에 `TokenProvider`를 주입함
- `JwtSecurityConfig`에서 `JwtFilter`를 등록함
- `SecurityConfig`에서 `JwtSecurityConfig`를 적용시킴
  - SecurityConfig에는 토큰이 없으면 접근할 수 없는 페이지를 설정

## SecurityUtil

- Service에서 사용하기 위한 별도의 클래스
- SecurityContextHolder에 저장된 값을 가져와 해당 유저의 아이디를 꺼내오는 용도

## AuthService

- jwt토큰을 만드는 역할 수행
- 나는 jwt토큰을 만들어 헤더에 담고 헤더 정보를 반환하는 역할까지를 구현
- 로그인 api에서 헤더에 토큰을 담을 때 사용

## CustomUserDetailsService

- `UserDetailsService`인터페이스를 구현한 구현체
  - 해당 인터페이스는 `UserDetails`를 반환하는 `loadUserByUsername` 메서드를 구현할껄 명시하고 있음
  - `security.core.userdetails.User`는 `UserDetails`인터페이스를 구현한 구현체 중 하나
- AuthService에서 Authentication을 생성하기 위해 `authenticationManagerBuilder.getObject().authenticate(authenticationToken);`를 호출할 때 실행됨
- UserDetails를 만들기 위해 repository의 findByUsername을 사용하게 됨
