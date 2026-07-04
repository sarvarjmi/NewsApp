# NewsApp

> **Phase:** Data Layer
>
> **Module:** Networking Foundation
>
> **Goal:** Design and implement a robust, scalable, secure, and testable networking layer using Retrofit, OkHttp, Kotlin Serialization, Coroutines, and Clean Architecture.

---

# Objective

The networking layer is responsible for communicating with the News API and providing clean, reliable data to the Repository layer.

A production-ready networking layer should:

- Be reusable
- Be testable
- Handle failures gracefully
- Support logging
- Support authentication
- Support timeouts
- Support retry policies
- Support pagination
- Be independent of UI
- Follow SOLID principles

At the end of this phase, the networking foundation will be ready for integrating Retrofit services and repositories.

---

# Network Layer Responsibilities

The Network Layer is responsible for:

- HTTP communication
- API authentication
- Request creation
- Response parsing
- Error mapping
- Connectivity handling
- Logging
- Retry strategy
- Timeout configuration
- API abstraction

The UI must never communicate directly with Retrofit.

---

# Architecture

```text
Presentation

↓

ViewModel

↓

UseCase

↓

Repository

↓

RemoteDataSource

↓

NewsApiService

↓

OkHttpClient

↓

Internet

↓

News API
```

---

# Network Flow

```text
User

↓

Compose Screen

↓

ViewModel

↓

GetTopHeadlinesUseCase

↓

NewsRepository

↓

RemoteNewsDataSource

↓

NewsApiService

↓

Retrofit

↓

OkHttp

↓

Internet

↓

NewsAPI Server

↓

JSON Response

↓

DTO

↓

Mapper

↓

Domain Model

↓

Repository

↓

ViewModel

↓

Compose UI
```

---

# Networking Stack

The project will use the following libraries:

| Library | Purpose |
|----------|----------|
| Retrofit | REST API Client |
| OkHttp | HTTP Client |
| Kotlin Serialization | JSON Parsing |
| Logging Interceptor | Request/Response Logs |
| Coroutines | Async Calls |
| Flow | Reactive Streams |
| Timber | Logging |

---

# Why Retrofit?

Retrofit provides:

- Type-safe API
- Coroutine support
- Converter support
- Easy testing
- Clean interface definitions

Example:

```kotlin
@GET("top-headlines")
suspend fun getTopHeadlines(...)
```

---

# Why OkHttp?

OkHttp handles:

- HTTP requests
- HTTPS
- Connection pooling
- Retry
- Compression
- Interceptors
- Timeouts

---

# Why Kotlin Serialization?

Advantages:

- Official Kotlin support
- Fast
- Null-safe
- Type-safe
- Smaller runtime
- Better Compose integration

---

# Remote Layer Structure

```text
data

└── remote

    ├── api

    ├── dto

    ├── mapper

    ├── interceptor

    ├── response

    ├── datasource

    ├── service

    └── paging
```

---

# Package Responsibilities

## api

Contains Retrofit interface.

Example

```
NewsApiService.kt
```

---

## dto

Contains response models.

Example

```
ArticleDto.kt

SourceDto.kt

NewsResponseDto.kt
```

---

## mapper

Maps DTO → Domain.

---

## interceptor

Contains OkHttp interceptors.

Example

```
ApiKeyInterceptor

LoggingInterceptor

ConnectivityInterceptor
```

---

## response

Contains wrappers.

Example

```
ApiResponse

ErrorResponse
```

---

## datasource

Contains RemoteDataSource implementation.

---

## paging

Contains PagingSource.

---

# HTTP Request Lifecycle

```text
ViewModel

↓

Repository

↓

RemoteDataSource

↓

Retrofit

↓

OkHttp

↓

Interceptor

↓

Internet

↓

API

↓

Response

↓

DTO

↓

Mapper

↓

Repository

↓

ViewModel
```

---

# Interceptors

The project will use multiple interceptors.

## Logging Interceptor

Purpose:

- Debug requests
- Debug responses
- View headers
- View body

Enabled only in Debug builds.

---

## API Key Interceptor

Automatically attaches:

```
apiKey=YOUR_API_KEY
```

to every request.

No ViewModel should know about the API key.

---

## Connectivity Interceptor

Checks internet availability.

Throws:

```
NoInternetException
```

before making the request.

---

## Header Interceptor

Adds common headers.

Example:

```
Accept

Content-Type

User-Agent
```

---

# Timeout Strategy

Recommended:

| Timeout | Value |
|----------|-------|
| Connect | 30 sec |
| Read | 30 sec |
| Write | 30 sec |

---

# Retry Strategy

The networking layer should retry only safe requests.

Example:

```
HTTP 500

↓

Retry

↓

Success
```

Avoid retrying:

- 401
- 403
- 404

---

# Response Parsing

Server

↓

JSON

↓

DTO

↓

Mapper

↓

Domain Model

Never expose DTOs outside the Data layer.

---

# Error Handling

Every network call returns:

```text
NetworkResult
```

Instead of throwing exceptions.

Example:

```text
Loading

Success

Error
```

Benefits:

- Predictable UI
- Better testing
- Cleaner ViewModels

---

# NetworkResult Flow

```text
Retrofit

↓

SafeApiCall

↓

NetworkResult

↓

Repository

↓

UseCase

↓

ViewModel

↓

UI
```

---

# Offline Strategy

If internet is unavailable:

