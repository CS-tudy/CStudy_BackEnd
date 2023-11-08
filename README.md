# 📖 취업 CS 걱정하지마, CS;tudy

## ✨ 프로젝트 소개

- 취업에 있어 필요한 CS 지식을 학습할 수 있는 웹 어플리케이션 플랫폼 입니다. 카테고리 별 문제를 통해 필요한 지식을 빠르게 학습할 수 있으며 틀린 문제를 모아서 부족한 부분을 빠르게 인지할 수 있습니다.  또한 경쟁을 통하여 지루한 CS 학습에 재미를 부여하며 선의의 경쟁을 통해 동료와 함께 성장이 가능합니다.

<br/>

## 👨‍기술 스택

<h3 align="center">어플리케이션</h3>

<p align="center">

<img src="https://img.shields.io/badge/Java 11-008FC7?style=for-the-badge&logo=Java&logoColor=white"/>
<img src="https://img.shields.io/badge/spring 2.7.9-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white"/>

<img src="https://img.shields.io/badge/-QueryDSL-blue?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=Junit5&logoColor=white"/>

<img src="https://img.shields.io/badge/Mockito-FF9900?style=for-the-badge&logo=Mockito&logoColor=white"/>
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white"/>

</p>


<h3 align="center">DB</h3>

<p align="center">  
<img src="https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white"/>

</p>

<h3 align="center">인프라</h3>

<p align="center">   

<img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white"/>
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/>

</p>

<h3 align="center">문서 / 협업</h3>

<p align="center">   

<img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/>
<img src="https://img.shields.io/badge/Git-F05032.svg?style=for-the-badge&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717.svg?style=for-the-badge&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=for-the-badge&logo=Postman&logoColor=white"/>
<img src="https://img.shields.io/badge/sentry-362D59.svg?style=for-the-badge&logo=sentry&logoColor=white"/>

</p>

<br>


## 🎨 ERD Diagram

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/d4cf09ff-9a03-4eee-b02c-eacd4cfd84fa)

<br>

## 🖥 모니터링

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/383ced45-7d62-40f9-9a6d-611a1c0ac9cc)



<br>


## 🐧Test Code

### Controller
- 테스트 코드는 단순히 데이터의 정합성을 검증하는 로직이 아니라 내 코드를 읽는 상대방에게 설명하는 코드라고 생각합니다.
- ``Controller``의 테스트는 Mock을 통하여 HTTP 요청, 검증에서 책임, 주요 관심사를 분리하여 ``요청,검증``을 분리를 하였습니다.

### Service
 ``Service Layer Test``의 경우에 Test Double(Mocking)을 통하여 테스트를 진행을 하였을 때 Repository의 인터페이스가 변화를 하면 실패를 한다.
- 단위 테스트에서 Mocking을 하면 연관 관계의 ``인터페이스와 강하게 의존``하여 리펙토링을 어렵기 때문에 Classic 방식을 통하여 테스트를 진행을 하였습니다.
- 제어할 수 없는 코드인 경우에만 Test Double을 사용을 하였습니다.
- ``LocalDateTime.now()``의 테스트 코드는 항상 일관성을 보장하기 힘들어 Service에 변수를 넘겨주는 방식으로 변경

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/cb0a17d2-31b3-43d6-bec1-27a5e71465c4)

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/be95e586-3c28-4974-87b6-c389bf100795)


<br>


## 📋 API 문서

[https://documenter.getpostman.com/view/23650109/2s9YRGxp2L](https://documenter.getpostman.com/view/23650109/2s9YRGxp2L)

<br>

## 📕CStudy의 여정의 기능을 소개할게요.

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/4b3a3265-9e7e-43c2-8bd9-da8732ebbf8b)


<br>

## 🏛️ CI/CD 아키텍처 (배포 자동화)

![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/9d287ab0-95e0-4ed4-89da-6ae0eca05cf7)


### 현재 배포를 선택한 이유

