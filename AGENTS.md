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
1. **Dependency Rule (Code Dependency Direction)**:
    - Communication must occur via `domain` interfaces or `core` modules.
    - All modules may depend on `core`.
    - `feature` and `data` modules may depend on `domain`.
    - `domain` must not depend on `feature` or `data` layers (Pure Kotlin logic).
    - Direct circular or peer dependencies between `feature` and `data` are strictly prohibited.

2. **Data Flow (Data Transfer Path)**:
    - Data propagates from the `data` layer, through the `domain` layer, and ultimately to the `feature` layer.
    - Use **Domain Models** to decouple layers during data transfer; never leak DTOs outside the `data` layer.

3. **Domain Isolation**:
    - `domain` modules must only contain pure Kotlin logic, with no dependencies on Android or Compose frameworks.

4. **Stability**:
    - If `compose-stability-analyzer` reports stale data, use the `--rerun-tasks` option to force a baseline update.

5. **Compose**:
    - All UI must be written in a `Declarative` manner, and state transitions must be handled via `CircuitEvent`.
