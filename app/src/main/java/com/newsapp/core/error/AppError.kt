package com.newsapp.core.error

/**
 * Represents a centralized, sealed error hierarchy for the NewsApp.
 *
 * This model ensures that implementation-specific exceptions (e.g., Retrofit's HttpException,
 * Room's SQLiteException) are mapped into a domain-specific error before reaching the UI.
 * This follows Clean Architecture principles by keeping the presentation layer agnostic 
 * of the data source details.
 */
sealed interface AppError {
    /**
     * A user-friendly message intended to be displayed to the user.
     */
    val message: String

    /**
     * An optional numerical code representing the error (e.g., HTTP status code or database error code).
     */
    val code: Int?

    /**
     * The original [Throwable] that caused the error, preserved for logging and debugging purposes.
     */
    val throwable: Throwable?

    /**
     * Represents connectivity-related failures.
     *
     * Examples:
     * - No internet connection.
     * - Request timeout.
     * - DNS resolution failure.
     */
    data class NetworkError(
        override val message: String = "No internet connection. Please check your settings.",
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * Represents failures returned by the remote API.
     *
     * Examples:
     * - HTTP 401 Unauthorized.
     * - HTTP 404 Not Found.
     * - HTTP 429 Too Many Requests (Rate limit reached).
     * - HTTP 500 Server Error.
     */
    data class ApiError(
        override val message: String,
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * Represents errors occurring in the local persistence layer (Room/SQLite).
     *
     * Examples:
     * - Disk full.
     * - Constraint violations.
     * - Database migration failures.
     */
    data class DatabaseError(
        override val message: String = "Failed to access local data.",
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * Represents errors that occur during input or data validation.
     *
     * Examples:
     * - Empty search query.
     * - Invalid article URL format.
     */
    data class ValidationError(
        override val message: String,
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * Represents errors specifically related to the Paging 3 library's loading operations.
     *
     * Examples:
     * - Failure to append the next page of articles.
     * - Initial refresh failure in a paginated list.
     */
    data class PagingError(
        override val message: String = "Failed to load more content.",
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * Represents errors occurring within the WebView or during article browsing.
     *
     * Examples:
     * - SSL certificate errors.
     * - Page not found (404) within the browser.
     */
    data class WebViewError(
        override val message: String = "Unable to load the article page.",
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError

    /**
     * A fallback category for any error that doesn't fit into the above specific types.
     */
    data class UnknownError(
        override val message: String = "An unexpected error occurred. Please try again.",
        override val code: Int? = null,
        override val throwable: Throwable? = null
    ) : AppError
}
