package com.newsapp.core.network

import java.io.IOException

/**
 * Exception thrown when there is no active internet connection.
 */
class NoInternetException(message: String = "No internet connection available") : IOException(message)
