# Visualize Me By Photo

<img src="https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>

## Demo

### 일상 언어로 사진 검색
<video src="https://github.com/user-attachments/assets/56c7dd41-de57-40ca-b7d3-4b4e7b8f23e8" width="400px"></video>

### 등록한 인물 이름으로 사진 검색
<video src="https://github.com/user-attachments/assets/228a8d81-05e6-4a41-9ea6-036703a5127e" width="400px"></video>

## Features
- Image Analysis
- Focusing Search
- Natural Language Search
- Face Analytics

### Intelligent Search

|자연어 검색|포커싱 검색|
|:---:|:---:|
|<img src="https://github.com/user-attachments/assets/c90afda9-8bb4-4e2d-a03a-6eeb6a74491b" width="250"/> |<img src="https://github.com/user-attachments/assets/b911e635-420a-4035-8b59-27f26baf9df6" width="280"/> |
|자연어를 통한 복잡한 조건으로 정확한 검색|간단한 드래그 동작으로 쉽고 빠른 검색|

### Person In My Gallery

|인물 정보 통합 및 관리|개인화|
|:---:|:---:|
|<img src="https://github.com/user-attachments/assets/1d9a19a8-e605-4806-8bba-6bb2894f5f05" width="230"/> |<img src="https://github.com/user-attachments/assets/c8dab122-27c8-4251-8caa-83d8d96ed1a6" width="230"/> |
|분석된 인물 통합 및 프로필 사진 변경|선호하는 인물로 등록하고 홈 화면에서 빠르게 접근|

## Local Settings
- local.properties
```.properties
OPENAI_API_KEY=
DEBUG_WEB_SERVER_URL=
RELEASE_WEB_SERVER_URL=
DEBUG_AI_SERVER_URL=
RELEASE_AI_SERVER_URL=
```

## Tech Stack

### Jetpack Libraries
- DataStore
- Room
- Splash
- WorkManager
- Paging3

### UI
- Jetpack Compose (Declarative UI framework)

### Permissions
- Accompanist Permissions

### DI
- Dagger/Hilt

### Network And Image Loading
- Retrofit
- OkHttp3
- Coil 3

### Code Quality
- Ktlint
- DeteKt

### Architecture
- MVI (Model-View-Intent) with [Slack Circuit](https://github.com/slackhq/circuit)
-  Module Strategy: Feature-based Multi-Module

### Project Dependency Graph
<img width="2452" height="383" alt="project-dependency-graph" src="https://github.com/user-attachments/assets/dbbd3ba7-8df1-4411-9875-3c59fe4ac70e" />

## Project Structure
```text
.
├── app                     # 앱 실행 진입점 (Hilt Setup)
├── build-logic             # Convention Plugins (Gradle 공통 설정 관리)
├── core                    # 공통 기능 모듈 (Shared Modules)
│   ├── common              # 유틸리티, 상수, 공통 코드
│   ├── data
│   │   ├── api
│   │   └── impl
│   ├── datastore           # Preference DataStore 관리
│   │   ├── api
│   │   └── impl
│   ├── designsystem        # 공통 컴포넌트 및 테마
│   ├── model               # 도메인 모델
│   ├── network             # Retrofit 설정 및 네트워크 서비스
│   ├── notification        # 알림(Notification) 생성 및 관리
│   ├── room                # Room 로컬 데이터베이스 설정
│   │   ├── api             # DAO 인터페이스
│   │   └── impl            # Database 생성 및 Migration 로직
│   └── ui
├── feature                 # 화면 단위 기능 모듈 (Feature Modules)
│   ├── detail
│   ├── graph               # 갤러리 전체 데이터 시각화 화면
│   ├── home                # 홈 화면 (이미지 분석 워커 포함)
│   ├── main
│   ├── person              # 인물 사진 모아보기 및 관리
│   ├── screens             # 메인 네비게이션 및 스크린 정의
│   ├── search              # AI 기반 자연어 및 포커싱 검색 화면
│   └── splash
└── gradle                  # Version Catalog (libs.versions.toml)
```
