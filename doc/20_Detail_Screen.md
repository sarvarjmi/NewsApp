# NewsApp

# 20_Detail_Screen.md

> **Phase:** Presentation Layer
>
> **Module:** Article Detail Screen
>
> **Goal:** Design and implement a modern, production-ready Article Detail Screen using Jetpack Compose, Material 3, Clean Architecture, MVVM, StateFlow, Hilt, and responsive UI principles.

---

# Objective

The Detail Screen displays the complete information of a selected news article.

Unlike the Home and Search screens, this screen focuses on content consumption rather than browsing.

The Detail Screen should support:

- Full article information
- Hero image
- Source information
- Reading time estimation
- Bookmark article
- Share article
- Open original article
- Pull-to-refresh (optional)
- Offline bookmark support
- Responsive layouts
- Accessibility
- Dark Mode

This screen should remain lightweight and delegate all business logic to the ViewModel and UseCases.

---

# Responsibilities

The Detail Screen is responsible for:

- Display article information
- Observe bookmark state
- Display loading state
- Display error state
- Allow bookmarking
- Allow sharing
- Open article in browser
- Navigate back

The Detail Screen should NOT:

- Call Retrofit
- Call Repository
- Perform business logic
- Handle database operations
- Manage navigation directly

---

# Architecture

```text
DetailScreen

↓

DetailViewModel

↓

ObserveArticleUseCase

ToggleBookmarkUseCase

↓

Repository

↓

Room + API

↓

Compose UI
```

---

# User Journey

```text
Home

↓

Tap News Card

↓

Detail Screen

↓

Read Article

↓

Bookmark

↓

Share

↓

Open Browser

↓

Back

↓

Home Restored
```

---

# Screen Layout

```text
Scaffold

│

├── Large Top App Bar

│

├── Hero Image

│

├── Source

├── Published Date

├── Reading Time

│

├── Title

│

├── Description

│

├── Content

│

├── Action Buttons

│

└── Bottom Spacer
```

---

# Layout Wireframe

```text
------------------------------------------------

← Back         Bookmark     Share

------------------------------------------------

Large Hero Image

------------------------------------------------

CNN • 2 hours ago • 5 min read

------------------------------------------------

Article Title

------------------------------------------------

Article Description

------------------------------------------------

Full Content

------------------------------------------------

Open Original Article Button

------------------------------------------------
```

---

# Components Used

The screen reuses Design System components.

```text
NetworkImage

BookmarkButton

PrimaryButton

ErrorView

LoadingShimmer

RetryButton

TopAppBar
```

---

# Hero Image

Requirements

- Full width
- 16:9 ratio
- Coil image loading
- Crossfade animation
- Placeholder
- Error image

---

# Metadata Section

Display

- News Source
- Published Time
- Reading Time
- Category (future)

Example

```
BBC News • 3 hours ago • 6 min read
```

---

# Reading Time

Estimated using

```text
Word Count

↓

÷ 200

↓

Minutes
```

Example

```
6 min read
```

The calculation belongs in the Domain layer or a helper utility.

---

# Title

Material 3

Typography

```
HeadlineLarge
```

Maximum readability

---

# Description

Typography

```
BodyLarge
```

Displayed only when available.

---

# Content

Display

- Full article content
- Paragraph spacing
- Maximum readability
- Text selection enabled (optional)

---

# Bookmark Flow

```text
Bookmark Button

↓

DetailEvent.ToggleBookmark

↓

DetailViewModel

↓

ToggleBookmarkUseCase

↓

Repository

↓

Room

↓

Updated UI
```

---

# Share Flow

```text
Share Button

↓

DetailEvent.Share

↓

ViewModel

↓

Side Effect

↓

Android Share Sheet
```

ViewModel emits only the event.

Compose launches the Intent.

---

# Open Original Article

Flow

```text
Read Full Article

↓

DetailEvent.OpenBrowser

↓

Side Effect

↓

Chrome Custom Tabs

↓

Browser
```

Preferred:

Chrome Custom Tabs

Fallback:

Default Browser

---

# Back Navigation

```text
Back

↓

Previous Screen

↓

Scroll Position Restored
```

---

# Loading State

Display

- Hero image shimmer
- Text shimmer
- Button shimmer

No blank screen.

---

# Error State

Display

- Illustration
- Friendly message
- Retry button

Possible errors

- Missing article
- Network failure
- Invalid URL

---

# Empty State

Rare case

If article data is unavailable

Display

```
Article unavailable
```

---

# Action Buttons

Primary

- Bookmark
- Share
- Open Original

Future

- Text Size
- Save Offline
- Listen to Article

---

# Accessibility

Requirements

- Image content description
- Bookmark state announcement
- Button labels
- Dynamic font support
- Proper semantic headings

---

# Responsive Layout

Phone

```text
Single Column
```

Tablet

```text
Two Pane

Image | Content
```

Landscape

Adaptive spacing

---

# Dark Theme

Requirements

- Comfortable reading
- High contrast
- Proper image overlay
- Accessible icon colors

---

# Performance

- rememberScrollState()
- Async image loading
- Stateless composables
- Derived state where appropriate
- Stable parameters
- Avoid unnecessary recomposition

