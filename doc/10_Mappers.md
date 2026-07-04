# NewsApp

> **Phase:** Data Layer
>
> **Module:** Model Mapping
>
> **Goal:** Design a scalable mapping layer that isolates API models, database entities, domain models, and UI models while preserving Clean Architecture boundaries.

---

# Objective

Different layers in a Clean Architecture application have different responsibilities and should not share the same data models.

Instead of exposing API DTOs or Room Entities directly to the UI, the application converts data through dedicated mappers.

The mapping layer acts as the bridge between architectural layers.

After completing this phase, the application will have:

- DTO → Domain mapping
- Entity → Domain mapping
- Domain → Entity mapping
- Domain → UI mapping
- Extension-based mappers
- Null-safe transformations
- Unit-testable mapping logic

---

# Why Do We Need Mappers?

Without mappers:

```text
News API

↓

ArticleDto

↓

Compose UI
```

Problems:

- Tight coupling
- API changes break UI
- Database models leak into Presentation
- Difficult testing
- Poor separation of concerns

---

With mappers:

```text
News API

↓

ArticleDto

↓

Mapper

↓

Article (Domain)

↓

Mapper

↓

ArticleUiModel

↓

Compose UI
```

Benefits:

- Decoupled layers
- Stable Domain models
- Easier maintenance
- Better testability
- Flexible UI evolution

---

# Mapping Architecture

```text
Remote API

↓

DTO

↓

DTO Mapper

↓

Domain Model

↓

Repository

↓

UseCase

↓

ViewModel

↓

UI Mapper

↓

UI Model

↓

Compose Screen
```

For locally stored data:

```text
Room Entity

↓

Entity Mapper

↓

Domain Model

↓

ViewModel

↓

UI Model
```

---

# Types of Mappers

The project will use four mapper categories.

## DTO → Domain

Converts API responses into business models.

Example:

```text
ArticleDto

↓

Article
```

---

## Entity → Domain

Converts Room entities into domain models.

Example:

```text
BookmarkEntity

↓

Article
```

---

## Domain → Entity

Converts business models into database entities.

Example:

```text
Article

↓

BookmarkEntity
```

---

## Domain → UI

Converts domain models into UI-specific models.

Example:

```text
Article

↓

ArticleUiModel
```

---

# Why Domain Models Are the Center

The Domain model represents the application's business language.

Everything flows through it.

```text
API DTO

↓

Domain

↓

Room Entity
```

or

```text
Room Entity

↓

Domain

↓

Compose UI
```

This ensures the Domain layer remains independent.

---

# Folder Structure

```text
data

└── remote

    └── mapper

        ArticleDtoMapper.kt

        SourceDtoMapper.kt

-----------------------------------

data

└── local

    └── mapper

        BookmarkEntityMapper.kt

-----------------------------------

presentation

└── mapper

        ArticleUiMapper.kt
```

---

# Mapping Flow

```text
API

↓

NewsResponseDto

↓

ArticleDto

↓

ArticleDtoMapper

↓

Article

↓

Repository

↓

UseCase

↓

ViewModel

↓

ArticleUiMapper

↓

ArticleUiModel

↓

Compose Screen
```

---

# Responsibilities

## DTO Mapper

Responsible for:

- Converting DTOs
- Handling null values
- Applying defaults
- No business logic

---

## Entity Mapper

Responsible for:

- Room ↔ Domain conversion
- Database defaults
- Offline persistence

---

## UI Mapper

Responsible for:

- Formatting dates
- Placeholder values
- Display-ready text
- Image fallback

No networking or persistence logic belongs here.

---

# Null Safety Strategy

Real-world APIs often return incomplete data.

Example:

```json
{
  "author": null,
  "urlToImage": null
}
```

The mapper should provide safe defaults where appropriate before the data reaches the UI.

The UI should not need to perform repetitive null checks.

---

# Default Value Strategy

Examples:

| Missing Field | Default |
|--------------|---------|
| Title | "Untitled" |
| Author | "Unknown" |
| Image | Placeholder URL / null |
| Description | Empty String |
| Content | Empty String |

The exact defaults will be decided during implementation.

---

# Extension Functions

Instead of utility classes, the project will use Kotlin extension functions.

Example concept:

```text
ArticleDto

↓

toDomain()

↓

Article
```

Benefits:

- Cleaner syntax
- Discoverable
- Easier testing
- Less boilerplate

---

# One-Way Mapping

Mapping should always move in one direction.

Example:

```text
DTO

↓

Domain
```

Never modify DTO objects directly.

Each mapper creates a new immutable object.

---

# Immutability

