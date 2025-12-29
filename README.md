# Visualize Me By Photo

## Features
- Image Analysis
- Focusing Search
- Natural Language Search

|Search|Search|
|:---:|:---:|
|<img src="https://github.com/user-attachments/assets/c90afda9-8bb4-4e2d-a03a-6eeb6a74491b" width="250"/> |<img src="https://github.com/user-attachments/assets/b911e635-420a-4035-8b59-27f26baf9df6" width="280"/> |
|Natural Language Search|Focusing Search|

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
