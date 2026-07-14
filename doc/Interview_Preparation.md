# NewsApp — Interview Preparation Guide

> Built from the 30 architecture/planning documents in this repository. Use this as a rehearsal script: read each answer, then try explaining it out loud in your own words using the project's specifics (Clean Architecture + MVVM, Hilt, Retrofit, Room, Paging 3, Coroutines/Flow, Navigation Compose, WorkManager, Deep Links) as concrete evidence for your claims.

---

## How to Use This Guide

Interviewers care less about textbook definitions and more about whether you can justify *decisions you made on a real project*. For every answer below, be ready to say: what the alternative was, why you didn't pick it, and what you'd do differently at 10x scale or with a real production incident. The "Common Follow-Up Questions" and "Real-World Scenario" sections exist specifically to rehearse that muscle.

---

## Section A — Project Walkthrough Questions

**A1. Walk me through this project in two minutes.**
NewsApp is a Kotlin/Jetpack Compose Android news reader built on Clean Architecture + MVVM. It fetches paginated headlines from a public news API (NewsAPI), lets users search with debounce, bookmark articles offline via Room, and read full articles through Chrome Custom Tabs with a WebView fallback. It uses Hilt for DI, Retrofit/OkHttp for networking, Paging 3 for infinite scroll, StateFlow for UI state, Navigation Compose with verified Android App Links for deep linking, and WorkManager for background refresh. The codebase is split into Presentation, Domain, and Data layers with dependencies pointing inward, and every failure path is normalized into a single `AppError` hierarchy so the UI never sees a raw exception.

**A2. Why did you choose Clean Architecture over a simpler MVC or "just put everything in the ViewModel" approach?**
At the size of a single feed-search-detail-bookmark app, a simpler architecture would technically work. I chose Clean Architecture because it was explicitly a demonstration of production-scale practices: it isolates business rules (Domain) from delivery mechanisms (UI, database, network), which makes the app easier to test in isolation, easier to onboard new engineers into (clear package boundaries = clear ownership), and resilient to swapping implementation details — e.g., replacing NewsAPI with GNews, or Retrofit with Ktor, would only touch the Data layer.

**A3. What was the single hardest architectural decision, and why?**
Deciding how much offline support to build in v1. Full offline-first (via `RemoteMediator` backing the entire feed with Room) is the "correct" production answer, but it adds real complexity (cache invalidation, conflict resolution, migration risk). I scoped v1 to offline bookmarks only (Room as SSOT for saved articles) and explicitly deferred `RemoteMediator` to a documented future enhancement — a classic "ship the 80% value at 20% the risk" tradeoff.

**A4. Why NewsAPI over GNews, Mediastack, or Currents API?**
NewsAPI was rated highest across documentation quality, pagination support, search support, and richness of article metadata (source, image, published date). The tradeoff to be aware of in an interview: NewsAPI's free tier is developer/localhost-restricted in production, which is a real constraint you'd have to disclose and solve (paid tier, or a self-hosted proxy) before shipping.

**A5. What would you change if you were rebuilding this from scratch?**
I'd modularize from day one (`:core`, `:data`, `:domain`, `:feature-*` Gradle modules) instead of package-based separation in a single module — package separation is fine early, but it doesn't enforce the dependency rule at compile time the way module boundaries do. I'd also build the `RemoteMediator` offline-first feed from the start rather than layering it in later, since retrofitting offline-first onto an existing paging implementation is more invasive than designing for it up front.

**A6. How is state managed across the app, end to end?**
User actions become `UiEvent`s consumed by the ViewModel. The ViewModel invokes a UseCase, which calls the Repository, which returns Domain models (never DTOs or Entities) wrapped in a `NetworkResult`/Flow. The ViewModel maps that into an immutable `UiState` (Loading/Success/Empty/Error) exposed as `StateFlow`, collected in Compose via `collectAsStateWithLifecycle()`. One-time effects (navigate, show snackbar, open share sheet) are emitted separately via `SharedFlow` so they don't get replayed on configuration change or double-collected on recomposition.

---

## Section B — Clean Architecture & MVVM

**B1. What are the three layers and what's not allowed to happen in each?**
*Presentation* — Compose UI + ViewModels + UI state; must not call Retrofit/Room or contain business rules. *Domain* — UseCases, Repository interfaces, domain models; must not import any Android framework class (no `Context`, no `LiveData`, no Retrofit/Room types) — this is what makes it 100%, framework-free unit-testable. *Data* — Retrofit, Room, DTOs, Entities, Repository implementation; must not leak DTOs or Entities outside itself — everything crossing the Data → Domain boundary is mapped to a Domain model first.

**B2. What is the Dependency Rule and why does it matter?**
Source code dependencies can only point inward: Presentation → Domain ← Data. Domain never imports from Presentation or Data. This means you can delete the entire Data layer and the Domain layer still compiles (it only knows about its own `NewsRepository` interface), and you can unit-test all business logic without an Android emulator, a database, or a network connection.

**B3. Where does MVVM fit inside Clean Architecture?**
MVVM only describes the Presentation layer. The "View" is the Compose screen (dumb, renders state, emits events); the "ViewModel" bridges View and Domain, exposing `StateFlow<UiState>`; the "Model" here is effectively the Domain layer (UseCases + Domain models) rather than a single class.

**B4. Explain the Repository pattern as implemented here, specifically.**
`NewsRepository` is an interface defined in Domain (e.g., `getTopHeadlines()`, `searchNews()`, `bookmarkArticle()`, `observeBookmarks()`). `NewsRepositoryImpl` in Data implements it, coordinating a `RemoteNewsDataSource` (Retrofit) and a `LocalNewsDataSource` (Room DAO), converting DTOs/Entities to Domain models via mappers, and deciding — transparently to every caller — whether data comes from network or cache. Neither the ViewModel nor the UseCase ever knows which source served the data.

**B5. Why UseCases instead of calling the Repository directly from the ViewModel?**
Single Responsibility: `GetTopHeadlinesUseCase` does exactly one thing and is independently unit-testable with a fake Repository. It keeps ViewModels thin (orchestration only, no business rules), makes logic reusable across screens (e.g., `ToggleBookmarkUseCase` used from Home, Search, and Detail), and gives you a stable seam to add cross-cutting logic later (e.g., analytics, validation) without touching the ViewModel.

