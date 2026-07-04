package com.newsapp.data.remote.datasource

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
 */
@Singleton
class RemoteNewsDataSourceImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : RemoteNewsDataSource {

    override suspend fun getTopHeadlines(category: String?, page: Int, pageSize: Int): NetworkResult<NewsResponseDto> {
        return safeApiCall {
            newsApiService.getTopHeadlines(category = category, page = page, pageSize = pageSize)
        }
    }

    override suspend fun searchNews(query: String, page: Int, pageSize: Int): NetworkResult<NewsResponseDto> {
        return safeApiCall {
            newsApiService.searchNews(query = query, page = page, pageSize = pageSize)
        }
    }
}
