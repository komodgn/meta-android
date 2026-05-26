<!-- Parent: ../AGENTS.md -->

# Feature Module AI Guide

This module contains UI feature units that follow the MVI pattern using the [Slack Circuit](https://github.com/slackhq/circuit) architecture.

## Development Rules
- **Circuit Compliance**: Every screen must implement the `Screen` interface and a corresponding `Presenter`.
- **State Management**: UI components must subscribe to a `UiState`, and all user interactions must be propagated via `CircuitEvent`.
- **Constructor Injection**: All dependencies must be injected via constructor using `Metro` DI.
- **Separation of Concerns**: Business logic must reside within the `Presenter`, while Composables should remain "pure" UI representations of the state.

## Prompting the AI
- "Generate a new feature structure including the Circuit `Screen`, `Presenter`, and Composable based on the existing patterns."
- "Refactor this Presenter's `present()` method to efficiently manage the `UiState` and handle MVI logic."
