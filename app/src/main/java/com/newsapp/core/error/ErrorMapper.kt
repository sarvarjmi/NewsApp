package com.newsapp.core.error

import android.database.sqlite.SQLiteException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps technical [Throwable] exceptions into domain-specific [AppError] types.
 *
 * This mapper ensures that the UI layer only ever receives user-friendly, 
 * categorized errors rather than implementation-specific exceptions from 
 * Retrofit, Room, or Kotlinx Serialization.
 */
@Singleton
class ErrorMapper @Inject constructor() {

    /**
     * Converts any [Throwable] into its corresponding [AppError].
     * 
     * @param throwable The exception caught in the data layer or UseCase.
     * @return A typed [AppError] with a user-friendly message.
     */
    fun map(throwable: Throwable): AppError {
        return when (throwable) {
            is SocketTimeoutException -> AppError.NetworkError(
                message = "The request timed out. Please try again.",
                throwable = throwable
            )
            
            is IOException -> AppError.NetworkError(
                message = "Connectivity issue. Please check your internet connection.",
                throwable = throwable
            )
            
            is HttpException -> {
                val code = throwable.code()
                AppError.ApiError(
                    message = mapHttpCodeToMessage(code),
                    code = code,
                    throwable = throwable
                )
            }
            
            is SerializationException -> AppError.UnknownError(
                message = "Failed to process data from the server.",
                throwable = throwable
            )
            
            is SQLiteException -> AppError.DatabaseError(
                message = "A database error occurred while accessing local data.",
                throwable = throwable
            )
            
            is IllegalArgumentException -> AppError.ValidationError(
                message = throwable.localizedMessage ?: "Invalid input provided.",
                throwable = throwable
            )
            
            else -> AppError.UnknownError(
                message = "An unexpected error occurred. Please try again later.",
                throwable = throwable
            )
        }
    }

    /**
     * Maps HTTP status codes to specific user-friendly messages.
     * 
     * @param code The HTTP status code (e.g., 401, 404, 500).
     * @return A string message tailored for the end-user.
     */
    private fun mapHttpCodeToMessage(code: Int): String {
        return when (code) {
            400 -> "The request was invalid. Please check your query."
            401 -> "Authentication failed. Please verify your API key."
            403 -> "Access denied. You do not have permission to view this content."
            404 -> "The requested news could not be found."
            408 -> "Request timeout. The server took too long to respond."
            429 -> "Too many requests. You've reached your daily limit."
            in 500..599 -> "Server is currently unavailable. Please try again later."
            else -> "A server error occurred (Status: $code)."
        }
    }
}
