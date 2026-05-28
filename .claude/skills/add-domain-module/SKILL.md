---
name: add-domain-module
description: "Creates a new domain module in the MetaSearch Android project. Use this skill when requested to 'add domain module', 'add a new domain' or 'add repository/usecase interface'."
---

# Domain Module Creation Skill

This skill creates a new domain module in the `domain/` package.

## Prerequisites & Pre-steps

Before generating the module, confirm:
1. **Module Naming**:
    - `{domain}`: Must be **snake_case** (e.g., `photo_tag`).
    - `{Domain}`: Must be **PascalCase** (e.g., `PhotoTag`).
2. **Module Purpose** (What business domain this represents)
3. **Repository Interfaces** (What data contracts are needed)
4. **Use Cases** (What business operations are needed)

## Module Structure

Each domain module consists of a single `api` submodule:

```
domain/
└── {domain}/
    └── api/               ← Public contracts (interfaces only)
        ├── build.gradle.kts
        └── src/main/java/com/metasearch/android/domain/{domain}/api/
            ├── repository/
            │   └── {Domain}Repository.kt
            └── usecase/
                └── {Action}{Domain}UseCase.kt
```

## Implementation Steps

### 1. Gradle Configuration (`domain/{domain}/api/build.gradle.kts`)

```kotlin
plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    api(projects.data.domain)

    implementation(libs.kotlinx.coroutines.core)
}
```

### 2. File Structure

Create the source directory:
`domain/{domain}/api/src/main/java/com/metasearch/android/domain/{domain}/api/`

### 3. Key Files to Create

#### Repository Interface (`repository/{Domain}Repository.kt`)
```kotlin
package com.metasearch.android.domain.{domain}.api.repository

import kotlinx.coroutines.flow.Flow

interface {Domain}Repository {
    fun getAll(): Flow<List<{DomainModel}>>

    suspend fun getById(id: Long): {DomainModel}?

    // TODO: Add data contract methods
}
```

#### UseCase Interface (`usecase/{Action}{Domain}UseCase.kt`)

**Flow return (reactive stream):**
```kotlin
package com.metasearch.android.domain.{domain}.api.usecase

import kotlinx.coroutines.flow.Flow

interface Get{Domain}UseCase {
    operator fun invoke(id: Long): Flow<{DomainModel}?>
}
```

**Result return (one-shot operation):**
```kotlin
package com.metasearch.android.domain.{domain}.api.usecase

interface Update{Domain}UseCase {
    suspend operator fun invoke(id: Long, value: String): Result<Unit>
}
```

### 4. Registration

Add to `settings.gradle.kts` in alphabetical order:
```kotlin
include(":domain:{domain}:api")
```

### 5. Sync & Verify

```bash
./gradlew :domain:{domain}:api:compileKotlin
```

## Naming & Architecture Rules

| Item                  | Convention                                          | Example (domain=`photo_tag`)                              |
|:----------------------|:----------------------------------------------------|:----------------------------------------------------------|
| **Module Directory**  | snake_case                                          | `photo_tag`                                               |
| **Class Prefix**      | PascalCase                                          | `PhotoTag`                                                |
| **Namespace**         | `com.metasearch.android.domain.{domain}.api`        | `com.metasearch.android.domain.photo_tag.api`             |
| **Repository**        | `{Domain}Repository`                                | `PhotoTagRepository`                                      |
| **UseCase**           | `{Verb}{Domain}UseCase`                             | `GetPhotoTagUseCase`, `UpdatePhotoTagUseCase`             |
| **Settings (Gradle)** | `:domain:{domain}:api`                              | `:domain:photo_tag:api`                                   |

## Best Practices

- **Pure Kotlin Only**: `domain` modules must not depend on any Android SDK, Compose, or platform-specific frameworks.
- **JVM Convention**: Use the `metasearch.jvm.library` plugin. This prevents accidental usage of Android APIs.
- **Interfaces Only**: Define only interfaces here. Implementations belong in the `data` layer.
- **UseCase = Single Responsibility**: Each UseCase must have exactly one `operator fun invoke`. One operation per class.
- **Domain Models**: Use `data.domain` models via `api(projects.data.domain)`. Do not define new domain models inside this module.
- **operator fun invoke**: Always expose UseCase logic through `operator fun invoke` for clean call-site syntax.
- **Feature Integration**: Feature modules must depend on `domain.{domain}.api`, never on `data` modules directly.
