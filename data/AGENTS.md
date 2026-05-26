<!-- Parent: ../AGENTS.md -->

# Data Module AI Guide

This module manages repositories, remote API clients, and local data sources.

## Development Rules
- **Single Source of Truth**: Define clear data origins and implement consistent caching strategies.
- **DTO Handling**: Data Transfer Objects (DTOs) must be mapped to domain models. Never expose DTOs outside the data layer.
- **Error Handling**: Use custom exception classes for network or database failures to provide clear, actionable feedback to the domain layer.

## Prompting the AI
- "Write the mapping logic between domain models and data models when implementing a new Repository."
- "Review the necessary steps for schema versioning and migration when modifying the Room database."
