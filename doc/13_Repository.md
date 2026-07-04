# NewsApp

# 13_Repository.md

> **Phase:** Data Layer
>
> **Module:** Repository Pattern
>
> **Goal:** Design a scalable Repository layer that acts as the Single Source of Truth (SSOT), coordinating Remote API, Local Database, Paging, and Domain Use Cases while remaining fully testable and independent of the UI.

---

# Objective

The Repository layer is the heart of the Data layer in Clean Architecture.

It acts as a bridge between:

- Remote API
- Local Database
- Domain Layer

Instead of allowing ViewModels or UseCases to decide where data comes from, the Repository encapsulates all data-fetching logic.

By the end of this phase, the project will have a Repository architecture capable of:

- Fetching paginated news
- Searching news
- Bookmark management
- Offline support
- Cache synchronization
- Error handling
- Future extensibility

---

# Why Repository Pattern?

Without Repository

```text
Compose

↓

ViewModel

↓

Retrofit

↓

API
```

Problems:

- Tight Coupling
- Hard to Test
- No Offline Support
- Duplicate Logic
- Business Logic in ViewModel

---

With Repository

```text
Compose

↓

ViewModel

↓

UseCase

↓

Repository

↓

Remote
+
Local

↓

API / Room
```

Benefits

- Single Source of Truth
- Better Testing
- Better Scalability
- Offline Support
- Easy Mocking

---

# Single Source of Truth (SSOT)

The Repository becomes the only component allowed to decide where data comes from.

```text
Repository

↓

Remote

↓

Room

↓

Memory Cache (Future)
```

Neither ViewModel nor UseCases know whether the data comes from:

- API
- Database
- Cache

---

# Repository Architecture

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

RemoteDataSource
        +
LocalDataSource

↓

Retrofit
Room
```

---

# Responsibilities

Repository is responsible for:

- Fetch API
- Search API
- Paging
- Save Bookmarks
- Delete Bookmarks
- Observe Bookmarks
- Cache Management
- Error Mapping
- Network Decisions
- Offline Decisions

Repository is NOT responsible for:

- UI Logic
- Compose State
- Navigation
- Formatting
- Business Rules

---

# Repository Structure

```text
domain

repository

NewsRepository.kt

↓

Implemented by

↓

data

repository

NewsRepositoryImpl.kt
```

---

# Repository Interface

The Domain layer defines contracts.

Example operations

```text
getTopHeadlines()

searchNews()

bookmarkArticle()

removeBookmark()

observeBookmarks()

isBookmarked()
```

The implementation details remain hidden inside the Data layer.

---

# Repository Implementation

```text
NewsRepositoryImpl

↓

RemoteDataSource

↓

Retrofit

+

LocalDataSource

↓

Room
```

The implementation coordinates all data sources.

---

# Remote Data Flow

```text
ViewModel

↓

UseCase

↓

Repository

↓

RemoteDataSource

↓

NewsApiService

↓

Internet
```

---

# Local Data Flow

```text
ViewModel

↓

UseCase

↓

Repository

↓

LocalDataSource

↓

DAO

↓

Room
```

---

# Combined Data Flow

```text
Repository

↓

RemoteDataSource

↓

API

↓

Mapper

↓

Domain

↓

UI

+

LocalDataSource

↓

Bookmarks

↓

UI
```

---

# Repository Methods

The Repository interface will expose:

## News

- Get Headlines
- Search News
- Refresh News

---

## Bookmark

- Save Bookmark
- Remove Bookmark
- Toggle Bookmark
- Observe Bookmarks
- Check Bookmark

---

## Future

- Categories
- Sources
- User Preferences
- Offline Cache
- RemoteMediator

---

# Paging Integration

Repository returns

```text
Flow<PagingData<Article>>
```

instead of

```text
List<Article>
```

Flow

↓

PagingData

↓

Compose

---

# Search Integration

```text
Search Query

↓

UseCase

↓

Repository

↓

PagingSource

↓

API
```

Each query creates a new paging stream.

---

# Offline Strategy

Internet Available

```text
Repository

↓

API

↓

Room Cache

↓

UI
```

Offline

```text
Repository

↓

Room

↓

UI
```

---

# Cache Strategy

Current implementation

```text
Bookmarks Only
```

Future implementation

```text
Articles

Bookmarks

Categories

User Settings
```

---

# Error Handling

Repository converts:

```text
IOException

HttpException

SerializationException
```

↓

NetworkResult

↓

UseCase

↓

ViewModel

No exceptions should reach the UI.

---

# Mapping Strategy

```text
DTO

↓

Mapper

↓

Domain

↓

Repository

↓

UseCase
```

Repository only exposes Domain Models.

---

# Concurrency

Repository uses

- Coroutines
- Flow
- suspend functions

No blocking calls.

---

# Dependency Injection

Repository is injected using Hilt.

```text
ViewModel

↓

UseCase

↓

Repository Interface

↓

RepositoryImpl
```

The ViewModel depends only on the interface.

---

# Folder Structure

```text
domain

repository

NewsRepository.kt

--------------------------------

data

repository

NewsRepositoryImpl.kt

--------------------------------

data

datasource

remote

RemoteNewsDataSource.kt

local

LocalNewsDataSource.kt
```

---

# Planned Repository Functions

```text
getTopHeadlines()

