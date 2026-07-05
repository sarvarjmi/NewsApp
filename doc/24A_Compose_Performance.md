# Compose Performance Optimization Guide

This document outlines the performance optimizations applied to the **NewsApp** project to ensure smooth 60 FPS scrolling and efficient recompositions.

---

## 1. Stable Models

Compose relies on the "stability" of parameters to skip recompositions. If a parameter is stable, Compose can tell if it has changed.

### Optimization
We ensure our UI models are stable. While Kotlin data classes are usually inferred as stable, we can be explicit using `@Immutable` or `@Stable` annotations (though we avoid adding Compose dependencies to the Domain layer).

In the Presentation layer, we use UI-specific models if needed:
```kotlin
@Immutable
data class ArticleUiModel(
    val title: String,
    val imageUrl: String,
    // ... other properties
)
```

---

## 2. use of `remember` and `rememberSaveable`

### `remember`
Used to store expensive calculations or state objects across recompositions.
- **Example**: `val scrollState = rememberLazyListState()`

### `rememberSaveable`
Used for state that must survive configuration changes (rotation) or process death.
- **Example**: `var searchQuery by rememberSaveable { mutableStateOf("") }`

---

## 3. `derivedStateOf` for Scroll Tracking

Tracking scroll position directly can cause excessive recompositions because the scroll offset changes on every frame. `derivedStateOf` allows us to "buffer" this state and only trigger recompositions when a specific condition changes.

### Optimization
In `HomeScreen.kt`, we use it to show/hide a "Scroll to Top" button:
```kotlin
val showScrollToTop by remember {
    derivedStateOf {
        lazyListState.firstVisibleItemIndex > 0
    }
}
```

---

## 4. `LazyColumn` and `LazyRow` Optimization

### Item Keys
Always provide a unique `key` for items. This helps Compose identify which items moved, stayed, or were removed, preventing full list re-renders.

### Content Types
Providing a `contentType` helps the list reuse item compositions more effectively.

---

## 5. Prevent Unnecessary Recompositions

- **Lambda Stability**: Pass method references or use `remember` for lambdas if they capture changing state.
- **State Hoisting**: Keep state as low as possible in the tree if it's only needed by a specific branch.

---

## Applied Implementation

The following files have been updated with these optimizations:
- `HomeScreen.kt`: Added `derivedStateOf` for FAB, added keys to `LazyRow`.
- `NewsPagingList`: Refined item keys and added content types.
- `CategoryChip`: Optimized for stability.
