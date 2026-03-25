# Contributing to MetaSearch
Thanks for stopping by! We are building a modern search experience and would love to have you onboard.

## 1. How to Get Started
### 1.1. Setup Your Environment
- Fork the repo and clone it to your local machine.

- Copy local.properties.template to local.properties and fill in your keys.
``` .local.properties
OPENAI_API_KEY=
DEBUG_OPENAI_SERVER_URL=
RELEASE_OPENAI_SERVER_URL=
DEBUG_WEB_SERVER_URL=
RELEASE_WEB_SERVER_URL=
DEBUG_AI_SERVER_URL=
RELEASE_AI_SERVER_URL=
```

### 1.2. Just Open an Issue
If you find a bug, have a feature idea, or even a tiny improvement, don't hesitate to open an issue. Just tell us what's on your mind! If you see an existing issue you'd like to work on, leave a comment so we can sync up.

## 2. Technical Requirements
- UI: 100% Jetpack Compose.
- Architecture: Slack Circuit for UDF (Unidirectional Data Flow).
- DI: Metro DI (We don't use Hilt/Dagger).
- Define dependencies using @DependencyGraph (interface-based).
- Use @ContributesTo(AppScope::class) for decentralized binding.
- Code Health: Run `./gradlew ktlintCheck detekt` and `./gradlew stabilityCheck` before pushing.

## 3. Questions?
Join our discussions or ping us in the PR comments.

---

# Download Server

## Meta-ML
- Download [docker image](https://hub.docker.com/r/komodgn/meta-ml)

## Meta-Web
- Download [docker image](#how-to-use-this-image)  
  **Meta-Web** is a backend server built with Node.js (Express). It bridges the Android client and the ML analysis server, managing metadata and serving a React-based web interface for knowledge graph visualizations([sample](https://meta-webview.vercel.app/demo/graph)).

### Tech Stack
- Backend: Express (Node.js)
- Neo4j:
    - Graph Database
    - Desktop (Multiple Databases)
    - Cypher Query Language
    - APOC Library ([apoc.periodic.iterate](https://neo4j.com/docs/apoc/current/overview/apoc.periodic/apoc.periodic.iterate/))
- Communication: REST API / Bolt Protocol

### Database Configuration (Neo4j Desktop)
This system is designed to support Multi-Database environments to isolate metadata for different users or projects.

#### Why Neo4j Desktop?
Standard Neo4j Community Docker images do not support multi-database features. To leverage full multi-database capabilities, we recommend using Neo4j Desktop on your host machine.

#### Connection
The server connects to your host's Neo4j instance via the Bolt protocol.

### How to use this image

#### Environment Variables
- Create a `.env` file with the following database and path configurations:

```.env
NEO4J_ADDRESS=(e.g. bolt://host.docker.internal:7687)
NEO4J_USER=
NEO4J_PASSWORD=
CSV_DIRECTORY_PATH=/app/csv_imports
```

#### Prerequisites
- `Neo4j Desktop`: Install it on your host machine and ensure a database is "Started".

#### To pull and run the image

```shell
$ docker pull ghcr.io/komodgn/meta-web-server:latest
```
```shell
$ docker run -d -p 8081:8081 \
  --env-file .env \
  -v "[Your_Neo4j_Import_Path]:/app/csv_imports" \
  --name meta-web \
  ghcr.io/komodgn/meta-web-server:latest
```
