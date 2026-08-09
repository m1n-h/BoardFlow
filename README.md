# 🚀 BoardFlow Engine

> **Spring Boot, Tomcat 등 외부 프레임워크나 서블릿 컨테이너 없이, Pure Java와 Socket API만으로 직접 구축한 경량 HTTP 웹 서버 엔진 프로젝트입니다.**  
> 웹 프레임워크의 내부 동작 원리(HTTP Protocol, Thread Management, I/O)를 깊이 있게 이해하고, 향후 구축할 **게시판·스케줄링·회원 인증 서비스**의 기반 인프라를 직접 설계합니다.

---

## 🛠 사용 기술 (Tech Stack)
- **언어:** Java 21
- **프로토콜:** HTTP/1.1
- **동시성 처리:** Java `java.util.concurrent.ExecutorService` (Thread Pool)
- **개발 환경:** IntelliJ IDEA, Git

---

## 🎯 핵심 아키텍처 및 구현 기능 (Core Architecture)

### 1. 멀티스레드 요청 처리
- `ServerSocket` 기반 클라이언트 연결 수신
- `ExecutorService`를 활용한 스레드 재사용 및 동시 요청 처리 최적화 (Resource Blocking 방지)

### 2. HTTP Request Parser
- Low-Level `InputStream` 텍스트 스트림을 자바 객체(`HttpRequest`)로 구조화
- Request Line (Method, Path, HTTP Version) 및 Request Headers Key-Value 파싱

### 3. Static File Serving
- Java `ClassLoader` 기반 `resources/static` 경로 내 정적 자원(`index.html` 등) 탐색 및 서빙
- 요청 자원 미존재 시 `404 Not Found` 예외 응답 헤더 및 바디 전송 처리

---

## 📊 로드맵 및 진행 상황

- [x] **Phase 1: 기본 소켓 서버 구축**
    - Socket 기반 요청 수신 및 기본 HTTP 응답 처리
- [x] **Phase 2: 동시성 처리 및 HTTP 파싱 인프라**
    - Thread Pool 적용 (`ExecutorService`)
    - HTTP Request Text ➔ `HttpRequest` 객체 파싱
    - ClassLoader 기반 정적 파일 서빙 및 404 예외 처리
- [ ] **Phase 3: Response & Router 구조화**
    - `HttpResponse` 캡슐화 및 Content-Type(MIME) 동적 처리
    - URL Path 기반 Request Handler Mapping (Simple Router)
- [ ] **Phase 4: 회원 인증 및 사용자 관리**
    - Cookie / Session 기반 회원가입 및 로그인 기능
- [ ] **Phase 5: 핵심 서비스 구현 (게시판 & 스케줄러)**
    - JDBC 기반 Database 연동
    - 게시판 CRUD 및 개인 스케줄 관리 서비스 구축