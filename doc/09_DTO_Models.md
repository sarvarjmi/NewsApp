# NewsApp

> **Phase:** Data Layer
>
> **Module:** API Data Transfer Objects (DTO)
>
> **Goal:** Design a robust DTO layer that safely represents API responses, isolates external data structures from the rest of the application, and prepares the project for reliable mapping into Domain models.

---

# Objective

The News API returns JSON responses.

The application should **never expose raw JSON** or Retrofit response models to the Domain or Presentation layers.

Instead, the Data layer converts JSON into **Data Transfer Objects (DTOs)**, which are then mapped into Domain models.

This separation ensures that API changes do not directly affect business logic or UI.

---

# What is a DTO?

A **Data Transfer Object (DTO)** is a Kotlin data class whose only responsibility is to represent the structure of data received from or sent to an API.

DTOs:

- Represent API contracts
- Contain serialization annotations
- Live only in the Data layer
- Are not used directly by the UI
- Are mapped into Domain models

---

# Why Use DTOs?

Without DTOs:

```text
API JSON

↓

Compose UI
```

Problems:

- Tight coupling
- Difficult testing
- API changes break UI
- Poor separation of concerns

---

With DTOs:

```text
API JSON

↓

DTO

↓

Mapper

↓

Domain Model

↓

UI
```

Benefits:

- Decoupled architecture
- Stable business layer
- Easier testing
- Safer API evolution

---

# News API Response Structure

The selected News API returns a response similar to:

```json
{
  "status": "ok",
  "totalResults": 120,
  "articles": [
    {
      "source": {
        "id": "cnn",
        "name": "CNN"
      },
      "author": "...",
      "title": "...",
      "description": "...",
      "url": "...",
      "urlToImage": "...",
      "publishedAt": "...",
      "content": "..."
    }
  ]
}
```

This response will be represented using multiple DTOs rather than one large class.

---

# DTO Hierarchy

```text
NewsResponseDto

│

├── status

├── totalResults

└── articles

        │

        ▼

     ArticleDto

        │

        ▼

     SourceDto
```

Each DTO has a single responsibility.

---

# Planned DTO Files

```text
data

└── remote

    └── dto

        NewsResponseDto.kt

        ArticleDto.kt

        SourceDto.kt
```

Additional DTOs may be introduced if the API expands.

---

# DTO Responsibilities

## NewsResponseDto

Represents the root API response.

Contains:

- status
- totalResults
- list of ArticleDto

---

## ArticleDto

Represents one article.

Contains:

- title
- description
- image URL
- content
- source
- author
- published date
- article URL

---

## SourceDto

Represents the article's source.

Contains:

- id
- name

---

# Serialization Strategy

The project uses **Kotlin Serialization**.

Every DTO will be annotated appropriately for JSON parsing.

Benefits:

- Type safety
- Better performance
- Official Kotlin support
- Null-safe parsing
- Less reflection

---

# Null Safety Strategy

Real-world APIs often omit fields.

Example:

```json
{
  "title": null
}
```

Instead of crashing, DTOs should:

- Use nullable types where appropriate
- Provide sensible default values during mapping
- Never expose nulls to the UI when avoidable

---

# Mapping Strategy

DTOs are **never** exposed outside the Data layer.

```text
ArticleDto

↓

ArticleMapper

↓

Article (Domain Model)
```

Only Domain models are consumed by Use Cases and ViewModels.

---

# Folder Placement

```text
data

└── remote

    ├── dto
    ├── mapper
    └── api
```

DTOs remain isolated from business logic.

---

# Naming Conventions

| Type | Example |
|------|---------|
| DTO | ArticleDto |
| Response | NewsResponseDto |
| Mapper | ArticleMapper |
| Domain Model | Article |

Always suffix API models with **Dto**.

---

# Validation Rules

DTOs should:

- Match the API contract exactly
- Avoid business logic
- Avoid Android dependencies
- Avoid UI formatting
- Be immutable (`val` properties)

---

# Error Handling

