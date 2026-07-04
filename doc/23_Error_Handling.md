# NewsApp

# 23_Error_Handling.md

> **Phase:** Cross-Cutting Architecture
>
> **Module:** Error Handling & Resilience
>
> **Goal:** Design a centralized, scalable, user-friendly error handling architecture for the NewsApp that gracefully handles failures across Networking, Database, Paging, Navigation, WebView, and UI while following Clean Architecture principles.

---

# Objective

No production application is free from failures.

Users may experience:

- No Internet
- API Errors
- Server Errors
- Timeout
- Database Errors
- Parsing Errors
- Paging Errors
- Unknown Exceptions

Instead of handling these independently in every screen, NewsApp will implement a **Centralized Error Handling Framework**.

After completing this phase the application will have:

- Centralized Error Model
- Error Mapper
- Global Error Handler
- User Friendly Messages
- Retry Mechanism
- Logging Strategy
- Analytics Hooks
- Offline Recovery
- Crash Prevention

---

# Why Centralized Error Handling?

Without Architecture

```text
Home

↓

try-catch

Search

↓

try-catch

Bookmarks

↓

try-catch
```

Problems

- Duplicate code

- Different error messages

- Difficult testing

- Difficult maintenance

---

With Centralized Error Handling

```text
Network

↓

Error Mapper

↓

AppError

↓

Repository

↓

UseCase

↓

ViewModel

↓

UI
```

Benefits

- Consistency

- Reusable

- Testable

- Maintainable

---

# Error Flow

```text
Retrofit

↓

Exception

↓

SafeApiCall

↓

NetworkResult

↓

Repository

↓

AppError

↓

UiState

↓

Compose
```

---

# Error Categories

The application supports the following errors.

## Network Errors

Examples

- No Internet

- Timeout

- Connection Lost

---

## HTTP Errors

Examples

- 400

- 401

- 403

- 404

- 429

- 500

- 503

---

## Serialization Errors

Examples

- Invalid JSON

- Missing fields

- Parsing failures

---

## Database Errors

Examples

- Room Exception

- Constraint Violation

- Migration Failure

---

## Paging Errors

Examples

- Refresh Error

- Append Error

- LoadState Error

---

## WebView Errors

Examples

- SSL Error

- Invalid URL

- Page Not Found

---

## Unknown Errors

Fallback

```text
Something went wrong.
```

---

# Error Hierarchy

```text
Throwable

↓

AppError

│

├── NetworkError

├── ApiError

├── DatabaseError

├── ValidationError

├── PagingError

├── WebError

└── UnknownError
```

---

# AppError Model

Every layer communicates using AppError.

Example

```text
NoInternet

↓

AppError.Network
```

The UI never receives Retrofit exceptions directly.

---

# Network Error Flow

```text
Retrofit

↓

IOException

↓

SafeApiCall

↓

NetworkError

↓

Repository

↓

UiState.Error
```

---

# HTTP Mapping

| HTTP | UI Message |
|-------|------------|
|400|Invalid Request|
|401|Authentication Failed|
|403|Access Denied|
|404|Article Not Found|
|408|Request Timeout|
|429|Too Many Requests|
|500|Server Error|
|503|Service Unavailable|

---

# User-Friendly Messages

Technical

```
SocketTimeoutException
```

↓

User

```
Request timed out.
Please try again.
```

Never expose stack traces.

---

# Retry Strategy

Retry supported for

- Timeout

- No Internet

- HTTP 500

- HTTP 503

Do not retry

- 401

- 403

- Invalid Request

---

# UI Error States

Each screen supports

```text
Loading

↓

Success

↓

Error

↓

Retry

↓

Success
```

---

# Error Components

Reusable UI

```text
ErrorView

RetryButton

OfflineBanner

Snackbar

Dialog
```

---

# Snackbar Strategy

Use Snackbar for

- Bookmark Saved

- Bookmark Removed

- Temporary Errors

---

# Dialog Strategy

Dialogs

Only for critical situations

Examples

- Delete Confirmation

- Session Expired

---

# Full Screen Error

Display

- Illustration

- Message

- Retry

- Home Button (optional)

Used for

- Home

- Search

- WebView

---

# Paging Errors

Refresh Error

↓

Full Screen

Append Error

↓

Footer Retry

---

# Offline Strategy

When internet unavailable

```text
Room

↓

Bookmarks

↓

Offline Banner
```

---

# Logging Strategy

Development

- Timber

- Logcat

Production

- Crash Reporting

- Analytics

Never log

- API Keys

- Tokens

- Personal Data

---

# Analytics Hooks

Future Integration

- Firebase Crashlytics

- Analytics

- Sentry

Track

- API Failures

- Database Failures

- Screen Errors

---

# Global Exception Handler

Application level

```text
CoroutineExceptionHandler

↓

Logger

↓

AppError

↓

UiState
```

Avoid application crashes whenever possible.

---

# Validation Errors

Examples

- Empty Search

- Invalid URL

- Missing Parameters

These are handled before network requests.

---

# Connectivity Monitoring

Use

```text
ConnectivityManager

↓

NetworkMonitor

↓

StateFlow

↓

Offline Banner
```

The application reacts automatically when connectivity changes.

---

# Error Recovery

Flow

```text
Error

↓

Retry

↓

Reload

↓

Success
```

