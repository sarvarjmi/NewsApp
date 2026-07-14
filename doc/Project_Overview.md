# NewsApp — Project Overview

> Synthesized from the 30 architecture/planning documents in this repository (`01_Project_Overview.md` through `29_Final_Review.md`, plus `25H_Production_Best_Practices.md` and `26D_AppLinks_Configuration.md`).

---

## 1. Project Goals

NewsApp is a modern Android news-reader application built to demonstrate production-level Android engineering practices for a Senior Android Developer (SDE3) assignment. The stated goals across the documentation are:

- Build a complete news reader that fetches, paginates, searches, bookmarks, and displays articles from a public news API (NewsAPI is the recommended/selected choice, with GNews, Mediastack, and Currents API listed as alternatives).
- Demonstrate **production-ready architecture** using Clean Architecture and MVVM rather than a quick prototype.
- Achieve a **scalable folder structure**, **robust error handling**, **efficient state management**, and a **smooth, offline-capable user experience**.
- Reach **high test coverage** across every layer (target: >85% overall, 95% for Domain/Utilities, 90% for Repository/ViewModel).
- Apply **modern Android best practices**: Kotlin, Jetpack Compose, Material 3, Hilt, Retrofit, Room, Paging 3, Coroutines/Flow, Navigation Compose, WorkManager, and Deep Linking.
- Produce a project that could realistically be shipped to production, not just satisfy an assignment rubric — including performance budgets, accessibility, and security considerations.

---

## 2. Technology Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose + Material Design 3 |
| Architecture | Clean Architecture + MVVM |
| Dependency Injection | Hilt (Dagger-based) |
| Networking | Retrofit + OkHttp |
| Serialization | Kotlin Serialization (preferred over Gson/Moshi) |
| Local Persistence | Room (SQLite) |
| Image Loading | Coil (Compose) |
| Pagination | Paging 3 |
| Concurrency | Kotlin Coroutines |
| Reactive Streams | Kotlin Flow, StateFlow, SharedFlow |
| Navigation | Navigation Compose |
| Background Work | WorkManager (CoroutineWorker + Hilt) |
| Logging | Timber |
| Testing | JUnit, MockK, Turbine, MockWebServer, Compose UI Testing, Espresso, WorkManager Test |
| Static Analysis | Detekt, Ktlint, Android Lint |
| Build System | Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`) + KSP (preferred over KAPT) |
| Min SDK | 24 (Android 7.0) — Target/Compile SDK: latest stable |

---

## 3. Project Architecture

NewsApp follows **Clean Architecture** layered with **MVVM** in the Presentation layer, structured as three concentric layers where dependencies always point inward:

```text
Presentation Layer  →  Compose Screens, ViewModels, UiState, UiEvent, Side Effects
        ↓ (calls Use Cases)
Domain Layer         →  Business Logic, Repository Interfaces, Domain Models
        ↑ (implemented by)
