# NewsApp

> **Phase:** Domain Layer
>
> **Module:** Use Cases (Business Logic)
>
> **Goal:** Design a production-ready Domain Layer where every business operation is encapsulated inside a dedicated Use Case, keeping business logic independent from Android, UI, networking, and database implementations.

---

# Objective

The Domain Layer is the **core of Clean Architecture**.

It contains the application's business rules and orchestrates interactions between the Presentation Layer and the Repository Layer.

Instead of allowing ViewModels to directly call repositories, every user action is represented as an individual **Use Case**.

By the end of this phase, the application will have:

- Clean business logic
- Independent Domain Layer
- Single Responsibility UseCases
- Testable business rules
- Reusable business operations
- Feature-based organization

---

# Why Use Cases?

Without Use Cases

```text
Compose

↓

ViewModel

↓

Repository

↓

API
```

Problems

- Large ViewModels
- Mixed business logic
- Difficult testing
- Poor reusability
- Tight coupling

---

With Use Cases

```text
Compose

↓

ViewModel

↓

UseCase

↓

Repository

↓

API
```

Benefits

- Small ViewModels
- Reusable logic
- Easy testing
- Better scalability
- Clear responsibilities

---

# Clean Architecture

```text
Presentation

↓

ViewModel

↓

UseCase

↓

Repository Interface

↓

Repository Implementation

↓

Remote
+
Local
```

The Domain Layer does **not** depend on Android framework classes.

---

# Domain Responsibilities

The Domain Layer is responsible for:

- Business Rules
- Application Logic
- Repository Contracts
- Domain Models
- Use Cases

The Domain Layer is **not** responsible for:

- Retrofit
- Room
- Compose
- Navigation
- Android Context
- UI formatting

---

# Use Case Philosophy

Each Use Case performs **one business operation**.

Examples

```
GetTopHeadlinesUseCase

SearchNewsUseCase

ToggleBookmarkUseCase

ObserveBookmarksUseCase
```

Avoid combining unrelated operations into one class.

---

# Folder Structure

```text
domain

├── model
│
├── repository
│
└── usecase
    │
    ├── news
    │
    │   GetTopHeadlinesUseCase.kt
    │
    │   RefreshNewsUseCase.kt
    │
    ├── search
    │
    │   SearchNewsUseCase.kt
    │
    ├── bookmark
    │
    │   AddBookmarkUseCase.kt
    │
    │   RemoveBookmarkUseCase.kt
    │
    │   ToggleBookmarkUseCase.kt
    │
    │   ObserveBookmarksUseCase.kt
    │
    │   IsBookmarkedUseCase.kt
    │
    └── common
```

Grouping by feature keeps the Domain layer scalable.

---

# Use Case Categories

## News

Responsible for:

- Fetch headlines
- Refresh news

---

## Search

Responsible for:

- Search articles
- Search pagination

---

## Bookmark

Responsible for:

- Save bookmark
- Delete bookmark
- Toggle bookmark
- Observe bookmarks
- Check bookmark status

---

## Future

Future features may include:

- Categories
- Sources
- User preferences
- Notifications
- Background sync

---

# Business Flow

Example:

```text
User Opens Home

↓

HomeViewModel

↓

GetTopHeadlinesUseCase

↓

Repository

↓

PagingData<Article>

↓

Compose UI
```

---

# Bookmark Flow

```text
User Clicks Bookmark

↓

ViewModel

↓

ToggleBookmarkUseCase

↓

Repository

↓

Room

↓

Updated Flow

↓

Compose UI
```

---

# Search Flow

```text
User Types

↓

debounce()

↓

SearchNewsUseCase

↓

Repository

↓

PagingSource

↓

API

↓

UI
```

---

# One Responsibility Per Use Case

Correct

```text
GetBookmarksUseCase
```

Incorrect

```text
BookmarkManagerUseCase
```

Small Use Cases are easier to:

- Test
- Reuse
- Maintain

---

# Operator Function

Every Use Case will expose:

```kotlin
operator fun invoke(...)
```

Benefits:

```kotlin
getTopHeadlinesUseCase()

searchNewsUseCase(query)

toggleBookmarkUseCase(article)
```

Cleaner syntax than calling custom methods.

---

# Dependency Injection

Use Cases are injected through Hilt.

```text
ViewModel

↓

GetTopHeadlinesUseCase

↓

Repository
```

The ViewModel never creates UseCases manually.

---

# Repository Communication

Use Cases depend only on the Repository interface.

```text
UseCase

↓

NewsRepository
```

The Domain layer never references:

- Retrofit
- Room
- DTO
- Entity

---

# Domain Models

Every Use Case works with:

```text
Article

Source

Bookmark
```

Never use:

```
ArticleDto

BookmarkEntity
```

inside the Domain layer.

---

# Return Types

Different Use Cases return different data.

Examples:

```text
Flow<PagingData<Article>>

Flow<List<Article>>

Boolean

Unit
```

The return type should match the business operation.

---

# Error Handling

Use Cases should propagate repository results rather than handling UI concerns.

Example flow:

