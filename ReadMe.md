# 카카오 블로그 검색 API

### Developer

- Email : js.wise10@kakao.com

### Info

- Project Link : https://github.com/jaykingg/SearchBlog
- Project jar File Link : https://drive.google.com/file/d/1jNryi2TaQ4xlIWyT5eDyzCMXuo34oAys/view?usp=share_link

### Skills

- SpringBoot 2.7.5
- Spring Webflux
- Sprint JPA
- Kotest
- Kotlin 1.6.21 / Java 17
- Gradle
- H2 (in-memory)

### Requirements

- 키워드를 통한 검색
    - 검색 결과에서 Sorting(정확도순, 최신순) 기능을 지원.
      > ParameterObject 를 사용하여 키워드 외 Sort 값이 요청되면 그에 따른 결과값 응답.
    - 검색 결과는 Pagination 형태로 제공.
      > 아래와 같은 형태로 요청된 Size 에 맞게 응답하며, 카카오에서 제공된 page 정보를 응답.
    - 검색 소스는 카카오 API의 키워드로 블로그 검색을 활용.
      > Webclient 를 사용하여 카카오 / 네이버 검색 API 연동.
    - 추후 카카오 API 이외에 새로운 검색 소스가 추가될 수 있음을 고려.


- 인기 검색어 목록
    - 사용자들이 많이 검색한 순서대로, 최대 10개의 검색 키워드를 제공.
      > 애플리케이션에 검색이 요청되면 h2 에 Save / Update 되고, JPQL 를 통해 상위 10개의 인기 검색어를 검색결과와 함께 응답.
    - 검색어 별로 검색된 횟수도 함께 표기.
      > 인기 검색어와 함께 횟수도 함께 응답.


- 개선사항
    - 대용량 트래픽 및 저장된 데이터에 대한 처리.
      > 비동기 처리를 위한 Spring-Webflux 를 사용. in-memory DB인 H2 는 Reactive 지원하지 않으므로 완전한 비동기 방식이라고는 할 수 없으나
      실제 개발에서 MongoDB 등 Reactive 를 지원하는 DB 를 사용 시 해결되며 그에 따라 임시로 Transaction 및 Coroutine Scope 로 묶음처리.
    - 카카오 API 장애 시 대응.
      > 네이버 검색 API 를 연동하여 카카오 검색 API 에서 4XX, 5XX Error 발생 시 네이버 검색 API를 통해 결과를 노출시킬 수 있게 처리.

### Policy

- Rest api key 및 Client-id , Client-secret 의 노출 위험 때문에 application.yml 은 **원격저장소에서 제외되며 jar 파일에만 포함됩니다**.
- 주 결과 검색은 카카오 블로그 검색으로, 장애 발생 시 요청하는 네어버 블로그 검색은 카카오 블로그 검색 응답에 맞게 변환됩니다.
- 두 API 가 요청, 응답이 다름에 따라 카카오 블로그 Domain 에 없는 Field 는 Null 로 처리합니다.

### Request

| Name  |  Type  | Description  |
|:-----:|:------:|:------------:|
| query | String |     키워드      |
| page  |  Int   |    페이지 번호    |
| size  |  Int   | 한 페이지에 보여질 수 |
| sort  | String |    정렬 방식     |

### Response

|   Name   |        Type         | Description |
|:--------:|:-------------------:|:-----------:|
| populars | List\<BlogKeyword\> |   인기검색 결과   |
|  items   |  List\<KakaoBlog\>  |  블로그 검색 결과  |
|   page   | KakaoBlogMetaInfo?  |  페이지 메타정보   |

