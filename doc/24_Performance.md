# NewsApp

> **Phase:** Cross-Cutting Architecture
>
> **Module:** Performance Optimization
>
> **Goal:** Build a fast, scalable, memory-efficient, battery-friendly NewsApp by applying Android and Jetpack Compose performance best practices across every layer of the application.

---

# Objective

A production Android application should not only work correctly—it should also be:

- Fast
- Responsive
- Memory efficient
- Battery friendly
- Smooth at 60 FPS
- Optimized for low-end devices
- Optimized for tablets
- Optimized for slow networks

Performance optimization must be part of the architecture rather than added after development.

---

# Performance Architecture

Performance optimization is applied throughout every layer.

```text
UI

↓

Compose Optimization

↓

ViewModel

↓

StateFlow

↓

Repository

↓

Paging

↓

Retrofit

↓

Room

↓

Storage
```

Every layer contributes to overall performance.

---

# Performance Goals

Target metrics

| Metric | Target |
|---------|---------|
|Cold Startup|< 2 sec|
|Warm Startup|< 1 sec|
|Screen Navigation|< 300 ms|
|Frame Rate|60 FPS|
|Image Loading|< 500 ms|
|Memory Usage|Minimal|
|Network Requests|Optimized|
|APK Size|Reduced|

---

# Optimization Areas

The application will optimize:

- Compose UI
- Recomposition
- Paging
- Images
- Networking
- Database
- Memory
- Battery
- Startup
- Animations
- Build Performance

---

# Compose Optimization

Compose is efficient, but poor architecture can trigger unnecessary recompositions.

Best practices

- Stateless composables
- Stable models
- Immutable UI state
- remember()
- rememberSaveable()
- derivedStateOf()
- key()
- collectAsStateWithLifecycle()

---

# Recomposition Strategy

Avoid

```text
Entire Screen

↓

Recompose
```

Instead

```text
Changed Component

↓

Recompose
```

Only affected UI should update.

---

# Stable Models

Use immutable data classes.

Good

```kotlin
data class Article(...)
```

Avoid mutable UI models.

---

# State Hoisting

State belongs to

```text
ViewModel

↓

StateFlow

↓

Composable
```

Never store business state inside composables.

---

# remember()

Use for

- Scroll state
- LazyListState
- Temporary UI state

---

# rememberSaveable()

Use for

- Search Query
- Selected Tab
- User Input

Configuration changes should not lose state.

---

# derivedStateOf()

Use for expensive calculations.

Example

```text
Scroll Position

↓

Show FAB
```

Avoid recalculating on every recomposition.

---

# LazyColumn Optimization

Always

- Stable keys
- Item content types
- Paging integration

Avoid nested scrolling.

---

# Paging Performance

Best practices

- pageSize = 20
- prefetchDistance = 5
- cachedIn(viewModelScope)
- Enable placeholders only when necessary

Never request unnecessary pages.

---

# Image Loading

Use Coil Compose.

Configuration

- Memory cache
- Disk cache
- Placeholder
- Error image
- Crossfade

Avoid loading original resolution images when thumbnails are sufficient.

---

# Image Optimization

Recommendations

- Use HTTPS
- Resize server-side if available
- Lazy load
- Avoid GIFs
- Cache aggressively

---

# Network Optimization

Use

- OkHttp cache
- Compression
- Connection pooling
- Timeouts
- HTTP/2

Avoid duplicate API calls.

---

# Database Optimization

Room best practices

- Index frequently queried columns
- Flow queries
- Suspend DAO methods
- Avoid unnecessary joins
- Use transactions where appropriate

---

# Memory Management

Avoid

- Static Context
- Leaking Activities
- Retained Views

Prefer

- Hilt
- ViewModel
- Lifecycle-aware APIs

---

# WebView Performance

- Reuse WebView carefully
- Clear resources
- Enable hardware acceleration
- Cache static assets where appropriate

---

# Startup Optimization

Startup sequence

```text
Splash

↓

Dependency Injection

↓

ViewModel

↓

First API

↓

UI
```

Avoid heavy work inside `Application.onCreate()`.

---

# Baseline Profiles

Use Android Baseline Profiles to improve startup and scrolling.

Benefits

- Faster cold start
- Faster navigation
- Better runtime optimization

Future implementation:

- Macrobenchmark
- Baseline Profile generation

---

# Battery Optimization

Avoid

- Continuous polling
- Excessive refresh
- Background loops

Use

- WorkManager
- Lifecycle awareness
- Flow cancellation

---

# Coroutine Optimization

Use

- Structured concurrency
- Cancellation
- SupervisorJob where appropriate

Never launch unbounded coroutines.

---

# Flow Optimization

Use

- debounce()
- distinctUntilChanged()
- shareIn()
- stateIn()

Avoid collecting the same Flow multiple times.

---

# Navigation Performance

- Single NavHost
- Restore state
- launchSingleTop
- Avoid duplicate destinations

---

# Animation Performance

Animations should:

- Be lightweight
- Use Compose animation APIs
- Avoid animating large layouts

Target 60 FPS.

---

# APK Optimization

Release build

- R8 enabled
- Resource shrinking
- Code shrinking
- Remove unused resources

---

# Build Performance

Recommendations

- KSP over KAPT
- Gradle Configuration Cache
- Incremental compilation
- Version Catalog
- Parallel Gradle builds

---

# Logging Optimization

Development

- Timber

Release

- Remove verbose logs

