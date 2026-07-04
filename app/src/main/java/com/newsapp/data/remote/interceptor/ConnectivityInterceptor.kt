package com.newsapp.data.remote.interceptor

import com.newsapp.core.network.NetworkMonitor
import com.newsapp.core.network.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that checks for internet connectivity before proceeding with a request.
 * Throws [NoInternetException] if no connection is detected.
 */
@Singleton
class ConnectivityInterceptor @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkMonitor.isConnected()) {
            throw NoInternetException()
        }
        return chain.proceed(chain.request())
    }
}
