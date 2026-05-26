<!-- Parent: ../AGENTS.md -->

# Domain Module AI Guide

This module contains the application's business logic, UseCases, and domain entities.

## Development Rules
- **Pure Kotlin**: Code must be written in pure Kotlin without dependencies on the Android framework or Jetpack Compose.
- **Architectural Compliance**: Each `UseCase` must adhere to the Single Responsibility Principle, performing exactly one business operation.
- **Data Encapsulation**: Domain entities must be protected from changes in external layers (Data/UI) to maintain business logic stability.

## Prompting the AI
- "Design this new UseCase to ensure that business rules preserve the immutability of the domain entity."
- "Apply the Dependency Inversion Principle to ensure this logic does not directly depend on the data layer implementation."