```
Repository

↓

Room

↓

Cached Articles
```

Otherwise:

```
API

↓

Room Cache

↓

UI
```

---

# Security

Best practices:

- HTTPS only
- Hide API Key
- Do not log secrets
- Enable ProGuard
- Certificate Pinning (future enhancement)

---

# API Key Management

Never hardcode:

```kotlin
const val API_KEY = "..."
```

Instead:

- BuildConfig field
- Local properties
- CI/CD secrets

---

# Pagination Support

Networking layer must support:

```
page

pageSize
```

This enables Paging 3.

---

# Search Support

API should support:

```
query

page

pageSize

language
```

---

# Common Network Errors

| Error | Handling |
|--------|----------|
| No Internet | Show Retry UI |
| Timeout | Retry Button |
| HTTP 401 | Authentication Error |
| HTTP 403 | Forbidden |
| HTTP 404 | Resource Not Found |
| HTTP 429 | Rate Limit |
| HTTP 500 | Retry |
| Serialization Error | Generic Error |

---

# Logging Strategy

Development:

- Full logs

Production:

- Disable body logs
- Log only critical errors

---

# Testing Strategy

Network layer should support:

- Fake API
- MockWebServer
- Repository testing
- Integration testing

No UI dependency should exist.

---

# Performance Considerations

- Reuse OkHttpClient
- Singleton Retrofit
- Enable gzip
- Connection pooling
- Avoid blocking calls
- Use suspend functions
- Stream large responses

---

# Planned Kotlin Files

The following files will be implemented across the next networking documents.

```text
data/remote/api

NewsApiService.kt

------------------------------------

data/remote/dto

ArticleDto.kt

SourceDto.kt

NewsResponseDto.kt

------------------------------------

data/remote/interceptor

ApiKeyInterceptor.kt

ConnectivityInterceptor.kt

------------------------------------

data/remote/mapper

ArticleMapper.kt

------------------------------------

data/remote/datasource

RemoteNewsDataSource.kt

------------------------------------

core/network

SafeApiCall.kt

NetworkMonitor.kt

------------------------------------

core/result

NetworkResult.kt
```

---

# Files Created in This Document

None.

This document defines the networking architecture and implementation strategy.

---

# What We Completed

- Designed the networking architecture
- Planned Retrofit integration
- Planned OkHttp configuration
- Defined interceptor strategy
- Designed error handling approach
- Planned pagination support
- Planned search support
- Defined testing strategy
- Planned security practices

---

# Next Documents

The networking implementation will be divided into compile-safe milestones.

## 07A_Retrofit_Setup.md

Configure:

- Retrofit
- Kotlin Serialization
- Converter Factory
- Base URL
- Retrofit Builder

---

## 07B_OkHttp_Configuration.md

Implement:

- OkHttpClient
- LoggingInterceptor
- Timeout
- Retry Policy
- API Key Interceptor
- Header Interceptor

---

## 07C_Api_Service.md

Create:

- NewsApiService
- Endpoints
- Query Parameters
- Pagination API
- Search API

---

## 07D_DTO_Models.md

Create:

- NewsResponseDto
- ArticleDto
- SourceDto

---

## 07E_Network_Result.md

Implement:

- NetworkResult
- SafeApiCall
- Exception Mapping
- Error Models

---

## 07F_Remote_Data_Source.md

Implement:

- RemoteNewsDataSource
- Repository Communication
- Coroutine Integration

---

## 07G_Network_Testing.md

Implement:

- MockWebServer
- Fake Responses
- API Tests
- Error Tests

---

# Prompt for Code Generation

## Prompt: 07A_Retrofit_Setup.md

You are a Senior Android Engineer.

Generate the complete Retrofit setup for the **NewsApp** project.

### Requirements

1. Configure Retrofit using Kotlin Serialization.
2. Create:
    - `RetrofitProvider.kt`
    - `ApiConstants.kt`
    - `SerializationConfig.kt`
3. Use Hilt to provide Retrofit as a singleton.
4. Read the base URL from a constant or BuildConfig.
5. Explain every configuration line.
6. Provide complete runnable Kotlin code.
7. Include build verification steps and Git commit message.

---

## Prompt: 07B_OkHttp_Configuration.md

Generate the complete OkHttp configuration.

Requirements:

- Singleton OkHttpClient
- LoggingInterceptor
- API Key Interceptor
- Connectivity Interceptor
- Header Interceptor
- Timeout configuration
- Retry strategy
- Hilt module integration
- Production-ready implementation with explanations.

---

## Prompt: 07C_Api_Service.md

Generate a complete `NewsApiService.kt` using the selected News API.

Requirements:

- Top Headlines endpoint
- Search endpoint
- Pagination support
- Query parameters
- Suspend functions
- Kotlin Serialization models
- Full Retrofit annotations
- Explanation of every endpoint.

---

# UI Design Prompt

## Prompt: Network State UI Specification

Design reusable UI states that represent network operations throughout the application.

Include wireframes and interaction guidelines for:

- Full-screen loading
- Inline loading
- Shimmer placeholders
- Empty state
- No internet screen
- Timeout screen
- Server error screen
- Retry interaction
- Snackbar error messages
- Pull-to-refresh indicator
- Pagination loading footer

Follow Material 3 guidelines, accessibility standards, and responsive layouts. Do **not** generate Jetpack Compose code yet—produce a visual specification to guide later implementation.