**B6. How do you enforce SOLID here, concretely?**
*S* — `NewsRepository` only does data retrieval, `HomeViewModel` only manages Home state. *O* — adding a new data source doesn't require changing existing ViewModels. *L* — `NewsRepositoryImpl` can be swapped for `FakeNewsRepository` in tests without breaking any consumer. *I* — `NewsRepository` only exposes news-related operations rather than one giant "god" interface. *D* — the ViewModel depends on the `NewsRepository` interface, and Hilt injects the concrete `NewsRepositoryImpl` at runtime.

**B7. Why did you choose `operator fun invoke()` for UseCases?**
Purely ergonomic and idiomatic Kotlin: `getTopHeadlinesUseCase()` reads like a function call instead of `getTopHeadlinesUseCase.execute()`, while still being a proper injectable, testable class under the hood.

**B8. What is Unidirectional Data Flow and why does it matter for debugging?**
Data flows in exactly one direction: Event → ViewModel → UseCase → Repository → new immutable State → UI. Because state can only change through this one path, and states are immutable snapshots, you can reproduce any bug by replaying the sequence of events — there's no "the UI mutated some shared mutable object out from under the ViewModel" class of bug.

---

## Section C — Kotlin Language Concepts Used in the Project

**C1. Where are sealed classes/interfaces used and why?**
`NetworkResult` (Loading/Success/Error), `AppError` (NetworkError/ApiError/DatabaseError/ValidationError/PagingError/WebViewError/UnknownError), and every screen's `UiState` are sealed hierarchies. This gives exhaustive `when` compilation checks — the compiler forces you to handle every state, so adding a new error type surfaces every place in the app that needs to react to it.

**C2. What's the difference between `data class` and a regular class, and why are Domain models data classes?**
`data class` auto-generates `equals()`/`hashCode()`/`copy()`/`toString()` based on declared properties. Domain models (`Article`, `Source`, `Bookmark`) are immutable `data class`es with `val` properties so Compose can treat them as stable, so mappers can produce brand-new instances rather than mutate shared state, and so `copy()` makes producing modified UI states trivial and safe.

**C3. Explain Kotlin extension functions and where they're used here.**
Extension functions add a method to an existing type without modifying its source (`fun ArticleDto.toDomain(): Article`). The project uses them exclusively for mapping (`toDomain()`, `toEntity()`, `toUiModel()`) instead of static mapper classes/utility objects — it reads more naturally (`dto.toDomain()` vs `ArticleMapper.map(dto)`) and keeps each mapper colocated with clear, discoverable naming.

**C4. What's the difference between `val`/`var` immutability discipline in this codebase and why it's enforced?**
Every model across every layer uses `val`; the project explicitly bans mutable collections (`List` not `MutableList`) in state and models. This is a deliberate Compose-performance and correctness decision: an immutable snapshot can be safely compared for equality (skip recomposition) and can't be mutated by one screen while another still holds a reference to it.

**C5. Null safety strategy — how does the project handle a field like `author` or `urlToImage` being `null` from the API?**
DTOs mark genuinely optional API fields nullable. Mappers apply safe defaults at the DTO→Domain boundary (e.g., `author ?: "Unknown"`, `imageUrl` falling back to a placeholder) so the UI layer never has to null-check display data — by the time an `Article` reaches the ViewModel, it's guaranteed presentable.

**C6. What are Kotlin coroutines' structured concurrency, and how is it applied?**
Structured concurrency means every coroutine has a defined scope/lifecycle parent, so cancellation propagates automatically instead of leaking. ViewModels launch coroutines in `viewModelScope` (cancelled automatically on `onCleared()`); `CoroutineWorker`s use their own managed scope; the project explicitly bans `GlobalScope` for exactly this reason — an unscoped coroutine can outlive the screen that started it and leak memory or crash on a stale reference.

**C7. What Kotlin idiom replaces the `!!` non-null assertion here, and why does it matter?**
The style guide explicitly prohibits `!!` in favor of `?.let { }`, the Elvis operator `?:`, and `requireNotNull()` with a descriptive message. `!!` throws an unhelpful `NullPointerException` with no context; the alternatives either handle the null gracefully or fail with an explicit, debuggable message.

**C8. Why Kotlin Serialization over Gson/Moshi here?**
Kotlin Serialization is compile-time (uses a Kotlin compiler plugin to generate serializers), avoiding Gson's runtime reflection — faster, and it fails at compile time rather than at runtime for structural mismatches. It's also more natural with Kotlin's null-safety and default-value semantics and has first-class support from JetBrains.

---

## Section D — Jetpack Compose Questions

**D1. What causes unnecessary recomposition, and how does this project avoid it?**
Recomposition happens when a Composable reads a `State` that changed. Unnecessary recomposition typically comes from unstable parameters (mutable lists, lambdas recreated every call) or state read too high in the tree. The project mitigates this with immutable `data class` models, `remember`/`rememberSaveable` for local UI state, `derivedStateOf` for expensive derived values (e.g., "should the scroll-to-top FAB show"), stable `LazyColumn` item keys, and hoisting state to the ViewModel rather than storing business state inside Composables.

**D2. `remember` vs `rememberSaveable` — where does each apply here?**
`remember` survives recomposition but not configuration changes or process death (used for `LazyListState`, transient UI-only state). `rememberSaveable` survives configuration changes and process death by saving into the `Bundle` (used for the search query text and selected bottom-nav tab, since losing those on rotation would be a bad UX).

**D3. How does the Home screen avoid a blank screen during the initial load?**
It renders a `LoadingShimmer` skeleton immediately instead of a spinner or empty space, then swaps in real `NewsCard`s once Paging 3 delivers the first page — appended pages only show a small footer loader so already-visible content never disappears.

**D4. How do you keep `LazyColumn` scrolling smooth with 1000+ paginated items?**
Stable, unique item keys (e.g., article URL/ID, not list index) so Compose can correctly diff and avoid needless recomposition/relayout when items are inserted or removed; `LazyPagingItems` integration so only currently-visible + prefetch-window items are composed; avoiding nested scrolling containers; and disabling Paging 3 placeholders unless truly needed.

**D5. How is dark mode / theming implemented?**
A Material 3 `Theme.kt` defines light and dark `ColorScheme`s (with dynamic color support on Android 12+), consumed via `MaterialTheme` at the root; every screen and component reads colors/typography/shapes from the theme rather than hardcoding values, so the whole app switches automatically with the system setting.

