# 🚪 spring-cloud-gateway

## 🔍 요약
(1) Spring 기반의 **API Gateway** 솔루션으로, 

(2) 마이크로 서비스 아키텍처(MSA) 환경에서 클라이언트 요청을 적절한 서비스로 **라우팅**하고, 

(3) 보안, 로깅, 모니터링 등의 기능을 제공하는 역할을 한다.

## 1. 주요 기능
### ✅ 1) 라우팅 (Routing)
클라이언트 요청을 특정 서비스로 전달하는 기능을 한다.
- Predicate 기반 라우팅: 요청의 URL, 헤더, 쿼리 파라미터 등을 기반으로 라우팅 가능
- 로드 밸런싱 지원: Spring Cloud LoadBalancer 또는 Netflix Ribbon과 함께 사용할 수 있음

### ✅ 2) 필터 (Filter)
요청(Request)과 응답(Response)에 대해 사전/사후 처리를 수행하는 기능
- 인증/인가 (예: JWT 토큰 검증)
- 요청/응답 로깅
- 헤더 추가/변경
- CORS 정책 적용
- 요청 변경 (예: URI, 파라미터 수정)

### ✅ 3) 동적 라우팅 (Dynamic Routing)
- Eureka, Consul 같은 서비스 디스커버리와 연동하여 동적으로 라우팅 가능

### ✅ 4) 속도 제한 (Rate Limiting)
- 특정 IP 또는 클라이언트에 대해 초당 요청 수를 제한 가능

### ✅ 5) WebFlux 기반
- Netty 기반의 비동기 논블로킹 구조
- 고성능 API Gateway 구축 가능

## 2. 아키텍처
Spring Cloud Gateway는 위에서 설명한 것처럼 WebFlux 기반으로 동작한다.

주요 컴포넌트는,
1. Route (라우트): 요청이 어디로 가야 하는지 정의 (예: /api/users -> user-service)
2. Predicate (조건 매칭): 특정 조건에 따라 요청을 특정 경로로 라우팅
3. Filter (필터): 요청 및 응답을 가공 (예: 인증, 로깅, 헤더 추가)

