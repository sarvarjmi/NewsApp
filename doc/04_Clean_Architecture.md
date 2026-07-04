# NewsApp

> **Phase:** Architecture Foundation
>
> **Goal:** Design a scalable, maintainable, testable, and production-ready architecture before writing any business logic.

---

# Objective

In this document, we will design the application's architecture using **Clean Architecture + MVVM**, following Google's recommended Android app architecture and SOLID principles.

By the end of this document, you will understand:

- Why Clean Architecture is used
- How MVVM fits into Clean Architecture
- Responsibilities of each layer
- Dependency Rule
- Data Flow
- Project Folder Structure
- Feature-based organization
- Repository Pattern
- Use Cases
- State Management
- Error Handling Strategy
- Testing Strategy

> **Important:** No business logic or UI implementation is added in this phase. We are designing the architecture blueprint that the entire project will follow.

---

# Why Clean Architecture?

As Android applications grow, code can quickly become tightly coupled and difficult to maintain.

Common problems include:

- UI directly calling APIs
- Business logic inside Activities or Composables
- Database logic mixed with networking
- Difficult testing
- Hard-to-reuse code
- Fragile architecture

Clean Architecture solves these problems by separating responsibilities into independent layers.

Benefits:

- Modular codebase
- Easy maintenance
- High testability
- Better scalability
- Reusable business logic
- Clear dependency flow

---

# Architecture Overview

The application follows:

```text
                 Presentation Layer
        ┌─────────────────────────────┐
        │ Compose Screens             │
        │ ViewModels                  │
        │ UI State                    │
        │ UI Events                   │
        └──────────────▲──────────────┘
                       │
                Use Cases (Domain)
                       │
        ┌──────────────▼──────────────┐
        │ Domain Layer                │
        │ Business Logic              │
        │ Repository Contracts        │
        │ Domain Models               │
        └──────────────▲──────────────┘
                       │
            Repository Implementation
                       │
        ┌──────────────▼──────────────┐
        │ Data Layer                  │
        │ Retrofit                    │
        │ Room                        │
        │ Paging                      │
        │ DTO                         │
        │ Mappers                     │
        └─────────────────────────────┘
```

---

# Dependency Rule

The most important rule:

**Dependencies always point inward.**

```text
Presentation

↓

Domain

↓

Data
```

The Presentation layer knows about the Domain layer.

The Domain layer does **not** know about the Presentation layer.

The Data layer implements contracts defined by the Domain layer.

This makes the Domain layer completely independent and easily testable.

---

# MVVM within Clean Architecture

MVVM is used only in the Presentation layer.

```text
Compose UI

↓

ViewModel

↓

UseCase

↓

Repository

↓

Remote API / Room
```

Responsibilities:

### View

- Displays state
- Sends user events
- No business logic

### ViewModel

- Handles UI state
- Calls Use Cases
- Exposes StateFlow
- Processes UI events

### Model

In this project, the "Model" is represented by the Domain layer (Use Cases + Domain Models).

---

# Layer Responsibilities

## Presentation Layer

Responsible for:

- Jetpack Compose UI
- ViewModels
- Navigation
- UI State
- UI Events
- Side Effects

Must **not** contain:

- API calls
- Database access
- Business rules

---

## Domain Layer

The heart of the application.

Contains:

- Domain Models
- Repository Interfaces
- Use Cases
- Business Rules

This layer has **no Android dependencies**, making it easy to unit test.

---

## Data Layer

Responsible for:

- Retrofit API
- Room Database
- DTOs
- Entities
- PagingSource
- Repository Implementation
- Data Mapping

This layer knows how to retrieve and store data but does not contain UI logic.

---

# SOLID Principles

The architecture follows SOLID principles.

## S — Single Responsibility Principle

Each class has one responsibility.

Example:

```text
NewsRepository
```

Only handles data retrieval.

```text
GetTopHeadlinesUseCase
```

Only retrieves headlines.

```text
HomeViewModel
```

Only manages Home screen state.

---

## O — Open/Closed Principle

Classes should be open for extension but closed for modification.

Example:

Adding a new data source should not require changing existing ViewModels.

---

## L — Liskov Substitution Principle

Repository implementations can be replaced without affecting consumers.

```kotlin
NewsRepository

↓

NewsRepositoryImpl
```

or

```kotlin
FakeNewsRepository
```

for testing.

---

## I — Interface Segregation Principle

Keep interfaces focused.

Example:

```text
NewsRepository
```

Only exposes news-related operations.

Avoid large "God interfaces."

---

## D — Dependency Inversion Principle

High-level modules depend on abstractions.

```text
ViewModel

↓

NewsRepository Interface

↓

Repository Implementation
```

---

# Repository Pattern

The Repository acts as a single source of truth.

```text
UI

↓

ViewModel

↓

UseCase

↓

Repository

↓

API

+

Room
```

Benefits:

- Abstracts data sources
- Simplifies testing
- Supports offline caching
- Decouples business logic from data retrieval

---

# Data Sources

The Repository combines multiple sources:

```text
NewsRepository

↓

RemoteDataSource

+

LocalDataSource
```