searchNews()

bookmarkArticle()

removeBookmark()

toggleBookmark()

getBookmarks()

observeBookmarks()

isBookmarked()
```

---

# Repository Decision Matrix

| Feature | Remote | Local |
|----------|--------|-------|
| Headlines | ✅ | Optional Cache |
| Search | ✅ | ❌ |
| Bookmark | ❌ | ✅ |
| Bookmark Status | ❌ | ✅ |
| Offline Reading | Optional | ✅ |

---

# Testing Strategy

Repository Tests

- Fake RemoteDataSource
- Fake LocalDataSource
- Mock Repository
- Flow Testing
- Paging Testing

Repository tests should not require Compose.

---

# Future Enhancements

Future improvements include:

- RemoteMediator
- Memory Cache
- Multi-source Repository
- Background Sync
- Conflict Resolution
- Analytics Hooks

---

# Architecture Diagram

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

RemoteDataSource

+

LocalDataSource

↓

Retrofit

Room
```

---

# Best Practices

- Repository exposes Domain Models only
- Never expose DTOs
- Never expose Room Entities
- Keep Repository thin
- Business Rules belong to UseCases
- Use Flow for streams
- Use suspend for one-shot operations
- Repository is the Single Source of Truth

---

# Files Planned

```text
domain/

repository/

NewsRepository.kt

--------------------------------

data/

repository/

NewsRepositoryImpl.kt

--------------------------------

data/

datasource/

remote/

RemoteNewsDataSource.kt

--------------------------------

data/

datasource/

local/

LocalNewsDataSource.kt
```

---

# Files Created

None.

This document defines the Repository architecture and implementation strategy.

---

# What We Completed

✅ Designed the Repository architecture

✅ Defined Single Source of Truth

✅ Planned Repository responsibilities

✅ Designed Remote + Local coordination

✅ Planned paging integration

✅ Planned bookmark management

✅ Defined error handling strategy

✅ Planned testing approach

---

# Next Documents

The Repository implementation will be divided into compile-safe milestones.

## 13A_Repository_Interface.md

Create:

- `NewsRepository.kt`
- Repository contract
- Domain-facing APIs
- Documentation

---

## 13B_Remote_Repository.md

Implement:

- Remote repository operations
- News fetching
- Search
- Paging integration

---

## 13C_Local_Repository.md

Implement:

- Bookmark operations
- Local persistence
- Flow observation
- Offline support

---

## 13D_Repository_Impl.md

Implement:

- `NewsRepositoryImpl.kt`
- Remote + Local coordination
- Mapping
- Error handling
- Dependency Injection

---

## 13E_Cache_Strategy.md

Implement:

- Repository caching strategy
- Cache invalidation
- Refresh policy
- Future RemoteMediator integration

---

## 13F_Repository_Testing.md

Implement:

- Fake Repository
- Repository unit tests
- Paging tests
- Flow tests
- Error handling tests

---

# Prompt for Code Generation

## Prompt: 13A_Repository_Interface.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `NewsRepository.kt`.

### Requirements

1. Create the repository interface inside the Domain layer.
2. Expose only Domain models.
3. Include methods for:
    - Top headlines
    - Search news
    - Refresh news
    - Save bookmark
    - Remove bookmark
    - Toggle bookmark
    - Observe bookmarks
    - Check bookmark status
4. Return `Flow<PagingData<Article>>` where appropriate.
5. Use suspend functions for write operations.
6. Add KDoc comments for every function.
7. Explain every design decision.
8. Provide complete runnable Kotlin code.

---

## Prompt: 13B_Remote_Repository.md

Generate the remote repository implementation.

Requirements:

- Use `RemoteNewsDataSource`
- Integrate Paging 3
- Convert DTOs to Domain models
- Handle `NetworkResult`
- Return `Flow<PagingData<Article>>`
- Explain every function
- Provide complete runnable code.

---

## Prompt: 13C_Local_Repository.md

Generate the local repository implementation.

Requirements:

- Use `LocalNewsDataSource`
- Implement bookmark CRUD operations
- Observe bookmarks using Flow
- Convert Entities to Domain models
- Handle Room exceptions gracefully
- Provide complete runnable Kotlin code.

---

## Prompt: 13D_Repository_Impl.md

Generate the complete `NewsRepositoryImpl.kt`.

Requirements:

1. Implement `NewsRepository`.
2. Inject:
    - RemoteNewsDataSource
    - LocalNewsDataSource
3. Coordinate Remote and Local data.
4. Map DTOs and Entities to Domain models.
5. Keep business logic out of the Repository.
6. Use Hilt constructor injection.
7. Explain every method and dependency.
8. Provide complete runnable Kotlin code.

---

# UI Design Prompt

## Prompt: Repository-to-UI Data Flow Specification

Design how repository data should drive the NewsApp UI.

Include:

- Home feed data flow
- Search results flow
- Bookmark synchronization
- Offline behavior
- Pull-to-refresh updates
- Pagination updates
- Loading, success, empty, and error state transitions
- UI reactions to bookmark changes
- Tablet and phone behavior
- Material 3 interaction guidelines

Do **not** generate Jetpack Compose code. Produce a data-flow specification and wireframe-level interaction guide that will be used during the UI implementation phase.