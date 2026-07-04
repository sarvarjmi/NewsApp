# NewsApp

# 08_Retrofit_Implementation.md

> **Phase:** Data Layer
>
> **Module:** Retrofit Networking
>
> **Goal:** Design a production-ready networking architecture using Retrofit, OkHttp, Kotlin Serialization, Hilt, Coroutines, and Clean Architecture.

---

# Objective

This document defines how NewsApp communicates with the News API.

Instead of allowing different parts of the application to make HTTP requests directly, we build a dedicated networking layer that is:

- Scalable
- Testable
- Reusable
- Secure
- Easy to maintain

This layer becomes the single entry point for all remote data.

---

# Why Retrofit?

Retrofit is Google's most widely adopted HTTP client for Android.

Advantages:

- Type-safe REST client
- Coroutine support
- Easy JSON conversion
- Testable interfaces
- Simple annotations
- Excellent Hilt integration
- Works with Paging 3

---

# Network Architecture

```text
Compose UI

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

Retrofit

↓

OkHttpClient

↓

Internet

↓

News API
```

The Presentation layer never communicates directly with Retrofit.

---

# Complete Networking Pipeline

```text
User Action

↓

HomeViewModel

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

Interceptors

↓

Internet

↓

NewsAPI

↓

JSON

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

StateFlow

↓

Compose UI
```

---

# Responsibilities

## Retrofit

Responsible for:

- HTTP API interface
- JSON conversion
- Request execution
- Coroutine integration

---

## OkHttp

Responsible for:

- HTTP client
- Timeouts
- Headers
- Logging
- Retry
- Interceptors

---

## RemoteDataSource

Responsible for:

- Calling Retrofit
- Returning NetworkResult
- No business logic

---

## Repository

Responsible for:

- Combining Remote + Local
- Mapping DTO → Domain
- Cache strategy
- Offline support

---

# Networking Components

The networking layer consists of:

```text
ApiConstants

↓

RetrofitProvider

↓

OkHttpClient

↓

Interceptors

↓

NewsApiService

↓

SafeApiCall

↓

NetworkResult

↓

RemoteDataSource
```

---

# Retrofit Builder

The Retrofit builder will configure:

- Base URL
- JSON Converter
- OkHttp Client
- Coroutine Support

Retrofit itself contains no business logic.

---

# OkHttp Configuration

OkHttp will be configured with:

- API Key Interceptor
- Logging Interceptor
- Header Interceptor
- Connectivity Interceptor
- Retry Policy
- Timeout Policy

Each interceptor has a single responsibility.

---

# API Constants

The networking layer will centralize:

- Base URL
- API Version
- Endpoint Paths
- Query Keys
- Default Page Size
- Language

Never hardcode values throughout the project.

---

# Authentication Strategy

The API key will never be hardcoded inside ViewModels or repositories.

Recommended flow:

```text
local.properties

↓

BuildConfig

↓

ApiKeyInterceptor

↓

HTTP Header / Query Parameter
```

Benefits:

- Keeps secrets out of source code
- Easier CI/CD integration
- Safer open-source repository

---

# JSON Serialization

The project uses Kotlin Serialization.

Flow:

```text
JSON

↓

Serializable DTO

↓

Mapper

↓

Domain Model
```

DTOs never leave the Data layer.

---

# DTO Mapping

```text
ArticleDto

↓

ArticleMapper

↓

Article
```

Reasons:

- Decouple API changes
- Stable Domain models
- Easier testing

---

# Safe API Calls

No Retrofit exception should ever reach the UI.

Instead:

```text
Retrofit

↓

SafeApiCall

↓

NetworkResult

↓

Repository
```

The UI receives predictable states instead of exceptions.

---

# NetworkResult

Every request returns:

```text
Loading

Success

Error
```

Advantages:

- Cleaner ViewModels
- Better testing
- Consistent UI

---

# Pagination

The API supports:

```text
page

pageSize
```

Paging 3 will use these values through a custom `PagingSource`.

---

# Search

Search requests include:

```text
query

page

pageSize

language

sortBy
```

Search uses Kotlin Flow with debounce in the Presentation layer.

---

# Timeout Strategy

Recommended values:

| Type | Value |
|------|-------|
| Connect | 30 sec |
| Read | 30 sec |
| Write | 30 sec |

---

# Retry Strategy

Retry only transient failures.

Retry:

- HTTP 500
- HTTP 502
- HTTP 503
- Network timeout

Do not retry:

- 401
- 403
- 404

---

# Logging Strategy

Debug builds:

- Full request logs
- Full response logs

Release builds:

- Disable body logging
- Log only critical failures

---

# Security Considerations

The networking layer should:

- Use HTTPS only
- Hide API keys
- Disable verbose logging in release
- Validate SSL certificates
- Support future certificate pinning

---

# Offline Strategy

```text
Internet Available

↓

Remote API

↓

Room Cache

↓

UI
```

