# NewsApp

# 03_Gradle_Configuration.md

> **Phase:** Foundation
>
> **Goal:** Configure the entire Gradle build system using Kotlin DSL and Version Catalog while keeping the project scalable, maintainable, and aligned with modern Android development best practices.

---

# Objective

In this document, we will transform the default Android Studio project into a production-ready Gradle project.

After completing this document, the project will:

- Build successfully
- Use Version Catalog
- Support Kotlin DSL
- Support Jetpack Compose
- Support Material 3
- Support Hilt
- Support Navigation Compose
- Support Retrofit
- Support Room
- Support Paging 3
- Support WorkManager
- Support Coil
- Support Timber
- Support Unit Testing
- Support Compose UI Testing

No application logic will be implemented yet.

---

# Why Configure Gradle First?

Every Android project depends on a stable build configuration.

A proper Gradle setup provides:

- Dependency management
- Faster builds
- Version consistency
- Easier upgrades
- Modular architecture
- Better IDE support
- Reliable CI/CD integration

Instead of adding libraries one by one later, we configure everything at the beginning so future implementation only focuses on business logic.

---

# Gradle Architecture

```text
                Project

                   │

        settings.gradle.kts

                   │

         Version Catalog

        libs.versions.toml

                   │

      Root build.gradle.kts

                   │

      App build.gradle.kts

                   │

         Android Project
```

---

# Files Overview

| File | Purpose |
|------|----------|
| settings.gradle.kts | Project configuration |
| build.gradle.kts | Root plugins |
| app/build.gradle.kts | Android app configuration |
| gradle.properties | Global Gradle settings |
| libs.versions.toml | Dependency versions |

---

# Version Catalog

Google recommends Version Catalog because it centralizes all versions.

Instead of

```kotlin
implementation("androidx.room:room-runtime:2.7.0")
```

we will write

```kotlin
implementation(libs.androidx.room.runtime)
```

Advantages

- Cleaner Gradle
- Easy upgrades
- Less duplication
- Safer dependency management

---

# Required Libraries

## AndroidX

- Core KTX
- Activity Compose
- Lifecycle Runtime
- Lifecycle ViewModel
- Splash Screen

---

## Compose

- Compose BOM
- UI
- Material3
- UI Tooling
- Foundation
- Animation

---

## Navigation

- Navigation Compose

---

## Dependency Injection

- Hilt
- Hilt Navigation Compose

---

## Networking

- Retrofit
- OkHttp
- Logging Interceptor
- Kotlin Serialization Converter

---

## Database

- Room Runtime
- Room KTX
- Room Compiler

---

## Pagination

- Paging Runtime
- Paging Compose

---

## Image Loading

- Coil Compose

---

## Async

- Kotlin Coroutines

---

## Background

- WorkManager

---

## Logging

- Timber

---

## Testing

- JUnit
- MockK
- Turbine
- MockWebServer
- Compose Testing
- Espresso

---

# Plugin Strategy

The project will use the following plugins.

| Plugin | Purpose |
|---------|----------|
| Android Application | Android app |
| Kotlin Android | Kotlin support |
| Kotlin Compose | Compose compiler |
| KSP | Code generation |
| Hilt | Dependency Injection |
| Kotlin Serialization | JSON parsing |

---

# KSP vs KAPT

We will use **KSP** because:

- Faster builds
- Better incremental compilation
- Official recommendation
- Required by modern Room versions

Only use KAPT if a library still requires it.

---

# Dependency Organization

Dependencies will be grouped by category.

```text
AndroidX

Compose

Navigation

Hilt

Networking

Room

Paging

WorkManager

Testing
```

This keeps the Gradle file readable.

---

# Build Variants

Initially

```text
debug

release
```

Later we may add

```text
staging
```

---

# Recommended Build Features

Enable

- Compose
- BuildConfig generation
- Vector Drawable support

---

# Compiler Options

Configure

- JVM 17
- Kotlin Compiler
- Compose Compiler

---

# Packaging Rules

Exclude duplicate metadata.

Example

```
META-INF/LICENSE

META-INF/AL2.0

META-INF/LGPL2.1
```