### 1. 기존에 Jenkins를 통하여 배포 자동화
![image](https://github.com/CS-tudy/CStudy_BackEnd/assets/103854287/498a3c20-3c9b-46a0-8d83-0cacdd350479)

- 처음 배포를 선택한 `파이프라인`입니다. 이때 Jenkins를 선택한 이유는 스터디를 통하여 Jenkins를 학습한 경험이 있어 다른 2개의 기술보다 러닝커브가 낮다고 생각하여 적용을 하였습니다.
- Jenkins를 통하여 배포의 문제점은 트래픽이 증가를 하였을 때 Auto Scailing을 처리하기 위해 추가적인 작업이 필요하여 스프링 부트와 AWS로 혼자 구현하는 웹 서비스의 무중단 배포로 구조를 변경을 했습니다.

### 2. Github Action 배포 자동화 변경
 
[ 현재 문제 ]
- SSE를 추가하면서 분산 환경에 대한 제약사항이 발생을 하였습니다.
- 이러한 문제로 WAS를 1개만 사용하게 변경

[ 현재 적용 ]
- Git과 연동성이 좋은 Github Action을 통한 배포 자동화로 변경을 했습니다.
- 변경한 이유는 가장 간단하게 배포 자동화를 구성할 수 있었고, 제한된 Resource를 통하여 최대의 효율로 관리가 가능하다. ( Action만 사용하여 파이프라인 효율적 관리, 배포 성공시 알람이 나오는 ChatOps를 통해 서비스 오류에 빠르게 대응이 가능하다. )

[ 변경을 생각하는 부분 ] 
- 현재 SSE의 확장성을 생각하여 메세지(Kafka)를 적용하여 AWS 고가용성 아키텍처를 사용하여 배포를 고려하고 있습니다.

<br>



## 🥕프로젝트 백엔드 의사결정 및 이슈

### 최적화
- [실행 계획 분석을 통한 인덱스 추가 및 QueryDSL 페이징 쿼리 성능 튜닝 58.20% 개선](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EC%8B%A4%ED%96%89-%EA%B3%84%ED%9A%8D-%EB%B6%84%EC%84%9D%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EC%B6%94%EA%B0%80-%EB%B0%8F-QueryDSL-%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5-%ED%8A%9C%EB%8B%9D-58.20%25-%EA%B0%9C%EC%84%A0)
- [문제 Bulk Insert 문제를 Recursion(재귀)에서 Batch Insert으로 10,000건 데이터 1440초에서 105초 성능 개선](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EB%AC%B8%EC%A0%9C-Bulk-Insert-%EB%AC%B8%EC%A0%9C%EB%A5%BC-Recursion(%EC%9E%AC%EA%B7%80)%EC%97%90%EC%84%9C-Batch-Insert%EC%9C%BC%EB%A1%9C-10,000%EA%B1%B4-%EB%8D%B0%EC%9D%B4%ED%84%B0-1440%EC%B4%88%EC%97%90%EC%84%9C-105%EC%B4%88)
- [AWS S3 이미지 파일 Deflater 활용하여 압축을 통해서 JPG 기준 이미지 16.01% 크기 감소 ](https://github.com/CS-tudy/CStudy_BackEnd/wiki/AWS-S3-%EC%9D%B4%EB%AF%B8%EC%A7%80-%ED%8C%8C%EC%9D%BC-Deflater-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-%EC%95%95%EC%B6%95%EC%9D%84-%ED%86%B5%ED%95%B4%EC%84%9C-JPG-%EA%B8%B0%EC%A4%80-%EC%9D%B4%EB%AF%B8%EC%A7%80-16.01%25-%ED%81%AC%EA%B8%B0-%EA%B0%90%EC%86%8C)

### 프로젝트 진행 이슈
- [사용자 관점에 Polling 방식에서 SSE 방식으로 알림 개선]()
- [영 로그 관리, 접근성을 위해 Logback Rolling FileAppender 적용 및 Sentry 도입]()
- [CS 문제에 대한 번호를 선택을 하여 7개의 데이터 정합성 체크](https://github.com/CS-tudy/CStudy_BackEnd/wiki/CS-%EB%AC%B8%EC%A0%9C%EC%97%90-%EB%8C%80%ED%95%9C-%EB%B2%88%ED%98%B8%EB%A5%BC-%EC%84%A0%ED%83%9D%EC%9D%84-%ED%95%98%EC%97%AC-7%EA%B0%9C%EC%9D%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%95%ED%95%A9%EC%84%B1-%EC%B2%B4%ED%81%AC)
- [IP 기반 유저 블랙리스트 추가](https://github.com/CS-tudy/CStudy_BackEnd/wiki/IP-%EA%B8%B0%EB%B0%98-%EC%9C%A0%EC%A0%80-%EB%B8%94%EB%9E%99%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EC%B6%94%EA%B0%80)
- [FE & BE 운영 및 테스트를 위하여 Logback Rolling FileAppendar 적용 및  Slack Webhook 도입](https://github.com/CS-tudy/CStudy_BackEnd/wiki/FE-&-BE-%EC%9A%B4%EC%98%81-%EB%B0%8F-%ED%85%8C%EC%8A%A4%ED%8A%B8%EB%A5%BC-%EC%9C%84%ED%95%98%EC%97%AC-Logback-Rolling-FileAppendar-%EC%A0%81%EC%9A%A9-%EB%B0%8F--Slack-Webhook-%EB%8F%84%EC%9E%85)
- [Exception을 추상 클래스을 활용하여 응집도 증가 및 Front와 협업을 위해 Custom Error Status 생성](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Exception%EC%9D%84-%EC%B6%94%EC%83%81-%ED%81%B4%EB%9E%98%EC%8A%A4%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9D%91%EC%A7%91%EB%8F%84-%EC%A6%9D%EA%B0%80-%EB%B0%8F-Front%EC%99%80-%ED%98%91%EC%97%85%EC%9D%84-%EC%9C%84%ED%95%B4-Custom-Error-Status-%EC%83%9D%EC%84%B1)

### 리펙토링

- [전략 패턴을 사용하여 Param에 따른 서비스 호출 및 개별 컴포넌트 분리](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EC%A0%84%EB%9E%B5-%ED%8C%A8%ED%84%B4%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-Param%EC%97%90-%EB%94%B0%EB%A5%B8-%EC%84%9C%EB%B9%84%EC%8A%A4-%ED%98%B8%EC%B6%9C-%EB%B0%8F-%EA%B0%9C%EB%B3%84-%EC%BB%B4%ED%8F%AC%EB%84%8C%ED%8A%B8-%EB%B6%84%EB%A6%AC)
- [Redis 자료구조 랭킹 시스템 도입 및 캐싱 오버헤드를 고려하여 Redis Pub/Sub 캐싱 정합성](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Redis-%EC%9E%90%EB%A3%8C%EA%B5%AC%EC%A1%B0-%EB%9E%AD%ED%82%B9-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EB%8F%84%EC%9E%85-%EC%BA%90%EC%8B%B1-%EC%98%A4%EB%B2%84%ED%97%A4%EB%93%9C%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%98%EC%97%AC-Redis-Pub-Sub-%EC%BA%90%EC%8B%B1-%EC%A0%95%ED%95%A9%EC%84%B1)
- [테스트 코드를 통하여 코드 안전성 검증 및 Test Double 사용하며 컨트롤 할 수 없는 코드 영향 최소화](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C%EB%A5%BC-%ED%86%B5%ED%95%98%EC%97%AC-%EC%BD%94%EB%93%9C-%EC%95%88%EC%A0%84%EC%84%B1-%EA%B2%80%EC%A6%9D-%EB%B0%8F-Test-Double-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A9%B0-%EC%BB%A8%ED%8A%B8%EB%A1%A4-%ED%95%A0-%EC%88%98-%EC%97%86%EB%8A%94-%EC%BD%94%EB%93%9C-%EC%98%81%ED%96%A5-%EC%B5%9C%EC%86%8C%ED%99%94)
- [랭킹 참가 낙관적 락을 이용해 동시성 문제를 통한 중복요청 제어](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EB%9E%AD%ED%82%B9-%EC%B0%B8%EA%B0%80-%EB%82%99%EA%B4%80%EC%A0%81-%EB%9D%BD%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%B4-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%A4%91%EB%B3%B5%EC%9A%94%EC%B2%AD-%EC%A0%9C%EC%96%B4)
- [일반 문제 오답노트 기존에 MySQL에서 MongoDB Data Modelig 변경](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EC%9D%BC%EB%B0%98-%EB%AC%B8%EC%A0%9C-%EC%98%A4%EB%8B%B5%EB%85%B8%ED%8A%B8-%EA%B8%B0%EC%A1%B4%EC%97%90-MySQL%EC%97%90%EC%84%9C-MongoDB-Data-Modelig-%EB%B3%80%EA%B2%BD)
- [랭킹 시스템 동일한 점수일 경우 시간을 추가하여 Redis Structure 변경](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EB%9E%AD%ED%82%B9-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EB%8F%99%EC%9D%BC%ED%95%9C-%EC%A0%90%EC%88%98%EC%9D%BC-%EA%B2%BD%EC%9A%B0-Redis-Data-Structure-%EB%B3%80%EA%B2%BD)
- [HandlerMethodArgumentResolver, AOP를 적용하여 횡단 관심사 분리하기](https://github.com/CS-tudy/CStudy_BackEnd/wiki/HandlerMethodArgumentResolver,-AOP%EB%A5%BC-%EC%A0%81%EC%9A%A9%ED%95%98%EC%97%AC-%ED%9A%A1%EB%8B%A8-%EA%B4%80%EC%8B%AC%EC%82%AC-%EB%B6%84%EB%A6%AC%ED%95%98%EA%B8%B0)
- [Session 방식의 문제점  JWT 개선](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Session-%EB%B0%A9%EC%8B%9D%EC%9D%98-%EB%AC%B8%EC%A0%9C%EC%A0%90--JWT-%EA%B0%9C%EC%84%A0)

### 인프라
- [Github Actions CI + CodeDeploy로 CI/CD 구현하기](https://velog.io/@geon_km/Github-Actions-CI-CodeDeploy%EB%A1%9C-CICD-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-vum9u82d)
- [도커 컴포즈를 통한 고정 IP를 이용한 클라우드 서비스 배포](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EB%8F%84%EC%BB%A4-%EC%BB%B4%ED%8F%AC%EC%A6%88%EB%A5%BC-%ED%86%B5%ED%95%9C-%EA%B3%A0%EC%A0%95-IP%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%B0%B0%ED%8F%AC)


### 지속적인 리펙토링 
- [REST API에 적합한 URL 수정](https://github.com/CS-tudy/CStudy_BackEnd/wiki/REST-API%EC%97%90-%EC%A0%81%ED%95%A9%ED%95%9C-URL-%EC%88%98%EC%A0%95)
- [Test Code 실행 시간을 감소를 위한 Application Context 재활용](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Test-Code-%EC%8B%A4%ED%96%89-%EC%8B%9C%EA%B0%84%EC%9D%84-%EA%B0%90%EC%86%8C%EB%A5%BC-%EC%9C%84%ED%95%9C-Application-Context-%EC%9E%AC%ED%99%9C%EC%9A%A9)
- [Spring Security .hasAnyAuthority (권한) 가독성 높이기](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Spring-Security-.hasAnyAuthority-(%EA%B6%8C%ED%95%9C)-%EA%B0%80%EB%8F%85%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0)
- [PUT, PATCH 구분]()
- [Presentation layer Test Code 관심사 분리 및 가독성 증가]()
- [예외 코드 추상 클래스 구조 변경](https://github.com/CS-tudy/CStudy_BackEnd/wiki/%EC%98%88%EC%99%B8-%EC%BD%94%EB%93%9C-%EC%B6%94%EC%83%81-%ED%81%B4%EB%9E%98%EC%8A%A4-%EA%B5%AC%EC%A1%B0-%EB%B3%80%EA%B2%BD)
- [Swagger, Rest Docs 어떤 걸 선택을 할까?](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Swagger,-Rest-Docs-%EC%96%B4%EB%96%A4-%EA%B1%B8-%EC%84%A0%ED%83%9D%EC%9D%84-%ED%95%A0%EA%B9%8C%3F)
- [Jenkins Git Webhook을 통한 배포 자동화(SSHAgent)를 Github Actions CI + CodeDeploy 배포 자동화 변경](https://github.com/CS-tudy/CStudy_BackEnd/wiki/Github-Webhook%EC%9D%84-%ED%86%B5%ED%95%B4-Jenkins%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%B0%B0%ED%8F%AC-%EC%9E%90%EB%8F%99%ED%99%94---SSHAgent)


## 🥃 Wireframe

[📝 Figma 바로가기 ](https://www.figma.com/file/67asFaSpQCu4s2CKAJqxac/Untitled?type=design&node-id=0-1&mode=design&t=DdRtY5ictOvnNkSn-0)

![image](https://github.com/CStudyTeam/CStudy-backend/assets/103854287/cf4eae6b-43b5-409d-9125-178e33b89473)