- **RemoteDataSource:** Fetches articles from the News API.
- **LocalDataSource:** Stores bookmarks and cached articles using Room.

---

# Use Cases

Each business action is represented by a dedicated Use Case.

Examples:

- GetTopHeadlinesUseCase
- SearchNewsUseCase
- GetBookmarksUseCase
- ToggleBookmarkUseCase

Benefits:

- One responsibility per class
- Reusable
- Easy to test
- Keeps ViewModels small

---

# Data Flow

```text
User taps Refresh

↓

HomeScreen

↓

HomeViewModel

↓

GetTopHeadlinesUseCase

↓

NewsRepository

↓

Remote API

↓

Repository

↓

UseCase

↓

ViewModel

↓

StateFlow

↓

Compose UI
```

---

# State Management

The application follows **Unidirectional Data Flow (UDF)**.

```text
User Action

↓

UI Event

↓

ViewModel

↓

UseCase

↓

Repository

↓

New UI State

↓

Compose Screen
```

Benefits:

- Predictable state
- Easier debugging
- No hidden side effects

---

# UI States

Every screen will support:

- Loading
- Success
- Empty
- Error

Example:

```text
Loading

↓

Success

↓

Empty

↓

Retry
```

---

# Error Handling Strategy

Errors are mapped into a unified result type.

```text
NetworkResult

↓

Success

↓

Error

↓

Loading
```

The UI never handles raw exceptions.

Instead, ViewModels expose meaningful UI states.

---

# Folder Structure

```text
com.example.newsapp

├── core
│   ├── common
│   ├── dispatcher
│   ├── network
│   ├── result
│   └── util
│
├── data
│   ├── remote
│   │   ├── api
│   │   ├── dto
│   │   ├── mapper
│   │   └── paging
│   │
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   ├── entity
│   │   └── mapper
│   │
│   └── repository
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│
├── presentation
│   ├── home
│   ├── search
│   ├── detail
│   ├── bookmark
│   ├── components
│   ├── navigation
│   ├── theme
│   └── state
│
├── di
│
└── MainActivity.kt
```

---

# Feature-Based Package Structure

Each feature owns its UI components and ViewModel.

Example:

```text
presentation

├── home
│   ├── HomeScreen.kt
│   ├── HomeViewModel.kt
│   ├── HomeUiState.kt
│   └── HomeEvent.kt
│
├── search
│
├── detail
│
└── bookmark
```

This improves scalability and team collaboration.

---

# Dependency Injection Flow

```text
Hilt

↓

ViewModel

↓

UseCase

↓

Repository

↓

Remote / Local Data Sources
```

---

# Testing Strategy

Each layer is tested independently.

| Layer | Test Type |
|--------|-----------|
| Domain | Unit Tests |
| Repository | Unit Tests |
| Networking | MockWebServer |
| ViewModel | StateFlow Tests |
| UI | Compose UI Tests |

This layered approach ensures high confidence and easier maintenance.

---

# Architecture Principles Checklist

- Single Responsibility
- Separation of Concerns
- Dependency Inversion
- Repository Pattern
- Use Cases
- Unidirectional Data Flow
- Feature-Based Packaging
- Testability
- Scalability

---

# Files Created in This Phase

No Kotlin files are created.

This document establishes the architectural blueprint for the project.

---

# What We Completed

- Selected Clean Architecture + MVVM
- Defined Presentation, Domain, and Data layers
- Explained the Dependency Rule
- Designed the Repository Pattern
- Planned Use Cases
- Established State Management with UDF
- Defined folder and feature structure
- Planned testing strategy

---

# Next Document

## 05_Project_Folder_Structure.md

In the next document we will:

1. Create the complete package structure in Android Studio.
2. Add placeholder classes for each layer.
3. Configure feature-based modules.
4. Create common/core packages.
5. Prepare the project for Hilt integration.

At the end of the next step, the project will have a fully organized package structure ready for implementation.

---

# Prompt for Code Generation

## Prompt: 05_Project_Folder_Structure.md

You are a Senior Android Engineer.

Generate the complete project folder structure for the NewsApp project based on the Clean Architecture defined in `04_Clean_Architecture.md`.

Requirements:

1. Create the complete package hierarchy for:
    - core
    - data
    - domain
    - presentation
    - di
2. Use feature-based organization for presentation (home, search, detail, bookmark).
3. Create placeholder Kotlin files (empty classes/interfaces where appropriate) so the project compiles.
4. Explain the purpose of every package and file.
5. Show the final directory tree.
6. Identify all new files and folders created.
7. End with a verification checklist and Git commit message.

---

# UI Design Prompt

## Prompt: Information Architecture & Navigation Blueprint

Design the application's information architecture before implementing any Compose screens.

Include:

- User flow diagram
- Screen hierarchy
- Bottom navigation structure
- Navigation graph
- Deep link entry points
- Screen transition recommendations
- Back navigation behavior
- Tablet navigation considerations
- Accessibility navigation (TalkBack focus order)
- Wireframe-level layouts (no colors or Compose code)

The output should serve as the blueprint for implementing navigation and UI in later documents.