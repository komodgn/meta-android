# Domain Module Creation Skill

This skill handles the addition of new modules within the `domain/` package.

## Development Rules
- **Pure Kotlin Only**: `domain` modules must not depend on any Android SDK, Compose, or platform-specific frameworks. Use pure Kotlin to ensure portability and testability.
- **JVM Convention**: Use the `metasearch.jvm.library` plugin in `build.gradle.kts`. This prevents accidental usage of Android APIs.
- **Repository Interfaces**: Define data contracts (interfaces) here. Do not reference the `data` layer implementation directly.
- **Use Case Pattern**: Encapsulate business logic into single-responsibility `UseCase` classes.
- **Domain Models**: Use dedicated Domain Entities for business logic. Do not leak `data` layer DTOs into the domain layer.

## Dependency Configuration (`build.gradle.kts`)
```kotlin
plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    api(projects.data.domain)

    implementation(libs.kotlinx.coroutines.core)
}
