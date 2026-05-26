# Visualize Me By Photo: AI Agent Context

## Architecture Principles
- **MVI with Circuit**: Each feature is composed of `Screen`, `Presenter`, and `UiState`.
- **DI**: Dependency injection is managed via `Metro`.
- **Stability**: All major state classes must comply with `compose-stability-analyzer` for `STABLE` status.

## Module Structure
- **`feature/*`**: Independent UI feature modules (Centered around Circuit Screen/Presenter).
- **`domain/*`**: Business logic, use cases, and domain entities (Pure Kotlin).
- **`data/*`**: Local (Room) and remote (Retrofit) data sources (Data mapping and repository).
- **`core/*`**: Shared components, design system, and utilities.

## Global Rules for AI
1. **Dependency Rule**: Direct dependencies between feature modules are prohibited. Communication between modules must occur via `domain` interfaces or `core` modules.
2. **Domain Isolation**: `domain` modules must only contain pure Kotlin logic, with no dependencies on Android or Compose frameworks.
3. **Data Flow**: Maintain dependency direction in the order of `feature` -> `domain` -> `data` -> `core`. All layers may depend on `core`.
4. **Stability**: If `compose-stability-analyzer` reports stale data, use the `--rerun-tasks` option to force a baseline update.
5. **Compose**: All UI must be written in a `Declarative` manner, and state transitions must be handled via `CircuitEvent`.