**D6. How would you test a Composable in isolation?**
`ComposeTestRule` + a fake/preview `UiState` passed directly into the stateless screen composable (not through the real ViewModel), asserting on semantics (`onNodeWithText`, `onNodeWithContentDescription`) rather than pixel output; screenshot/regression testing (e.g., Paparazzi) covers the visual-fidelity gap that semantics-based tests don't catch.

---

## Section E — Coroutines & Flow

**E1. `StateFlow` vs `SharedFlow` — how does the project decide which to use?**
`StateFlow` is used for persistent, replayable UI state (`HomeUiState`) — it always has a current value and new collectors immediately get the latest one. `SharedFlow` (or a `Channel`) is used for one-time side effects (navigate, show snackbar, launch share intent) that must *not* replay on the next collection (e.g., after a configuration change), which is exactly the bug you'd get if you modeled a one-time navigation event as `StateFlow`.

**E2. How is search debounced, precisely?**
The query is pushed into a `MutableStateFlow<String>`; the ViewModel applies `.debounce(500).distinctUntilChanged()` before invoking `SearchNewsUseCase`, so only the final query after a 500ms typing pause triggers an API call, and repeating an identical query (e.g., retype the same text) doesn't re-trigger a network request.

**E3. What is `cachedIn(viewModelScope)` and why is it required for Paging 3?**
It multicasts the `Flow<PagingData<Article>>` and caches the currently loaded pages in the ViewModel's scope, so rotating the device (a new Compose collector attaches) doesn't reissue network requests and lose scroll position — without it, every configuration change would silently restart pagination from page one.

**E4. Explain the difference between `suspend fun` and `Flow` returns in this Repository, and when each is used.**
One-shot operations (save/remove/toggle a bookmark, refresh) are `suspend fun` — call once, get a result, done. Continuous/observable data (`observeBookmarks()`, the paged headline stream) returns `Flow`/`Flow<PagingData<T>>` — the caller subscribes and receives updates as the underlying data changes (e.g., Room emits a new list automatically whenever a bookmark is added or removed).

**E5. How does a `CoroutineExceptionHandler` fit into the error strategy?**
It's the last line of defense at the application/worker level — it catches otherwise-unhandled exceptions from a coroutine, logs them, and prevents an uncaught exception from crashing the process, while the primary error-handling path (`SafeApiCall` → `NetworkResult`/`AppError`) is expected to catch and map almost everything before it ever reaches this handler.

**E6. What Flow operators are explicitly called out as performance tools, and why?**
`debounce()` and `distinctUntilChanged()` cut down redundant work (search); `shareIn()`/`stateIn()` convert a cold Flow into a hot, shared one so multiple collectors don't each trigger independent upstream work (e.g., multiple Composables observing the same bookmark-status Flow).

**E7. Why are dispatchers injected (`DispatcherProvider`) instead of hardcoded (`Dispatchers.IO`)?**
Hardcoding `Dispatchers.IO` inside a Repository or UseCase makes it impossible to substitute a deterministic `TestDispatcher` in unit tests. Injecting a `DispatcherProvider` (with `io`, `default`, `main` properties, each qualified via a Hilt `@Qualifier`) lets tests swap in `StandardTestDispatcher`/`UnconfinedTestDispatcher` for fully deterministic, fast coroutine tests.

---

## Section F — Dependency Injection (Hilt)

**F1. Why Hilt instead of manual DI or Koin?**
Hilt is Google's official recommendation, builds on Dagger (compile-time-verified graph, not reflection-based like Koin), integrates natively with `ViewModel`, `WorkManager`, and Compose Navigation, and dramatically reduces the Dagger boilerplate (components, subcomponents) a team would otherwise hand-write.

**F2. What Hilt scopes are used and why?**
`@Singleton` for app-lifetime objects that are expensive to create or must be shared (Retrofit, OkHttpClient, Room database, Repository implementations). `@ViewModelScoped` for dependencies that should live exactly as long as one ViewModel instance (e.g., a UseCase that holds per-screen transient state, if any). `@ActivityRetainedScoped` is reserved for state that should survive Activity recreation but not the whole app process.

**F3. Constructor injection vs field injection vs module (`@Provides`) — when is each used?**
Constructor injection is the default and preferred approach for anything you own (`ViewModel`s, UseCases, Repository implementations — annotated `@Inject constructor`). Field injection (`@Inject lateinit var`) is reserved for Android framework classes you don't construct yourself (e.g., a `BroadcastReceiver`). `@Provides` module methods are used for third-party types you don't own the constructor of (Retrofit, Room, OkHttpClient) or where you need custom construction logic.

**F4. What are Hilt qualifiers and where are they needed here?**
Qualifiers (`@IoDispatcher`, `@DefaultDispatcher`, `@MainDispatcher`) disambiguate multiple bindings of the same type (`CoroutineDispatcher`) — without them, Hilt wouldn't know which dispatcher instance to inject where.

**F5. How would you unit-test a ViewModel that depends on Hilt-injected UseCases?**
You don't invoke Hilt in a unit test at all — you construct the ViewModel directly, passing hand-built fakes/mocks for its UseCase dependencies (`FakeGetTopHeadlinesUseCase` or a MockK mock), because constructor injection means the ViewModel has no compile-time dependency on the Hilt framework itself.

**F6. What's a common Hilt mistake and how is it avoided here?**
Forgetting `@HiltAndroidApp` on the `Application` class or `@AndroidEntryPoint` on an Activity/Fragment, missing `@InstallIn` on a module, or picking the wrong scope (e.g., binding a `Repository` at `@ViewModelScoped` when it should be `@Singleton`, causing redundant Retrofit/Room instances). The project's best-practices list explicitly flags all of these as review-checklist items.

---

## Section G — Networking (Retrofit / OkHttp)

**G1. Walk through what happens, layer by layer, when the Home screen loads.**
`HomeViewModel` calls `GetTopHeadlinesUseCase` → `NewsRepository.getTopHeadlines()` → `RemoteNewsDataSource` → `NewsApiService` (Retrofit) → OkHttp applies interceptors (API key, headers, connectivity check, logging) → the request hits NewsAPI → the JSON response is deserialized into `NewsResponseDto`/`ArticleDto` via Kotlin Serialization → `SafeApiCall` wraps the whole thing and converts any exception into `NetworkResult` → the Repository maps DTOs to `Article` Domain models → returns `Flow<PagingData<Article>>` up through the UseCase to the ViewModel → `HomeUiState` → Compose.

