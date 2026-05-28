---
name: add-data-module
description: "Creates a new data module in the MetaSearch Android project. Use this skill when requested to 'add data module', 'implement a repository', 'implement a usecase', or 'add a data layer implementation'."
---

# Data Module Creation Skill

This skill creates a new implementation module in the `data/` package.

## Prerequisites & Pre-steps

Before generating the module, confirm:
1. **Module Naming**:
    - `{domain}`: Must be **snake_case** (e.g., `photo_tag`).
    - `{Domain}`: Must be **PascalCase** (e.g., `PhotoTag`).
2. **Corresponding domain module** (`domain.{domain}.api`) must already exist.
3. **Structure type** (see below — Simple or Full?)
4. **Data sources required** (Remote API, Room DB, DataStore, local file system)

---

## Structure Type

### Simple — `di/` + `repository/` only
Use when the module only wraps a local data source (Room DAO, DataStore, ContentProvider, system API).
No remote response mapping, no business logic beyond simple delegation.

**Real examples:** `data:device`, `data:gallery`, `data:file`

```
data/{domain}/impl/src/main/java/com/metasearch/android/data/{domain}/impl/
├── repository/
│   └── {Domain}RepositoryImpl.kt
└── di/
    └── {Domain}DataGraph.kt
```

### Full — `di/` + `mapper/` + `repository/` + `usecase/`
Use when the module involves remote API calls (needs response-to-model mapping) or contains UseCase implementations with business logic.

**Real examples:** `data:analysis`, `data:person`, `data:search`

```
data/{domain}/impl/src/main/java/com/metasearch/android/data/{domain}/impl/
├── repository/
│   └── {Domain}RepositoryImpl.kt
├── usecase/
│   └── {Action}{Domain}UseCaseImpl.kt
├── mapper/
│   └── ResponseToModel.kt
└── di/
    └── {Domain}DataGraph.kt
```

---

## Implementation Steps

### 1. Gradle Configuration (`data/{domain}/impl/build.gradle.kts`)

**Simple:**
```kotlin
plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.{domain}.impl"
}

dependencies {
    implementation(projects.core.di)
    implementation(projects.domain.{domain}.api)

    implementation(projects.core.room.api)        // if Room DB is needed
    implementation(projects.core.datastore.api)   // if DataStore is needed
    implementation(libs.androidx.core.ktx)
}
```

**Full:**
```kotlin
plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization) // if serialization is needed
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.{domain}.impl"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.di)
    implementation(projects.data.remote)
    implementation(projects.domain.{domain}.api)

    implementation(projects.core.room.api)        // if Room DB is needed
    implementation(projects.core.datastore.api)   // if DataStore is needed
    implementation(libs.androidx.core.ktx)
}
```

---

### 2. Key Files

#### Repository Implementation (`repository/{Domain}RepositoryImpl.kt`)

```kotlin
package com.metasearch.android.data.{domain}.impl.repository

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.{domain}.api.repository.{Domain}Repository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class {Domain}RepositoryImpl(
    // TODO: Inject data sources (Client, DAO, DataSource, etc.)
) : {Domain}Repository {

    // TODO: Implement repository methods
}
```

#### UseCase Implementation — Full only (`usecase/{Action}{Domain}UseCaseImpl.kt`)

```kotlin
package com.metasearch.android.data.{domain}.impl.usecase

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.{domain}.api.usecase.{Action}{Domain}UseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class {Action}{Domain}UseCaseImpl(
    // TODO: Inject required repositories
) : {Action}{Domain}UseCase {

    override suspend fun invoke(/* params */): Result<{Model}> = runSuspendCatching {
        // TODO: Implement
    }
}
```

#### Mapper — Full only (`mapper/ResponseToModel.kt`)

```kotlin
package com.metasearch.android.data.{domain}.impl.mapper

import com.metasearch.android.data.domain.{DomainModel} // e.g., import com.metasearch.android.data.domain.PhotoTag
import com.metasearch.android.data.remote.{domain}.response.{Domain}Response

internal fun {Domain}Response.toModel(): {DomainModel} = {DomainModel}(
    // TODO: Map response fields to domain model fields
)
```

#### DI Graph (`di/{Domain}DataGraph.kt`)

**Simple:**
```kotlin
package com.metasearch.android.data.{domain}.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.{domain}.impl.repository.{Domain}RepositoryImpl
import com.metasearch.android.domain.{domain}.api.repository.{Domain}Repository
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface {Domain}DataGraph {
    @Binds
    val {Domain}RepositoryImpl.bind: {Domain}Repository
}
```

**Full:**
```kotlin
package com.metasearch.android.data.{domain}.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.{domain}.impl.repository.{Domain}RepositoryImpl
import com.metasearch.android.data.{domain}.impl.usecase.{Action}{Domain}UseCaseImpl
import com.metasearch.android.domain.{domain}.api.repository.{Domain}Repository
import com.metasearch.android.domain.{domain}.api.usecase.{Action}{Domain}UseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface {Domain}DataGraph {
    @Binds
    val {Domain}RepositoryImpl.bind: {Domain}Repository

    @Binds
    val {Action}{Domain}UseCaseImpl.bind: {Action}{Domain}UseCase
}
```

---

### 3. Registration

Add to `settings.gradle.kts` in alphabetical order:

```kotlin
include(":data:{domain}:impl")
```

### 4. Sync & Verify

```bash
./gradlew :data:{domain}:impl:compileDebugKotlin
```

---

## Naming & Architecture Rules

| Item | Convention | Example (domain=`photo_tag`) |
|:-----|:-----------|:-----------------------------|
| **Module Directory** | snake_case | `photo_tag` |
| **Class Prefix** | PascalCase | `PhotoTag` |
| **Namespace** | `com.metasearch.android.data.{domain}.impl` | `com.metasearch.android.data.photo_tag.impl` |
| **Repository Impl** | `{Domain}RepositoryImpl` | `PhotoTagRepositoryImpl` |
| **UseCase Impl** | `{Verb}{Domain}UseCaseImpl` | `GetPhotoTagUseCaseImpl` |
| **DI Graph** | `{Domain}DataGraph` | `PhotoTagDataGraph` |
| **Mapper** | `internal fun {Response}.toModel()` | `fun PhotoTagResponse.toModel()` |
| **Settings (Gradle)** | `:data:{domain}:impl` | `:data:photo_tag:impl` |

---

## Best Practices

- **Domain contract first**: The corresponding `domain.{domain}.api` module must exist before creating a data module.
- **`@SingleIn(DataScope::class)`**: All implementations must be scoped to `DataScope` to avoid redundant instances.
- **`@Binds` property syntax**: Use `val {Impl}.bind: {Interface}` — not function syntax — as per the project convention.
- **`runSuspendCatching`**: Wrap all suspend calls in `runSuspendCatching` from `core:common` for consistent `Result` wrapping.
- **Mapper visibility**: Mapper extension functions must be `internal` — they are an implementation detail of the data module.
- **No domain logic**: Data modules only translate and deliver data. Business logic belongs in the UseCase implementations, not in repositories.
- **Remote is shared**: Never create a new `data:remote` sub-module. Add new Retrofit services directly to the existing `data:remote` module.
- **Domain models only**: Use models from `data:domain`. Do not define new data classes in impl modules.
