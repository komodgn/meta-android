---
name: add-feature-module
description: "Creates a new feature module in the MetaSearch Android project using Circuit and Metro. Use this skill when requested to 'add feature module', 'add a new feature' or 'add a new screen'."
---

# Feature Module Creation Skill

This skill creates a new feature module in the `feature/` package.

## Prerequisites & Pre-steps

Before generating the module, confirm:
1. **Screen Definition (Crucial)**: Before creating a new feature module, you **MUST** first add the new `Screen` definition to `com.metasearch.android.feature.screens.Screens.kt`.
    - Ensure the new `Screen` (object or data class) is defined and registered in that file.
2. **Module Naming**:
    - `{module}`: Must be **snake_case** (e.g., `person_detail`).
    - `{Module}`: Must be **PascalCase** (e.g., `PersonDetail`).
3. **Module Purpose** (What feature/screen this is for)
4. **Domain Dependencies** (Which `domain.*.api` modules are needed)

## Implementation Steps

### 1. Gradle Configuration (`build.gradle.kts`)

Each feature module must use the standard feature plugins.
**Note:** All feature modules automatically depend on `feature.screens`, so you do not need to add it manually.

```kotlin
plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.{module}"
}

dependencies {
    // Note: projects.feature.screens is already provided by the feature plugin.

    // Add required Domain APIs here
    // implementation(projects.domain.{domain}.api)
}
```

### 2. File Structure
Create the source directory structure:
`feature/{module}/src/main/java/com/metasearch/android/feature/{module}/`

### Screen Definition
All `Screen` classes should be defined in the `feature.screens` module.
Do not define new `Screen` classes inside the feature module.
Instead, reference the existing `Screen` from `com.metasearch.android.feature.screens`.

#### Screen Definition Example
When adding a new screen to `feature/screens/Screens.kt`, follow these patterns:

**1. For simple screens (No parameters):**
```kotlin
@Parcelize
data object {Module}Screen : Screen
```

**2. For screens with parameters:**
```kotlin
@Parcelize
data class {Module}Screen(
    val id: Long,
) : Screen
```

### Key Files to Create
(using {Module} as PascalCase):
1. `{Module}Presenter.kt`: Handles business logic.
```kotlin
package com.metasearch.android.feature.{module}

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.metasearch.android.feature.screens.{Module}Screen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class {Module}Presenter(
    @Assisted private val navigator: Navigator,
    // TODO: Inject UseCases or Repository
) : Presenter<{Module}UiState> {

    @CircuitInject({Module}Screen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): {Module}Presenter
    }

    @Composable
    override fun present(): {Module}UiState {
        val scope = rememberCoroutineScope()

        // TODO: Define states (remember / rememberRetained)

        fun handleEvent(event: {Module}UiEvent) {
            when (event) {
                // TODO: Handle events
            }
        }

        return {Module}UiState(
            eventSink = ::handleEvent,
            // TODO: Assign states
        )
    }
}
```

2. `{Module}Ui.kt`: UI components, annotated with `@CircuitInject({Module}Screen::class, AppScope::class)`.
```kotlin
package com.metasearch.android.feature.{module}

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.metasearch.android.feature.screens.{Module}Screen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject({Module}Screen::class, AppScope::class)
@Composable
fun {Module}Ui(
    state: {Module}UiState,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement UI using MetaSearchTheme
}
```

3. `{Module}UiState.kt`: UI state data class.
```kotlin
package com.metasearch.android.feature.{module}

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class {Module}UiState(
    val eventSink: ({Module}UiEvent) -> Unit,
    // TODO: Add state properties
) : CircuitUiState

sealed interface {Module}UiEvent : CircuitUiEvent {
    // TODO: Add events
}
```

### 3. Registration
Add the new module to the project and link it to the `app` module.

1. `settings.gradle.kts`
Include the new module in the feature modules block (maintain alphabetical order):
```kotlin
include(":feature:{module}")
```

2. `app/build.gradle.kts`
Add the dependency using the type-safe Gradle accessor.
- Note: Gradle automatically generates a `camelCase` accessor for every `snake_case` module name you define in `settings.gradle.kts`.
- Mapping Rule: `person_detail` (snake_case) → `personDetail` (camelCase)
```kotlin
// Always use the camelCase accessor (e.g., personDetail)
implementation(projects.feature.{Module})
```

3. Sync & Verify:
```bash
./gradlew :feature:{module}:compileDebugKotlin
```
## Naming & Architecture Rules

| Item                 | Convention                                 | Example(module=`person_detail`)                |
|:---------------------|:-------------------------------------------|:-----------------------------------------------|
| **Module Directory** | snake_case                                 | `person_detail`                                |
| **Class Prefix**     | PascalCase                                 | `PersonDetail`                                 |
| **Namespace**        | `com.metasearch.android.feature.{module}`  | `com.metasearch.android.feature.person_detail` |
| **Screen Reference** | Import from `feature.screens`              | `PersonDetailScreen`                           |
| **Settings(Gradle)** | `:feature:{module}`                        | `:feature:person_detail`                       |
| **Type-safe Accessor** | `projects.feature.{Module}` (camelCase)  | `projects.feature.personDetail`                |

## Best Practices
- Screen Definition: Always define new `Screen` classes in `feature/screens/Screens.kt` first. Never define them inside feature modules.
- Design System: Always use `MetaSearchTheme` tokens from `:core:designsystem`. Do not hardcode colors or spacing.
- Circuit Inject: Every UI Composable must be annotated with `@CircuitInject`.
- UI: UI Composables should be stateless and take State and Event lambdas as parameters.