Data Layer           →  Retrofit, Room, DTOs, Entities, Mappers, Repository Impl
```

**The Dependency Rule:** Presentation depends on Domain; Domain never depends on Presentation or Data; Data implements the contracts (interfaces) that Domain defines. This keeps the Domain layer pure Kotlin with zero Android framework dependencies, making business logic trivially unit-testable.

**High-level data flow:**

```text
User → Compose Screen → ViewModel → UseCase → Repository → (Remote API + Room DB) → Mapper → Domain Model → ViewModel (StateFlow) → Compose UI
```

**Key architectural mechanisms:**

- **Repository as Single Source of Truth (SSOT):** the Repository is the only component that decides whether data comes from the network or the local cache; ViewModels and UseCases never know or care.
- **Unidirectional Data Flow (UDF):** user actions become `UiEvent`s, the ViewModel processes them via UseCases and emits a new immutable `UiState`, which Compose renders. One-time actions (navigation, snackbars, share intents) are modeled as side effects via `SharedFlow`/`Channel`, kept separate from persistent `StateFlow` state.
- **NetworkResult / AppError sealed hierarchies:** raw exceptions (`IOException`, `HttpException`, `SerializationException`, Room exceptions) are never allowed to reach the UI. They are intercepted by a `SafeApiCall` wrapper and mapped into a centralized `AppError` sealed hierarchy (`NetworkError`, `ApiError`, `DatabaseError`, `ValidationError`, `PagingError`, `WebViewError`, `UnknownError`), each carrying a user-friendly message.
- **Offline strategy:** when online, the Repository fetches from the API (optionally refreshing the Room cache); when offline, it serves cached/bookmarked data from Room. Bookmarks are always Room-backed and available offline.

---

## 4. Folder Structure

The project uses a **hybrid layer + feature structure**: `data`, `domain`, and `core` are organized by architectural layer, while `presentation` is organized by feature (screen).

```text
com.newsapp
├── core
│   ├── common          (UiState/UiEvent/BaseViewModel helpers)
│   ├── constants        (base URLs, API keys, DB names)
│   ├── dispatcher       (DispatcherProvider, IO/Default/Main qualifiers)
│   ├── error            (AppError, ErrorMapper, ErrorHandler)
│   ├── extensions        (Kotlin extension functions)
│   ├── network           (ApiConstants, RetrofitProvider, SafeApiCall, NetworkMonitor)
│   ├── result             (NetworkResult wrapper)
│   ├── util                (DateFormatter, TimeUtils, Logger)
│   └── validation
│
├── data
│   ├── remote
│   │   ├── api            (NewsApiService — Retrofit interface)
│   │   ├── dto             (NewsResponseDto, ArticleDto, SourceDto)
│   │   ├── mapper          (DTO → Domain mappers)
│   │   ├── interceptor      (ApiKeyInterceptor, ConnectivityInterceptor, LoggingInterceptor, HeaderInterceptor)
│   │   ├── paging            (NewsPagingSource)
│   │   └── response
│   ├── local
│   │   ├── database         (NewsDatabase — Room)
│   │   ├── dao                (BookmarkDao)
│   │   ├── entity             (BookmarkEntity)
│   │   ├── converter          (Room TypeConverters)
│   │   └── mapper             (Entity ↔ Domain mappers)
│   ├── datasource
│   │   ├── remote            (RemoteNewsDataSource)
│   │   └── local              (LocalNewsDataSource)
│   └── repository            (NewsRepositoryImpl)
│
├── domain
│   ├── model                (Article, Source, Bookmark — pure Kotlin)
│   ├── repository            (NewsRepository interface)
│   └── usecase
│       ├── news                (GetTopHeadlinesUseCase, RefreshNewsUseCase)
│       ├── search               (SearchNewsUseCase)
│       └── bookmark              (AddBookmarkUseCase, RemoveBookmarkUseCase, ToggleBookmarkUseCase, ObserveBookmarksUseCase, IsBookmarkedUseCase)
│
├── presentation
│   ├── home / search / detail / bookmark / webview   (Screen.kt, ViewModel.kt, UiState.kt, Event.kt, SideEffect.kt per feature)
│   ├── navigation             (NavGraph, Routes, Destination, BottomBar, DeepLinks)
│   ├── theme                  (Theme, Color, Typography, Shapes, Dimens, Elevation)
│   └── components              (NewsCard, SearchBar, LoadingShimmer, ErrorView, EmptyState, BookmarkButton, RetryButton)
│
├── background
│   ├── worker                  (RefreshNewsWorker, CacheCleanupWorker)
│   └── scheduler                (WorkScheduler, WorkConstraints)
│
├── di                           (NetworkModule, DatabaseModule, RepositoryModule, DispatcherModule)
├── util
└── test / androidTest            (unit, integration, and instrumented tests mirroring the structure above)
```

---

## 5. Design Patterns Used

- **Clean Architecture** — strict separation into Presentation / Domain / Data layers with the Dependency Rule enforced.
- **MVVM (Model-View-ViewModel)** — applied within the Presentation layer; ViewModels expose `StateFlow<UiState>` and never hold UI references.
- **Repository Pattern** — `NewsRepository` interface (Domain) + `NewsRepositoryImpl` (Data) acting as the Single Source of Truth over Remote + Local data sources.
- **UseCase / Command Pattern** — every business operation is a dedicated class exposing `operator fun invoke(...)`, keeping ViewModels thin and business rules reusable/testable.
- **Dependency Injection / Dependency Inversion** — Hilt wires the entire graph; high-level modules (ViewModels) depend on abstractions (`NewsRepository` interface), not concrete implementations.
- **Single Source of Truth (SSOT)** — the Repository, not the UI or ViewModel, decides where data originates.
- **Observer Pattern** — Kotlin `Flow`/`StateFlow`/`SharedFlow` propagate state changes reactively from Room and the ViewModel to Compose.
- **Mapper / Adapter Pattern** — dedicated one-way mappers convert between DTO ↔ Domain ↔ Entity ↔ UI Model, implemented as Kotlin extension functions (`toDomain()`, `toEntity()`, etc.) so no layer's model type leaks into another.
- **Sealed Class State Modeling** — `NetworkResult`, `AppError`, and every screen's `UiState` are sealed types representing Loading/Success/Empty/Error, enabling exhaustive `when` handling.
- **Factory Pattern** — Hilt `@Provides` modules construct singletons (Retrofit, Room database, OkHttpClient).
- **Unidirectional Data Flow (UDF)** — a specific application of the Observer/Event patterns constraining state mutation to one direction: Event → ViewModel → UseCase → Repository → State → UI.

---

## 6. Key Features

- **Home feed** — latest headlines with Paging 3 infinite scroll, pull-to-refresh, shimmer loading, and full/footer error states with retry.
- **Search** — debounced (500ms) live search with `distinctUntilChanged()`, its own paginated results, and empty-query/empty-result states.
- **Article Detail** — hero image, source/date/reading-time metadata, bookmark toggle, share sheet, and "read full article" action.
- **Article Reader** — Chrome Custom Tabs preferred, with an in-app WebView and external-browser fallback for full article content the API truncates.
- **Bookmarks** — Room-backed, fully offline, with swipe/undo-delete via Snackbar.
- **Deep Links & App Links** — verified HTTPS App Links (`https://newsapp.com/article/{id}`, `/search?q=`, `/bookmarks`) plus a custom `newsapp://` scheme, with `assetlinks.json` domain verification.
- **Background Sync** — WorkManager `CoroutineWorker` periodically refreshes headlines and cleans expired cache under network/battery constraints.
- **Centralized Error Handling** — every failure (network, HTTP, serialization, database, paging, WebView) is normalized into a single `AppError` hierarchy with consistent, user-friendly UI treatment.
- **Material 3 / Dark Theme / Accessibility** — full light/dark theming, dynamic color, TalkBack support, 48dp touch targets, and dynamic font scaling.