```text
Repository

↓

NetworkResult

↓

UseCase

↓

ViewModel

↓

UiState
```

No Toasts, Snackbars, or Compose state belong in the Domain layer.

---

# Concurrency

Use Cases use:

- suspend functions
- Flow

No blocking calls.

Dispatchers are injected rather than hardcoded.

---

# Testing Strategy

Each Use Case is tested independently.

Examples:

- Fake Repository
- Mock Repository
- Flow verification
- Paging verification
- Bookmark logic
- Search logic

No Android framework required.

---

# Planned Use Cases

```text
news/

GetTopHeadlinesUseCase.kt

RefreshNewsUseCase.kt

--------------------------------

search/

SearchNewsUseCase.kt

--------------------------------

bookmark/

AddBookmarkUseCase.kt

RemoveBookmarkUseCase.kt

ToggleBookmarkUseCase.kt

ObserveBookmarksUseCase.kt

IsBookmarkedUseCase.kt
```

---

# Dependency Diagram

```text
Compose

↓

ViewModel

↓

UseCase

↓

Repository Interface

↓

RepositoryImpl

↓

Remote
+
Local
```

---

# Best Practices

- One responsibility per Use Case
- Keep Domain Android-free
- Depend on interfaces
- Use immutable models
- Use constructor injection
- Prefer operator invoke()
- Keep business rules inside UseCases
- Never expose DTOs or Entities

---

# Files Created

None.

This document defines the Use Case architecture and business-layer strategy.

---

# What We Completed

✅ Defined the Domain Layer responsibilities

✅ Planned feature-based UseCase organization

✅ Designed business flows

✅ Planned Hilt integration

✅ Defined return types

✅ Planned testing strategy

---

# Next Documents

The Use Case implementation will be divided into compile-safe milestones.

## 14A_GetTopHeadlines_UseCase.md

Create:

- `GetTopHeadlinesUseCase.kt`
- Paging integration
- Repository communication
- KDoc documentation

---

## 14B_SearchNews_UseCase.md

Implement:

- `SearchNewsUseCase.kt`
- Search query validation
- Paging support
- Debounce expectations

---

## 14C_Bookmark_UseCases.md

Implement:

- `AddBookmarkUseCase.kt`
- `RemoveBookmarkUseCase.kt`
- `ToggleBookmarkUseCase.kt`
- `ObserveBookmarksUseCase.kt`
- `IsBookmarkedUseCase.kt`

---

## 14D_RefreshNews_UseCase.md

Implement:

- Refresh operation
- Cache invalidation strategy
- Future WorkManager integration

---

## 14E_UseCase_Testing.md

Implement:

- Unit tests
- Fake Repository
- MockK examples
- Turbine Flow tests
- Paging verification

---

# Prompt for Code Generation

## Prompt: 14A_GetTopHeadlines_UseCase.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `GetTopHeadlinesUseCase.kt`.

### Requirements

1. Create the Use Case inside `domain/usecase/news`.
2. Inject `NewsRepository` using constructor injection.
3. Expose `operator fun invoke(...)`.
4. Return `Flow<PagingData<Article>>`.
5. Keep the Use Case free from Android framework classes.
6. Add KDoc documentation.
7. Explain every design decision.
8. Provide complete runnable Kotlin code.

---

## Prompt: 14B_SearchNews_UseCase.md

Generate the complete `SearchNewsUseCase.kt`.

Requirements:

- Validate search query.
- Delegate paging to Repository.
- Return `Flow<PagingData<Article>>`.
- Use `operator fun invoke(query: String)`.
- Explain every function.
- Provide complete runnable code.

---

## Prompt: 14C_Bookmark_UseCases.md

Generate the complete bookmark UseCases.

Requirements:

Create:

- `AddBookmarkUseCase.kt`
- `RemoveBookmarkUseCase.kt`
- `ToggleBookmarkUseCase.kt`
- `ObserveBookmarksUseCase.kt`
- `IsBookmarkedUseCase.kt`

Requirements:

- Inject `NewsRepository`.
- Use immutable Domain models.
- Use suspend functions where appropriate.
- Return Flow for observation.
- Add complete KDoc.
- Explain every Use Case.
- Provide complete runnable Kotlin code.

---

## Prompt: 14D_RefreshNews_UseCase.md

Generate the complete `RefreshNewsUseCase.kt`.

Requirements:

- Trigger repository refresh.
- Prepare for future WorkManager integration.
- Keep business logic inside the Domain layer.
- Explain refresh strategy and cache invalidation.
- Provide complete runnable code.

---

# UI Design Prompt

## Prompt: Business Logic to UI Interaction Specification

Design how each Use Case affects the NewsApp user interface.

Include:

- Home screen data loading flow
- Search interaction flow (with debounce)
- Bookmark add/remove interactions
- Refresh interaction
- Loading, success, empty, and error transitions
- Retry behavior
- Pull-to-refresh UX
- Tablet and phone interaction differences
- Material 3 state transitions
- Accessibility considerations

Do **not** generate Jetpack Compose code. Produce a UX flow and interaction specification that maps each Use Case to expected UI behavior.