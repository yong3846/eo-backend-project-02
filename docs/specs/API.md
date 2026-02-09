# API 명세서 작성

### 1. API 소개
게시판 및 게시물, 신고/문의/쪽지 기능을 제공한다.

사용자는 게시물을, 생성,조회,수정,삭제할 수 있으며, 게시판 단위로 게시물을 관리한다.
사용자는 신고/문의/쪽지를 통해 다른 사용자와 소통할 수 있다.
소통한 내용은 저장된다.

### 2. 공통 요청 / 응답 형식
    - application / json

### 3. 인증 / 인가 방식
    - 모든 게시물 조회는 API는 로그인 없이 가능하다.
    - 게시물 작성, 수정, 삭제 API만 로그인이 필수이다.
    - JWT(Authorization Header)
    - 게시물 조회를 제외한 다른 API 기능은 로그인이 필수이다.

### 4. API 사용 순서
   1. 게시판 목록 조회(인증 불필요) 
   2. 특정 게시판의 게시물 목록 조회(인증 불필요)
   3. 게시물 상세 조회(인증 불필요)
   4. 댓글 목록 조회(인증 불필요)
   5. [인증 필요] 로그인(JWT 토큰 발급)
   6. [인증/인가 확인] 게시물 / 댓글 작성(본인 인증)
   7. [인증/인가 확인] 게시물 / 댓글 수정(본인 작성 게시물 만)
   8. [인증/인가 확인] 게시물 / 댓글 삭제(본인 작성 게시물, 관리자 권한 사용자)
   9. [인증] 게시물 / 댓글 신고 접수
   10. [사용자기능] 1:1 문의 작성(로그인 필요)
   11. [사용자기능] 쪽지 발송(본인 인증)
   12. [사용자기능] 쪽지함 조회 및 상세 읽기
   13. [사용자기능] 신고 목록 조회
   14. [관리자 권한] 신고 목록 조회 
   15. [관리자 권한] 신고 상세 확인 및 답변 작성
   16. [관리자 권한] 사용자 문의 목록 조회
   17. [관리자 권한] 문의 상세 확인 및 처리
   18. [관리자 권한] 쪽지 관리 및 시스템 공지 쪽지
    

### 5. API Refrerence
    1. 게시물 및 게시판 (Posts & boards)
    2. 댓글 (comments)
    3. 쪽지 및 소통 (message & reports)
    4. 관리자 기능 (admin)

|    기능 설명     |   요청 방식   | URL | JSON 포함 내용   | 제약 조건 및 응답  |
|:------------:|:---------:| ---- |--------------|-------------|
|    게시물 목록    |    GET    | /{board_id}/list | | 게시판 종류별 페이징 조회 |
 |    게시물 조회    |    GET    | /{board_id}/{post_id}/read | | 등록된 post_id 여야 함 (존재하지 않을 시 오류)
|    게시물 작성    |   POST    | /{board_id}/write | | 제목 1~80자, 비로그인 작성 불가
|    게시물 수정    |   PATCH   | /{post_id}/update | | 작성자 본인만 수정 가능(작성 제약조건을 지켜야함
|    게시물 삭제    |  DELETE   | /{post_id}/delete | | 관리자/매니저/작성자만 삭제 가능
|    댓글 작성     |   POST    | /{post_id}/comment/write | | 내용 1~1000자, 비로그인 작성 불가
|    댓글 수정     |   PATCH   | /{post_id}/{comment_id}/update | | 작성자 본인만 수정 가능(작성 제약조건을 지켜야함)
|    댓글 삭제     |  DELETE   | /{post_id}/{comment_id}/delete | | 관리자/매니저/작성자만 삭제 가능
|    신고 목록     |    GET    | /admin/reports/list | | 관리자 신고함 페이징 조회
|    신고 조회     |    GET    | /admin/reports/{report_id}/read | | 접수된 report_id 여야 함(존재하지 않을 시 오류)
|    신고 답변     |   POST    | /admin/reports/{report_id}/write | | 내용 1~1000자, 관리자 작성 가능
|    문의 목록     |    GET    | /admin/supports/list | | 관리자 문의함 페이징 조회
|    문의 조회     |    GET    | /admin/{supports_id}/read | | 접수된 support_id 여야 함 (존재하지 않을 시 오류)
|    문의 답변     |   POST    | /admin/{supports_id}/write | | 내용 1~1000자, 관리자 작성 가능
|    신고 작성     |   POST    | /report | | 카테고리 선택 필수, 내용 1~1000자, 비로그인 작성 불가
|    문의 작성     |   POST    | /support | | 내용 1~1000자, 비로그인 작성 불가
|    문의 목록     |    GET    | /supports/list | | 문의함 페이징 조회
|    문의 조회     |    GET    | /supports/{support_id}/read | | 접수한 support_id 여야 함(존재하지 않을 시 오류)
|     쪽지작성     |   POST    | /message/send | | 수신인 필수, 내용 1000자 이내
|   보낸쪽지 목록    |    GET    | /message/send/list | | 보낸 쪽지함 페이징 조회
|   받은 쪽지 목록   |    GET    | /message/receive/list | | 받은 쪽지함 페이징 조회
|    쪽지 조회     |    GET    | /message/{message_id}/read | | 받거나 보낸 message_id 여야 함(존재하지 않을 시 오류)

### HTTP 상태코드

| Status Code  |   Description   | 사용 예시 |
|:------------:|:---------------:| -------- |
|     200      |       OK        | 게시물 조회, 사용자 정보 조회, 정보 수정 성공 시 |
|     201      |     Created     | 회원 가입 완료, 게시물/댓글/쪽지 등록 성공시 |
|     400      |   Bad Request   | 필수 항목 누락, 글자수 제한 초과, 이메일 형식 오류 |
|     401      |  Unauthorized   | 비로그인 상태로 글쓰기, 정보 조회 시도 시 |
|     403      |    Forbidden    | 본인 글이 아닌데 수정/삭제 시도, 관리자 메뉴 접근 시 |
|     404      |    Not Found    | 존재하지 않는 게시물 번호나 수신자 ID 요청 시 |
|     409      |    Confilict    | 이미 사용 중인 이메일이나 닉네임으로 가입 시도 시 | 
|     500      | Internal Error  | 서버 내부 로직 처리 중 예상치 못한 오류 발생 |

### Error 코드 

|     Error Code      | Description | HTTP Status  |
|:-------------------:| ----------- |:------------:|
|   PPROCESS_LOCKED   | 이미 완료된 신고를 중복 처리하려고 할 때 |     400      |
|  VALIDATION_ERROR   | 필수값 누락, 이메일 형식 불일치, 글자수 초과 |     400      |
|    AUTH_REQUIRED    | 로그인이 필요한 서비스에 비로그인으로 접근 |     401      |
|    ACCESS_DENIED    | 게시물 수정/삭제 권한 부족, 관리자 페이지 접근 불가 |     403      |
| RESOURCE_NOT_FOUND  | 게시물, 사용자, 쪽지 등을 찾을 수 없음 |     404      |
|   DUPLICATE_ERROR   | 중복된 이메일 또는 닉네임 사용 시도 |     409      |
|   INTERNAL_ERROR    | 서버 내부 데이터베이스 연동 및 시스템 오류 |     500      |