# NewsApp

> **Phase:** Data Layer
>
> **Module:** Pagination
>
> **Goal:** Design a scalable, production-ready pagination architecture using Android Paging 3 that efficiently loads news articles, minimizes network usage, and provides a smooth scrolling experience.

---

# Objective

Modern news applications rarely load every article at once.

Instead, they request small chunks of data as the user scrolls.

Android Paging 3 provides an efficient way to:

- Reduce memory usage
- Minimize network requests
- Improve scrolling performance
- Support infinite scrolling
- Handle refresh automatically
- Integrate with Room
- Work seamlessly with Kotlin Flow

This document defines the overall paging architecture that will be implemented in subsequent steps.

---

# Why Paging 3?

Without pagination:

```text
API

↓

500 Articles

↓

Memory

↓

UI
```

Problems:

- Large network response
- Slow loading
- High memory usage
- Poor scrolling
- Unnecessary bandwidth

---

With Paging 3:

```text
API

↓

20 Articles

↓

User Scrolls

↓

Next 20 Articles

↓

Next 20 Articles
```

Benefits:

- Fast initial load
- Infinite scrolling
- Better UX
- Lower memory consumption

---

# Paging Architecture

```text
Compose Screen

↓

LazyPagingItems

↓

ViewModel

↓

Pager

↓

PagingSource

↓

NewsRepository

↓

RemoteDataSource

↓

News API
```

Every page is loaded only when needed.

---

# Complete Data Flow

```text
User Scrolls

↓

LazyColumn

↓

LazyPagingItems

↓

Pager

↓

PagingSource

↓

Repository

↓

RemoteDataSource

↓

Retrofit

↓

News API

↓

DTO

↓

Mapper

↓

Domain Model

↓

PagingData<Article>

↓

Compose UI
```

---

# Paging Components

The Paging layer consists of:

```text
PagingSource

↓

Pager

↓

PagingConfig

↓

PagingData

↓

LazyPagingItems

↓

LoadState
```

Each component has a dedicated responsibility.

---

# PagingSource

The PagingSource is responsible for:

- Loading one page
- Calling the API
- Returning the next key
- Returning the previous key
- Handling failures

It should not contain business logic.

---

# Pager

The Pager:

- Owns the PagingSource
- Configures page size
- Creates Flow<PagingData<T>>
- Supports caching in ViewModel

---

# PagingConfig

The Paging configuration controls:

- Page size
- Prefetch distance
- Initial load size
- Placeholder support
- Maximum cache size

Recommended defaults:

| Setting | Value |
|----------|------:|
| pageSize | 20 |
| initialLoadSize | 20 |
| prefetchDistance | 5 |
| enablePlaceholders | false |

---

# Repository Integration

The Repository exposes:

```text
Flow<PagingData<Article>>
```

The ViewModel never interacts with Retrofit directly.

---

# ViewModel Integration

The ViewModel:

- Requests paging data
- Caches the flow
- Exposes it to Compose

Responsibilities:

- No pagination logic
- No Retrofit calls
- No database queries

---

# Compose Integration

Compose consumes:

```text
LazyPagingItems<Article>
```

Benefits:

- Automatic loading
- Efficient recomposition
- Built-in load states

---

# Load States

Paging 3 exposes three primary load states.

## Refresh

Initial load.

Used to display:

- Full-screen loading
- Initial error
- Empty state

---

## Append

Loading additional pages.

Displayed as:

- Bottom loading indicator
- Retry footer

---

## Prepend

Loading previous pages.

Not used in this application because articles are loaded from newest to oldest.

---

# Error Handling

Paging integrates with `LoadState`.

Possible states:

```text
Loading

↓

Success

↓

Append Loading

↓

Error

↓

Retry
```

Users should always have a retry option.

---

# Refresh Strategy

The application supports:

- Pull-to-refresh
- Swipe refresh
- Automatic refresh when returning to the feed (optional)

Refreshing invalidates the PagingSource and reloads data.

---

# Offline Support

Initial implementation:

```text
API

↓

PagingSource

↓

UI
```

Bonus implementation:

```text
API

↓

RemoteMediator

↓

Room

↓

PagingSource(Room)

↓

UI
```

This satisfies the optional offline caching enhancement.

---

# Performance Considerations

Best practices:

- Small page sizes
- Cache in ViewModel
- Avoid unnecessary refreshes
- Disable placeholders
- Stable keys in LazyColumn
- Avoid expensive item recomposition

---

# Retry Strategy

Retry only transient failures.

Examples:

- Network timeout
- HTTP 500
- HTTP 503

Do not retry:

- HTTP 401
- HTTP 403
- Invalid requests

---

# Search Pagination

Search results also use Paging 3.

Flow:

```text
Search Query

↓

Debounce

↓

New Pager

↓

PagingSource

↓

API

↓

Compose
```

Every new query creates a fresh PagingSource.

---

# Paging with Bookmarks

Bookmarks are stored locally and reflected in the paged list.

Flow:

```text
PagingData<Article>

+

Bookmarks Flow

↓

Merged UI State

↓

Compose
```