---

# Build Optimization

Enable

- Configuration Cache
- Parallel Build
- Incremental Compilation
- Non-transitive R Classes

---

# Dependency Graph

```text
Compose

↓

Navigation

↓

ViewModel

↓

UseCases

↓

Repository

↓

Retrofit
Room

↓

API
```

---

# Files to Create

```text
gradle/libs.versions.toml
```

---

# Files to Modify

```text
settings.gradle.kts

build.gradle.kts

app/build.gradle.kts

gradle.properties
```

---

# Build Verification

After configuration verify:

✅ Gradle Sync

✅ Build Success

✅ Emulator Launch

✅ Compose Preview

✅ No Dependency Conflicts

---

# Git Commit

```text
feat: configure Gradle with Version Catalog and modern Android dependencies
```

---

# What We Completed

- Planned Gradle architecture
- Identified required plugins
- Selected dependency management strategy
- Defined build optimization approach
- Prepared project for feature implementation

No feature code has been added yet.

---

# Next Documents

Instead of generating all code in one response, the implementation will be split into focused documents:

### 03A_Gradle_Version_Catalog.md

Contains:

- `libs.versions.toml`
- All versions
- All libraries
- All plugin aliases

---

### 03B_Project_Gradle.md

Contains:

- `settings.gradle.kts`
- Root `build.gradle.kts`
- `gradle.properties`

---

### 03C_App_Gradle.md

Contains:

- Complete `app/build.gradle.kts`
- Compose configuration
- Hilt
- Room
- Retrofit
- Paging
- Testing
- BuildConfig
- Packaging

---

### 03D_First_Build_Verification.md

Contains:

- Gradle Sync
- Build verification
- Common errors
- Fixes
- Git commit
- Project checklist

---

# Prompt for Code Generation

## Prompt: 03A_Gradle_Version_Catalog.md

You are a Senior Android Engineer.

Generate the complete `gradle/libs.versions.toml` file for the NewsApp project.

Requirements:

1. Use the latest stable Android libraries.
2. Organize versions by category.
3. Include:
    - Android Gradle Plugin
    - Kotlin
    - Compose BOM
    - Material 3
    - Activity Compose
    - Lifecycle
    - Navigation Compose
    - Hilt
    - KSP
    - Retrofit
    - OkHttp
    - Kotlin Serialization
    - Room
    - Paging 3
    - Coil
    - WorkManager
    - Timber
    - Coroutines
    - JUnit
    - MockK
    - Turbine
    - MockWebServer
    - Espresso
    - Compose UI Test
4. Define plugin aliases.
5. Define library aliases.
6. Explain each dependency.
7. Output the complete runnable `libs.versions.toml`.

---

## Prompt: 03B_Project_Gradle.md

Generate:

- `settings.gradle.kts`
- Root `build.gradle.kts`
- `gradle.properties`

Explain every configuration line and ensure the project syncs successfully.

---

## Prompt: 03C_App_Gradle.md

Generate the complete production-ready `app/build.gradle.kts`.

Requirements:

- Kotlin DSL
- Compose
- Material 3
- Hilt
- Navigation
- Retrofit
- Room (KSP)
- Paging 3
- Coil
- Timber
- WorkManager
- Kotlin Serialization
- BuildConfig fields
- Packaging options
- Release build type
- ProGuard configuration
- Unit test configuration
- Compose UI test configuration

Provide the complete file with explanations.

---

# UI Design Prompt

Although no UI is implemented in this phase, prepare the design system.

**Prompt: Design System Foundation**

Create a Material 3 design system for **NewsApp** with:

- Light & Dark themes
- Color palette
- Typography scale
- Shape system
- Spacing tokens (4dp grid)
- Elevation tokens
- Iconography
- Responsive layout guidelines
- Accessibility (48dp touch targets, contrast, dynamic font scaling)
- Reusable component specifications (Buttons, Cards, Chips, SearchBar, TopAppBar, Bottom Navigation, Loading Shimmer, Empty State, Error State)

Do not generate Compose code yet—only the visual design specification that will be implemented in later documents.