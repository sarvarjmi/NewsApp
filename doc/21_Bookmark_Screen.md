# NewsApp

> **Phase:** Presentation Layer
>
> **Module:** Bookmark Screen
>
> **Goal:** Design and implement a modern, offline-first Bookmark Screen using Jetpack Compose, Material 3, Room Database, StateFlow, Kotlin Flow, MVVM, and Clean Architecture.

---

# Objective

The Bookmark Screen allows users to view, manage, and read articles that have been saved locally.

Unlike the Home and Search screens, this screen displays **only local data** from the Room database.

The Bookmark Screen should provide:

- Offline article browsing
- Bookmark list
- Remove bookmark
- Undo delete
- Pull-to-refresh (refresh local state)
- Empty bookmark state
- Error handling
- Navigation to Detail Screen
- Search within bookmarks (future)
- Sort & Filter (future)

The screen should continue working without an internet connection.

---

# Responsibilities

The Bookmark Screen is responsible for:

- Display bookmarked articles
- Observe Room Flow
- Remove bookmarks
- Undo removal
- Navigate to Detail
- Display loading, success, empty, and error states

The Bookmark Screen should NOT:

- Access Retrofit
- Perform business logic
- Execute SQL directly
- Manage navigation directly

---

# Architecture

```text
BookmarkScreen

↓

BookmarkViewModel

↓

ObserveBookmarksUseCase

↓

Repository

↓

LocalDataSource

↓

BookmarkDao

↓

Room Database

↓

Compose UI
```

---

# User Journey

```text
Bottom Navigation

↓

Bookmarks

↓

Saved Articles

↓

Open Detail

↓

Back

↓

Bookmarks Restored
```

---

# Screen Layout

```text
Scaffold

│

├── Top App Bar

│

├── Pull Refresh

│

└── LazyColumn

      │

      ├── Bookmark Card

      ├── Bookmark Card

      ├── Bookmark Card

      └── Bottom Spacer
```

---

# Layout Wireframe

```text
------------------------------------------------

Bookmarks

------------------------------------------------

Saved Articles (12)

------------------------------------------------

News Card

------------------------------------------------

News Card

------------------------------------------------

News Card

------------------------------------------------
```

---

# Components Used

Reusable Design System components

```text
NewsCard

BookmarkButton

LoadingShimmer

ErrorView

EmptyState

RetryButton

ConfirmationDialog

Snackbar
```

---

# Top App Bar

Contains

- Screen Title
- Total Bookmark Count
- Search Icon (future)
- Sort Icon (future)

Material 3 MediumTopAppBar.

---

# Bookmark List

Bookmarks are displayed using

```text
LazyColumn
```

Requirements

- Stable keys
- Smooth scrolling
- Efficient recomposition
- State restoration

---

# Bookmark Card

Each card contains

- Thumbnail
- Source
- Title
- Description
- Saved Date
- Bookmark Button

Clicking opens Detail Screen.

---

# Bookmark Removal

Flow

```text
Bookmark Button

↓

BookmarkEvent.Remove

↓

ViewModel

↓

RemoveBookmarkUseCase

↓

Repository

↓

Room

↓

Updated Flow

↓

Compose
```

---

# Undo Delete

Immediately after deletion

```text
Snackbar

↓

UNDO

↓

Restore Bookmark
```

Snackbar should disappear automatically.

---

# Empty State

If no bookmarks exist

Display

```text
Illustration

↓

"No bookmarks yet"

↓

Browse News Button
```

The button navigates to the Home Screen.

---

# Loading State

Display

- Loading shimmer
- Placeholder cards

Only during the initial load.

---

# Error State

Display

- Error illustration
- Friendly message
- Retry button

Possible issues

- Database corruption
- Migration failure
- Unexpected Room error

---

# Pull-to-Refresh

Refresh reloads the latest data from Room.

```text
User Pulls

↓

Reload Flow

↓

Updated UI
```

No network request is performed.

---

# Offline Experience

The Bookmark Screen is fully functional without internet.

```text
Room

↓

Flow<List<Article>>

↓

ViewModel

↓

Compose
```

---

# Sorting (Future)

Support

- Recently Saved
- Oldest Saved
- Alphabetical
- Source

Architecture should allow easy expansion.

---

# Search (Future)

Support

```text
Search Bookmarks
```

using local database queries.

---

# Accessibility

Requirements

- TalkBack support
- Bookmark state announcements
- 48dp touch targets
- Dynamic font scaling
- Semantic grouping

---

# Responsive Layout

Phone

```text
Single Column
```

Tablet

```text
Grid Layout (future)
```

Landscape

Adaptive spacing.

---

# Dark Theme

Requirements

- Proper contrast
- Comfortable reading
- Material 3 compliance

---

# Performance

- Observe Flow
- Immutable UI state
- Stable LazyColumn keys
- Stateless composables
- Remember list state
- Avoid unnecessary recompositions

---

# Folder Structure

```text
presentation

bookmark

├── BookmarkScreen.kt

├── BookmarkContent.kt

├── BookmarkTopBar.kt

├── BookmarkViewModel.kt

├── BookmarkUiState.kt

├── BookmarkEvent.kt

├── BookmarkSideEffect.kt

└── BookmarkSnackbar.kt
```

---

# UI State

```text
Loading

↓

Bookmarks Loaded

↓

Empty

↓

Error
```

---

# Interaction Flow

