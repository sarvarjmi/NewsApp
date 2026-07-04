# NewsApp

# 19_Search_Screen.md

> **Phase:** Presentation Layer
>
> **Module:** Search Screen
>
> **Goal:** Design and implement a modern, high-performance Search Screen using Jetpack Compose, Material 3, Paging 3, StateFlow, Kotlin Flow, Coroutines, and Clean Architecture.

---

# Objective

The Search Screen enables users to quickly discover news articles by entering keywords.

Unlike the Home Screen, the Search Screen must support:

- Real-time searching
- Debounced API requests
- Infinite scrolling
- Pull-to-refresh
- Search history (future)
- Empty query handling
- Empty result handling
- Retry after network failures

The implementation should remain scalable, lifecycle-aware, and fully integrated with MVVM and Clean Architecture.

---

# Responsibilities

The Search Screen is responsible for:

- Accepting user search input
- Displaying paginated search results
- Debouncing API requests
- Showing loading, empty, success, and error states
- Opening article details
- Bookmarking articles
- Restoring previous search state

The Search Screen should **NOT**:

- Call Retrofit directly
- Access Repository directly
- Perform business logic
- Manage navigation directly

---

# Architecture

```text
SearchScreen

↓

SearchViewModel

↓

SearchNewsUseCase

↓

Repository

↓

PagingSource

↓

News API

↓

Compose UI
```

---

# User Journey

```text
Open Search

↓

Search Field Focused

↓

User Types

↓

Debounce

↓

API Search

↓

Results Loaded

↓

Infinite Scroll

↓

Open Detail

↓

Back

↓

Previous Query Restored
```

---

# Screen Layout

```text
Scaffold

│

├── Search Top App Bar

├── Search TextField

├── Recent Searches (Future)

├── Filter Chips (Future)

└── LazyColumn

      │

      ├── NewsCard

      ├── NewsCard

      ├── NewsCard

      └── Paging Footer
```

---

# Search States

The screen supports multiple states.

```text
Idle

↓

Typing

↓

Searching

↓

Results

↓

No Results

↓

Error
```

Each state has a dedicated UI.

---

# Search Flow

```text
User Types

↓

MutableStateFlow(query)

↓

debounce(500ms)

↓

distinctUntilChanged()

↓

SearchNewsUseCase

↓

Repository

↓

PagingData<Article>

↓

Compose
```

---

# Debounce Strategy

To prevent unnecessary API requests:

```text
H

↓

He

↓

Hea

↓

Head

↓

Headl

↓

Headline

↓

500ms Pause

↓

API Call
```

Only the final query triggers a request.

---

# Query Validation

Before searching:

- Trim whitespace
- Ignore blank input
- Ignore duplicate queries
- Minimum query length (optional: 2–3 characters)

Invalid queries should not trigger network calls.

---

# Search Bar

Features:

- Material 3 SearchBar or OutlinedTextField
- Leading search icon
- Clear text button
- Placeholder text
- Keyboard search action
- Focus management

---

# Search Results

Results are displayed using:

```text
LazyColumn

↓

LazyPagingItems<Article>
```

Features:

- Infinite scrolling
- Stable keys
- Smooth recomposition

---

# Empty Query State

When no query is entered:

Display:

- Search illustration
- Helpful message

Example:

```
Start typing to search news
```

No API request is made.

---

# Empty Results

If the API returns no articles:

Display:

- Empty illustration
- Search keyword
- Retry suggestion

Example:

```
No articles found for "Android 17"
```

---

# Loading State

Display:

- Search shimmer
- Skeleton cards
- Progress indicator

The existing results remain visible during incremental loads where appropriate.

---

# Error State

Display:

- Error illustration
- Friendly message
- Retry button

Common errors:

- No Internet
- Timeout
- Server unavailable

---

# Pull-to-Refresh

Flow:

```text
User Pulls

↓

Refresh

↓

PagingSource.invalidate()

↓

Reload Current Query
```

The query remains unchanged.

---

# Pagination

Search uses Paging 3.

```text
Search Results

↓

Scroll

↓

Next Page

↓

Next Page

↓

End
```

---

# Bookmark Flow

```text
Bookmark Click

↓

ToggleBookmarkUseCase

↓

Repository

↓

Room

↓

Updated Search UI
```

---

# Navigation

```text
Search Result

↓

Detail Screen

↓

Back

↓

Search Results Restored
```

Query and scroll position should be preserved.

---

# Keyboard Behavior

Requirements:

- Show keyboard automatically on first open
- IME Search action
- Hide keyboard after search (optional)
- Restore focus appropriately

---

# Search History (Future)

Future enhancement:

```text
Recent Searches

Trending Searches

Suggested Queries
```

Stored using Room or DataStore.

---

# Filter Support (Future)

Possible filters:

- Language
- Category
- Country
- Date
- Sort Order

Architecture should allow easy expansion.

---

# Performance Considerations

- debounce()
- distinctUntilChanged()
- cachedIn(viewModelScope)
- Stable LazyColumn keys
- Stateless composables
- Avoid unnecessary recomposition

---

# Accessibility

Requirements:

- Content descriptions
- TalkBack labels
- Keyboard navigation
- Dynamic font scaling
- Minimum touch targets (48dp)

---

# Responsive Layout

Phone:

```text
Single Column
```

Tablet:

```text
Two-pane search (future)
```

Landscape:

Maintain consistent spacing and adaptive layouts.

---

# Folder Structure

