package com.newsapp.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.newsapp.core.network.NetworkResult
import com.newsapp.data.local.database.NewsDatabase
import com.newsapp.data.local.entity.NewsRemoteKeysEntity
import com.newsapp.data.local.mapper.toCacheEntity
import com.newsapp.data.local.model.NewsArticleWithBookmarkStatus
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.mapper.toDomain
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val remoteDataSource: RemoteNewsDataSource,
    private val database: NewsDatabase,
    private val category: String
) : RemoteMediator<Int, NewsArticleWithBookmarkStatus>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsArticleWithBookmarkStatus>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        Timber.d("RemoteMediator: Loading page $page for category=$category")

        val result = remoteDataSource.getTopHeadlines(
            category = category,
            page = page,
            pageSize = state.config.pageSize
        )

        return when (result) {
            is NetworkResult.Success -> {
                val articles = result.data.articles.map { it.toDomain() }
                val endOfPaginationReached = articles.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.newsRemoteKeysDao.clearRemoteKeys()
                        database.newsArticleDao.deleteArticlesByCategory(category)
                    }

                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    
                    val keys = articles.map {
                        NewsRemoteKeysEntity(articleUrl = it.url, prevPage = prevKey, nextPage = nextKey)
                    }
                    
                    val entities = articles.map { it.toCacheEntity(category) }

                    database.newsRemoteKeysDao.upsertRemoteKeys(keys)
                    database.newsArticleDao.upsertArticles(entities)
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            is NetworkResult.Error -> {
                MediatorResult.Error(result.exception ?: Exception(result.message))
            }
            else -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, NewsArticleWithBookmarkStatus>): NewsRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { wrapped ->
                database.newsRemoteKeysDao.getRemoteKeysForArticle(wrapped.article.url)
            }
    }
}
