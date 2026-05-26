<!-- Parent: ../AGENTS.md -->

# Core Module AI Guide

This module contains shared utilities, the design system, and common logic for network/data layers.

## Development Rules
- **Dependency Isolation**: Do not depend on feature modules. The core layer should only depend on external libraries or pure Kotlin code.
- **Stability Priority**: Since these data classes and components are used across all features, ensure they are annotated with `@Stable` or `@Immutable` to optimize recomposition.
- **Design System**: All UI components must prioritize using theme tokens defined in `core:designsystem`.

## Prompting the AI
- "Review this utility code to ensure the Compose compiler's skippability is maintained."
