# NewsApp

> **Phase:** Data Layer
>
> **Module:** Local Persistence
>
> **Goal:** Design a scalable offline storage layer using Room that supports bookmarks, caching, future offline capabilities, and seamless integration with Clean Architecture.

---

# Objective

The NewsApp should continue providing value even when the user has no internet connection.

The Room Database layer enables:

- Bookmarking articles
- Offline reading
- Local caching
- Fast data retrieval
- Reactive UI updates
- Future offline synchronization

Rather than exposing database entities directly, Room integrates with the Repository layer through Local Data Sources and mappers.

---

# Why Room?

Room is Google's recommended persistence library for Android.

Advantages:

- Compile-time SQL validation
- Coroutine & Flow support
- Type-safe queries
- Schema versioning
- Automatic migrations
- Tight integration with Jetpack libraries

---

# Local Storage Architecture

```text
Compose UI

↓

ViewModel

↓

UseCase

↓

Repository

↓

LocalDataSource

↓

DAO

↓

Room Database

↓

SQLite
```

The UI never interacts with Room directly.

---

# Repository as Single Source of Truth

The Repository coordinates local and remote data.

```text
                Repository
                     │
        ┌────────────┴────────────┐
        │                         │
RemoteDataSource          LocalDataSource
        │                         │
    Retrofit                    Room
```

This approach allows the Repository to:

- Read cached data
- Save bookmarks
- Synchronize API results
- Support offline mode

---

# Room Components

The local persistence layer contains four primary components.

```text
Room Database

│

├── Entity

├── DAO

├── Database

└── TypeConverters
```

Each component has a single responsibility.

---

# Planned Folder Structure

```text
data

└── local

    ├── database

    │      NewsDatabase.kt

    ├── dao

    │      BookmarkDao.kt

    ├── entity

    │      BookmarkEntity.kt

    ├── converter

    │      Converters.kt

    ├── datasource

    │      LocalNewsDataSource.kt

    └── mapper

           BookmarkEntityMapper.kt
```

---

# Entity Design

The application initially stores bookmarked articles.

Example fields:

```text
BookmarkEntity

id

title

description

author

source

url

imageUrl

publishedAt

content

bookmarkedAt
```

Only fields required for offline access should be stored.

---

# DAO Responsibilities

The DAO is responsible for database operations.

Typical operations include:

- Insert bookmark
- Delete bookmark
- Update bookmark
- Get all bookmarks
- Observe bookmark changes
- Check bookmark status

All database access goes through the DAO.

---

# Database Responsibilities

The Room database:

- Registers entities
- Exposes DAOs
- Manages schema versions
- Handles migrations
- Creates the SQLite database

Only one database instance exists throughout the application.

---

# Type Converters

Some data types cannot be stored directly in SQLite.

Type Converters will handle:

- Date ↔ Long
- Lists ↔ JSON (future)
- Complex objects (if needed)

Initially, converters may be minimal but are included for future extensibility.

---

# Local Data Source

The Repository should not communicate directly with DAOs.

Instead:

```text
Repository

↓

LocalNewsDataSource

↓

BookmarkDao

↓

Room
```

Benefits:

- Easier testing
- Better abstraction
- Replaceable implementations

---

# Reactive Data Flow

Room integrates with Kotlin Flow.

```text
Database Change

↓

DAO Flow

↓

Repository

↓

UseCase

↓

ViewModel

↓

StateFlow

↓

Compose UI
```

Whenever bookmarks change, the UI updates automatically.

---

# Offline Strategy

When online:

```text
API

↓

Repository

↓

Room (optional cache)

↓

UI
```

When offline:

```text
Room

↓

Repository

↓

UI
```

Bookmarks remain accessible without an internet connection.

---

# Transactions

When multiple operations must succeed together, Room transactions ensure consistency.

Example:

- Replace cached articles
- Insert multiple records
- Update related tables

Although bookmarks are simple, transaction support prepares the project for future caching.

---

# Schema Versioning

Every database change increments the schema version.

Example:

```text
Version 1

↓

Add Category Column

↓

Version 2

↓

Migration
```

Migrations prevent users from losing stored data during app updates.

---

# Error Handling

Database failures should never crash the UI.

Possible issues:

- Corrupted database
- Constraint violation
- Disk full
- Migration failure

These are handled in the Repository and exposed as meaningful UI states.

---

# Performance Considerations

Best practices:

- Use indexes where appropriate
- Keep entities lightweight
- Avoid unnecessary columns
- Execute queries off the main thread
- Observe data with Flow
- Reuse the singleton database instance

---