```text
User

↓

Remove Bookmark

↓

ViewModel

↓

Repository

↓

Room

↓

Updated Flow
```

---

# State Restoration

Configuration changes should preserve

- Scroll position
- Snackbar state (where appropriate)
- Current list

---

# Testing Strategy

Verify

- Loading state
- Empty state
- Bookmarks displayed
- Remove bookmark
- Undo delete
- Detail navigation
- Accessibility
- Dark theme

Use

- Compose UI Tests
- Fake Repository
- Turbine
- MockK
- Screenshot Tests

---

# Build Verification

After implementation

- Bookmarks display correctly
- Updates occur automatically
- Remove bookmark works
- Undo restores bookmark
- Navigation works
- Offline mode works
- Dark mode renders correctly
- Accessibility passes

---

# Files Created

None.

This document defines the Bookmark Screen architecture and implementation strategy.

---

# What We Completed

✅ Designed the Bookmark Screen architecture

✅ Planned Room integration

✅ Planned offline-first behavior

✅ Planned undo delete workflow

✅ Planned responsive layout

✅ Planned accessibility

---

# Next Documents

The Bookmark implementation will be divided into compile-safe milestones.

## 21A_Bookmark_UI_State.md

Create

- `BookmarkUiState.kt`
- Loading
- Success
- Empty
- Error

---

## 21B_Bookmark_Events.md

Create

- `BookmarkEvent.kt`
- Remove
- Undo
- Retry
- OpenArticle

---

## 21C_Bookmark_Screen_Compose.md

Implement

- `BookmarkScreen.kt`
- Scaffold
- LazyColumn
- Pull-to-refresh
- Snackbar

---

## 21D_Bookmark_ViewModel.md

Implement

- Observe bookmarks
- Remove bookmark
- Undo support
- Navigation events
- StateFlow integration

---

## 21E_Bookmark_Actions.md

Implement

- Delete confirmation (optional)
- Snackbar undo
- Detail navigation

---

## 21F_Bookmark_Testing.md

Implement

- ViewModel tests
- DAO integration tests
- Compose UI tests
- Accessibility tests

---

# Prompt for Code Generation

## Prompt: 21A_Bookmark_UI_State.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `BookmarkUiState.kt`.

### Requirements

1. Create an immutable UI state model.
2. Support:
    - Loading
    - Success
    - Empty
    - Error
3. Include:
    - List of bookmarked articles
    - Bookmark count
    - Refresh state
4. Use immutable data classes.
5. Add KDoc documentation.
6. Explain every property.
7. Provide complete runnable Kotlin code.

---

## Prompt: 21C_Bookmark_Screen_Compose.md

Generate the complete `BookmarkScreen.kt`.

Requirements:

- Material 3 Scaffold
- MediumTopAppBar
- LazyColumn
- Pull-to-refresh
- NewsCard integration
- Snackbar for Undo
- Loading shimmer
- Empty state
- Error state
- Responsive layout
- Dark mode support
- Accessibility semantics
- Preview composables
- Complete runnable Jetpack Compose code with explanations.

---

## Prompt: 21D_Bookmark_ViewModel.md

Generate the complete `BookmarkViewModel.kt`.

Requirements:

1. Inject:
    - ObserveBookmarksUseCase
    - RemoveBookmarkUseCase
    - AddBookmarkUseCase
2. Expose `StateFlow<BookmarkUiState>`.
3. Observe bookmarks using Flow.
4. Implement:
    - Remove bookmark
    - Undo removal
    - Refresh
    - Navigation side effects
5. Emit one-time events using `SharedFlow`.
6. Keep business logic inside UseCases.
7. Add KDoc documentation.
8. Explain every method.
9. Provide complete runnable Kotlin code.

---

## Prompt: 21E_Bookmark_Actions.md

Generate the complete bookmark action handling.

Requirements:

- Snackbar undo
- Optional delete confirmation dialog
- Navigation to Detail
- Material 3 implementation
- Accessibility support
- Explain every interaction.
- Provide complete runnable Kotlin and Compose code.

---

# UI Design Prompt

## Prompt: Bookmark Screen UI/UX Specification

Design the complete **Bookmark Screen** experience for **NewsApp**.

### Include

1. High-fidelity wireframe
2. Top App Bar specification
3. Bookmark list layout
4. Bookmark card anatomy
5. Empty bookmark screen
6. Loading shimmer specification
7. Error state design
8. Snackbar undo interaction
9. Delete confirmation flow (optional)
10. Pull-to-refresh interaction
11. Navigation to Detail
12. Tablet and phone responsive layouts
13. Dark mode appearance
14. Motion and animation guidelines
15. Accessibility requirements
16. Material 3 spacing, typography, elevation, and color token usage

Do **not** generate Jetpack Compose code. Produce a complete UI/UX specification that will serve as the implementation blueprint for the Bookmark Screen.

---

# Recommended Next Phase

After implementing all four primary screens:

- ✅ Home
- ✅ Search
- ✅ Detail
- ✅ Bookmarks

The next logical document should be:

## **22_Reusable_UI_Components.md**

This document should build the shared Compose component library used across every screen, including:

- NewsCard
- AsyncImage
- BookmarkButton
- SearchBar
- LoadingShimmer
- ErrorView
- EmptyState
- RetryButton
- SectionHeader
- PullRefreshContainer
- AppTopBar
- NetworkStatusBanner
- ChipGroup
- MetadataRow

These reusable components will eliminate duplicate UI code and ensure complete consistency across the application.