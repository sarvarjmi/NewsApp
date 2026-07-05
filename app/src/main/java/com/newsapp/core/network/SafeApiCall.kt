package com.newsapp.core.network

import com.newsapp.core.error.AppError
import com.newsapp.core.error.ErrorMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * A generic wrapper for executing network calls within a coroutine context.
 *
 * This utility ensures that all exceptions thrown during an API call are 
 * caught, categorized via the [ErrorMapper], and transformed into a 
 * structured [NetworkResult].
 *
 * @param dispatcher The coroutine dispatcher on which the API call should execute (Defaults to IO).
 * @param mapper The domain-specific error mapper to translate technical throwables.
 * @param apiCall The suspendable lambda containing the actual Retrofit service call.
 * @return A [NetworkResult] wrapping either the successful data or a categorized [AppError].
 */
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    mapper: ErrorMapper,
    apiCall: suspend () -> T
): NetworkResult<T> {
    return withContext(dispatcher) {
        try {
            // Execution: Attempt the network request
            val response = apiCall()
            NetworkResult.Success(response)
        } catch (throwable: Throwable) {
            // Logging: Ensure the raw exception is captured for developer debugging
            Timber.e(throwable, "SafeApiCall: Network request failed")
            
            // Categorization: Delegate the exception mapping to our centralized ErrorMapper
            val appError = mapper.map(throwable)
            
            // Result Construction: Return the mapped error to the repository/UI
            NetworkResult.Error(
                message = appError.message,
                exception = throwable
            )
        }
    }
}