Recovery should be seamless.

---

# Folder Structure

```text
core

error

├── AppError.kt

├── ErrorMapper.kt

├── ErrorHandler.kt

├── NetworkError.kt

├── DatabaseError.kt

├── ValidationError.kt

└── UnknownError.kt

--------------------------------

presentation

components

ErrorView.kt

OfflineBanner.kt

RetryButton.kt

SnackbarManager.kt
```

---

# Architecture Diagram

```text
Throwable

↓

AppError

↓

Repository

↓

UseCase

↓

ViewModel

↓

UiState

↓

Compose
```

---

# Best Practices

- Never expose exceptions to UI

- Centralize error mapping

- Use user-friendly messages

- Retry only recoverable failures

- Keep logging separate

- Test every error path

- Handle offline mode gracefully

---

# Testing Strategy

Test

- Network failures

- HTTP errors

- Database failures

- Paging failures

- Offline mode

- Retry

- Error mapping

- Snackbar messages

Use

- MockWebServer

- Fake Repository

- Turbine

- Compose Tests

---

# Build Verification

After implementation

- Network errors show correct UI

- Retry works

- Offline banner appears

- Paging errors handled

- Database errors handled

- Unknown exceptions handled

- No crashes from expected failures

---

# Files Created

None.

This document defines the global error handling architecture.

---

# What We Completed

✅ Designed centralized error handling

✅ Defined AppError hierarchy

✅ Planned retry strategy

✅ Planned UI error components

✅ Planned logging strategy

✅ Planned connectivity monitoring

✅ Planned analytics integration

---

# Next Documents

The error handling implementation will be divided into compile-safe milestones.

---

## 23A_AppError.md

Create

- `AppError.kt`

- Error hierarchy

- Sealed interface/classes

---

## 23B_ErrorMapper.md

Implement

- `ErrorMapper.kt`

- Map Exceptions → AppError

- HTTP mapping

- Serialization mapping

---

## 23C_SafeApiCall.md

Implement

- Generic SafeApiCall

- Exception handling

- NetworkResult conversion

---

## 23D_Global_Error_Handler.md

Implement

- CoroutineExceptionHandler

- Global logging

- Error reporting

---

## 23E_Network_Monitor.md

Implement

- ConnectivityManager

- StateFlow

- Offline detection

- Network callbacks

---

## 23F_UI_Error_Components.md

Implement

- ErrorView

- OfflineBanner

- RetryButton

- SnackbarManager

---

## 23G_Error_Recovery.md

Implement

- Retry framework

- Paging retry

- Pull-to-refresh recovery

- Automatic reconnect

---

## 23H_Crash_Reporting.md

Implement

- Timber

- Crashlytics hooks

- Analytics logging

- Production logging rules

---

## 23I_Error_Testing.md

Implement

- Unit tests

- Integration tests

- Compose UI tests

- Offline tests

- Paging tests

---

# Prompt for Code Generation

## Prompt: 23A_AppError.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete `AppError.kt`.

### Requirements

1. Create a sealed interface (or sealed class) representing all application errors.
2. Include:
    - NetworkError
    - ApiError
    - DatabaseError
    - ValidationError
    - PagingError
    - WebViewError
    - UnknownError
3. Each error should contain:
    - User-friendly message
    - Optional error code
    - Original throwable (when appropriate)
4. Add KDoc documentation.
5. Explain every error type.
6. Provide complete runnable Kotlin code.

---

## Prompt: 23B_ErrorMapper.md

Generate the complete `ErrorMapper.kt`.

Requirements:

- Convert Exceptions into AppError.
- Handle:
    - IOException
    - SocketTimeoutException
    - HttpException
    - SerializationException
    - SQLiteException
    - IllegalArgumentException
    - UnknownException
- Map HTTP codes to user-friendly messages.
- Provide complete runnable Kotlin code with explanations.

---

## Prompt: 23C_SafeApiCall.md

Generate the complete SafeApiCall implementation.

Requirements:

- Generic coroutine wrapper.
- Convert exceptions into NetworkResult.
- Integrate ErrorMapper.
- Support suspend functions.
- Explain every implementation detail.
- Provide complete runnable Kotlin code.

---

## Prompt: 23F_UI_Error_Components.md

Generate reusable Compose error components.

Requirements:

Create:

- `ErrorView.kt`
- `OfflineBanner.kt`
- `RetryButton.kt`
- `SnackbarManager.kt`

Requirements:

- Material 3 design
- Accessibility support
- Dark mode support
- Stateless composables
- Preview composables
- Complete runnable Compose code with explanations.

---

# UI Design Prompt

## Prompt: Error Handling UX Specification

Design the complete error handling experience for **NewsApp**.

### Include

1. Full-screen error state
2. Inline error state
3. Offline banner
4. Snackbar specifications
5. Retry button behavior
6. Loading-to-error transitions
7. Paging footer error
8. Empty state vs Error state distinction
9. Dialogs for critical failures
10. WebView error screen
11. Tablet and phone layouts
12. Dark mode appearance
13. Motion and animation guidelines
14. Accessibility (TalkBack announcements, focus management)
15. Material 3 spacing, typography, elevation, and color usage

Do **not** generate Jetpack Compose code. Produce a comprehensive UX specification that defines how every error scenario should be presented consistently across the entire application.