**G2. What interceptors exist and what does each do?**
`ApiKeyInterceptor` attaches the API key to every request so no call site has to know about it. `ConnectivityInterceptor` checks network availability before dispatching and short-circuits with a `NoInternetException` if offline. `HeaderInterceptor` adds common headers (`Accept`, `Content-Type`, `User-Agent`). `LoggingInterceptor` logs full request/response bodies in debug builds only, disabled in release to avoid leaking data and hurting performance.

**G3. How are the API key and other secrets kept out of source control?**
The key lives in `local.properties` (git-ignored), is exposed to the app via a Gradle-generated `BuildConfig` field, and is injected into requests exclusively through `ApiKeyInterceptor` — it's never hardcoded in a Kotlin file or referenced directly by a ViewModel/Repository.

**G4. What's `SafeApiCall`, precisely, and why does it exist?**
A generic coroutine wrapper around every Retrofit call that catches `IOException`, `SocketTimeoutException`, `HttpException`, and `SerializationException`, converting each into a typed `NetworkResult.Error`/`AppError` rather than letting the raw exception propagate. It exists so no individual Repository method has to hand-write a try/catch — the mapping logic lives in exactly one place.

**G5. What's the retry policy, and why not retry everything?**
Retry is applied only to transient failures — timeouts, HTTP 500/502/503. 401/403/404 are never retried, because they're not transient: retrying an invalid API key or a resource that genuinely doesn't exist just wastes battery/bandwidth and delays showing the user an accurate error.

**G6. Why prefer Chrome Custom Tabs over an in-app WebView for reading full articles?**
Custom Tabs share the device's existing Chrome session/cookies, inherit Chrome's security and Safe Browsing protections, load faster (pre-warmed process), and get native browser UI (address bar, share) for free. WebView is kept only as a fallback for when Custom Tabs aren't available, with tightly scoped configuration (JavaScript only if required, no universal file access, mixed content disabled).

**G7. How would you add response caching to reduce redundant network calls?**
Add an OkHttp `Cache` (disk-backed) plus `Cache-Control` handling if the API supports it, or implement `RemoteMediator` so Room becomes the real cache layer and repeated navigations serve from disk instead of network at all — the second approach is the one already scoped as a documented future enhancement.

---

## Section H — Database (Room)

**H1. What's stored in Room and why only that?**
Only bookmarked articles (`BookmarkEntity`), because bookmarks are the one dataset that must survive offline and app restarts by design. General headline/search caching is deliberately out of v1 scope and deferred to a `RemoteMediator`-based redesign.

**H2. Walk through the local persistence stack.**
`BookmarkScreen` observes `BookmarkViewModel`, which calls `ObserveBookmarksUseCase` → `NewsRepository.observeBookmarks()` → `LocalNewsDataSource` → `BookmarkDao.getAllBookmarks(): Flow<List<BookmarkEntity>>` → Room emits automatically whenever the underlying table changes → an Entity→Domain mapper converts to `Article` → the change flows all the way up to `BookmarkUiState` and Compose recomposes with zero manual refresh logic.

**H3. Why route through a `LocalNewsDataSource` instead of injecting the DAO directly into the Repository?**
Abstraction and testability: the Repository can be tested against a fake `LocalNewsDataSource` without touching Room at all, and if the local storage mechanism ever changed (e.g., DataStore for something else), only the DataSource implementation would need to change.

**H4. How are Room migrations handled?**
Every schema change increments the database version; Room's migration APIs (or `AutoMigration` where the change is simple enough) define how to transform the old schema into the new one, so an app update never silently deletes a user's existing bookmarks. Migration tests using an in-memory or exported schema are part of the documented Room testing strategy.

**H5. Why `Flow` return types from the DAO instead of a one-shot `suspend fun` list?**
Because bookmark status needs to be reactive — when the user taps "bookmark" on the Detail screen, the Bookmark screen (and any other card showing that article) should update immediately without a manual refresh. A `Flow`-returning DAO query gives you that "observe forever" behavior for free from Room.

**H6. What would break the offline experience, and how do you defend against it?**
Corrupted database, constraint violations, or a failed migration. These are caught at the Repository layer and mapped to `AppError.DatabaseError` with a friendly message and retry, rather than crashing the app — and are explicitly called out in the Bookmark screen's error-state design.

**H7. Why is the database currently unencrypted, and what's the risk?**
It's a plain SQLite database via Room with no encryption in v1 — acceptable because bookmarks are non-sensitive public news content, but explicitly flagged as a future hardening step (SQLCipher, or Jetpack Security) that would become necessary the moment any personally identifiable or authentication-related data is stored locally.

---

## Section I — Paging 3

**I1. Why Paging 3 instead of manually tracking page number/offset and appending to a list?**
Paging 3 gives you memory-bounded, automatically-prefetched pages, built-in `LoadState` (refresh/append/prepend, each with its own loading/error/not-loading state), automatic Compose integration via `LazyPagingItems`, and a documented, testable seam (`PagingSource`) instead of a hand-rolled, bug-prone "am I near the bottom, should I fetch more" scroll listener.

**I2. What does `NewsPagingSource` actually do?**
Extends `PagingSource<Int, Article>`; its `load()` function fetches one page from the API given a page key, maps DTOs to Domain models, and returns either `LoadResult.Page` (with `prevKey`/`nextKey`) or `LoadResult.Error`. Its `getRefreshKey()` determines which page to reload after an invalidation (e.g., pull-to-refresh) so the user doesn't jump to a jarring position.

**I3. What are the three `LoadState`s and how does each map to UI?**
*Refresh* — the initial/full reload; drives full-screen loading, initial error, or empty-state UI. *Append* — loading the next page while scrolling; drives a small bottom footer spinner or footer-retry button, without disturbing already-visible items. *Prepend* — loading a previous page; unused here since articles are always requested newest-to-oldest.

**I4. How does search get its own independent pagination stream?**
Every new (debounced, distinct) query constructs a brand-new `Pager`/`PagingSource`, rather than reusing the Home feed's pager — each search is treated as an independent paginated dataset with its own load states.

