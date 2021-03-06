# Bitravel Project
### Bitravel Project Main Repository

* [시연 영상 보기](https://www.youtube.com/watch?v=Jzt2YoSWCfM)


* [bitravel-resources](https://github.com/bitravel/bitravel-resources) : 데이터 전처리용 Python code 일부, 그 외 어떤 자료건 업로드 가능

## 사용 기술
* Java (Spring Boot, Gradle, JPA Hibernate, Thymeleaf, Spring Security)
* Python (Pandas, Numpy, Selenium)
* HTML/CSS
* Javascript (jQuery, fetch API)
* MySQL
* Windows 10
* Linux Ubuntu
* Docker (Redis)
* Visual Studio Code
* Eclipse EE
* 대한민국 공공데이터 (Tour API, 기상청 단기예보/중기예보 API)
* Kakao Maps API
* Primary Sources (Bootstrap, Font Awesome, Summernote Lite, Assan 4, Lucy XSS Servlet Filter)

## 담당 업무
### [배승준](https://github.com/seungjunbae)
팀 리더, 프로젝트 기획·배포, DB 구성 및 관리, 게시물/후기 관련 기능 FE·BE

### [김수교](https://github.com/holydonkey2)
프로젝트 기획, HTML 페이지 레이아웃 구성 및 제작, 메인/마이페이지 관련 기능 FE·BE

### [김종희](https://github.com/spacekay)
프로젝트 기획·배포, 일정 및 VCS 관리, DB 구성, 회원/여행지/검색결과/추천 관련 기능 FE·BE


## 일정표 확인
https://docs.google.com/spreadsheets/d/1xrJY5oFsCTjjSEwxY2uCujWKFVm5Q1uY/edit?rtpof=true

## DB 구성
(2021-12-11 기준)

![bitravel-db](https://blog.kakaocdn.net/dn/kK0nW/btrnBMlCXOl/PU5sPBTAVrTm2E5ZUUuQk0/img.png)

## Frontend
* __페이지 구성__ : Assan 4 templates, Summernote Lite, Bootstrap, Font Awesome 등 기반
* __레이아웃 설정__ : Thymeleaf 기반 반복 영역 처리
* __JavaScript__ : 필요에 따라 일부 jQuery 적용, 그 외에는 가능한 경우 Vanilla JS 적용
* __REST API 실행 결과 출력__ : fetch API를 통한 Ajax 활용

## Backend
* __DB 전처리__ : Python Pandas, Selenium
* __서버 구성__ : Spring Boot(Tomcat 포함), MySQL, Redis
* __회원 기능__ : Spring Security, jwt (Cookie에 토큰 저장), Redis (로그아웃된 토큰 저장 후 검증)
* __게시물/후기 기능__ : @RestController 및 Pageable interface 활용
* __지도 열람 기능__ : Kakao Maps API 기반 지정 여행지 위치 표시 구현
* __여행지 날씨 기능__ : 기상청 단기예보/중기예보 API 기반 json data parsing
* __Custom error page__ : 주요 Status code 대응 (404, 500, 401, 403, 400)
* __JAR Build__ : Gradle 기반 JAR build 및 Quality Test 진행

## Deployment
* __외부 환경 배포__ : AWS EC2 인스턴스 상 MySQL, Redis 구현 후 JAR file 구동 성공
* __반응형 웹 구현 검증__ : Windows 10, Ubuntu Linux, Android, iOS 상 Chrome, Firefox, Safari에서 작동 확인

## Status
* 최종 발표 완료 (12/29)

## Additional Suggestion
* __기능 고도화__ : 지역 뿐 아니라 테마 등으로 여행지를 분류하여 추천 기능에 반영
* __모바일 환경 최적화__ : 모바일 환경에서의 View 지속 개선

powered by spacekay 
