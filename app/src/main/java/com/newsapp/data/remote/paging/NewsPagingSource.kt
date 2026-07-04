package com.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapp.core.network.NetworkResult
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.mapper.toDomain
import com.newsapp.domain.model.Article
import timber.log.Timber

/**
 * A [PagingSource] implementation for fetching news articles from the [RemoteNewsDataSource].
 */
class NewsPagingSource(
    private val remoteDataSource: RemoteNewsDataSource,
    private val category: String? = null,
    private val query: String? = null
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        
        Timber.d("PagingSource: Loading page $page for category=$category, query=$query")
        
        val result = if (query != null) {
            remoteDataSource.searchNews(
                query = query,
                page = page,
                pageSize = params.loadSize
            )
        } else {
            remoteDataSource.getTopHeadlines(
                category = category,
                page = page,
                pageSize = params.loadSize
            )
        }
        
        return when (result) {
            is NetworkResult.Success -> {
                val articles = result.data.articles.map { it.toDomain() }
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (articles.isEmpty()) null else page + 1
                )
            }
            is NetworkResult.Error -> {
                LoadResult.Error(result.exception ?: Exception(result.message))
            }
            is NetworkResult.Loading -> {
                // Loading is not expected from the data source in this context
                LoadResult.Error(IllegalStateException("Unexpected loading state"))
            }
        }
    }
}