---

# Folder Structure

```text
presentation

detail

├── DetailScreen.kt

├── DetailContent.kt

├── DetailTopBar.kt

├── DetailViewModel.kt

├── DetailUiState.kt

├── DetailEvent.kt

├── DetailSideEffect.kt

└── components

        MetadataRow.kt

        ReadingTimeChip.kt
```

---

# UI State

```text
DetailUiState

↓

Loading

↓

Success

↓

Error
```

---

# Interaction Flow

```text
User

↓

Bookmark

↓

ViewModel

↓

Repository

↓

Room

↓

Updated UI
```

---

# State Restoration

Configuration changes should preserve

- Article
- Scroll position
- Bookmark state

---

# Testing Strategy

Test

- Loading
- Success
- Error
- Bookmark
- Share
- Browser launch
- Navigation
- Dark Theme
- Accessibility

Tools

- Compose UI Test
- Fake ViewModel
- MockK
- Turbine
- Screenshot Testing

---

# Build Verification

After implementation

- Detail screen opens correctly
- Hero image loads
- Bookmark updates instantly
- Share opens Android share sheet
- Browser launches correctly
- Back navigation restores previous screen
- Dark mode works
- Accessibility passes

---

# Files Created

None.

This document defines the Detail Screen architecture and implementation strategy.

---

# What We Completed

✅ Designed the Detail Screen architecture

✅ Planned article presentation

✅ Planned bookmark interaction

✅ Planned share workflow

✅ Planned browser integration

✅ Planned accessibility

✅ Planned responsive layouts

---

# Next Documents

The Detail Screen implementation will be divided into compile-safe milestones.

## 20A_Detail_UI_State.md

Create

- `DetailUiState.kt`
- Loading
- Success
- Error states

---

## 20B_Detail_Events.md

Create

- `DetailEvent.kt`
- ToggleBookmark
- Share
- OpenBrowser
- Retry
- BackPressed

---

## 20C_Detail_Screen_Compose.md

Implement

- `DetailScreen.kt`
- Scaffold
- Scrollable content
- Hero image
- Metadata
- Buttons

---

## 20D_Detail_ViewModel.md

Implement

- Observe article
- Bookmark state
- Share side effects
- Browser side effects
- Error handling

---

## 20E_Detail_Actions.md

Implement

- Bookmark
- Share
- Chrome Custom Tabs
- External browser fallback

---

## 20F_Detail_Animations.md

Implement

- Hero image transition
- Scroll behavior
- Loading shimmer
- Bookmark animation

---

## 20G_Detail_Testing.md

Implement

- ViewModel tests
- Compose UI tests
- Accessibility tests
- Screenshot tests

---

# Prompt for Code Generation

## Prompt: 20A_Detail_UI_State.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `DetailUiState.kt`.

### Requirements

1. Create an immutable UI state model.
2. Include:
    - Loading
    - Success
    - Error
    - Bookmark state
    - Reading time
3. Add KDoc documentation.
4. Explain every property.
5. Provide complete runnable Kotlin code.

---

## Prompt: 20C_Detail_Screen_Compose.md

Generate the complete `DetailScreen.kt`.

Requirements:

- Material 3 Scaffold
- LargeTopAppBar
- Hero image with Coil
- Scrollable article content
- Metadata row
- Bookmark button
- Share button
- "Read Full Article" button
- Responsive layout
- Dark mode support
- Accessibility semantics
- Preview composables
- Complete runnable Jetpack Compose code with explanations.

---

## Prompt: 20D_Detail_ViewModel.md

Generate the complete `DetailViewModel.kt`.

Requirements:

1. Inject:
    - ObserveArticleUseCase
    - ToggleBookmarkUseCase
2. Expose `StateFlow<DetailUiState>`.
3. Handle:
    - Bookmark
    - Share
    - Browser
    - Retry
4. Emit one-time side effects using `SharedFlow`.
5. Keep business logic inside UseCases.
6. Add KDoc documentation.
7. Explain every method.
8. Provide complete runnable Kotlin code.

---

## Prompt: 20E_Detail_Actions.md

Generate the complete action handling layer.

Requirements:

- Chrome Custom Tabs integration
- Android Share Sheet integration
- Bookmark animation trigger
- Browser fallback
- Explain every implementation.
- Provide complete runnable Kotlin and Compose code.

---

# UI Design Prompt

## Prompt: Article Detail Screen UI/UX Specification

Design the complete **Article Detail** experience for **NewsApp**.

### Include

1. High-fidelity wireframe
2. Hero image behavior
3. Metadata section (source, date, reading time)
4. Typography hierarchy
5. Article content layout
6. Bookmark interaction
7. Share interaction
8. Read Original Article CTA
9. Loading shimmer specification
10. Error and unavailable article screens
11. Tablet two-pane layout
12. Dark mode appearance
13. Motion and animation guidelines
14. Accessibility requirements
15. Material 3 spacing, typography, color, and elevation usage from the Design System

Do **not** generate Jetpack Compose code. Produce a comprehensive UI/UX specification that will serve as the implementation blueprint for the Article Detail Screen.