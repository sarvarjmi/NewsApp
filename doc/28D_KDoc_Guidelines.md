# NewsApp

# 28D_KDoc_Guidelines.md

> **Phase:** Engineering Excellence
>
> **Module:** KDoc Documentation Standards
>
> **Goal:** Establish comprehensive KDoc (Kotlin documentation) guidelines to ensure all public APIs are well-documented and maintainable.

---

# Objective

KDoc is the Kotlin equivalent of JavaDoc. It provides inline documentation for classes, functions, and properties. This document establishes KDoc standards for the NewsApp project.

---

# KDoc Overview

KDoc:

- Documents public APIs
- Generates API documentation
- Improves code IDE assistance
- Maintains developer knowledge
- Supports HTML generation

---

# KDoc Format

## Basic Structure

```kotlin
/**
 * [Brief description of what this does]
 *
 * [Detailed description if needed, explaining the purpose, behavior, and usage]
 *
 * [Any implementation notes or warnings]
 *
 * @param [parameter name] [description of the parameter]
 * @return [description of the return value]
 * @throws [exception] [when this exception is thrown]
 *
 * @sample [SampleUsageClass.sampleFunctionName]
 */
```

---

# Classes & Interfaces

Document the purpose and responsibility of each class.

## Example: Repository Class

```kotlin
/**
 * Repository for managing news articles from multiple data sources.
 *
 * Coordinates data retrieval from the network and local database,
 * implementing the repository pattern for clean separation of concerns.
 *
 * Responsibilities:
 * - Fetch articles from NewsAPI
 * - Cache articles in local Room database
 * - Provide offline support
 * - Handle data mapping between DTO and domain models
 *
 * @property networkDataSource Data source for network calls
 * @property localDataSource Data source for local database
 */
class NewsRepository(
    private val networkDataSource: NewsNetworkDataSource,
    private val localDataSource: NewsLocalDataSource
) : NewsRepositoryInterface {
    // ...
}
```

## Example: Interface

```kotlin
/**
 * Defines operations for managing user bookmarks.
 *
 * Implementations must handle both local and remote bookmark storage,
 * ensuring consistency across devices.
 */
interface BookmarkRepository {
    /**
     * Toggles the bookmark status of an article.
     *
     * @param articleId The ID of the article to bookmark/unbookmark
     * @return true if article is now bookmarked, false otherwise
     */
    suspend fun toggleBookmark(articleId: String): Boolean
}
```

---

# Functions & Methods

Document parameters, return values, and exceptions.

## Example: Suspend Function

```kotlin
/**
 * Fetches the latest news articles based on the search query.
 *
 * Attempts to fetch from the network first. If the network request fails
 * and cached data is available, returns the cached data. Otherwise,
 * propagates the network error.
 *
 * @param query The search keyword for news articles
 * @param pageSize The number of articles to fetch (default: 20)
 * @return A list of news articles matching the query
 * @throws IOException If both network and cache fail
 * @throws InvalidQueryException If the query is empty or invalid
 *
 * @sample NewsRepositoryDemo.fetchArticles
 */
suspend fun fetchArticles(
    query: String,
    pageSize: Int = 20
): List<NewsArticle>
```

## Example: Flow Function

```kotlin
/**
 * Observes changes to the bookmarked articles list.
 *
 * Emits the current list immediately, then emits updates whenever
 * the bookmarks are modified. The list is guaranteed to be sorted
 * by date in descending order.
 *
 * @return A Flow that emits lists of bookmarked articles
 */
fun observeBookmarkedArticles(): Flow<List<NewsArticle>> = bookmarkDao
    .observeBookmarkedArticles()
    .map { entities -> entities.toDomainModels() }
```

## Example: ViewModel Function

```kotlin
/**
 * Handles the user's search request and updates the UI state.
 *
 * Cancels any previous search operation and initiates a new one.
 * Updates [_searchState] with loading state, then success or error.
 *
 * @param query The search keywords entered by the user
 *
 * @see SearchState
 */
fun onSearchArticles(query: String) {
    if (query.isBlank()) {
        _searchState.value = SearchState.Idle
        return
    }

    viewModelScope.launch {
        _searchState.value = SearchState.Loading
        try {
            val results = searchUseCase(query)
            _searchState.value = SearchState.Success(results)
        } catch (e: Exception) {
            _searchState.value = SearchState.Error(e.message ?: "Unknown error")
        }
    }
}
```

