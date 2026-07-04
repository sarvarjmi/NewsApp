# NewsApp

> **Phase:** Architecture Setup
>
> **Goal:** Create a scalable project structure based on Clean Architecture + MVVM that will be used throughout the NewsApp development lifecycle.

---

# Objective

Before implementing Hilt, Retrofit, Room, or any UI, we must organize the project into logical packages.

A well-structured project:

- Improves readability
- Makes onboarding easier
- Scales for large teams
- Simplifies testing
- Separates responsibilities
- Supports feature-based development

After completing this document, the project will have a fully organized package structure with placeholder classes that compile successfully.

---

# Folder Organization Strategy

The project follows **Feature + Layer Hybrid Architecture**.

- Layer-based organization for `data`, `domain`, and `core`
- Feature-based organization for `presentation`

This is the architecture commonly used in production Android applications.

---

# Final Project Structure

```text
NewsApp
│
├── app
│
├── core
│   ├── common
│   ├── constants
│   ├── dispatcher
│   ├── error
│   ├── extensions
│   ├── network
│   ├── result
│   ├── util
│   └── validation
│
├── data
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   ├── entity
│   │   └── mapper
│   │
│   ├── remote
│   │   ├── api
│   │   ├── dto
│   │   ├── interceptor
│   │   ├── mapper
│   │   ├── paging
│   │   └── response
│   │
│   ├── datasource
│   │   ├── local
│   │   └── remote
│   │
│   └── repository
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│       ├── bookmark
│       ├── news
│       └── search
│
├── presentation
│   ├── components
│   ├── navigation
│   ├── theme
│   ├── home
│   ├── search
│   ├── detail
│   ├── bookmark
│   └── common
│
├── di
│
└── MainActivity.kt
```

---

# Core Package

The **core** package contains reusable code shared across every feature.

```text
core
│
├── common
├── constants
├── dispatcher
├── error
├── extensions
├── network
├── result
├── util
└── validation
```

## Responsibilities

### common

Common helper classes.

Examples:

- UiState
- UiEvent
- BaseViewModel

---

### constants

Application constants.

Examples:

- Base URLs
- API Keys
- Database Names

---

### dispatcher

Coroutine dispatchers.

Example

```
DispatcherProvider
```

---

### error

Application error models.

Example

```
NetworkException

ApiException

NoInternetException
```

---

### extensions

Kotlin extension functions.

Example

```
Context.kt

String.kt

Date.kt
```

---

### network

Networking utilities.

Example

```
NetworkMonitor

ConnectivityObserver
```

---

### result

Application Result Wrapper.

```
Success

Loading

Error
```

---

### util

Utility classes.

Example

```
DateFormatter

TimeUtils

Logger
```

---

### validation

Validation helpers.

---

# Data Layer

The Data layer is responsible for retrieving and storing data.

```text
data

├── remote
├── local
├── datasource
└── repository
```

---

## Remote

Contains everything related to the News API.

```text
remote

api

dto

mapper

paging

response

interceptor
```

---

### api

Retrofit interfaces.

Example

```
NewsApiService.kt
```

---

### dto

DTOs received from the API.

Example

```
ArticleDto.kt

SourceDto.kt
```

---

### mapper

DTO → Domain mapping.

---

### paging

PagingSource implementation.

---

### response

API wrapper models.

---

### interceptor

OkHttp interceptors.

---

## Local

Contains Room components.

```text
local

dao

database

entity

mapper
```

---

### dao

Room DAOs.

---

### database

Room Database.

---

### entity

Room entities.

---

### mapper

Entity ↔ Domain mapping.

---

## DataSource

Separates API implementation from Repository.

```text
datasource

remote

local
```

Example

```
RemoteNewsDataSource

LocalBookmarkDataSource
```

---

## Repository

Contains repository implementations.

Example

```
NewsRepositoryImpl
```

---

# Domain Layer

The Domain layer contains business logic.

```text
domain

model

repository

usecase
```

---

## model

Pure Kotlin models.

Example

```
Article

Source

Bookmark
```

---

## repository

Repository interfaces.

Example

```
NewsRepository
```

---

## usecase

Grouped by feature.

```text
news

search

bookmark
```

Example

```
GetTopHeadlinesUseCase

SearchNewsUseCase

ToggleBookmarkUseCase
```

---

# Presentation Layer

Presentation is feature-based.

