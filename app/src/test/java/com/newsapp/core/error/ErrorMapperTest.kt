package com.newsapp.core.error

import com.google.common.truth.Truth.assertThat
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ErrorMapperTest {

    private val mapper = ErrorMapper()

    @Test
    fun `map should return NetworkError for IOException`() {
        val exception = IOException()
        val result = mapper.map(exception)
        assertThat(result).isInstanceOf(AppError.NetworkError::class.java)
        assertThat(result.message).contains("Connectivity issue")
    }

    @Test
    fun `map should return NetworkError with timeout message for SocketTimeoutException`() {
        val exception = SocketTimeoutException()
        val result = mapper.map(exception)
        assertThat(result).isInstanceOf(AppError.NetworkError::class.java)
        assertThat(result.message).contains("timed out")
    }

    @Test
    fun `map should return ApiError for 500 HttpException`() {
        val response = Response.error<Any>(500, "".toResponseBody(null))
        val exception = HttpException(response)
        val result = mapper.map(exception)
        assertThat(result).isInstanceOf(AppError.ApiError::class.java)
        assertThat(result.code).isEqualTo(500)
    }

    @Test
    fun `map should return UnknownError for generic exception`() {
        val exception = RuntimeException("Unknown")
        val result = mapper.map(exception)
        assertThat(result).isInstanceOf(AppError.UnknownError::class.java)
    }
}