All mapped models should remain immutable.

Rules:

- Use `data class`
- Use `val`
- Never mutate incoming objects

---

# Error Handling

Mapping should never crash the application because of missing optional fields.

If required data is missing:

- Apply safe defaults
- Log the issue (debug only)
- Continue processing where possible

Unexpected structural errors should already have been handled during serialization.

---

# Performance Considerations

Mappers should:

- Avoid reflection
- Avoid unnecessary allocations
- Avoid nested conversions where possible
- Be lightweight and deterministic

---

# Testing Strategy

Each mapper should have dedicated unit tests.

Verify:

- Normal mapping
- Null values
- Empty collections
- Missing fields
- Default values
- Edge cases

No network or database dependencies are required.

---

# Planned Kotlin Files

The following files will be implemented across the next mapping documents.

```text
data/remote/mapper

ArticleDtoMapper.kt

SourceDtoMapper.kt

------------------------------------

data/local/mapper

BookmarkEntityMapper.kt

------------------------------------

presentation/mapper

ArticleUiMapper.kt

------------------------------------

test/

ArticleDtoMapperTest.kt

BookmarkEntityMapperTest.kt
```

---

# Mapping Dependency Diagram

```text
News API

↓

DTO

↓

DTO Mapper

↓

Domain

↓

UseCases

↓

ViewModel

↓

UI Mapper

↓

Compose
```

---

# Best Practices

- Keep mappers pure
- One responsibility per mapper
- No Android framework dependencies
- No business rules
- No database access
- No network access
- Prefer extension functions
- Return immutable objects

---

# Files Created in This Document

None.

This document defines the mapping architecture and implementation strategy.

---

# What We Completed

- Defined the purpose of mappers
- Planned mapping responsibilities
- Established DTO, Entity, Domain, and UI model boundaries
- Planned extension-based mapping
- Defined null-safety and default-value strategies
- Planned testing approach

---

# Next Documents

The mapper implementation will be divided into compile-safe milestones.

## 10A_DTO_To_Domain.md

Create:

- `ArticleDtoMapper.kt`
- `SourceDtoMapper.kt`
- Extension functions
- DTO → Domain conversion

---

## 10B_Entity_To_Domain.md

Implement:

- `BookmarkEntityMapper.kt`
- Entity → Domain mapping
- Offline conversion

---

## 10C_Domain_To_Entity.md

Implement:

- Domain → Room Entity mapping
- Bookmark persistence conversion

---

## 10D_Domain_To_UI.md

Implement:

- `ArticleUiMapper.kt`
- Display-ready formatting
- Placeholder handling
- Date formatting strategy

---

## 10E_Mapping_Extensions.md

Implement:

- Shared extension functions
- Collection mapping helpers
- Reusable mapper utilities

---

## 10F_Mapping_Testing.md

Implement:

- Mapper unit tests
- Null-handling tests
- Default-value tests
- Collection mapping tests
- Edge-case validation

---

# Prompt for Code Generation

## Prompt: 10A_DTO_To_Domain.md

You are a Senior Android Engineer.

Generate the complete DTO → Domain mapping layer for the NewsApp project.

### Requirements

1. Create:
    - `ArticleDtoMapper.kt`
    - `SourceDtoMapper.kt`
2. Use Kotlin extension functions.
3. Convert every DTO into immutable Domain models.
4. Handle nullable fields safely.
5. Apply default values where appropriate.
6. Keep mappers pure and free of Android dependencies.
7. Explain every mapping decision.
8. Provide complete runnable Kotlin code.

---

## Prompt: 10B_Entity_To_Domain.md

Generate the complete Entity → Domain mapper.

Requirements:

- Convert `BookmarkEntity` to `Article`.
- Handle nulls safely.
- Preserve business data.
- Use extension functions.
- Provide complete runnable code with explanations.

---

## Prompt: 10C_Domain_To_Entity.md

Generate the complete Domain → Entity mapper.

Requirements:

- Convert `Article` to `BookmarkEntity`.
- Preserve all bookmarkable fields.
- Use immutable data classes.
- Explain every property mapping.

---

# UI Design Prompt

## Prompt: Data-to-UI Mapping Specification

Design how mapped Domain models are presented in the NewsApp UI.

Include:

- News card field mapping (title, image, source, date, description)
- Detail screen content mapping
- Bookmark list presentation
- Placeholder behavior for missing values
- Date/time formatting guidelines
- Image fallback rules
- Accessibility considerations for dynamic text and images

Do **not** generate Jetpack Compose code. Produce a design specification and wireframe-level mapping guide that will be used during UI implementation.