---

# Properties & Variables

Document important properties, especially those exposed in public APIs.

```kotlin
/**
 * The current state of the search operation.
 *
 * - [SearchState.Idle]: No search has been performed
 * - [SearchState.Loading]: Search is in progress
 * - [SearchState.Success]: Search completed successfully
 * - [SearchState.Error]: Search failed with an error
 *
 * @see SearchState
 */
private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)

/**
 * Observable state of the search operation. Emits updates as the search progresses.
 */
val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
```

---

# Composable Functions

Document composable parameters and behavior.

```kotlin
/**
 * Displays a single news article card with image, title, and description.
 *
 * The card is clickable and navigates to the article detail screen when tapped.
 * Images are loaded asynchronously with a loading placeholder and error state.
 *
 * For accessibility, provides content descriptions for images and proper
 * TalkBack support via semantics.
 *
 * @param article The news article to display
 * @param onArticleClick Callback invoked when the card is clicked
 * @param modifier Modifier for customizing the card's layout and appearance
 *
 * @sample NewsScreenDemo.articleCardPreview
 */
@Composable
fun NewsArticleCard(
    article: NewsArticle,
    onArticleClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onArticleClick(article) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(article.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(article.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
```

---

# Sealed Classes & Enums

Document each state or case thoroughly.

```kotlin
/**
 * Represents the possible states of a search operation.
 */
sealed class SearchState {
    /**
     * No search has been initiated yet.
     */
    data object Idle : SearchState()

    /**
     * A search operation is currently in progress.
     */
    data object Loading : SearchState()

    /**
     * The search completed successfully.
     *
     * @property articles The list of articles matching the search query
     */
    data class Success(val articles: List<NewsArticle>) : SearchState()

    /**
     * The search failed with an error.
     *
     * @property message A user-friendly error message
     */
    data class Error(val message: String) : SearchState()
}
```

## Enum Example

```kotlin
/**
 * Defines the available sorting options for news articles.
 */
enum class ArticleSortOrder {
    /**
     * Sort by publication date, newest first.
     */
    NEWEST,

    /**
     * Sort by publication date, oldest first.
     */
    OLDEST,

    /**
     * Sort by relevance to the search query.
     */
    RELEVANCE,

    /**
     * Sort by popularity (view count).
     */
    POPULARITY
}
```

---

# UseCase Classes

Document business logic and preconditions.

```kotlin
/**
 * Use case for searching news articles based on a user query.
 *
 * Implements the following business logic:
 * - Validates the search query (must not be empty)
 * - Limits results to 20 articles per page
 * - Sorts results by relevance
 * - Maps DTOs to domain models
 *
 * @property repository The repository responsible for data retrieval
 *
 * @throws IllegalArgumentException If the query is empty or null
 */
class SearchNewsUseCase(
    private val repository: NewsRepository
) {
    /**
     * Executes the search operation.
     *
     * @param query The search keywords
     * @return A list of matching articles
     */
    suspend operator fun invoke(query: String): List<NewsArticle> {
        require(query.isNotBlank()) { "Search query cannot be empty" }
        return repository.searchArticles(query)
    }
}
```

---

# Data Classes

Document complex data classes with many fields.

```kotlin
/**
 * Represents a single news article in the domain layer.
 *
 * This is an immutable data class that guarantees consistency of article data
 * throughout the application.
 *
 * @property id Unique identifier for the article (primary key)
 * @property title The article's headline
 * @property description A brief summary of the article
 * @property content The full article text
 * @property imageUrl URL of the article's thumbnail image
 * @property source The news source that published this article
 * @property publishedAt The publication date and time (UTC)
 * @property url Direct link to the article on the publisher's website
 * @property isBookmarked Whether the user has bookmarked this article
 */
data class NewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val source: NewsSource,
    val publishedAt: Instant,
    val url: String,
    val isBookmarked: Boolean = false
)
```