This ensures bookmark status stays synchronized.

---

# Folder Structure

```text
data

└── remote

    └── paging

        NewsPagingSource.kt

--------------------------------

data

└── repository

        NewsRepositoryImpl.kt

--------------------------------

presentation

└── home

        HomeViewModel.kt

--------------------------------

presentation

└── components

        PagingLoading.kt

        PagingError.kt

        PagingFooter.kt
```

---

# Planned Kotlin Files

The following files will be implemented across the Paging documents.

```text
data/remote/paging/

NewsPagingSource.kt

--------------------------------

data/repository/

NewsRepositoryImpl.kt

--------------------------------

presentation/home/

HomeViewModel.kt

--------------------------------

presentation/components/

PagingLoading.kt

PagingError.kt

PagingFooter.kt
```

---

# Architecture Diagram

```text
Compose

↓

LazyPagingItems

↓

PagingData

↓

Pager

↓

PagingSource

↓

Repository

↓

RemoteDataSource

↓

News API
```

---

# Best Practices

- Keep PagingSource focused on data loading
- Expose `Flow<PagingData<DomainModel>>`
- Cache paging data in the ViewModel
- Use stable keys in Compose
- Handle all `LoadState`s
- Avoid business logic inside PagingSource
- Prepare for future `RemoteMediator` support

---

# Files Created in This Document

None.

This document defines the Paging 3 architecture and implementation strategy.

---

# What We Completed

- Defined the Paging 3 architecture
- Planned PagingSource responsibilities
- Designed Pager configuration
- Planned Repository and ViewModel integration
- Established LoadState handling
- Planned pull-to-refresh support
- Designed search pagination
- Planned offline caching strategy
- Defined testing approach

---

# Next Documents

The Paging implementation will be divided into compile-safe milestones.

## 12A_Paging_Architecture.md

Create:

- Paging package structure
- Paging flow diagram
- PagingConfig decisions
- Build verification

---

## 12B_NewsPagingSource.md

Implement:

- `NewsPagingSource.kt`
- API loading
- Next/previous keys
- Error handling
- Refresh key logic

---

## 12C_Pager_Configuration.md

Implement:

- `Pager`
- `PagingConfig`
- Repository integration
- Flow<PagingData<Article>>

---

## 12D_Repository_Paging.md

Implement:

- Paging repository methods
- Search pagination
- Refresh handling

---

## 12E_ViewModel_Paging.md

Implement:

- Paging Flow
- `cachedIn(viewModelScope)`
- UI state integration

---

## 12F_Compose_Paging.md

Implement:

- `LazyPagingItems`
- `LazyColumn`
- Stable keys
- Efficient recomposition

---

## 12G_LoadState_Handling.md

Implement:

- Initial loading UI
- Append footer
- Retry
- Empty state
- Error state

---

## 12H_Pull_To_Refresh.md

Implement:

- Material pull-to-refresh
- Paging refresh
- Refresh indicator

---

## 12I_RemoteMediator_Bonus.md

Implement:

- RemoteMediator
- Room cache
- Offline-first paging
- Synchronization strategy

---

## 12J_Paging_Testing.md

Implement:

- PagingSource tests
- Repository paging tests
- ViewModel paging tests
- Compose paging UI tests

---

# Prompt for Code Generation

## Prompt: 12B_NewsPagingSource.md

You are a Senior Android Engineer.

Generate the complete `NewsPagingSource.kt` for the NewsApp project.

### Requirements

1. Extend `PagingSource<Int, Article>`.
2. Load paginated news from the configured News API.
3. Handle:
    - Initial load
    - Next page
    - Previous page
    - Network failures
    - Empty responses
4. Implement `getRefreshKey()` correctly.
5. Return Domain models rather than DTOs.
6. Explain every method and design decision.
7. Provide complete runnable Kotlin code.

---

## Prompt: 12C_Pager_Configuration.md

Generate the complete paging configuration.

Requirements:

- Create `Pager`.
- Configure `PagingConfig`.
- Return `Flow<PagingData<Article>>`.
- Integrate with Repository.
- Explain page size, prefetch distance, and placeholder decisions.
- Provide complete runnable code.

---

## Prompt: 12F_Compose_Paging.md

Generate the complete Compose Paging integration.

Requirements:

- Use `LazyPagingItems`.
- Display paginated articles in `LazyColumn`.
- Handle `LoadState`.
- Use stable item keys.
- Optimize recomposition.
- Provide complete runnable Jetpack Compose code with explanations.

---

# UI Design Prompt

## Prompt: Infinite Scrolling Experience

Design the complete UX for paginated news feeds in **NewsApp**.

Include:

- Initial loading skeleton
- Infinite scrolling behavior
- Bottom loading footer
- End-of-list indicator
- Pull-to-refresh interaction
- Error and retry footer
- Empty feed layout
- Search results pagination
- Bookmark indicators within paged cards
- Tablet and phone layouts
- Accessibility considerations
- Material 3 interaction patterns

Do **not** generate Jetpack Compose code. Produce a visual specification and interaction guide that will be implemented during the UI phase.