**I5. What are sensible `PagingConfig` values here and why?**
`pageSize = 20`, `initialLoadSize = 20`, `prefetchDistance = 5`, `enablePlaceholders = false`. Placeholders are disabled because the API doesn't return a reliable total count for every query, and enabling them without an accurate count causes scrollbar/measurement glitches.

**I6. How do bookmarks stay in sync inside a *paged* list?**
The paged `Flow<PagingData<Article>>` is combined with the separate bookmarks `Flow` at the ViewModel/UI layer (each card looks up its own bookmark state), rather than baking bookmark status into the cached paging snapshot — this keeps the two data sources decoupled while still rendering a consistent bookmark icon.

**I7. What's the "bonus" `RemoteMediator` feature and why isn't it in v1?**
A `RemoteMediator` would let Room become the actual backing store for the paged feed (network fetches write into Room, and `PagingSource` reads from Room), making the whole feed — not just bookmarks — available offline. It's deferred because it meaningfully increases complexity (conflict resolution, cache invalidation, extra migration surface) for a v1 scope that prioritized shipping the core reading/search/bookmark loop first.

---

## Section J — Navigation & Deep Links

**J1. How is navigation structured, and why keep the `NavController` out of the ViewModel?**
A single `NavHost`/`NavController` at the root hosts a Root graph containing a Bottom Navigation graph (Home/Search/Bookmarks) plus a Detail destination reachable from all three. ViewModels never hold a `NavController` reference — they emit a one-time `NavigationEvent` via `SharedFlow`, and the Composable (which does own the `NavController`) reacts to it. This keeps ViewModels testable without Android Navigation dependencies and avoids memory leaks from a ViewModel outliving/holding a stale `NavController`.

**J2. Deep Links vs App Links — what's the difference and which does this project use?**
A plain Deep Link matches any URI scheme/host declared in an intent filter and can trigger Android's disambiguation ("Open with...") dialog. An Android App Link is a *verified* HTTPS deep link — the domain proves ownership via a hosted `assetlinks.json`, so the OS opens the app directly with no dialog. NewsApp uses verified App Links (`https://newsapp.com/article/{id}`, `/search?q=`, `/bookmarks`) as the primary mechanism, with a `newsapp://` custom scheme as a secondary/fallback entry point.

**J3. How does `assetlinks.json` verification actually work?**
The app declares `android:autoVerify="true"` on its HTTPS intent filter; on install, Android fetches `https://<domain>/.well-known/assetlinks.json`, checks that it lists the app's package name and SHA-256 signing certificate fingerprint, and if it matches, marks the domain "verified" — from then on, matching links open the app directly instead of showing a browser or a chooser.

**J4. How do you defend against a malicious or malformed deep link?**
Every incoming URI is validated before navigation: HTTPS-only, trusted host, well-formed and non-empty required parameters (e.g., a real article ID). Malformed or unsupported paths route to an explicit error/fallback screen (back to Home) instead of crashing or navigating with garbage state.

**J5. What happens to the back stack when a user opens a deep link, then presses back?**
Deep-linked navigation is expected to feel natural (Detail → Back → Home), which typically means synthesizing a reasonable back stack under the destination rather than leaving the user stuck with no way "back" except exiting the app — this is a deliberate UX requirement in the documented back-stack strategy, not an accident of default `NavController` behavior.

**J6. How does scroll/paging state survive a Detail → Back navigation?**
Because the paged `Flow` is cached in the ViewModel (`cachedIn(viewModelScope)`) and the ViewModel isn't destroyed by simple back-navigation (only by the whole screen being popped off the graph long-term), the `LazyListState`'s remembered scroll position plus the still-cached paging data combine to restore the feed exactly where the user left it.

---

## Section K — Testing

**K1. What does the testing pyramid look like here?**
Roughly 70% unit tests (mappers, UseCases, ViewModels, utilities — no Android framework, fast), 20% integration tests (Repository against fakes/MockWebServer/in-memory Room), 10% UI tests (Compose UI tests, navigation tests). This mirrors the standard testing pyramid, weighted toward the cheapest, fastest, most deterministic tests.

**K2. How would you unit test `SearchNewsUseCase`?**
Construct it with a fake/mocked `NewsRepository` (MockK), call `invoke(query)`, and assert it delegates to `repository.searchNews(query)` with the exact expected argument and correctly propagates the returned `Flow<PagingData<Article>>` — no real network or database involved, so the test runs in milliseconds.

**K3. How would you test a ViewModel's `StateFlow` transitions?**
Using Turbine: `viewModel.uiState.test { assertEquals(Loading, awaitItem()); assertEquals(Success(...), awaitItem()) }`, combined with `kotlinx-coroutines-test`'s `TestDispatcher` (injected via the `DispatcherProvider` abstraction) so time-based operators like `debounce()` can be advanced deterministically instead of relying on real delays.

**K4. How is the Repository tested without hitting a real API or database?**
`FakeRemoteDataSource` and `FakeLocalDataSource` (hand-written test doubles) or `MockWebServer` for the network side, and an in-memory Room database for the local side — verifying success paths, error paths, and that DTOs/Entities are correctly mapped to Domain models.

**K5. How would you test the `NewsPagingSource` specifically?**
Using the Paging 3 testing library's `TestPager` (or manually invoking `load()`), asserting the correct `LoadResult.Page` is returned for a given key, that `nextKey`/`prevKey` are computed correctly, and that an API failure produces `LoadResult.Error` rather than throwing.

**K6. How do you test Compose UI without a real ViewModel/network/database?**
Pass a hand-built `UiState` directly into the stateless screen Composable inside a `ComposeTestRule`, and assert on the rendered semantics tree (e.g., a `NewsCard` with a known title is present, a `RetryButton` appears in the Error state) — this decouples UI-rendering correctness from business-logic correctness, which is tested separately at the ViewModel/UseCase level.

**K7. What are Fakes vs Mocks, and which does this project prefer?**
A Fake is a lightweight real implementation (e.g., an in-memory `FakeNewsRepository` backed by a `MutableList`); a Mock (via MockK) is a dynamically generated stand-in you script behavior/verification onto. The documented best practice here is "prefer fakes over excessive mocks" — fakes tend to produce less brittle tests that don't break every time an internal call sequence changes.

