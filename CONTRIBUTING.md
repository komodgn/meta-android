# Contributing to MetaSearch
Thanks for stopping by! We are building a modern search experience and would love to have you onboard.

## How to Get Started
### 1. Setup Your Environment
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

### 2. Just Open an Issue
If you find a bug, have a feature idea, or even a tiny improvement, don't hesitate to open an issue. Just tell us what's on your mind! If you see an existing issue you'd like to work on, leave a comment so we can sync up.

## Technical Requirements
- UI: 100% Jetpack Compose.
- Architecture: Slack Circuit for UDF (Unidirectional Data Flow).
- DI: Metro DI (We don't use Hilt/Dagger).
- Define dependencies using @DependencyGraph (interface-based).
- Use @ContributesTo(AppScope::class) for decentralized binding.
- Code Health: Run `./gradlew ktlintCheck detekt` and `./gradlew stabilityCheck` before pushing.

## Questions?
Join our discussions or ping us in the PR comments.
