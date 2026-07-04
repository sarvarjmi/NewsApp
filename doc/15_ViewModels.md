# NewsApp

> **Phase:** Presentation Layer
>
> **Module:** ViewModels
>
> **Goal:** Design a scalable, lifecycle-aware Presentation Layer using ViewModels, StateFlow, SharedFlow, Hilt, and Unidirectional Data Flow (UDF) while keeping UI logic separate from business logic.

---

# Objective

The ViewModel is the bridge between the UI and the Domain layer.

It receives user interactions from Jetpack Compose, invokes the appropriate Use Cases, transforms results into UI state, and exposes observable state to the UI.

By the end of this phase, the application will have:

- Lifecycle-aware ViewModels
- StateFlow-based UI state
- SharedFlow-based one-time events
- Feature-specific ViewModels
- Unidirectional Data Flow
- Paging integration
- Hilt injection
- Comprehensive testing strategy

---

# Why ViewModels?

Without ViewModels:

```text
Compose Screen

↓

Repository

↓

API
```

Problems:

- Business logic inside UI
- Difficult testing
- State loss on configuration changes
- Tight coupling

---

With ViewModels:

```text
Compose Screen

↓

ViewModel

↓

UseCase

↓

Repository

↓

API / Room
```

Benefits:

- Survives configuration changes
- Keeps UI declarative
- Easy testing
- Clear separation of concerns
- Lifecycle awareness

---

# MVVM Architecture

```text
Compose UI

↓

UI Events

↓

ViewModel

↓

UseCases

↓

Repository

↓

Remote
+
Local

↓

StateFlow

↓

Compose UI
```

The ViewModel is the only layer aware of both UI and Domain.

---

# Responsibilities

ViewModels are responsible for:

- UI State
- User Events
- Side Effects
- Calling Use Cases
- Paging Flow
- Error Mapping
- Loading State
- Retry
- Refresh

ViewModels are **not** responsible for:

- Navigation implementation
- Retrofit
- Room
- SQL
- Business rules
- UI rendering

---

# Feature ViewModels

Each feature owns its own ViewModel.

```text
presentation

home

HomeViewModel.kt

-------------------------

search

SearchViewModel.kt

-------------------------

detail

DetailViewModel.kt

-------------------------

bookmark

BookmarkViewModel.kt
```

No shared "God ViewModel."

---

# State Management

The application uses **StateFlow** for persistent UI state.

```text
ViewModel

↓

MutableStateFlow

↓

StateFlow

↓

Compose collectAsStateWithLifecycle()

↓

UI
```

The UI never modifies state directly.

---

# UI State

Every screen exposes a dedicated immutable UI state.

Examples:

```text
HomeUiState

SearchUiState

DetailUiState

BookmarkUiState
```

Each state contains only information required to render the screen.

---

# User Events

User interactions are modeled as events.

Examples:

- Refresh
- Retry
- Search
- Bookmark
- Share
- Open Article

The ViewModel receives events and decides which Use Case to execute.

---

# Side Effects

One-time actions are separated from persistent state.

Examples:

- Snackbar
- Toast
- Navigation
- Share Sheet
- Open Browser

These should not be stored inside `StateFlow`.

Instead, use `SharedFlow` or `Channel`.

---

# Unidirectional Data Flow (UDF)

The project follows UDF.

```text
User Action

↓

UiEvent

↓

ViewModel

↓

UseCase

↓

Repository

↓

UiState

↓

Compose
```

Data flows in one direction, making the application predictable and easier to debug.

---

# Home Screen Flow

```text
App Launch

↓

HomeViewModel

↓

GetTopHeadlinesUseCase

↓

Repository

↓

PagingData<Article>

↓

HomeUiState

↓

Compose
```

---

# Search Flow

```text
User Types

↓

UiEvent.Search

↓

debounce()

↓

SearchNewsUseCase

↓

PagingData<Article>

↓

SearchUiState

↓

Compose
```

---

# Bookmark Flow

```text
User Clicks Bookmark

↓

ToggleBookmarkUseCase

↓

Repository

↓

Room

↓

Bookmarks Flow

↓

BookmarkUiState

↓

Compose
```

---

# Detail Flow

```text
Open Article

↓

DetailViewModel

↓

ObserveBookmarkStatus

↓

DetailUiState

↓

Compose
```

---

# Paging Integration

The ViewModel exposes:

```text
Flow<PagingData<Article>>
```

using:

```text
cachedIn(viewModelScope)
```

This prevents unnecessary reloads during configuration changes.

---

# Error Handling

The ViewModel converts repository results into user-friendly UI states.

Typical states:

- Loading
- Success
- Empty
- Error

The UI does not process exceptions.

---

# Dependency Injection

ViewModels receive dependencies through Hilt.

```text
HomeViewModel

↓

GetTopHeadlinesUseCase

↓

NewsRepository
```

No manual object creation.

---

# Folder Structure

```text
presentation

├── home

│   HomeViewModel.kt

│   HomeUiState.kt

│   HomeEvent.kt

│   HomeSideEffect.kt

├── search

│   SearchViewModel.kt

│   SearchUiState.kt

│   SearchEvent.kt

├── detail

│   DetailViewModel.kt

│   DetailUiState.kt

├── bookmark

│   BookmarkViewModel.kt

│   BookmarkUiState.kt

└── common

    BaseViewModel.kt
```

---

# Planned ViewModels