```text
presentation

search

├── SearchScreen.kt

├── SearchContent.kt

├── SearchBar.kt

├── SearchViewModel.kt

├── SearchUiState.kt

├── SearchEvent.kt

├── SearchSideEffect.kt

└── SearchTopBar.kt

--------------------------------

presentation

components

NewsCard.kt

LoadingShimmer.kt

ErrorView.kt

EmptyState.kt
```

---

# UI State Flow

```text
SearchViewModel

↓

MutableStateFlow

↓

StateFlow<SearchUiState>

↓

collectAsStateWithLifecycle()

↓

Compose
```

---

# Interaction Flow

```text
User Types

↓

UiEvent.QueryChanged

↓

SearchViewModel

↓

SearchNewsUseCase

↓

PagingData

↓

Compose
```

---

# Testing Strategy

The Search Screen should be tested for:

- Empty query
- Debounce
- Search results
- Pagination
- Pull-to-refresh
- Retry
- Error state
- Bookmark interaction
- Navigation
- Scroll restoration

Testing tools:

- Compose UI Test
- Turbine
- MockK
- Paging Test
- Fake Repository

---

# Build Verification

After implementation:

- Search field accepts input
- Debounce prevents excessive API calls
- Results load correctly
- Pagination works
- Pull-to-refresh reloads results
- Empty states display correctly
- Error handling works
- Navigation to Detail functions correctly
- Search state survives configuration changes

---

# Files Created

None.

This document defines the Search Screen architecture and implementation strategy.

---

# What We Completed

✅ Designed the Search Screen architecture

✅ Planned debounced search

✅ Planned Paging integration

✅ Defined loading, empty, and error states

✅ Planned bookmark interaction

✅ Planned navigation flow

✅ Planned accessibility

---

# Next Documents

The Search implementation will be divided into compile-safe milestones.

## 19A_Search_UI_State.md

Create:

- `SearchUiState.kt`
- Immutable UI state
- Query state
- Loading/Error support

---

## 19B_Search_Events.md

Create:

- `SearchEvent.kt`
- QueryChanged
- Retry
- Refresh
- BookmarkClicked
- ArticleClicked

---

## 19C_Search_Bar.md

Implement:

- Material 3 SearchBar
- Keyboard actions
- Clear button
- Focus management

---

## 19D_Search_Screen_Compose.md

Implement:

- `SearchScreen.kt`
- Scaffold
- SearchBar
- LazyColumn
- Pull-to-refresh
- Paging integration

---

## 19E_Search_ViewModel.md

Implement:

- Query StateFlow
- debounce()
- distinctUntilChanged()
- Paging Flow
- Refresh
- Retry

---

## 19F_Search_LoadStates.md

Implement:

- Loading shimmer
- Empty query
- Empty results
- Error state
- Append loading

---

## 19G_Search_Navigation.md

Implement:

- Detail navigation
- Bookmark interaction
- Side effects
- Scroll restoration

---

## 19H_Search_Testing.md

Implement:

- ViewModel tests
- Debounce tests
- Paging tests
- Compose UI tests
- Accessibility tests

---

# Prompt for Code Generation

## Prompt: 19A_Search_UI_State.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `SearchUiState.kt`.

### Requirements

1. Create an immutable UI state model.
2. Include:
    - Current query
    - Loading state
    - Empty state
    - Error state
    - Search results state
3. Support Paging 3 integration.
4. Add KDoc documentation.
5. Explain every property.
6. Provide complete runnable Kotlin code.

---

## Prompt: 19C_Search_Bar.md

Generate the complete reusable SearchBar component.

Requirements:

- Material 3 SearchBar or OutlinedTextField
- Search icon
- Clear icon
- Placeholder
- Keyboard search action
- FocusRequester support
- Accessibility semantics
- Dark mode support
- Preview composables
- Complete runnable Jetpack Compose code with explanations.

---

## Prompt: 19D_Search_Screen_Compose.md

Generate the complete `SearchScreen.kt`.

Requirements:

- Material 3 Scaffold
- Top App Bar
- SearchBar integration
- Pull-to-refresh
- LazyColumn with LazyPagingItems
- Stable keys
- Handle loading, empty, success, and error states
- Responsive layout
- Accessibility support
- Complete runnable Compose code with explanations.

---

## Prompt: 19E_Search_ViewModel.md

Generate the complete `SearchViewModel.kt`.

Requirements:

1. Inject `SearchNewsUseCase`.
2. Manage query using `MutableStateFlow`.
3. Apply:
    - debounce(500)
    - distinctUntilChanged()
4. Expose paginated search results.
5. Handle refresh and retry.
6. Emit navigation side effects.
7. Use `cachedIn(viewModelScope)`.
8. Keep business logic out of the ViewModel.
9. Explain every method.
10. Provide complete runnable Kotlin code.

---

# UI Design Prompt

## Prompt: Search Screen UI/UX Specification

Design the complete Search experience for **NewsApp**.

### Include

1. High-fidelity wireframe
2. Search bar anatomy
3. Search interaction flow
4. Typing and debounce behavior
5. Loading shimmer specification
6. Search results layout
7. Empty query screen
8. Empty results screen
9. Error and retry screen
10. Infinite scrolling behavior
11. Pull-to-refresh interaction
12. Bookmark interaction
13. Tablet and phone responsive layouts
14. Dark mode appearance
15. Motion and animation guidelines
16. Accessibility requirements
17. Design token usage from the Design System

Do **not** generate Jetpack Compose code. Produce a complete UI/UX specification that will serve as the blueprint for implementing the Search Screen.