# NewsApp

> **Phase:** Foundation
> **Goal:** Create a production-ready Android Studio project that builds successfully before implementing any features.
>
> **Status after this document:** ✅ Project compiles successfully with all modern Android dependencies configured.

---

# Objective

In this document we will create the Android Studio project from scratch and configure it using the latest recommended Android development practices.

At the end of this step you will have:

- Android Studio Project
- Git initialized
- Version Catalog
- Kotlin DSL
- Jetpack Compose
- Material 3
- Hilt
- Retrofit
- Room
- Paging3
- Navigation Compose
- Coil
- Coroutines
- Timber
- Testing Libraries

The app will build successfully even though no business logic has been implemented yet.

---

# Project Information

| Item | Value |
|-------|--------|
| Project Name | NewsApp |
| Package | com.newsapp |
| Language | Kotlin |
| UI | Jetpack Compose |
| Build | Kotlin DSL |
| Architecture | Clean Architecture + MVVM |
| Min SDK | 24 |
| Target SDK | Latest Stable |
| Compile SDK | Latest Stable |

---

# Software Requirements

Install the following software before starting.

## Android Studio

Latest Stable Version

Recommended:

```
Android Studio Meerkat+
```

---

## JDK

```
JDK 17
```

---

## Android SDK

Install

- Android SDK Platform Latest
- Android SDK Build Tools
- Android Emulator
- Platform Tools

---

## Git

Latest Version

---

## Gradle

Use Gradle Wrapper only.

Never install Gradle manually.

---

# Create Project

Open Android Studio

```
New Project
```

Select

```
Empty Activity
```

Configuration

```
Project Name

NewsApp
```

Package

```
com.newsapp
```

Language

```
Kotlin
```

Minimum SDK

```
API 24
```

Build Configuration

```
Kotlin DSL
```

Finish.

---

# Verify Project

Run

```
Run App
```

You should see

```
Hello Android!
```

---

# Initialize Git

```
git init

git add .

git commit -m "Initial project setup"
```

---

# Project Structure

Initially Android Studio creates

```
NewsApp

│

├── app

├── gradle

├── build.gradle.kts

├── settings.gradle.kts

├── gradle.properties

└── local.properties
```

---

# Configure Version Catalog

Create

```
gradle/libs.versions.toml
```

This file will contain ALL library versions.

Benefits

- Single source of truth
- Easy dependency upgrades
- Cleaner Gradle files
- Recommended by Google

---

# Required Libraries

The project will use the following libraries.

## UI

- Compose BOM
- Material3
- Activity Compose
- Lifecycle Compose

---

## Architecture

- ViewModel
- StateFlow
- Lifecycle Runtime

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

## Image Loading

- Coil Compose

---

## Database

- Room
- Room KTX
- Room Compiler

---

## Pagination

- Paging 3
- Paging Compose

---

## Background

- WorkManager

---

## Testing

- JUnit
- MockK
- Turbine
- MockWebServer
- Compose Testing

---

## Logging

- Timber

---

# Gradle Files

The following files will be updated.

```
settings.gradle.kts

build.gradle.kts

app/build.gradle.kts

gradle.properties

libs.versions.toml
```

---

# Project Configuration Flow

```
Android Studio

↓

Project

↓

Gradle

↓

Version Catalog

↓

Dependencies

↓

Compose

↓

Material3

↓

Hilt

↓

Navigation

↓

Networking

↓

Room

↓

Paging

↓

Build Successful
```

---

# Coding Standards

Throughout the project we will follow

- Kotlin Coding Conventions
- Official Android Guidelines
- SOLID
- Clean Code
- Clean Architecture
- MVVM
- Unidirectional Data Flow

---

# Git Workflow

Every major feature will have its own commit.

Example

```
Initial Project

↓

Gradle Setup

↓

Compose Setup

↓

Hilt Setup

↓

Networking

↓

Room

↓

Paging

↓

Repository

↓

UseCases

↓

UI

↓

Testing
```

