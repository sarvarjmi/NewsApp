# NewsApp

> **Phase:** Dependency Injection
>
> **Goal:** Configure Hilt as the Dependency Injection framework for the NewsApp project and establish a scalable dependency graph that supports Clean Architecture, MVVM, Retrofit, Room, Paging 3, WorkManager, and future features.

---

# Objective

As the application grows, manually creating objects becomes difficult and tightly couples classes together.

Dependency Injection (DI) solves this by allowing objects to receive their dependencies rather than creating them internally.

For this project we will use **Hilt**, Google's officially recommended DI framework for Android.

After completing this phase, the project will have:

- Hilt configured
- Application class
- Dependency graph
- Modules
- Constructor Injection
- ViewModel Injection
- Coroutine Dispatcher Injection
- Placeholder modules for Network, Database and Repository

No networking or database logic will be implemented yet.

---

# What is Dependency Injection?

Instead of this:

```kotlin
class HomeViewModel {

    private val repository = NewsRepository()

}
```

We inject the dependency:

```kotlin
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
)
```

Advantages:

- Loose coupling
- Easier testing
- Better scalability
- Centralized dependency creation
- Replace implementations during testing

---

# Why Hilt?

Google recommends Hilt because it:

- Builds on Dagger
- Reduces boilerplate
- Integrates with Android components
- Supports ViewModels
- Works seamlessly with Compose
- Supports WorkManager
- Simplifies testing

---

# Hilt Architecture

```text
Application

↓

SingletonComponent

↓

Modules

↓

Repository

↓

UseCases

↓

ViewModels

↓

Compose UI
```

The dependency graph is created once and reused throughout the application.

---

# Dependency Graph Overview

```text
NewsApplication

↓

SingletonComponent

│

├── DispatcherModule

├── NetworkModule

├── DatabaseModule

├── RepositoryModule

│

↓

ViewModels

↓

Compose Screens
```

---

# Hilt Components

The project will use the following components.

| Component | Lifetime | Usage |
|-----------|----------|-------|
| SingletonComponent | Entire app | Retrofit, Room, Repository |
| ActivityRetainedComponent | Activity recreation | Shared resources |
| ViewModelComponent | ViewModel lifecycle | UseCases, ViewModels |

---

# Injection Types

## Constructor Injection

Preferred approach.

```kotlin
class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
)
```

---

## Field Injection

Used in Android framework classes.

```kotlin
@Inject
lateinit var analytics: Analytics
```

---

## Module Injection

Used for third-party classes.

Example:

- Retrofit
- Room
- OkHttpClient
- Gson/Kotlin Serialization
- WorkManager

---

# Hilt Modules

The project will include the following modules.

## DispatcherModule

Provides coroutine dispatchers.

Responsibilities:

- IO
- Default
- Main

---

## NetworkModule

Provides:

- Retrofit
- OkHttpClient
- LoggingInterceptor
- API Service

---

## DatabaseModule

Provides:

- Room Database
- DAO
- Database Name

---

## RepositoryModule

Provides:

- NewsRepository implementation
- DataSources

---

# Dependency Flow

```text
Compose Screen

↓

HomeViewModel

↓

GetTopHeadlinesUseCase

↓

NewsRepository

↓

RemoteDataSource
+
LocalDataSource

↓

Retrofit
Room
```

Hilt is responsible for constructing and wiring every dependency automatically.

---

# Scopes

Choosing the correct scope is essential.

## @Singleton

One instance for the entire application.

Use for:

- Retrofit
- Room
- Repository
- OkHttpClient
- Database

---

## @ActivityRetainedScoped

One instance across Activity recreation.

Useful for shared resources tied to an Activity lifecycle.

---

## @ViewModelScoped

One instance per ViewModel.

Ideal for:

- UseCases
- Feature-specific helpers

---

# Qualifiers

When multiple implementations of the same type exist, qualifiers are used.

Example:

```kotlin
@IoDispatcher

@DefaultDispatcher

@MainDispatcher
```

These qualifiers allow injecting the correct `CoroutineDispatcher`.

---

# Integration with Clean Architecture