```text
No Internet

↓

Room Cache

↓

UI
```

The Repository decides which source to use.

---

# Folder Structure

```text
data

└── remote

    ├── api
    ├── datasource
    ├── dto
    ├── interceptor
    ├── mapper
    ├── response
    └── paging

core

└── network

    ApiConstants.kt

    RetrofitProvider.kt

    SafeApiCall.kt

core

└── result

    NetworkResult.kt
```

---

# Planned Kotlin Files

The following files will be implemented in subsequent documents.

```text
core/network/

ApiConstants.kt

RetrofitProvider.kt

SerializationConfig.kt

SafeApiCall.kt

--------------------------------

data/remote/api/

NewsApiService.kt

--------------------------------

data/remote/interceptor/

ApiKeyInterceptor.kt

HeaderInterceptor.kt

LoggingInterceptor.kt

ConnectivityInterceptor.kt

--------------------------------

core/result/

NetworkResult.kt

--------------------------------

data/remote/datasource/

RemoteNewsDataSource.kt
```

---

# Build Verification Goals

After all networking implementation documents are complete:

- Project builds successfully
- Retrofit instance created
- API service injectable via Hilt
- NetworkResult returned correctly
- SafeApiCall handles exceptions
- API key injected securely
- MockWebServer tests pass

---

# Files Created

None.

This document defines the Retrofit implementation architecture.

---

# What We Completed

- Designed the Retrofit networking layer
- Defined Retrofit and OkHttp responsibilities
- Planned API authentication
- Planned serialization strategy
- Designed SafeApiCall architecture
- Planned NetworkResult handling
- Defined pagination and search support
- Established security and testing strategies

---

# Next Documents

The implementation will be divided into compile-safe milestones.

## 08A_Api_Configuration.md

Create:

- `ApiConstants.kt`
- BuildConfig API key
- Base URL
- Query constants
- API configuration

---

## 08B_Retrofit_Client.md

Implement:

- `RetrofitProvider.kt`
- Kotlin Serialization
- Retrofit Builder
- Hilt integration

---

## 08C_OkHttp_Client.md

Implement:

- OkHttpClient
- LoggingInterceptor
- ApiKeyInterceptor
- HeaderInterceptor
- ConnectivityInterceptor
- Timeout configuration

---

## 08D_NewsApi_Service.md

Implement:

- NewsApiService
- Top Headlines endpoint
- Search endpoint
- Pagination
- Endpoint documentation

---

## 08E_Network_Result.md

Implement:

- NetworkResult
- Result states
- Error models

---

## 08F_SafeApiCall.md

Implement:

- SafeApiCall
- Exception mapping
- HTTP error handling
- Serialization handling

---

## 08G_Remote_DataSource.md

Implement:

- RemoteNewsDataSource
- Coroutine integration
- Repository communication

---

## 08H_Retrofit_Testing.md

Implement:

- MockWebServer
- Fake API responses
- Success tests
- Failure tests
- Timeout tests

---

# Prompt for Code Generation

## Prompt: 08A_Api_Configuration.md

You are a Senior Android Engineer building the NewsApp project.

Generate the complete API configuration layer.

Requirements:

1. Create:
    - `ApiConstants.kt`
    - `BuildConfig` fields
    - `ApiConfig.kt`
2. Read API keys securely from `local.properties`.
3. Expose only required constants.
4. Support Debug and Release builds.
5. Explain every constant and configuration.
6. Provide complete runnable Kotlin and Gradle code.
7. End with build verification steps and Git commit message.

---

## Prompt: 08B_Retrofit_Client.md

Generate the complete Retrofit client.

Requirements:

- Singleton Retrofit instance
- Kotlin Serialization
- Hilt provider
- Base URL
- Converter Factory
- Explain every line
- Production-ready implementation

---

## Prompt: 08C_OkHttp_Client.md

Generate the complete OkHttp client.

Requirements:

- LoggingInterceptor
- API Key Interceptor
- HeaderInterceptor
- ConnectivityInterceptor
- Timeout strategy
- Retry policy
- Hilt module integration
- Complete runnable code

---

## Prompt: 08D_NewsApi_Service.md

Generate the complete `NewsApiService.kt`.

Requirements:

- Top Headlines endpoint
- Search endpoint
- Pagination
- Suspend functions
- Kotlin Serialization DTO support
- Full Retrofit annotations
- Explain every endpoint

---

# UI Design Prompt

## Prompt: Loading, Error & Refresh UX Specification

Design the complete user experience for all network-driven UI states in NewsApp.

Include:

- Initial loading screen
- Skeleton shimmer cards
- Pull-to-refresh interaction
- Infinite scroll loading footer
- Empty search results
- Offline state
- Timeout state
- Generic server error
- Retry button behavior
- Snackbar messaging
- Accessibility considerations
- Phone and tablet layouts

Do **not** generate Jetpack Compose code. Produce a detailed visual and interaction specification that will be implemented in later UI documents.