---

# Extension Functions

Document the purpose and use cases.

```kotlin
/**
 * Converts a DTO article to a domain model article.
 *
 * Maps all relevant fields from the API response to the domain model,
 * handling null values and type conversions appropriately.
 *
 * @return A domain model NewsArticle
 */
fun NewsArticleDTO.toDomainModel(): NewsArticle = NewsArticle(
    id = this.articleId,
    title = this.headline,
    description = this.summary,
    // ... more mappings
)
```

---

# Deprecation & Migration

Mark deprecated items with migration guidance.

```kotlin
/**
 * Fetches articles from the API (deprecated).
 *
 * @deprecated Use [fetchArticlesWithPaging] instead, which provides
 * better memory efficiency and supports pagination.
 *
 * @see fetchArticlesWithPaging
 */
@Deprecated(
    message = "Use fetchArticlesWithPaging instead",
    replaceWith = ReplaceWith("fetchArticlesWithPaging()")
)
fun fetchArticles(): List<NewsArticle> {
    // ...
}
```

---

# Sample Documentation

Use `@sample` to reference working code examples.

```kotlin
/**
 * Searches for news articles matching the given query.
 *
 * @sample com.example.newsapp.samples.NewsRepositorySamples.searchArticles
 */
suspend fun searchArticles(query: String): List<NewsArticle>
```

Create sample file: `src/samples/kotlin/com/example/newsapp/samples/NewsRepositorySamples.kt`

```kotlin
object NewsRepositorySamples {
    suspend fun searchArticles(repository: NewsRepository) {
        val articles = repository.searchArticles("kotlin")
        articles.forEach { article ->
            println("${article.title} - ${article.source}")
        }
    }
}
```

---

# Documentation Best Practices

1. **Write for future readers**: Assume they don't know the context
2. **Document the "why"**: Explain design decisions, not just what the code does
3. **Use examples**: Provide sample usage for complex functions
4. **Keep it current**: Update docs when code changes
5. **Use standard tags**: @param, @return, @throws, @see, @deprecated
6. **Be concise**: One paragraph for simple items, multiple for complex ones
7. **Avoid stating the obvious**: Don't document trivial getters/setters
8. **Link related items**: Use @see and reference types

---

# Files to Document

Priority order:

1. **Public interfaces** - Must have KDoc
2. **Public classes** - Must have KDoc
3. **Public functions** - Must have KDoc (especially suspend/Flow)
4. **Public properties** - Should have KDoc if non-obvious
5. **Composables** - Should have KDoc with parameter descriptions
6. **ViewModels** - Should have class and public method documentation
7. **UseCases** - Must have KDoc explaining business logic
8. **Data classes** - Should have KDoc for complex classes

---

# IDE Integration

### Android Studio

- **Ctrl+Q** or **Cmd+J**: Show documentation
- **Quick Documentation**: Displays during development
- **Generate KDoc**: Use IDE template to auto-generate structure

---

# Generating HTML Documentation

```bash
# Using Dokka (Kotlin documentation engine)
./gradlew dokkaHtml

# Documentation is generated in build/dokka/html
```

---

# Files Modified/Created

- Updated all public APIs with KDoc
- Created sample files for complex functions

---

# What We Completed

✅ Defined KDoc format and structure

✅ Provided examples for all API types

✅ Documented best practices

✅ Explained tag usage and conventions

✅ Showed IDE integration tips

---

# Checklist: Before Committing Code

- [ ] All public classes have KDoc
- [ ] All public functions have KDoc with @param/@return
- [ ] All public properties have KDoc (if non-obvious)
- [ ] Complex algorithms have explanation comments
- [ ] Deprecated items have migration guidance
- [ ] Examples provided for complex APIs

---

# Next Steps

1. Review existing codebase for undocumented public APIs
2. Add KDoc to all public interfaces, classes, and functions
3. Generate HTML documentation: `./gradlew dokkaHtml`
4. Review generated documentation for clarity and completeness
5. Integrate documentation review into code review process