Never log sensitive information.

---

# Performance Monitoring

Future tools

- Android Profiler
- Layout Inspector
- Compose Layout Inspector
- Macrobenchmark
- Baseline Profiles
- Firebase Performance Monitoring

---

# Accessibility Performance

Accessibility should not degrade UI performance.

Use semantics efficiently and avoid excessive nesting.

---

# Folder Structure

```text
core

performance

├── PerformanceConfig.kt

├── ImageLoaderModule.kt

├── BaselineProfile.md

├── PerformanceLogger.kt

└── Benchmark.md
```

---

# Performance Checklist

## UI

- Stateless composables
- Stable models
- State hoisting
- Remember usage

---

## Networking

- OkHttp cache
- Compression
- Timeouts
- Retry policy

---

## Database

- Indexed queries
- Flow
- Suspend
- Transactions

---

## Images

- Coil
- Cache
- Crossfade
- Placeholder

---

## Build

- R8
- Shrink resources
- KSP
- Version Catalog

---

## Startup

- Lazy initialization
- Baseline Profiles
- Splash optimization

---

# Best Practices

- Optimize before shipping
- Measure before optimizing
- Avoid premature optimization
- Profile regularly
- Use immutable state
- Prefer lazy loading
- Minimize allocations
- Keep composables small

---

# Testing Strategy

Measure

- Startup
- Scrolling
- Memory
- CPU
- Battery
- Network
- Database

Tools

- Android Studio Profiler
- Macrobenchmark
- Baseline Profiles
- LeakCanary
- Firebase Performance

---

# Build Verification

After optimization

- Fast startup
- Smooth scrolling
- No dropped frames
- Minimal memory leaks
- Efficient paging
- Optimized image loading
- Reduced APK size

---

# Files Created

None.

This document defines the application's performance optimization strategy.

---

# What We Completed

✅ Planned Compose performance optimization

✅ Planned Paging optimization

✅ Planned image optimization

✅ Planned startup optimization

✅ Planned memory management

✅ Planned networking optimization

✅ Planned database optimization

✅ Planned build optimization

---

# Next Documents

Performance implementation will be divided into compile-safe milestones.

---

## 24A_Compose_Performance.md

Implement

- Recomposition optimization
- Stable parameters
- remember()
- rememberSaveable()
- derivedStateOf()

---

## 24B_Paging_Optimization.md

Implement

- PagingConfig tuning
- Prefetch
- Load states
- Scroll optimization

---

## 24C_Image_Optimization.md

Implement

- Coil ImageLoader
- Memory cache
- Disk cache
- Placeholders
- Crossfade

---

## 24D_Startup_Optimization.md

Implement

- Splash optimization
- Lazy initialization
- App Startup library
- Dependency initialization

---

## 24E_Memory_Optimization.md

Implement

- Leak prevention
- Lifecycle cleanup
- Coroutine cancellation
- WebView cleanup

---

## 24F_Baseline_Profiles.md

Implement

- Macrobenchmark module
- Baseline Profile generation
- Startup benchmarks
- Scroll benchmarks

---

## 24G_Build_Optimization.md

Implement

- R8 configuration
- Resource shrinking
- ProGuard rules
- Version Catalog improvements

---

## 24H_Performance_Testing.md

Implement

- Macrobenchmark tests
- Startup tests
- Scroll performance tests
- Memory profiling guide

---

# Prompt for Code Generation

## Prompt: 24A_Compose_Performance.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete Compose performance optimization guide and implementation.

### Requirements

1. Demonstrate:
    - Stable models
    - remember
    - rememberSaveable
    - derivedStateOf
    - key()
2. Optimize LazyColumn.
3. Prevent unnecessary recompositions.
4. Explain every optimization.
5. Provide complete runnable Kotlin examples.

---

## Prompt: 24C_Image_Optimization.md

Generate the complete Coil optimization setup.

Requirements:

- Custom ImageLoader
- Memory cache
- Disk cache
- Placeholder handling
- Error images
- Crossfade
- Hardware bitmap configuration
- Explain every configuration.
- Provide complete runnable Kotlin code.

---

## Prompt: 24D_Startup_Optimization.md

Generate the startup optimization implementation.

Requirements:

- Optimize Application startup
- Lazy dependency initialization
- SplashScreen API
- AndroidX Startup integration
- Baseline Profile preparation
- Explain every optimization.
- Provide complete runnable Kotlin code.

---

## Prompt: 24F_Baseline_Profiles.md

Generate a complete Baseline Profile setup.

Requirements:

- Macrobenchmark module
- Baseline Profile configuration
- Startup benchmark
- Scroll benchmark
- Build configuration
- Explain every file.
- Provide complete runnable code.

---

# UI Design Prompt

## Prompt: Performance & Motion UX Specification

Design the performance-focused UX strategy for **NewsApp**.

### Include

1. Startup experience
2. Splash-to-home transition
3. Loading shimmer behavior
4. Infinite scrolling performance
5. Image loading transitions
6. Pull-to-refresh animation
7. Screen navigation animations
8. Scroll behavior
9. Reduced motion accessibility mode
10. Tablet performance considerations
11. Material 3 motion guidelines
12. Skeleton loading specifications
13. Perceived performance improvements
14. Animation durations and easing recommendations
15. Performance metrics to monitor

Do **not** generate Jetpack Compose code. Produce a comprehensive UX and performance specification that minimizes perceived latency while maintaining a smooth, accessible user experience.