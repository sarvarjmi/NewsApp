# NewsApp

# 16_Navigation.md

> **Phase:** Presentation Layer
>
> **Module:** Navigation Architecture
>
> **Goal:** Design a scalable, type-safe, lifecycle-aware navigation architecture using Jetpack Compose Navigation that supports deep links, bottom navigation, state restoration, and future feature expansion.

---

# Objective

Navigation is one of the most important parts of any Android application.

Instead of allowing Composables to navigate directly using hardcoded routes, we will create a centralized navigation architecture.

By the end of this phase, NewsApp will have:

- Navigation Graph
- Type-safe Routes
- Bottom Navigation
- Nested Navigation Graphs
- Deep Linking
- Safe Argument Passing
- Back Stack Management
- State Restoration
- Navigation Testing Strategy

The navigation system will support all assignment requirements including article detail navigation and deep linking. :contentReference[oaicite:0]{index=0}

---

# Why Navigation Architecture?

Without proper navigation:

```text
Home

↓

navigate("detail?id=1")

↓

Search

↓

navigate("home")
```

Problems

- Hardcoded routes
- Difficult refactoring
- Duplicate navigation logic
- Poor testing
- Broken back stack
- Difficult deep linking

---

With Navigation Architecture

```text
Compose

↓

NavController

↓

Navigation Graph

↓

Destination

↓

Screen
```

Benefits

- Centralized navigation
- Type safety
- Better testing
- Easier maintenance
- Better scalability

---

# Navigation Architecture

```text
MainActivity

↓

NewsNavHost

↓

Root Graph

↓

Bottom Navigation Graph

↓

Feature Graph

↓

Destination
```

---

# Navigation Flow

```text
Application

↓

MainActivity

↓

NewsNavHost

↓

Root Navigation

↓

Bottom Navigation

↓

Home

↓

Detail

↓

Back
```

---

# Navigation Components

The project consists of:

```text
Routes

↓

Destination

↓

NavGraph

↓

Bottom Navigation

↓

Deep Links

↓

NavController
```

---

# Screens

The application contains four primary screens.

```text
Home

Search

Bookmarks

Article Detail
```

Future:

```text
Settings

About

Notifications
```

---

# Navigation Graph

```text
Root

│

├── Home

├── Search

├── Bookmarks

└── Detail
```

---

# Root Graph

```text
Root Graph

↓

Bottom Navigation Graph

↓

Home

Search

Bookmarks

↓

Article Detail
```

Detail is intentionally outside Bottom Navigation.

---

# Bottom Navigation

```text
Home

Search

Bookmarks
```

The bottom bar remains visible only on top-level destinations.

Hidden on:

```text
Article Detail

WebView
```

---

# Navigation Tree

```text
NewsApp

│

├── Home

│      │

│      └── Detail

│

├── Search

│      │

│      └── Detail

│

└── Bookmarks

       │

       └── Detail
```

Every feature can navigate to the Detail screen.

---

# Navigation Packages

```text
presentation

navigation

NavGraph.kt

Routes.kt

Destination.kt

BottomBar.kt

NavigationExtensions.kt

DeepLinks.kt
```

---

# Route Strategy

Never use

```kotlin
"detail?id=$id"
```

Instead use typed route definitions.

Example

```text
Home

Search

Bookmark

Detail
```

Routes should be centralized.

---

# Destination Model

Each destination contains:

- Route
- Arguments
- Deep Link
- Title
- Icon

No screen defines its own navigation strings.

---

# Safe Argument Passing

The Detail screen requires:

```text
Article URL

or

Article ID
```

Arguments should be:

- Encoded safely
- Restorable
- Nullable only when appropriate

---

# Deep Linking

The assignment requires deep links.

Flow:

```text
Browser

↓

Article URL

↓

Deep Link

↓

Navigation Graph

↓

Detail Screen
```

Future support:

- Notification deep links
- Shared links
- App links

---

# Back Stack Strategy

Expected behavior:

```text
Home

↓

Detail

↓

Back

↓

Home
```

Search:

```text
Search

↓

Detail

↓

Back

↓

Search Results
```

Bookmarks:

```text
Bookmarks

↓

Detail

↓

Back

↓

Bookmarks
```

State should be preserved.

---

# State Restoration

Bottom Navigation should:

- Restore scroll position
- Restore selected tab
- Preserve ViewModel state
- Avoid unnecessary recomposition

---

# Navigation Events

Navigation is triggered through ViewModels.

Flow:

```text
User Click

↓

UiEvent

↓

ViewModel

↓

NavigationEvent

↓

Compose

↓

NavController
```

The ViewModel should never hold a NavController reference.

---

# Navigation Side Effects

Examples:

- Open Detail
- Open Browser
- Share
- Navigate Back

These are emitted as one-time events.

---

# Navigation with Paging

```text
Home

↓

Paged Article

↓

Click

↓

Detail

↓

Back

↓

Paged List Restored
```