---

# Folder Naming Convention

```
lowercase

feature based

no spaces

meaningful names
```

Example

```
presentation

domain

data

navigation

ui

di

common

core
```

---

# Package Naming Convention

```
com.newsapp

data

domain

presentation

navigation

di

core

common
```

---

# Coding Convention

Functions

```
camelCase()
```

Classes

```
PascalCase
```

Constants

```
UPPER_CASE
```

Variables

```
camelCase
```

---

# Build Verification Checklist

Before moving forward ensure

- Project builds successfully
- Emulator runs
- Compose works
- No Gradle warnings
- Git initialized
- Kotlin DSL enabled
- Version Catalog created

---

# Deliverables

At the end of this document you should have

✅ Android Studio project

✅ Kotlin DSL

✅ Compose enabled

✅ Git initialized

✅ Gradle synchronized

✅ Project builds successfully

---

# Files Created

```
gradle/libs.versions.toml
```

---

# Files Modified

```
settings.gradle.kts

build.gradle.kts

app/build.gradle.kts

gradle.properties
```

---

# What We Completed

- Created Android Studio project
- Configured development environment
- Planned dependency management
- Initialized Git repository
- Prepared Gradle configuration
- Established coding standards

At this point, the project skeleton is ready, but no external libraries have been added yet.

---

# Prompt for Code Generation (Next Step)

Use the following prompt to generate the complete Gradle configuration in the next document:

---

## Prompt: 03_Gradle_Configuration.md

You are a Senior Android Engineer building a production-ready Android application.

Generate the complete Gradle configuration for the **NewsApp** project created in **02_Project_Setup.md**.

Requirements:

1. Use the latest stable Android libraries.
2. Use Kotlin DSL (`build.gradle.kts`).
3. Use Version Catalog (`libs.versions.toml`).
4. Configure:
    - `settings.gradle.kts`
    - Root `build.gradle.kts`
    - `app/build.gradle.kts`
    - `gradle.properties`
    - `libs.versions.toml`
5. Enable:
    - Jetpack Compose
    - Material 3
    - Kotlin Compiler
    - Hilt
    - Navigation Compose
    - Retrofit
    - OkHttp
    - Kotlin Serialization
    - Room
    - Paging 3
    - Coil
    - WorkManager
    - Timber
    - Lifecycle
    - Coroutines
6. Configure KSP (preferred) where applicable.
7. Add all required testing dependencies:
    - JUnit
    - MockK
    - Turbine
    - MockWebServer
    - Compose UI Test
    - Espresso
8. Explain every dependency and why it is included.
9. Clearly identify every new or modified file.
10. Provide the complete runnable content for each file.
11. End with a build verification checklist and Git commit message.

The output should be production-ready and compile successfully before moving to the next phase.

---

# UI Generation Prompt (For AI Design Tools)

After the Gradle setup is complete, use the following prompt to generate the application's design system:

**Prompt: NewsApp UI Foundation**

Design a modern Android News application using **Material 3** and **Jetpack Compose**.

Design requirements:

- Clean and minimal news-reader aesthetic
- Adaptive layout for phones and tablets
- Light and Dark themes
- Material 3 color system
- Dynamic typography
- Rounded cards and subtle elevation
- Bottom navigation with:
    - Home
    - Search
    - Bookmarks
- Home screen with:
    - Top app bar
    - Search shortcut
    - Category chips
    - News cards (image, title, source, date, description)
    - Loading shimmer
    - Empty state
    - Error state
- Search screen with debounced search UI and recent searches
- Detail screen with large hero image, article content, share, bookmark, open-in-browser actions
- Bookmark screen with offline article list and empty state
- Reusable components:
    - NewsCard
    - LoadingShimmer
    - ErrorView
    - EmptyState
    - SearchBar
    - PrimaryButton
- Follow Material 3 spacing, accessibility, and responsive design guidelines.

This design will be implemented incrementally in later documents using Jetpack Compose.