```text
HomeViewModel

SearchViewModel

DetailViewModel

BookmarkViewModel
```

Each ViewModel is responsible for one feature only.

---

# Testing Strategy

Every ViewModel should have dedicated unit tests.

Tests verify:

- Initial state
- Loading state
- Success state
- Empty state
- Error state
- Paging Flow
- StateFlow updates
- SharedFlow events

No Compose or Android framework dependencies required.

---

# Performance Considerations

- Keep UI state immutable
- Use `cachedIn(viewModelScope)` for Paging
- Avoid exposing `MutableStateFlow`
- Minimize recompositions
- Keep ViewModels lightweight
- Delegate business logic to UseCases

---

# Best Practices

- One ViewModel per feature
- Immutable UI state
- StateFlow for state
- SharedFlow for one-time events
- Constructor injection
- No Android Context
- No direct repository access from UI
- No business rules inside ViewModels

---

# Files Planned

```text
presentation/home/

HomeViewModel.kt

HomeUiState.kt

HomeEvent.kt

HomeSideEffect.kt

--------------------------------

presentation/search/

SearchViewModel.kt

SearchUiState.kt

SearchEvent.kt

--------------------------------

presentation/detail/

DetailViewModel.kt

DetailUiState.kt

--------------------------------

presentation/bookmark/

BookmarkViewModel.kt

BookmarkUiState.kt

--------------------------------

presentation/common/

BaseViewModel.kt
```

---

# Files Created

None.

This document defines the ViewModel architecture and presentation-layer strategy.

---

# What We Completed

- Designed the Presentation Layer architecture
- Defined ViewModel responsibilities
- Planned feature-based ViewModels
- Established StateFlow and SharedFlow usage
- Designed UI state, events, and side effects
- Planned Paging integration
- Defined testing strategy

---

# Next Documents

The ViewModel implementation will be divided into compile-safe milestones.

## 15A_Base_ViewModel.md

Create:

- `BaseViewModel.kt`
- Common coroutine handling
- Error handling helpers
- Loading state support

---

## 15B_UI_State.md

Create:

- `HomeUiState.kt`
- `SearchUiState.kt`
- `DetailUiState.kt`
- `BookmarkUiState.kt`

---

## 15C_UI_Events.md

Create:

- `HomeEvent.kt`
- `SearchEvent.kt`
- `DetailEvent.kt`
- `BookmarkEvent.kt`

---

## 15D_UI_SideEffects.md

Create:

- Snackbar events
- Navigation events
- Share events
- Browser events

---

## 15E_Home_ViewModel.md

Implement:

- Headlines loading
- Paging integration
- Refresh
- Retry
- Bookmark interaction

---

## 15F_Search_ViewModel.md

Implement:

- Search debounce
- Paging
- Empty state
- Retry

---

## 15G_Detail_ViewModel.md

Implement:

- Bookmark status
- Share action
- Browser action

---

## 15H_Bookmark_ViewModel.md

Implement:

- Observe bookmarks
- Delete bookmark
- Undo support

---

## 15I_StateFlow_Strategy.md

Implement:

- StateFlow
- SharedFlow
- Lifecycle collection
- Flow best practices

---

## 15J_ViewModel_Testing.md

Implement:

- ViewModel unit tests
- Turbine Flow tests
- Fake repositories
- Paging tests

---

## 15K_Presentation_Best_Practices.md

Document:

- Recomposition optimization
- Lifecycle awareness
- Memory leak prevention
- Large-screen considerations

---

# Prompt for Code Generation

## Prompt: 15A_Base_ViewModel.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `BaseViewModel.kt`.

### Requirements

1. Create a reusable base ViewModel.
2. Include:
    - Coroutine exception handling
    - Loading state support
    - Error state support
    - SharedFlow for one-time events
3. Keep it Android lifecycle-aware.
4. Use Kotlin Coroutines and StateFlow.
5. Explain every function.
6. Provide complete runnable Kotlin code.

---

## Prompt: 15B_UI_State.md

Generate the complete UI state models.

Requirements:

Create:

- `HomeUiState.kt`
- `SearchUiState.kt`
- `DetailUiState.kt`
- `BookmarkUiState.kt`

Requirements:

- Immutable data classes
- Loading, Success, Empty, Error support
- Paging support where applicable
- KDoc documentation
- Complete runnable Kotlin code

---

## Prompt: 15E_Home_ViewModel.md

Generate the complete `HomeViewModel.kt`.

Requirements:

- Inject `GetTopHeadlinesUseCase`
- Inject `ToggleBookmarkUseCase`
- Expose `StateFlow<HomeUiState>`
- Expose paged news
- Implement refresh and retry
- Use `cachedIn(viewModelScope)`
- Handle UI events
- Explain every method
- Provide complete runnable Kotlin code

---

# UI Design Prompt

## Prompt: Presentation Layer UX Specification

Design the interaction model for every ViewModel-driven screen in **NewsApp**.

Include:

- Home screen state transitions
- Search typing and debounce UX
- Bookmark interactions
- Detail screen actions
- Loading, success, empty, and error flows
- Snackbar behavior
- Pull-to-refresh interaction
- Pagination loading
- Tablet vs phone behavior
- Material 3 animations and transitions
- Accessibility guidelines (TalkBack, focus order, dynamic text)

Do **not** generate Jetpack Compose code. Produce a presentation-layer UX specification that maps ViewModel states, events, and side effects to expected user interactions.