Paging state should remain intact.

---

# WebView Navigation

Flow:

```text
Detail

↓

Open Original Article

↓

WebView

↓

Back

↓

Detail
```

---

# Folder Structure

```text
presentation

navigation

├── NavGraph.kt

├── Routes.kt

├── Destination.kt

├── BottomBar.kt

├── NavigationEvent.kt

├── NavigationExtensions.kt

└── DeepLinks.kt
```

---

# Navigation Diagram

```text
MainActivity

↓

NavHost

↓

RootGraph

↓

BottomGraph

↓

Home

↓

Detail
```

---

# Navigation Best Practices

- One NavHost
- One NavController
- Centralized routes
- No hardcoded strings
- No NavController inside ViewModels
- Feature-based destinations
- Stable arguments
- State restoration enabled

---

# Large Screen Navigation

Future tablet support:

```text
List

|

Detail
```

Two-pane layout can reuse the same navigation architecture.

---

# Testing Strategy

Navigation tests should verify:

- Home → Detail
- Search → Detail
- Bookmark → Detail
- Deep links
- Back stack
- State restoration
- Bottom Navigation

Compose Navigation testing APIs will be used.

---

# Planned Kotlin Files

```text
presentation/navigation/

Routes.kt

Destination.kt

NavGraph.kt

BottomBar.kt

NavigationEvent.kt

NavigationExtensions.kt

DeepLinks.kt
```

---

# Files Created

None.

This document defines the navigation architecture.

---

# What We Completed

✅ Designed the navigation architecture

✅ Planned Bottom Navigation

✅ Designed navigation graph

✅ Planned deep links

✅ Planned safe argument passing

✅ Planned state restoration

✅ Defined navigation testing strategy

---

# Next Documents

The navigation implementation will be divided into compile-safe milestones.

## 16A_Routes.md

Create:

- `Routes.kt`
- Route constants
- Typed destinations
- Navigation arguments

---

## 16B_Destination_Model.md

Implement:

- `Destination.kt`
- Screen metadata
- Icons
- Labels
- Bottom navigation configuration

---

## 16C_NavGraph.md

Implement:

- `NewsNavGraph.kt`
- Root graph
- Nested graphs
- Detail navigation

---

## 16D_BottomNavigation.md

Implement:

- Bottom bar
- Selected state
- State restoration
- Material 3 integration

---

## 16E_Navigation_Events.md

Implement:

- Navigation events
- SharedFlow navigation
- ViewModel integration

---

## 16F_DeepLinks.md

Implement:

- Deep links
- Article URL support
- Notification support
- Browser integration

---

## 16G_BackStack_Strategy.md

Implement:

- Back navigation
- popUpTo
- launchSingleTop
- restoreState

---

## 16H_Navigation_Testing.md

Implement:

- Compose navigation tests
- Deep link tests
- Back stack verification
- Bottom navigation tests

---

# Prompt for Code Generation

## Prompt: 16A_Routes.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `Routes.kt`.

### Requirements

1. Create a centralized route definition.
2. Define routes for:
    - Home
    - Search
    - Bookmarks
    - Detail
    - WebView (optional)
3. Support typed route arguments.
4. Avoid hardcoded strings throughout the application.
5. Add KDoc documentation.
6. Explain every route.
7. Provide complete runnable Kotlin code.

---

## Prompt: 16B_Destination_Model.md

Generate the complete `Destination.kt`.

Requirements:

- Sealed interface or sealed class.
- Material 3 icons.
- Title resource support.
- Route information.
- Bottom navigation metadata.
- Deep link metadata.
- Complete runnable code with explanations.

---

## Prompt: 16C_NavGraph.md

Generate the complete `NewsNavGraph.kt`.

Requirements:

- Single `NavHost`.
- Bottom navigation graph.
- Nested navigation.
- Detail navigation.
- Safe argument handling.
- Deep link integration.
- Material 3 support.
- Explain every composable destination.
- Provide complete runnable Kotlin code.

---

## Prompt: 16D_BottomNavigation.md

Generate the complete Bottom Navigation implementation.

Requirements:

- Material 3 NavigationBar.
- Home, Search, Bookmarks tabs.
- Selected state.
- Restore state.
- Hide on Detail/WebView.
- Explain every composable.
- Provide complete runnable Compose code.

---

# UI Design Prompt

## Prompt: Navigation UX & Information Architecture

Design the complete navigation experience for **NewsApp**.

Include:

- Information architecture diagram
- User journey (Home → Detail, Search → Detail, Bookmarks → Detail)
- Bottom navigation specifications
- Top app bar behavior
- Screen transition recommendations
- Shared element transition opportunities
- Deep link entry flow
- Back navigation behavior
- Tablet two-pane navigation
- Accessibility (TalkBack focus order, gesture navigation)
- Material 3 motion and navigation guidelines

Do **not** generate Jetpack Compose code. Produce a navigation UX specification and wireframe-level blueprint that will guide the implementation documents.