# Security Considerations

Current implementation:

- Plain Room database

Future enhancements:

- SQLCipher encryption
- EncryptedSharedPreferences for secrets
- Backup control
- Secure export/import

---

# Testing Strategy

The Room layer should support:

- In-memory database
- DAO unit tests
- Migration tests
- Repository integration tests

Tests verify:

- Insert
- Delete
- Query
- Flow emissions
- Migrations

---

# Planned Kotlin Files

The following files will be implemented across the next Room documents.

```text
data/local/entity/

BookmarkEntity.kt

--------------------------------

data/local/dao/

BookmarkDao.kt

--------------------------------

data/local/database/

NewsDatabase.kt

--------------------------------

data/local/converter/

Converters.kt

--------------------------------

data/local/datasource/

LocalNewsDataSource.kt

--------------------------------

data/local/mapper/

BookmarkEntityMapper.kt
```

---

# Architecture Diagram

```text
Compose

↓

ViewModel

↓

UseCase

↓

Repository

↓

LocalNewsDataSource

↓

BookmarkDao

↓

Room Database

↓

SQLite
```

---

# Best Practices

- One entity per table
- One DAO per aggregate
- Keep entities persistence-focused
- Use immutable data classes
- Observe data using Flow
- Keep database access inside the Data layer
- Never expose Room entities outside the Repository

---

# Files Created in This Document

None.

This document defines the Room Database architecture and implementation strategy.

---

# What We Completed

- Designed the local persistence architecture
- Planned Room components
- Defined Entity, DAO, Database, and Type Converter responsibilities
- Planned Local Data Source abstraction
- Established offline data flow
- Planned migration strategy
- Defined testing approach

---

# Next Documents

The Room implementation will be divided into compile-safe milestones.

## 11A_Room_Setup.md

Create:

- Room dependencies verification
- Hilt database module
- Database constants
- Build configuration

---

## 11B_Bookmark_Entity.md

Create:

- `BookmarkEntity.kt`
- Primary key
- Indexed columns
- Entity annotations

---

## 11C_Bookmark_DAO.md

Implement:

- Insert
- Delete
- Update
- Get bookmarks
- Observe Flow
- Exists query

---

## 11D_News_Database.md

Implement:

- `NewsDatabase.kt`
- Singleton Room database
- DAO registration
- Version configuration

---

## 11E_Type_Converters.md

Implement:

- Date converters
- Future-proof JSON converters
- Registration with Room

---

## 11F_Local_DataSource.md

Implement:

- `LocalNewsDataSource.kt`
- DAO abstraction
- Coroutine support
- Repository integration

---

## 11G_Database_Migrations.md

Implement:

- Version migration
- AutoMigration (where applicable)
- Manual migration example
- Migration testing

---

## 11H_Room_Testing.md

Implement:

- In-memory database
- DAO unit tests
- Flow testing
- Migration tests

---

# Prompt for Code Generation

## Prompt: 11A_Room_Setup.md

You are a Senior Android Engineer.

Generate the complete Room setup for the NewsApp project.

### Requirements

1. Verify Room dependencies in Gradle.
2. Configure Room with KSP.
3. Create placeholder Hilt database module.
4. Create database constants.
5. Explain every configuration.
6. Provide complete runnable code.
7. End with build verification steps and Git commit message.

---

## Prompt: 11B_Bookmark_Entity.md

Generate the complete `BookmarkEntity.kt`.

Requirements:

- Use Room annotations.
- Define the table name.
- Add a primary key.
- Add appropriate indexes.
- Include all fields required for offline bookmarking.
- Use immutable properties.
- Explain every annotation and field.
- Provide complete runnable Kotlin code.

---

## Prompt: 11C_Bookmark_DAO.md

Generate the complete `BookmarkDao.kt`.

Requirements:

- Insert bookmark
- Delete bookmark
- Update bookmark
- Observe bookmarks using Flow
- Get bookmark by URL or ID
- Check bookmark existence
- Use suspend functions where appropriate
- Explain every query and annotation.

---

# UI Design Prompt

## Prompt: Offline Bookmarks UX Specification

Design the complete user experience for local bookmarks in NewsApp.

Include:

- Bookmark button states (saved/unsaved)
- Bookmark list layout
- Empty bookmarks screen
- Offline indicator
- Delete confirmation behavior
- Undo via Snackbar
- Loading and synchronization indicators
- Tablet and phone layouts
- Accessibility considerations
- Material 3 interaction patterns

Do **not** generate Jetpack Compose code. Produce a visual specification and interaction guide that will be implemented during the UI phase.