---

## 7. Development Workflow

The project was planned as an **incremental, documentation-driven build** across 13 phases (Foundation → Architecture → DI → Networking → Local Storage → Repository → Domain → UI → Navigation → Advanced Features → Testing → Optimization → Final Review), where each phase is designed to compile successfully before the next begins. Notable workflow conventions:

- **Version Catalog first** (`libs.versions.toml`) as the single source of truth for all dependency versions, referenced from Gradle Kotlin DSL files.
- **KSP over KAPT** for annotation processing (Room, Hilt) for faster, more modern builds.
- **One Git commit per feature/module** with **Conventional Commits** (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`).
- **Branch strategy:** `main` / `develop` / `feature/*` / `release/*` / `hotfix/*`.
- **Compile-safe milestones:** each of the 30 documents further breaks its phase into lettered sub-documents (e.g., `18A_Home_UI_State.md` → `18H_Home_Screen_Testing.md`) so that no single step leaves the project in a non-compiling state.
- **AI-assisted, prompt-driven code generation:** each document ends with an explicit "Prompt for Code Generation" section intended to be fed to an AI coding assistant to produce the actual Kotlin/Compose implementation for that milestone.
- **CI Quality Gates (planned):** build, unit tests, Android Lint, Detekt, Ktlint, and coverage reporting are expected to run on every PR, with no merge allowed on failing tests.
- **Code Review Checklist:** architecture adherence, naming, readability, test coverage, performance, accessibility, and security are all explicit review criteria.

---

## 8. Important Technical Decisions

- **Clean Architecture + MVVM** was chosen over a simpler MVC/MVP approach specifically for testability, scalability, and clear separation of concerns expected at a Senior/SDE3 level.
- **Hilt** was chosen over manual DI or a service locator for official Google support, boilerplate reduction, and first-class Compose/WorkManager/ViewModel integration.
- **Kotlin Serialization** was preferred over Gson/Moshi for type safety, null-safety, and tighter Kotlin/Compose integration, with Gson listed as an acceptable fallback.
- **Paging 3** was chosen over manual pagination to reduce memory footprint, minimize network usage, and get built-in `LoadState` handling; a `RemoteMediator` (Room-backed, offline-first paging) is explicitly scoped as a "bonus"/future enhancement rather than the initial implementation.
- **StateFlow/SharedFlow** were chosen over LiveData because of native Kotlin coroutine integration and better Compose interop (`collectAsStateWithLifecycle()`).
- **A centralized `AppError`/`NetworkResult` sealed hierarchy** was chosen over per-screen try/catch blocks to guarantee consistent, testable, user-friendly error UX across every layer.
- **Chrome Custom Tabs were prioritized over a raw WebView** for reading full articles, for better security (shared cookies, Chrome's Safe Browsing) and performance, with WebView kept only as a fallback and external browser as the final fallback.
- **Room is the offline source of truth for bookmarks only** in the initial scope; general article caching (`RemoteMediator`) is a deliberate future enhancement, not a v1 requirement.
- **Gradle Kotlin DSL + Version Catalog + KSP** was chosen as the standard, Google-recommended modern build configuration over Groovy DSL and KAPT.
- **Verified Android App Links (HTTPS, `assetlinks.json`, `autoVerify="true"`)** were chosen over plain (unverified) deep links to avoid the disambiguation dialog and improve security, with a custom `newsapp://` scheme kept as a secondary entry point.
- **Feature-based Presentation packaging with layer-based Data/Domain packaging** was adopted as the "Feature + Layer Hybrid Architecture" — the pattern most commonly used in production Android codebases, balancing feature team ownership with data-layer cohesion.

---

*Note: This summary is synthesized entirely from the `doc/` folder, which is a complete architecture and implementation specification (29 planning documents plus 2 supplementary configuration documents). Every document explicitly states "Files Created: None," and each ends with prompts intended to drive AI-assisted code generation for that milestone — meaning the `doc/` folder itself contains the full design blueprint rather than the Kotlin/Compose source. (The `doc/` folder was the only part of this project made available for this analysis, so the actual state of the `app/` source tree elsewhere in the project could not be verified.) See `Future_Improvements.md` for a breakdown of what a from-spec implementation would still need to reach production readiness.*
