<!-- Parent: ../AGENTS.md -->

# Core Design System AI Guide

This module defines the design tokens, typography, colors, and themes for the entire application.

## Development Rules
- **Theme Support**: All UI components must support both Light and Dark modes. Ensure `Theme.kt` is properly implemented to handle color surface updates.
- **Token-First Approach**: Never hardcode colors, spacing, or typography values. Always use the tokens defined in `designsystem`.
- **Consistency**: Any new UI components must be built using the existing design system tokens to ensure design consistency.

## Prompting the AI
- "Create a new Compose component following the `MetaSearchTheme` standards for both Light and Dark modes."
- "When adding new color tokens, show me how to register them in `Theme.kt` and the respective color palettes."