**K8. What does "Given/When/Then" test naming buy you here?**
Names like `givenNetworkAvailable_whenLoadNews_thenReturnArticles()` or `givenTimeout_whenRefresh_thenEmitError()` make a failing test's *intent* readable from the test report alone, without opening the test body — valuable when a CI failure needs to be triaged quickly.

**K9. What's currently the biggest testing gap in this project, honestly?**
No test code has actually been written yet — every one of the 30 planning documents reports "Files Created: None." The testing *strategy* is thoroughly designed (down to code-coverage targets per layer), but turning that strategy into an actual, CI-enforced test suite is the single largest remaining gap before this could be called production-ready. Being upfront about this in an interview is a strength, not a weakness — it shows you can distinguish a plan from a finished, verified product.

---

## Section L — System Design & Scalability Questions

**L1. How would this architecture scale to 10 more feature screens and a 20-engineer team?**
Split into Gradle feature modules (`:feature-home`, `:feature-search`, etc.) sharing `:core`, `:data`, `:domain` modules, so teams can own a module end-to-end, build times stay bounded (only changed modules recompile), and the Dependency Rule is enforced by the module graph itself, not just convention.

**L2. How would you design the news feed to handle millions of daily active users hitting a rate-limited third-party API?**
Introduce a thin backend/edge cache in front of the third-party API (so the app never calls NewsAPI directly at that scale — a BFF or CDN-cached proxy absorbs the rate limit), implement `RemoteMediator` so most feed reads are served from each device's local Room cache rather than a fresh network call, and add exponential backoff plus circuit-breaking in the client for when the upstream API is degraded.

**L3. How would you add multi-language/localization support without a rewrite?**
Introduce a `language`/`country` parameter threaded through the existing `SearchNewsUseCase`/`GetTopHeadlinesUseCase` (the API already supports a `language` query param per the DTO design), store the user's preference (Settings screen, DataStore), and externalize all UI strings to `strings.xml` resource sets per locale — the Clean Architecture boundary means this is purely an additive Domain-parameter + Data-layer change, the Presentation contracts barely move.

**L4. How would you support a tablet/foldable two-pane (list | detail) layout without duplicating screen logic?**
Keep the existing `HomeViewModel`/`SearchViewModel` and their `UiState`s exactly as-is; introduce a `WindowSizeClass`-aware layout at the Compose level that, on large screens, renders the existing list Composable in one pane and the existing Detail Composable in a second pane side-by-side, driven by the same navigation events instead of a full navigation transition — architecture already separates "what data/state exists" from "how it's laid out," which is exactly what makes this tractable.

**L5. If the News API changed its response schema tomorrow, what's the blast radius?**
Ideally, only the `dto` and `mapper` packages in the Data layer change. Domain models, UseCases, ViewModels, and all UI code are untouched, because they only ever see the stable `Article`/`Source` Domain models — this is the concrete payoff of the DTO/mapper boundary described in the architecture.