## 3. 예제
### application.yml 설정 예제
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service # http://localhost:8081 에 대한 서비스 이름
          uri: http://localhost:8081
          predicates:
            - Path=/api/users/** # 조건 매칭: 호스트 뒤의 uri가 /api/users 로 들어오는 모든 요청은 http://localhost:8081로 라우팅하라
          filters:
            - AddRequestHeader=X-RequestId, 1234 # RequestHeader에 X-Request-Id: 1234 추가
```
### 라우팅 - 1) Path 기반 라우팅
```yaml
routes:
  - id: order-service
    uri: http://localhost:8082
    predicates:
      - Path=/api/orders/**
```
✔ /api/orders/** 요청을 http://localhost:8082로 라우팅

### 라우팅 - 2) 헤더 조건으로 라우팅
```yaml
routes:
  - id: admin-service
    uri: http://localhost:8083
    predicates:
      - Header=X-Admin, true
```
✔ 요청 헤더에 X-Admin: true가 있을 경우 admin-service로 라우팅

## 4. Spring Cloud Gateway vs Netflix Zuul 비교
둘 다 API Gateway로 사용되며, 클라이언트 요청을 마이크로서비스로 라우팅하고 필터링하는 역할을 함

| 비교 항목          | Spring Cloud Gateway            | Netflix Zuul 1 (Zuul 2 제외)     |
|--------------------|--------------------------------|--------------------------------|
| **기본 역할**      | API Gateway (라우팅, 필터링)  | API Gateway (라우팅, 필터링)  |
| **비동기 처리**    | ✅ O (WebFlux, Netty 기반)   | ❌ X (블로킹 방식, 서블릿 기반) |
| **Spring WebFlux 지원** | ✅ O                      | ❌ X                          |
| **성능**          | ✅ 높음 (비동기)              | ❌ 낮음 (블로킹)              |
| **필터 방식**     | ✅ Spring 방식 (GatewayFilter) | ❌ Zuul Filter 방식 (Pre, Route, Post) |
| **로드 밸런싱**   | ✅ Spring Cloud LoadBalancer  | ✅ Ribbon 사용                |
| **서비스 디스커버리** | ✅ Eureka, Consul 등 지원 | ✅ Eureka 지원                |
| **마이그레이션 가능?** | ✅ 가능 (Zuul → Gateway) | ❌ Spring Cloud에서 더 이상 발전 없음 |

👉🏼 둘이 같은 역할을 하지만, Spring Cloud Gateway가 더 발전된 형태

### 1) Zuul은 기본적으로 동기(Blocking) 방식
- Zuul 1은 Servlet 기반 (Blocking I/O) 이라서 요청을 처리하는 동안 쓰레드가 계속 점유되고 있어서 성능이 떨어짐
- Spring Cloud Gateway는 Netty 기반 비동기 (Non-blocking I/O) 방식이라서 성능이 훨씬 좋음

### 2) Zuul 은 유지보수가 중단됨
- Spring Cloud는 더이상 공식적으로 Zuul을 발전시키지 않음 (Zuul 1에서 2로 넘어갔지만 Spring 에서는 안함)
- 그래서 Spring Cloud Gateway를 공식 API Gateway 솔루션으로 추천하고 있음

### 3) Spring Cloud Gateway가 Spring 생태계에 더 잘 맞음
- Zuul은 Netflix 에서 만든 솔루션이라서 Spring과 100% 일치하지 않음
- 하지만 Spring Cloud Gateway는 Spring Webflux 기반이라 Spring 프로젝트와 완벽하게 통합됨

## 5. 📌 미니 프로젝트 주제: API Gateway 기반 마이크로서비스 구축
### 📝 시나리오
💡 Spring Cloud Gateway를 활용하여 API Gateway를 구축하고, 마이크로서비스 간 라우팅 기능을 하는 프로젝트!

- 유저 서비스 (user-service): 회원 정보 제공
- 주문 서비스 (order-service): 주문 내역 제공
- API Gateway (api-gateway): 모든 요청을 받아서 유저/주문 서비스로 전달

### 1️⃣ 프로젝트 구조
```plain
spring-cloud-gateway/
│── api-gateway/          # Spring Cloud Gateway
│── user-service/         # 회원 서비스 (User API)
│── order-service/        # 주문 서비스 (Order API)
└── README.md             # 프로젝트 설명
```

### 2️⃣ api-gateway 설정
```yaml
server:
  port: 8000  # API Gateway 포트

spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/users/**
          filters:
            - AddRequestHeader=User-Header, MyValue
            - RewritePath=/api/users/(?<segment>.*), /$\{segment}  

        - id: order-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/orders/**
          filters:
            - AddRequestHeader=Order-Header, MyValue
            - RewritePath=/api/orders/(?<segment>.*), /$\{segment}
```
✔️ /api/users/** → user-service (8081)로 라우팅
✔️ /api/orders/** → order-service (8082)로 라우팅
✔️ 요청 시 User-Header 및 Order-Header 추가
✔️ RewritePath로 API 경로 변경

**1. AddRequestHeader=User-Header(Order-Header), MyValue**
- Gateway가 백엔드 서비스로 요청을 라우팅했을 때, User-Header: MyValue, Order-Header: MyValue 라는 헤더를 추가하는 역할을 한다.
- 예를 들어, 클라이언트가 아래와 같이 요청을 했다고 가정하자.
    ```http request
    GET /api/users/123 HTTP/1.1
    Host: gateway.example.com
    ```
- 그럼 이제 Gateway가 user-service로 요청을 전달할 때 User-Header: MyValue 이거를 추가해서 보내준다.
    ```http request
    GET /api/users/123 HTTP/1.1
    Host: gateway.example.com
    User-Header: MyValue
    ``` 
✨ 효과:
- 백엔드 서비스가 특정 헤더값이 있을 경우에만 처리를 하도록 제한할 수 있다.
- 인증, 로깅, 추적의 목적으로 추가 헤더를 활용할 수 있다.

**2. RewritePath=/api/users/(?<segment>.*), /$\{segment}**
- 요청 URL을 백엔드 서비스로 전달하기 전에 특정 패턴을 Rewrite(다시 쓰는) 역할을 한다. 
- `/api/users/(?<segment>.*)` 를 보면 /api/users 뒤에 오는 값을 segment에 저장하고 `/$\{segment}` 이것으로 다시 쓴다
- 예를 들면,
  - 클라이언트로부터 `GET /api/users/123` 요청이 들어오면
  - Gateway 내부에서 `/123` 으로 Rewrite 해서
  - 백엔드 서비스로 `GET /123` 만 전달하는 것이다.
- 즉 여기서는, /api/users 를 떼고 백엔드 서비스로 전달한다고 볼 수 있다.

✨ 효과:
- Gateway와 백엔드 서비스의 경로를 다르게 설정할 수 있어서 유연한 라우팅이 가능하다.
- 그리고 클라이언트에게 제공하는 API와 내부에서 사용하는 API 경로를 분리할 수 있다.

**정리**
위 설정이 적용되면, 클라이언트가 GET /api/users/123 요청을 보냈을 때 다음과 같은 변환이 일어남
```http request
GET /123 HTTP/1.1
Host: gateway.example.com
User-Header: MyValue
``` 