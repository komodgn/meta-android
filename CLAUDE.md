@AGENTS.md

## Code Quality

CI runs `./gradlew ktlintCheck detekt` on every PR.

Rules ktlint cannot auto-fix (detekt enforces these):
- No wildcard imports (`import foo.*` is banned)
- Trailing comma required on both call site and declaration site
- Blank line required between `package` declaration and `import` block

## Build & Check Commands

| Task | Command |
|------|---------|
| Lint check | `./gradlew ktlintCheck detekt` |
| Module dependency graph | `./gradlew generateProjectDependencyGraph` |
