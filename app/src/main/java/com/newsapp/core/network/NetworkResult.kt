package com.newsapp.core.network

/**
 * A sealed class to represent the result of a network operation.
 */
sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val exception: Throwable? = null) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}