Serialization failures should be handled by the networking layer.

Flow:

```text
JSON

↓

Serialization

↓

DTO

↓

SafeApiCall

↓

NetworkResult.Error
```

The UI should never see serialization exceptions.

---

# Future-Proofing

If the API introduces new fields:

```json
{
  "readingTime": 5
}
```

Only the DTO layer changes.

The Domain layer remains unaffected unless the new field is required for business logic.

---

# Testing Strategy

DTOs should be verified using:

- Sample JSON
- Kotlin Serialization
- MockWebServer
- Mapping tests

Tests should ensure:

- Correct parsing
- Nullable handling
- Default values
- Backward compatibility

---

# Best Practices

- One DTO per API object
- Keep DTOs immutable
- No Android framework classes
- No business logic
- No formatting logic
- Map DTOs immediately after parsing

---

# Architecture Flow

```text
News API

↓

JSON

↓

NewsResponseDto

↓

ArticleDto

↓

SourceDto

↓

Mapper

↓

Domain Model

↓

Repository

↓

ViewModel

↓

Compose UI
```

---

# Files Created in This Document

None.

This document defines the DTO architecture and modeling strategy.

---

# What We Completed

- Defined the purpose of DTOs
- Planned the DTO hierarchy
- Established serialization strategy
- Planned null-safety approach
- Defined mapping responsibilities
- Established naming conventions
- Planned testing strategy

---

# Next Documents

The DTO implementation will be divided into compile-safe milestones.

## 09A_NewsResponse_DTO.md

Create:

- `NewsResponseDto.kt`
- Root response model
- Serialization annotations
- Documentation

---

## 09B_Article_DTO.md

Create:

- `ArticleDto.kt`
- Complete article structure
- Nullable handling
- Serialization annotations

---

## 09C_Source_DTO.md

Create:

- `SourceDto.kt`
- Source model
- Nullable fields
- Documentation

---

## 09D_DTO_Serialization.md

Implement:

- Kotlin Serialization configuration
- JSON settings
- Unknown key handling
- Default values

---

## 09E_DTO_Validation.md

Implement:

- Null-safe defaults
- Validation helpers
- Mapping guidelines
- Defensive parsing

---

## 09F_DTO_Testing.md

Implement:

- DTO parsing tests
- Sample JSON tests
- Serialization tests
- Edge-case validation

---

# Prompt for Code Generation

## Prompt: 09A_NewsResponse_DTO.md

You are a Senior Android Engineer.

Generate the complete `NewsResponseDto.kt` for the NewsApp project.

Requirements:

1. Use Kotlin Serialization.
2. Match the selected News API response.
3. Include:
    - `status`
    - `totalResults`
    - `articles`
4. Use immutable properties (`val`).
5. Apply appropriate serialization annotations.
6. Handle nullable fields where necessary.
7. Explain every property.
8. Provide complete runnable Kotlin code.

---

## Prompt: 09B_Article_DTO.md

Generate the complete `ArticleDto.kt`.

Requirements:

- Match the News API article schema.
- Include:
    - source
    - author
    - title
    - description
    - url
    - urlToImage
    - publishedAt
    - content
- Use Kotlin Serialization annotations.
- Handle nullable fields safely.
- Explain every field and design decision.

---

## Prompt: 09C_Source_DTO.md

Generate the complete `SourceDto.kt`.

Requirements:

- Include:
    - id
    - name
- Support nullable values.
- Use Kotlin Serialization.
- Provide complete runnable code with explanations.

---

# UI Design Prompt

## Prompt: Article Data Mapping UI Blueprint

Design a visual specification showing how article data flows into the UI.

Include:

- Mapping from API fields to UI elements
- News card layout (image, title, source, date, description)
- Detail screen data mapping
- Placeholder behavior for missing images or text
- Skeleton loading representation
- Empty field handling (e.g., unknown author, missing image)
- Accessibility considerations for dynamic content

Do **not** generate Jetpack Compose code. Produce a wireframe and data-to-UI mapping specification for later implementation.