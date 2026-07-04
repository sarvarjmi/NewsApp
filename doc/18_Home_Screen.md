# NewsApp

# 18_Home_Screen.md

> **Phase:** Presentation Layer
>
> **Module:** Home Screen
>
> **Goal:** Design and implement a modern, production-ready Home Screen using Jetpack Compose, Material 3, Paging 3, StateFlow, MVVM, Clean Architecture, and responsive UI principles.

---

# Objective

The Home Screen is the primary entry point of the NewsApp.

Its responsibilities include:

- Display latest news
- Load paginated articles
- Pull-to-refresh
- Display loading state
- Display error state
- Display empty state
- Bookmark articles
- Navigate to article details
- Handle retry
- Preserve scroll position

The screen must remain fast, responsive, and scalable.

---

# Screen Responsibilities

The Home Screen should:

- Observe `HomeViewModel`
- Display paginated news
- Render Material 3 UI
- Show shimmer during loading
- Display retry on failure
- Refresh articles
- Navigate to Detail screen
- Toggle bookmark state

The Home Screen should NOT:

- Call Repository
- Perform business logic
- Call Retrofit
- Access Room
- Manage navigation directly

---

# Architecture

```text
HomeScreen

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

# User Journey

```text
App Launch

↓

Home Screen

↓

Loading

↓

Articles Loaded

↓

Scroll

↓

Load Next Page

↓

Open Detail

↓

Back

↓

Scroll Position Restored
```

---

# UI Structure

```text
Scaffold

│

├── TopAppBar

├── PullRefresh

│

└── LazyColumn

      │

      ├── Breaking News Header

      ├── News Cards

      ├── Loading Footer

      └── Retry Footer
```

---

# Home Layout

```text
-------------------------------------------------

Top App Bar

-------------------------------------------------

Search Shortcut (optional)

-------------------------------------------------

Breaking News

-------------------------------------------------

News Card

-------------------------------------------------

News Card

-------------------------------------------------

News Card

-------------------------------------------------

Loading Footer

-------------------------------------------------
```

---

# Components Used

The Home Screen uses reusable components.

```text
NewsCard

LoadingShimmer

ErrorView

EmptyState

RetryButton

BookmarkButton

PullRefreshIndicator
```

No custom UI should duplicate these components.

---

# Top App Bar

Contents:

- App Logo / Title
- Search Icon
- Refresh Icon (optional)

Behavior:

- Elevates while scrolling
- Material 3 LargeTopAppBar
- Supports dynamic color

---

# News Feed

The news feed uses:

```text
LazyColumn

↓

LazyPagingItems<Article>
```

Features:

- Infinite scrolling
- Stable item keys
- Smooth animations
- Efficient recomposition

---

# News Card Layout

Each article card contains:

```text
Image

↓

Source

↓

Title

↓

Description

↓

Published Time

↓

Bookmark Button
```

Optional:

- Read More indicator
- Category chip

---

# Image Loading

Recommended library:

Coil Compose

Behavior:

- Placeholder
- Error image
- Crossfade
- Content scale

Images should not distort.

---

# Bookmark Interaction

Flow:

```text
Bookmark Click

↓

UiEvent.ToggleBookmark

↓

ViewModel

↓

ToggleBookmarkUseCase

↓

Repository

↓

Room

↓

Updated UI
```

The interaction should feel instantaneous.

---

# Navigation

Clicking an article:

```text
NewsCard

↓

Navigation Event

↓

Detail Screen
```

The ViewModel emits a navigation side effect.

---

# Loading State

Initial load:

```text
LoadingShimmer

↓

News Cards
```

No blank screen should appear.

---

# Append Loading

During pagination:

```text
Bottom Loader
```

Only the footer loads.

Existing articles remain visible.

---

# Empty State

If no articles are returned:

```text
Illustration

↓

"No news available"

↓

Retry Button
```

---

# Error State

Display:

- Error illustration
- Retry button
- Friendly message

Examples:

- No internet
- Server unavailable
- Timeout

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

Reload
```

A Material 3 refresh indicator should be displayed.

---

# Scroll Restoration

Returning from Detail:

```text
Home

↓

Detail

↓

Back

↓

Same Scroll Position
```

State should be preserved automatically.

---

# Paging Load States

Handle:

- Refresh Loading
- Refresh Error
- Append Loading
- Append Error

Each state has its own UI representation.

---

# Animations

Recommended animations:

- Card fade-in
- Image crossfade
- Pull-to-refresh
- Snackbar
- Bookmark icon transition

Animations should be subtle.

---

# Accessibility

Requirements:

- Content descriptions for images
- TalkBack labels
- 48dp touch targets
- Dynamic font scaling
- Semantic grouping for cards

---

# Responsive Layout

Phone:

```text
1 Column
```

Tablet:

```text
2 Columns (future enhancement)
```

Foldables:

Adaptive layout supported through the design system.

---

# Performance Considerations

- Stable LazyColumn keys
- `rememberLazyListState()`
- Avoid nested scrolling
- Avoid unnecessary recompositions
- Cache Paging in ViewModel
- Async image loading
- Stateless components

---

# Error Recovery

Errors should support:

```text
Retry

↓

Reload Feed
```

No app restart required.

---

# Folder Structure

```text
presentation

home

├── HomeScreen.kt

├── HomeContent.kt

├── HomeTopBar.kt

├── HomeViewModel.kt

├── HomeUiState.kt

├── HomeEvent.kt

└── HomeSideEffect.kt

--------------------------------

presentation

components

NewsCard.kt

LoadingShimmer.kt

ErrorView.kt

RetryButton.kt
```

---

# UI State Flow

```text
ViewModel

↓

StateFlow<HomeUiState>

↓

collectAsStateWithLifecycle()

↓

Compose

↓

UI
```

---

# Interaction Flow

```text
User

↓

Click Article

↓

ViewModel

↓

Navigation Event

↓

Detail Screen
```

---

# Testing Strategy

The Home Screen should be tested for:

- Loading state
- Success state
- Empty state
- Error state
- Retry
- Pagination
- Pull-to-refresh
- Bookmark interaction
- Navigation
- Scroll restoration

Use:

- Compose UI Tests
- Fake ViewModel
- Paging test utilities

---

# Build Verification

After implementation:

- Home screen launches successfully
- Articles load using Paging 3
- Pull-to-refresh works
- Infinite scrolling works
- Bookmark toggling updates immediately
- Navigation to Detail works
- Scroll state is preserved
- Dark theme renders correctly
- Accessibility checks pass

---

# Files Created

None.

This document defines the Home Screen architecture and implementation strategy.

---

# What We Completed

✅ Designed the Home Screen architecture

✅ Planned paging integration

✅ Planned loading, empty, success, and error states

✅ Defined bookmark interaction

✅ Planned navigation flow

✅ Planned responsive layout

✅ Planned accessibility

---

# Next Documents

The Home Screen implementation will be divided into compile-safe milestones.

## 18A_Home_UI_State.md

Create:

- `HomeUiState.kt`
- UI state models
- Loading, Success, Empty, Error support

---

## 18B_Home_Events.md

Create:

- `HomeEvent.kt`
- User interactions
- Refresh, Retry, Bookmark, Open Article

---

## 18C_Home_Screen_Compose.md

Implement:

- `HomeScreen.kt`
- `Scaffold`
- `LazyColumn`
- Pull-to-refresh
- Paging integration

---

## 18D_Home_TopBar.md

Implement:

- `HomeTopBar.kt`
- Material 3 LargeTopAppBar
- Scroll behavior
- Search shortcut

---

## 18E_News_Card_Integration.md

Implement:

- Connect `NewsCard`
- Coil image loading
- Bookmark button
- Click handling

---

## 18F_Home_LoadStates.md

Implement:

- Shimmer loading
- Empty state
- Error state
- Append loading footer
- Retry footer

---

## 18G_Home_ViewModel_Integration.md

Implement:

- Connect `HomeViewModel`
- Paging Flow
- Refresh
- Retry
- Navigation side effects

---

## 18H_Home_Screen_Testing.md

Implement:

- Compose UI tests
- Paging tests
- Navigation tests
- Accessibility tests
- Screenshot tests

---

# Prompt for Code Generation

## Prompt: 18A_Home_UI_State.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `HomeUiState.kt`.

### Requirements

1. Create an immutable UI state model.
2. Support:
    - Loading
    - Success
    - Empty
    - Error
3. Include paging-related state where appropriate.
4. Add KDoc documentation.
5. Explain every property.
6. Provide complete runnable Kotlin code.

---

## Prompt: 18C_Home_Screen_Compose.md

Generate the complete `HomeScreen.kt`.

Requirements:

- Material 3 `Scaffold`
- LargeTopAppBar
- Pull-to-refresh
- `LazyColumn`
- `LazyPagingItems`
- Stable item keys
- `collectAsStateWithLifecycle()`
- Handle loading, empty, success, and error states
- Material 3 design
- Responsive layout
- Accessibility support
- Complete runnable Compose code with explanations.

---

## Prompt: 18G_Home_ViewModel_Integration.md

Generate the complete Home Screen integration.

Requirements:

1. Connect `HomeViewModel`.
2. Observe `StateFlow`.
3. Collect paging data.
4. Handle:
    - Refresh
    - Retry
    - Bookmark click
    - Article click
5. Emit navigation side effects.
6. Keep business logic out of the Composable.
7. Explain every interaction.
8. Provide complete runnable Kotlin code.

---

# UI Design Prompt

## Prompt: Home Screen UI/UX Specification

Design the complete Home Screen experience for **NewsApp**.

### Include

1. High-fidelity wireframe
2. Material 3 layout specification
3. Top app bar behavior
4. News feed layout
5. News card anatomy
6. Pull-to-refresh interaction
7. Infinite scrolling behavior
8. Loading shimmer specification
9. Empty state design
10. Offline/error state design
11. Bookmark interaction
12. Tablet and phone responsive layouts
13. Dark mode appearance
14. Motion and animation guidelines
15. Accessibility requirements
16. Spacing, typography, and color token usage from the Design System

Do **not** generate Jetpack Compose code. Produce a detailed UI/UX specification that will be used as the blueprint for implementing the Home Screen.