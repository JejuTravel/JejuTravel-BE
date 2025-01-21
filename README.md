<a href="http://15.165.133.21:5173/" target="_blank">

![Image](https://github.com/user-attachments/assets/a83d7951-14b0-4640-8dda-d448594f2ff2)
</a>
[서비스 링크(잠정 중단)](http://15.165.133.21:5173/)
<br/>
<br/>

# 0. Getting Started (시작하기)
```bash
# << Back-End >>

# +) application.properties에 key 값들 넣어주기
# 0. gradle 설치 확인 
$ gradle -v
# 1. 프로젝트 빌드 및 실행 
$ ./gradlew build
$ ./gradlew bootRun


# << Front-End >>

# +) .env 파일 만들고 key 값들 넣어주기
# 1. 프로젝트 실행
$ npm run dev
```


<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: 중국어로 된 제주 여행 소개 플랫폼 '济州旅游(제주 여행)'
- 프로젝트 설명: ‘2024 관광데이터 활용 공모전’ 참가 (https://www.2024tourapi.com/)
- 프로젝트 수상 결과 : 한국관광공사 장려상, 제주관광공사 특별상
- 프로젝트 소개 : 저희 济州旅游(제주 여행) 관광 웹사이트는 제주도를 방문하는 중국인 관광객들이 필요한 정보를 중국어로 쉽게 얻을 수 있도록 다양한 기능을 제공합니다. ‘관광 페이지’를 통해 각종 행사나 관광 명소에 대한 정보를, ‘쇼핑 페이지’를 통해 기념품 및 특산품을 살 공간들을, ‘음식점 페이지’를 통해 제주도 토속 음식들을 맛볼 식당을, ‘숙소 페이지’를 통해 적절한 쉴 곳을 찾을 수 있습니다. 또한, ‘버스 정류소’, ‘공중화장실’과 ‘공공 와이파이’ 위치 검색 기능을 제공하여 여행 시 편리함을 제공합니다. ‘캘린더 페이지’를 통해, 일정을 효율적으로 관리할 수 있으며 여행 계획을 체계적으로 세울 수 있습니다. 다양한 장소에 대해 ‘리뷰’를 남기고 타 사용자의 리뷰를 확인할 수 있습니다. 더하여, AI 기반으로, 사용자 여행 취향에 맞춘 여행지를 추천해 줍니다.

<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 김민 | 김현지 | 김혜연 | 방재경 | 방채원 | 
|:------:|:------:|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/u/59240554?v=4" alt="김민" width="150"> | <img src="https://avatars.githubusercontent.com/u/107786171?v=4" alt="김현지" width="150"> | <img src="https://avatars.githubusercontent.com/u/85014114?v=4" alt="김혜연" width="150"> | <img src="https://avatars.githubusercontent.com/u/173883601?v=4" alt="방재경" width="150"> |<img src="https://avatars.githubusercontent.com/u/118799810?v=4" alt="방채원" width="150"> |
| PL, BE | FE | BE | FE | BE |
| [GitHub](https://github.com/doraemon49) | [GitHub](https://github.com/LOCAL-HJ) | [GitHub](https://github.com/KimHyeyeonn) | [GitHub](https://github.com/Jae-kyoung) |[GitHub](https://github.com/chaewon02) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- 로그인 및 회원가입
- 홈 화면 이용
- 관광 정보 검색, 조회 및 리뷰 기능
- 쇼핑 정보 검색, 조회 및 리뷰 기능
- 음식점 정보 검색, 조회 및 리뷰 기능
- 숙소 정보 검색, 조회 및 리뷰 기능
- 버스 정류소 위치 검색 기능
- 공중화장실 위치 검색 기능
- 공공 와이파이 위치 검색 기능
- 일정 관리 기능
- AI 기반 사용자 여행 취향에 맞춘 여행지 추천 기능

<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 김민    |  <img src="https://avatars.githubusercontent.com/u/59240554?v=4" alt="김민" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>백엔드 개발</li><li>공공 API</li><li>카카오 톡캘린더 API</li></ul>     |
| 김현지   |  <img src="https://avatars.githubusercontent.com/u/107786171?v=4" alt="김민" width="100"> | <ul><li>프론트앤드 개발</li><li>디자인</li><li>API 연결</li></ul> |
| 김혜연   |  <img src="https://avatars.githubusercontent.com/u/85014114?v=4" alt="김혜연" width="100">    |<ul><li>백엔드 개발</li><li>로컬 로그인</li><li>카카오 로그인</li></ul>  |
| 방재경    |  <img src="https://avatars.githubusercontent.com/u/173883601?v=4" alt="방재경" width="100">    |  <ul><li>프론트앤드 개발</li><li>디자인</li></ul>    
| 방채원    |  <img src="https://avatars.githubusercontent.com/u/118799810?v=4" alt="방채원" width="100">    | <ul><li>백엔드 개발</li><li>공공 API</li><li>리뷰 DB</li><li>배포 (AWS)</li></ul>    |

<br/>
<br/>

# 5. Technology Stack (기술 스택)
<!-- ## 5.1 Language -->
<!-- |  |  |
|-----------------|-----------------|
| HTML5    |<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">| 
| CSS3    |   <img src="https://github.com/user-attachments/assets/c531b03d-55a3-40bf-9195-9ff8c4688f13" alt="CSS3" width="100">|
| Javascript    |  <img src="https://github.com/user-attachments/assets/4a7d7074-8c71-48b4-8652-7431477669d1" alt="Javascript" width="100"> |  -->

<br/>

## 5.1 Frotend
[![My Skills](https://skillicons.dev/icons?i=react,vite,tailwind,css,figma&theme=light)](https://skillicons.dev)
<!-- |  |  |  |
|-----------------|-----------------|-----------------|
| React    |  <img src="https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2" alt="React" width="100"> | 18.3.1    |
| StyledComponents    |  <img src="https://github.com/user-attachments/assets/c9b26078-5d79-40cc-b120-69d9b3882786" alt="StyledComponents" width="100">| 6.1.12   |
| MaterialUI    |  <img src="https://github.com/user-attachments/assets/75a46fa7-ebc0-4a9d-b648-c589f87c4b55" alt="MUI" width="100">    | 5.0.0  |
| DayJs    |  <img src="https://github.com/user-attachments/assets/3632d7d6-8d43-4dd5-ba7a-501a2bc3a3e4" alt="DayJs" width="100">    | 1.11.12    | -->

<br/>

## 5.2 Backend
[![My Skills](https://skillicons.dev/icons?i=java,spring,gradle&theme=light)](https://skillicons.dev)
<!-- |  |  |  |
|-----------------|-----------------|-----------------|
| Firebase    |  <img src="https://github.com/user-attachments/assets/1694e458-9bb0-4a0b-8fe6-8efc6e675fa1" alt="Firebase" width="100">    | 10.12.5    | -->

<br/>

## 5.3 Cooperation
[![My Skills](https://skillicons.dev/icons?i=git,notion,discord&theme=light)](https://skillicons.dev)
<!-- |  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">    |
| Git Kraken    |  <img src="https://github.com/user-attachments/assets/32c615cb-7bc0-45cd-91ea-0d1450bfc8a9" alt="git kraken" width="100">    |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100">    | -->

<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
<< Back-End >>
📦 
├─ .DS_Store
├─ .gitignore
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ .DS_Store
   ├─ main
   │  ├─ .DS_Store
   │  ├─ java
   │  │  ├─ .DS_Store
   │  │  └─ com
   │  │     ├─ .DS_Store
   │  │     └─ example
   │  │        ├─ .DS_Store
   │  │        └─ jejutravel
   │  │           ├─ .DS_Store
   │  │           ├─ JejuTravelBeApplication.java
   │  │           ├─ config
   │  │           │  ├─ JwtAuthenticationEntryPoint.java
   │  │           │  ├─ JwtAuthenticationFilter.java
   │  │           │  ├─ SecurityConfig.java
   │  │           │  ├─ SwaggerConfig.java
   │  │           │  └─ TokenProvider.java
   │  │           ├─ controller
   │  │           │  ├─ BusStopController.java
   │  │           │  ├─ CalenderController.java
   │  │           │  ├─ MyPageController.java
   │  │           │  ├─ PublicToiletController.java
   │  │           │  ├─ PublicWifiController.java
   │  │           │  ├─ RestaurantController.java
   │  │           │  ├─ ReviewController.java
   │  │           │  ├─ ShoppingController.java
   │  │           │  ├─ StayController.java
   │  │           │  ├─ TourismController.java
   │  │           │  ├─ UserController.java
   │  │           │  └─ kakaoController.java
   │  │           ├─ domain
   │  │           │  ├─ Dto
   │  │           │  │  ├─ AreaResponse.java
   │  │           │  │  ├─ MyPageResponse.java
   │  │           │  │  ├─ MyPageUpdateRequest.java
   │  │           │  │  ├─ SignInRequest.java
   │  │           │  │  ├─ SignInResponse.java
   │  │           │  │  ├─ SignOutRequest.java
   │  │           │  │  ├─ SignUpRequest.java
   │  │           │  │  ├─ SignUpResponse.java
   │  │           │  │  ├─ UserDto.java
   │  │           │  │  ├─ content
   │  │           │  │  │  ├─ BusStopResponse.java
   │  │           │  │  │  ├─ ContentListResponse.java
   │  │           │  │  │  ├─ PublicToiletResponse.java
   │  │           │  │  │  ├─ PublicWifiResponse.java
   │  │           │  │  │  ├─ RestaurantInfoResponse.java
   │  │           │  │  │  ├─ ShoppingInfoResponse.java
   │  │           │  │  │  ├─ StayInfoResponse.java
   │  │           │  │  │  ├─ StayListResponse.java
   │  │           │  │  │  └─ TourismInfoResponse.java
   │  │           │  │  └─ review
   │  │           │  │     ├─ ReviewListResponse.java
   │  │           │  │     ├─ ReviewResponse.java
   │  │           │  │     ├─ ReviewSaveRequset.java
   │  │           │  │     └─ ReviewUpdateRequest.java
   │  │           │  └─ Entity
   │  │           │     ├─ KakaoUser.java
   │  │           │     ├─ RefreshToken.java
   │  │           │     ├─ Review.java
   │  │           │     └─ User.java
   │  │           ├─ global
   │  │           │  ├─ PythonTranslator.java
   │  │           │  ├─ api
   │  │           │  │  ├─ ApiResponse.java
   │  │           │  │  ├─ PageResponse.java
   │  │           │  │  └─ Status.java
   │  │           │  └─ exception
   │  │           │     └─ ResponseExceptionHandler.java
   │  │           ├─ repository
   │  │           │  ├─ KakaoUserRepository.java
   │  │           │  ├─ RefreshTokenRepository.java
   │  │           │  ├─ ReviewRepository.java
   │  │           │  └─ UserRepository.java
   │  │           └─ service
   │  │              ├─ JejuApiManager.java
   │  │              ├─ MyPageService.java
   │  │              ├─ OpenApiManager.java
   │  │              ├─ ReviewService.java
   │  │              ├─ TourismService.java
   │  │              ├─ UserService.java
   │  │              └─ kakaoService.java
   │  └─ resources
   │     ├─ application.properties
   │     └─ translator.py
   └─ test
      └─ java
         └─ com
            └─ example
               └─ jejutravel
                  └─ JejuTravelBeApplicationTests.java


```

```plaintext
<< Front-End >>
📦 
.gitignore
README.md
index.html
package-lock.json
package.json
├─ postcss.config.js
├─ public
│  └─ index.html
src
│  ├─ App.jsx
│  ├─ apis
│  │  └─ index.js
│  ├─ assets
│  │  ├─ jeju.jpg
│  │  └─ land.jpg
│  ├─ components
│  │  ├─ Header.jsx
│  │  ├─ List.jsx
│  │  ├─ Map.jsx
│  │  ├─ Search.jsx
│  │  └─ TouristInfoSearch.jsx
│  ├─ hooks
│  │  └─ useInfiniteScroll.jsx
│  ├─ index.css
│  ├─ main.jsx
│  └─ pages
│     ├─ accommodation
│     │  ├─ components
│     │  │  ├─ accommodetail.jsx
│     │  │  ├─ accommolist.jsx
│     │  │  ├─ accommorecommend.jsx
│     │  │  └─ accommoreview.jsx
│     │  └─ index.jsx
│     ├─ bus
│     │  └─ index.jsx
│     ├─ home
│     │  ├─ components
│     │  │  ├─ HomeAccommodation.jsx
│     │  │  ├─ HomeShoppingRestaurant.jsx
│     │  │  ├─ HomeTourism.jsx
│     │  │  └─ MainBanner.jsx
│     │  └─ index.jsx
│     ├─ restaurant
│     │  ├─ components
│     │  │  ├─ RestaurantDetail.jsx
│     │  │  └─ RestaurantSection.jsx
│     │  └─ index.jsx
│     ├─ restroom
│     │  └─ index.jsx
│     ├─ shopping
│     │  ├─ components
│     │  │  ├─ ShoppingDetail.jsx
│     │  │  └─ ShoppingSection.jsx
│     │  └─ index.jsx
│     ├─ tourism
│     │  ├─ components
│     │  │  ├─ TourismDetail.jsx
│     │  │  └─ TourismSection.jsx
│     │  └─ index.jsx
│     └─ wifi
│        └─ index.jsx
├─ tailwind.config.js
└─ vite.config.js
```


<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

<< Back-End >>
- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.

- dev Branch
  - Main Branch에 업로드되기 전, 개발 단계에서 모든 브랜치의 코드를 통합하는 브랜치입니다.
  
- min / hy / cw-api Branch
  - 팀원 개별 개발 브랜치입니다.
  - 모든 기능 개발은 각자의 브랜치에서 진행됩니다.

<< Front-End >>
- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.

- dev Branch
  - Main Branch에 업로드되기 전, 개발 단계에서 모든 브랜치의 코드를 통합하는 브랜치입니다.

<br/>
<br/>
