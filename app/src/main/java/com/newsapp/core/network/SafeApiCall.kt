package com.newsapp.core.network

import retrofit2.HttpException
import java.io.IOException

/**
 * Executes a network call and wraps it in a [NetworkResult].
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> NetworkResult.Error("No internet connection", throwable)
            is HttpException -> {
                val code = throwable.code()
                val message = throwable.message()
                NetworkResult.Error("Error $code: $message", throwable)
            }
            else -> NetworkResult.Error("Unknown error occurred", throwable)
        }
    }
}
