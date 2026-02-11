<div align="center">
    <h1>Visualize Me By Photo</h1>
    <h5>스마트폰 속에 흩어져있는 나만의 정보를 지식화하여 활용하는 서비스</h5>
    <img src="https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>
</div>

## Features
- Image Analysis
- Drag Search
- Natural Language Search
- Face Analytics
 
|자연어 검색|드래그 검색|인물 정보 통합 및 관리|개인화|
|:---:|:---:|:---:|:---:|
|<img src="https://github.com/user-attachments/assets/da38e9a8-e09c-4347-9fa8-e65470ff0d33" width="200"/> |<img src="https://github.com/user-attachments/assets/aa075c0b-bbd6-4fec-9e32-06a140ae21aa" width="200"/> |<img src="https://github.com/user-attachments/assets/1d9a19a8-e605-4806-8bba-6bb2894f5f05" width="200"/> |<img src="https://github.com/user-attachments/assets/c8dab122-27c8-4251-8caa-83d8d96ed1a6" width="200"/> |
|자연어를 통한 복잡한 조건으로 정확한 검색|간단한 드래그 동작으로 쉽고 빠른 검색|분석된 인물 통합 및 프로필 사진 변경|선호하는 인물로 등록하고 홈 화면에서 빠르게 접근|

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
│   ├── search              # AI 기반 자연어 및 드래그 검색 화면
│   └── splash
└── gradle                  # Version Catalog (libs.versions.toml)
```
