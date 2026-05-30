@AGENTS.md

## Code Quality

CI runs `./gradlew ktlintCheck detekt` on every PR.

## Build & Check Commands

| Task | Command |
|------|---------|
| Lint check | `./gradlew ktlintCheck detekt` |
| Module dependency graph | `./gradlew generateProjectDependencyGraph` |