**L6. How would you design background sync to be battery-conscious at scale?**
WorkManager `PeriodicWorkRequest` with a `flexInterval` (letting the OS batch wake-ups with other apps' work instead of firing at an exact time), constraints (`setRequiredNetworkType`, `setRequiresBatteryNotLow`), unique work names (`enqueueUniquePeriodicWork` with `KEEP`) to prevent duplicate schedules stacking up across app restarts, and exponential backoff on failure rather than aggressive immediate retry.

**L7. How would you evolve this into an offline-first app without breaking existing consumers?**
Introduce `RemoteMediator` behind the *same* `NewsRepository` interface — `getTopHeadlines()` still returns `Flow<PagingData<Article>>`, so ViewModels and UseCases require zero changes; only the Data-layer implementation gains a Room-backed mediator. This is the textbook payoff of programming to an interface rather than a concrete class.

---

## Section M — Performance Questions

**M1. What are this app's stated performance targets?**
Cold startup < 2s, warm startup < 1s, screen navigation < 300ms, 60 FPS scrolling, image load < 500ms, and a minimized/optimized APK size via R8 + resource shrinking.

**M2. How do you keep Compose scrolling at 60 FPS with images in every row?**
Coil with memory + disk caching, crossfade transitions, and a defined placeholder/error image so layout never jank-shifts; stable `LazyColumn` keys and content types so Compose doesn't recompute layout for unrelated items; avoiding loading full-resolution images for thumbnail-sized cards.

**M3. What's a Baseline Profile and why hasn't it been implemented yet here?**
A Baseline Profile is a list of classes/methods the Android Runtime should ahead-of-time compile, pre-warming hot paths like app startup and the first scroll — measured via Macrobenchmark. It's fully specified in the documentation (`24F_Baseline_Profiles.md`) as a distinct future milestone, not yet implemented, because it's typically layered in once the app's real hot paths stabilize rather than guessed at up front.

**M4. What's the single biggest recomposition risk in a paginated list screen, and how do you catch it before it ships?**
Passing an unstable lambda or a non-`@Stable` object down into each `NewsCard`, causing every visible row to recompose on unrelated state changes. You catch it with the Compose Compiler's stability report (`--Xreports` / metrics output) plus the Layout Inspector's recomposition counts during manual QA, not just by reading the code.

**M5. How would you diagnose a reported "app feels slow scrolling the feed" bug?**
Reproduce with the Android Studio Profiler / Compose Layout Inspector to see actual frame times and recomposition counts, check whether images are being decoded on the main thread or re-fetched unnecessarily, verify `LazyColumn` keys are stable (not defaulting to index), and confirm Paging's prefetch distance isn't too small (causing visible stutter while waiting on the network mid-scroll).

---

## Section N — Security Questions

**N1. How is the API key protected?**
Stored in `local.properties` (excluded from version control), surfaced to the app only via a generated `BuildConfig` field, and attached to requests exclusively inside `ApiKeyInterceptor` — never hardcoded in a committed file, never logged.

**N2. What WebView security precautions are in place?**
JavaScript enabled only if strictly required, DOM storage/zoom controls scoped intentionally, Safe Browsing enabled, mixed content disabled, file access and "universal access from file URLs" disabled, and no arbitrary JavaScript interfaces exposed to loaded pages.

**N3. What's missing from the security posture today, and what would you add first?**
Certificate pinning and Room database encryption are both explicitly documented as future work, not yet implemented — meaning traffic integrity relies on standard TLS/CA trust (fine for public news content, insufficient the moment authentication or personal data is added) and the local bookmark database is unencrypted plaintext SQLite. I'd prioritize certificate pinning first if the app were handling any auth token, and database encryption the moment any PII enters local storage.

**N4. How do you keep sensitive data out of logs?**
Timber is used for structured logging in debug builds; the documented standard explicitly forbids logging API keys, tokens, or personal data, and OkHttp's request/response body logging is disabled entirely in release builds.

**N5. How is a deep link validated before it's trusted?**
Scheme must be HTTPS, host must match the verified domain, and required path parameters (e.g., article ID) must be present and well-formed before any navigation occurs — malformed or untrusted URIs are routed to a safe fallback rather than passed straight into a screen's data-loading logic.

---

## Section O — Real-World Scenario & Debugging Questions

**O1. "Users report the app crashes on rotation while on the Search screen." How do you investigate?**
First check whether `SearchViewModel` is scoped correctly (it should survive rotation via the standard Android ViewModel store — if it's being recreated, something's wrong with how it's obtained). Then check whether the search query is stored in `rememberSaveable` (if it's plain `remember`, it's lost on rotation, which could desync UI from the ViewModel's actual state) and whether `cachedIn(viewModelScope)` is actually applied to the paging flow (if missing, rotation could trigger a fresh network call mid-flight and a race condition on the old vs. new Composable's collector).

**O2. "The bookmark icon sometimes doesn't update immediately after tapping it." Diagnose.**
Likely one of: the DAO query isn't Flow-based (a one-shot `suspend fun getBookmarks()` wouldn't auto-update the UI — you'd need a manual refetch), the Repository is combining the bookmark-status Flow incorrectly with the paged list (stale combine causing a delayed emission), or the `NewsCard`'s bookmark button isn't reading from the reactive per-item bookmark state but from a stale snapshot captured at list-load time.

**O3. "Pull-to-refresh sometimes shows stale data for a second before updating." What's happening and how do you fix it?**
This is a classic race between the `PagingSource.invalidate()` call and the UI's `LoadState` collection — the fix is ensuring the refresh indicator is driven directly by `LazyPagingItems.loadState.refresh` rather than a separately-tracked boolean flag that can fall out of sync with the actual Paging 3 state machine.

**O4. "The app is rejected from the Play Store for a security issue with deep links." What do you check?**
Whether the intent filter accepts arbitrary/unvalidated schemes without `autoVerify`, whether incoming URIs are sanitized before being used to construct navigation arguments (a classic injection vector), and whether any `WebView`-loaded deep link could execute arbitrary JavaScript or load an untrusted domain.

**O5. "NewsAPI suddenly starts returning 429 Too Many Requests for a lot of users." What's your immediate and long-term response?**
Immediately: verify `SafeApiCall`'s HTTP-code mapping treats 429 as retryable-with-backoff, not treated the same as a hard 401/403 failure, and confirm the UI shows a "Too many requests, try again shortly" message rather than a generic error. Long-term: this is exactly the scaling gap called out in the System Design section — introduce a caching/proxy layer between the app and NewsAPI so client-side request volume doesn't map 1:1 to upstream API calls.

**O6. "A crash report shows a `SQLiteConstraintException` from bookmarking." Walk through your fix.**
Likely a `UNIQUE` constraint violation from attempting to insert a bookmark that already exists (e.g., a double-tap race condition on the bookmark button before the first insert completes). Fix: make the DAO insert an upsert (`OnConflictStrategy.REPLACE` or an explicit "insert or update" query) and/or debounce/disable the button while the toggle operation is in flight, and ensure the failure path still maps to `AppError.DatabaseError` instead of crashing.

**O7. "QA reports the WebView reader sometimes shows a blank screen with no error." How do you debug it?**
Check whether an SSL error or an unsupported URL scheme (e.g., `mailto:`, `tel:`) is being silently swallowed instead of triggering the documented error state; verify `shouldOverrideUrlLoading`/`WebViewClient` callbacks correctly route non-HTTP(S) schemes to an external Intent instead of trying to render them in the WebView (which would produce exactly this symptom).

---

## Section P — Code Review Questions

**P1. You see a PR where a ViewModel injects `NewsRepositoryImpl` (the concrete class) instead of `NewsRepository` (the interface). What do you say?**
Request a change: injecting the concrete type breaks Dependency Inversion, makes the ViewModel untestable without a real Repository implementation (or a subclassing mock, which is fragile), and defeats the entire purpose of defining `NewsRepository` as a Domain-layer interface in the first place.

**P2. A UseCase directly performs a network call and skips the Repository. What's your review comment?**
This violates the layering contract — UseCases should only ever depend on Repository interfaces, never Retrofit/OkHttp/DTOs directly. It also means this logic can't be tested with a fake Repository anymore, and any future caching/offline strategy added to the Repository would silently bypass this UseCase.

**P3. A Composable directly calls `viewModel.repository.getBookmarks()`. What's wrong, and what would you ask for?**
Multiple violations at once: the View is bypassing the ViewModel's state/event contract entirely, the ViewModel is (incorrectly) exposing its Repository as a public property, and this couples the Composable directly to a Data-layer type. Ask for the Composable to only ever read `StateFlow<UiState>` and emit `UiEvent`s — never reach through the ViewModel into its dependencies.

**P4. A PR adds `!!` to unwrap a nullable field from a DTO mapper. What do you flag?**
Flag it against the documented null-safety standard — request either a safe default (`?:`) or, if the field is genuinely required and its absence indicates a real API contract violation, `requireNotNull(value) { "expected X from API response" }` so a failure is debuggable instead of an anonymous NPE in production crash reports.

**P5. A new screen's ViewModel launches a coroutine with `GlobalScope.launch`. Why block this PR?**
`GlobalScope` coroutines aren't tied to the ViewModel's lifecycle, so they keep running (and can crash or leak) after the ViewModel — and possibly the screen, and possibly the whole feature — is gone. Every coroutine here should be scoped to `viewModelScope` (or an injected scope in a Worker) so cancellation is automatic.

**P6. A Repository method returns a Room `Entity` directly to the ViewModel to "save a mapping step." What's the concern?**
It breaks the architecture's core guarantee that the Domain/Presentation layers never see Data-layer types. If the local persistence mechanism ever changes, or a field is renamed at the schema level for a migration, this leaks straight into UI code with no insulating layer — exactly the coupling Clean Architecture exists to prevent.

**P7. A PR increases a Composable file to 600 lines by adding several unrelated dialogs and bottom sheets inline. What's your feedback?**
Flag it against the documented 300-line Composable guideline — request extraction of each dialog/bottom sheet into its own small, previewable, reusable Composable file. Large monolithic Composables are harder to preview, harder to test in isolation, and more prone to unrelated-state-triggering-unrelated-recomposition bugs.

---

## Section Q — Senior Android Developer Questions

**Q1. How would you justify this architecture's complexity to a PM who just wants "a simple news app" fast?**
I'd frame it in terms of iteration speed *after* the first release, not before it: the extra layers (UseCases, Repository interface, mappers) cost real time in week one, but they're what let a team add a Settings screen, swap the news API, or add offline mode in week twelve without a rewrite. For a true one-off prototype I'd cut corners; for anything expected to live and grow, this is the cheaper path over a 6–12 month horizon.

**Q2. How would you mentor a mid-level engineer who keeps putting business logic in ViewModels instead of UseCases?**
Point to a concrete, motivating failure mode rather than a rule: ask them to write a unit test for the logic as it exists today (hard, because it's entangled with `StateFlow`/Compose lifecycle concerns), then show how trivial the same test becomes once the logic is extracted into a UseCase with a fake Repository. Rules stick better once someone has felt the pain the rule prevents.

**Q3. How do you decide what belongs in `core` vs a feature package vs `domain`?**
`domain` is business rules with zero Android/framework awareness. `core` is genuinely cross-cutting infrastructure with no feature opinion (dispatcher provider, network monitor, error mapper, base UI state helpers) — the test is "would a second, unrelated feature need this unchanged?" If yes, `core`. If it's feature-specific state/UI, it belongs in that feature's presentation package, full stop, even if it looks superficially reusable today.

**Q4. What tradeoffs would you explain to a stakeholder about choosing NewsAPI's free tier for this MVP?**
It's the fastest path to a working demo/assignment, but the free tier is explicitly restricted to non-production/localhost use — meaning it is not viable to actually ship this to real users without upgrading to a paid tier or building a lightweight server-side proxy. That's a decision that needs to be made explicitly, not discovered during App Store review.

**Q5. How would you run a technical design review for the "add offline-first paging" feature before anyone writes code?**
Start from the existing `NewsRepository` interface as the non-negotiable contract (so Presentation/Domain are unaffected), then walk through: cache invalidation policy (time-based TTL? pull-to-refresh-only?), conflict handling (server data always wins vs. merge), migration plan for the new Room tables/columns, and a rollback plan if the `RemoteMediator` introduces regressions — then only after that's agreed, break it into the same kind of compile-safe milestones the rest of this project already uses.

**Q6. What would you say is this project's biggest single production risk today, and why?**
The complete absence of implemented tests despite an exhaustive testing *plan*. A beautifully designed architecture with zero automated verification is still one refactor away from a silent regression — the architecture makes testing *possible* and *cheap*, but possible-and-cheap isn't the same as *done*.

**Q7. How would you evaluate whether this codebase is "ready" for a new engineer's first week?**
Check whether the layering is actually enforced by structure (module boundaries, not just packages and good intentions), whether there's a working local dev setup documented (README, API key instructions — currently missing per this analysis), and whether a small, well-scoped first ticket (e.g., "add a Settings screen following the existing feature pattern") could be completed by reading the existing Home/Search feature as a template — that's the real test of whether an architecture's conventions are internalized or just aspirational.

---

## Section R — Rapid-Fire Follow-Up Questions

Interviewers frequently chain a quick follow-up onto whatever you just answered. Practice these short-answer pairs out loud.

- *"Why Flow over LiveData?"* → Native coroutine integration, richer operators (`debounce`, `combine`, `flatMapLatest`), and first-class Compose support via `collectAsStateWithLifecycle()`; LiveData is lifecycle-aware out of the box but was designed pre-Compose/pre-coroutines.
- *"Why not just use `LiveData` for one-time events and skip `SharedFlow`?"* → LiveData has no clean "consume-once" semantics; you end up needing a wrapper (`Event<T>`/`SingleLiveEvent`) to avoid re-delivering the same navigation event on every observer resubscription — `SharedFlow`/`Channel` model this correctly without a workaround.
- *"Why is the Domain layer allowed zero Android imports — what's the actual payoff?"* → You can run its entire test suite on the JVM with plain JUnit, no emulator, no Robolectric, in milliseconds — that's a CI speed and reliability win, not just theoretical purity.
- *"What breaks if you skip the DTO→Domain mapping step and just reuse the DTO everywhere?"* → Every API schema change becomes a UI-breaking change; a nullable API field renaming ripples through Compose screens instead of being absorbed at the mapping boundary.
- *"Why `operator fun invoke()` and not a named method like `.execute()`?"* → Purely stylistic/idiomatic Kotlin — functionally identical, just reads as a function call.
- *"Isn't Paging 3 overkill for a news feed that could just load 100 articles at once?"* → For 100 items maybe; for a scroll-forever, memory-bounded, infinite feed with search, it avoids unbounded memory growth and manual scroll-position/offset tracking bugs.
- *"What's the actual difference between `NetworkResult` and `AppError`?"* → `NetworkResult` is the immediate outcome of one network call (Loading/Success/Error, Data-layer facing). `AppError` is the app-wide normalized error taxonomy that any layer (network, database, paging, WebView) maps into, consumed uniformly by every ViewModel/UI.
- *"Why debounce at 500ms specifically, not 300ms or 1000ms?"* → It's a UX tuning tradeoff — short enough that search still feels responsive, long enough to meaningfully cut down redundant requests during normal typing speed; it's a value you'd actually A/B test in a real product, not a hard rule.
- *"Would you ever put a UseCase for something as simple as `GetBookmarksUseCase` if it's a one-line pass-through to the Repository?"* → Yes, for consistency and future-proofing — today it's a pass-through, but it's a stable seam if validation, analytics, or business rules need to be added later without hunting down every direct Repository call site.
- *"What's the fastest way to know if a Composable is recomposing too often?"* → Compose's built-in recomposition counts in Layout Inspector, or temporary debug logging inside the Composable body during development — not guessing from reading the code alone.
