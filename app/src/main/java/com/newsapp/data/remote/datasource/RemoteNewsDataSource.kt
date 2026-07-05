package com.newsapp.data.remote.datasource

import com.newsapp.core.dispatcher.DispatcherProvider
import com.newsapp.core.error.ErrorMapper
import com.newsapp.core.network.NetworkResult
import com.newsapp.core.network.safeApiCall
import com.newsapp.data.remote.api.NewsApiService
import com.newsapp.data.remote.dto.NewsResponseDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for the Remote Data Source.
 */
interface RemoteNewsDataSource {
    suspend fun getTopHeadlines(category: String?, page: Int, pageSize: Int): NetworkResult<NewsResponseDto>
    suspend fun searchNews(query: String, page: Int, pageSize: Int): NetworkResult<NewsResponseDto>
}

/**
 * Implementation of [RemoteNewsDataSource] using [NewsApiService] and [safeApiCall].
 * 
 * This class coordinates the network execution, threading, and error mapping.
 */
@Singleton
class RemoteNewsDataSourceImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val errorMapper: ErrorMapper,
    private val dispatcherProvider: DispatcherProvider
) : RemoteNewsDataSource {

    override suspend fun getTopHeadlines(category: String?, page: Int, pageSize: Int): NetworkResult<NewsResponseDto> {
        return safeApiCall(
            dispatcher = dispatcherProvider.io,
            mapper = errorMapper
        ) {
            newsApiService.getTopHeadlines(category = category, page = page, pageSize = pageSize)
        }
    }

    override suspend fun searchNews(query: String, page: Int, pageSize: Int): NetworkResult<NewsResponseDto> {
        return safeApiCall(
            dispatcher = dispatcherProvider.io,
            mapper = errorMapper
        ) {
            newsApiService.searchNews(query = query, page = page, pageSize = pageSize)
        }
    }
}