```text
Presentation

↓

ViewModel
        ↑
        │
     Injected

↓

UseCases
        ↑
        │
     Injected

↓

Repository Interface

↓

Repository Implementation
        ↑
        │
     Injected

↓

Retrofit / Room
```

The Presentation and Domain layers depend on abstractions, while Hilt provides concrete implementations.

---

# Planned Files

The following files will be created in subsequent implementation documents.

```text
app/

NewsApplication.kt

di/

DispatcherModule.kt

NetworkModule.kt

DatabaseModule.kt

RepositoryModule.kt

Qualifier.kt

presentation/

home/

HomeViewModel.kt
```

---

# AndroidManifest Changes

The manifest will be updated to register the custom `Application` class.

No other changes are required at this stage.

---

# Verification Goals

Once the Hilt setup is complete we should be able to:

- Build the project successfully
- Launch the application
- Inject a ViewModel
- Inject repositories
- Inject coroutine dispatchers
- Resolve all dependencies automatically

---

# Common Mistakes

Avoid the following:

- Using `new`/manual instantiation inside ViewModels
- Mixing KAPT and KSP unnecessarily
- Forgetting `@HiltAndroidApp`
- Forgetting `@AndroidEntryPoint`
- Missing module installation (`@InstallIn`)
- Using incorrect scopes

---

# Best Practices

- Prefer constructor injection
- Keep modules focused
- Inject interfaces instead of implementations
- Scope dependencies appropriately
- Avoid service locators
- Keep the dependency graph shallow
- Use qualifiers for multiple bindings

---

# Files Created in This Document

None.

This document defines the Hilt architecture and implementation strategy.

---

# What We Completed

- Selected Hilt as the DI framework
- Designed the dependency graph
- Planned DI modules
- Defined scopes and qualifiers
- Planned integration with Clean Architecture
- Prepared the project for dependency injection

---

# Next Documents

The implementation will be divided into smaller compile-safe steps.

## 06A_Hilt_Gradle.md

Configure:

- Hilt plugin
- KSP plugin
- Dependencies
- Gradle sync

---

## 06B_Hilt_Application.md

Create:

- `NewsApplication.kt`
- Update `AndroidManifest.xml`
- Configure `@HiltAndroidApp`

---

## 06C_Hilt_Modules.md

Create:

- `DispatcherModule.kt`
- `Qualifier.kt`
- `NetworkModule.kt` (placeholder)
- `DatabaseModule.kt` (placeholder)
- `RepositoryModule.kt` (placeholder)

---

## 06D_Hilt_ViewModel_Injection.md

Create:

- `HomeViewModel`
- Constructor Injection
- `@HiltViewModel`
- Inject dispatchers
- Inject placeholder repository

---

## 06E_Hilt_Verification.md

Verify:

- Dependency graph
- Build success
- Common errors
- Troubleshooting
- Git commit

---

# Prompt for Code Generation

## Prompt: 06A_Hilt_Gradle.md

You are a Senior Android Engineer.

Generate the complete Gradle configuration required to enable **Hilt** for the NewsApp project.

Requirements:

1. Update the Version Catalog with Hilt and KSP plugins.
2. Update:
    - `libs.versions.toml`
    - `settings.gradle.kts` (if required)
    - Root `build.gradle.kts`
    - `app/build.gradle.kts`
3. Configure:
    - Hilt plugin
    - KSP plugin
    - Hilt Navigation Compose
4. Explain every modification.
5. Ensure the project syncs and builds successfully.
6. End with a verification checklist and Git commit message.

---

# UI Design Prompt

## Prompt: Application Shell & Navigation Framework

Design the foundational application shell for **NewsApp** before implementing screens.

Include:

- Splash screen concept
- Root scaffold layout
- Top app bar behavior
- Bottom navigation specification
- Navigation rail adaptation for tablets
- Snackbar and dialog placement
- Loading overlay behavior
- Error banner strategy
- Theme switching behavior (Light/Dark)
- Accessibility guidelines (TalkBack, focus order, large text)

Do not generate Jetpack Compose code. Produce a visual and interaction specification that will guide later UI implementation.