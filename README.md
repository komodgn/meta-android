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
- Gallery Viewer - 🔗[Demo Link](https://meta-webview.vercel.app/demo/graph)

|자연어 검색|드래그 검색|갤러리 탐색|
|:---:|:---:|:---:|
|<img src="https://github.com/user-attachments/assets/da38e9a8-e09c-4347-9fa8-e65470ff0d33" width="200"/> |<img src="https://github.com/user-attachments/assets/aa075c0b-bbd6-4fec-9e32-06a140ae21aa" width="200"/> |<img src="https://github.com/user-attachments/assets/2cd33e51-7231-4158-98d2-ae26ca70ac71" width="200"/>|
|자연어를 통한 복잡한 조건으로 정확한 검색|간단한 드래그 동작으로 쉽고 빠른 검색|이미지 간의 연결된 정보로 사진 탐색|

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
- [Metro](https://github.com/ZacSweers/metro)

### Network And Image Loading
- Retrofit
- OkHttp3
- Coil 3

### Code Quality
- Ktlint
- DeteKt
- Stability: [compose-stability-analyzer](https://github.com/skydoves/compose-stability-analyzer)

### Architecture
- MVI (Model-View-Intent) with [Slack Circuit](https://github.com/slackhq/circuit)
-  Module Strategy: Feature-based Multi-Module

## Contributing
Welcome! You can see the [Contributing Guide](https://github.com/komodgn/meta-android?tab=contributing-ov-file).