```text
presentation

home

search

detail

bookmark
```

Each feature owns:

```
Screen

ViewModel

UiState

UiEvent

UiAction
```

---

Example

```text
home

HomeScreen.kt

HomeViewModel.kt

HomeUiState.kt

HomeEvent.kt
```

---

# Shared Components

Reusable Compose components.

```text
presentation/components

NewsCard.kt

SearchBar.kt

ErrorView.kt

LoadingView.kt

EmptyState.kt

PrimaryButton.kt
```

---

# Navigation

```text
presentation/navigation

NavGraph.kt

Routes.kt

Destination.kt
```

---

# Theme

```text
presentation/theme

Color.kt

Theme.kt

Typography.kt

Shape.kt
```

---

# Dependency Injection

```
di

NetworkModule.kt

DatabaseModule.kt

RepositoryModule.kt

DispatcherModule.kt
```

These files will be implemented later.

---

# Placeholder Files

Create empty placeholder files so the project compiles.

## Example

```kotlin
package com.example.newsapp.presentation.home

class HomeViewModel
```

Another example:

```kotlin
package com.example.newsapp.domain.repository

interface NewsRepository
```

Every package should contain at least one placeholder file.

---

# Package Naming Rules

- lowercase only
- no spaces
- feature-oriented
- singular names where appropriate

Example:

```
presentation.home

presentation.search

domain.model
```

---

# File Naming Rules

| Type | Naming |
|-------|--------|
| Screen | HomeScreen.kt |
| ViewModel | HomeViewModel.kt |
| State | HomeUiState.kt |
| Event | HomeEvent.kt |
| Repository | NewsRepository.kt |
| UseCase | SearchNewsUseCase.kt |
| Entity | BookmarkEntity.kt |
| DTO | ArticleDto.kt |

---

# Architecture Dependency Diagram

```text
Compose UI

↓

ViewModel

↓

UseCase

↓

Repository Interface

↓

Repository Implementation

↓

Remote Data Source
        +
Local Data Source

↓

Retrofit
Room
```

---

# Build Verification

After creating the package structure:

- Android Studio indexes all packages
- No package naming conflicts
- Placeholder classes compile
- No missing imports
- Project builds successfully

---

# Git Commit

```text
chore: create clean architecture project folder structure
```

---

# What We Completed

✅ Created the complete package hierarchy

✅ Defined responsibilities for every package

✅ Established feature-based presentation structure

✅ Planned repository and data source organization

✅ Added placeholder file strategy

✅ Prepared the project for Dependency Injection

---

# Next Document

## 06_Hilt_Setup.md

In the next document we will:

1. Configure Hilt
2. Create the Application class
3. Configure Gradle plugins
4. Add Hilt modules
5. Inject dependencies
6. Create DispatcherModule
7. Verify Hilt works end-to-end

After completing the next document, the project will be ready for implementing networking and database layers.

---

# Prompt for Code Generation

## Prompt: 06_Hilt_Setup.md

You are a Senior Android Engineer.

Generate the complete Hilt setup for the NewsApp project.

Requirements:

1. Configure Hilt using Kotlin DSL and KSP where appropriate.
2. Create:
    - `NewsApplication.kt`
    - `DispatcherModule.kt`
    - `NetworkModule.kt` (placeholder)
    - `DatabaseModule.kt` (placeholder)
    - `RepositoryModule.kt` (placeholder)
3. Update `AndroidManifest.xml` to register the Application class.
4. Demonstrate constructor injection, field injection, and `@HiltViewModel`.
5. Explain scopes (`@Singleton`, `@ActivityRetainedScoped`, `@ViewModelScoped`) and when to use them.
6. Provide complete runnable code for every new or modified file.
7. Include build verification steps and a Git commit message.

---

# UI Design Prompt

## Prompt: Application Navigation & Layout Blueprint

Create the UI architecture for **NewsApp** before implementing screens.

Include:

- Bottom navigation layout
- Top app bar behavior
- Screen hierarchy
- Navigation graph
- Responsive layouts (phone and tablet)
- Empty, loading, and error screen wireframes
- Component inventory (NewsCard, SearchBar, CategoryChip, LoadingShimmer, ErrorView, EmptyState)
- Material 3 spacing, typography, and elevation guidelines

Do **not** generate Jetpack Compose code yet. Produce a UI specification and wireframe blueprint that will guide implementation in subsequent documents.