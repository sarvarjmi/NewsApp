package com.newsapp.domain.usecase.news

import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to manually trigger a refresh of news data from the remote source.
 *
 * This Use Case encapsulates the business logic for refreshing news. While Paging 3
 * typically handles data loading automatically, this Use Case provides a way to
 * explicitly force a refresh, which is useful for "Pull-to-Refresh" interactions
 * or background synchronization tasks.
 *
 * Refresh Strategy & Cache Invalidation:
 * 1. **Trigger**: When called, it signals the [NewsRepository] to fetch the latest
 *    data from the API.
 * 2. **Cache Invalidation**: The repository implementation should handle the invalidation
 *    of any local caches or Paging sources. For Paging 3, this usually means calling
 *    `pagingSource.invalidate()`.
 * 3. **Background Sync**: Designed to be easily integrated with WorkManager. A
 *    `CoroutineWorker` can inject this Use Case and call it periodically to ensure
 *    the user always has fresh news headlines ready.
 */
class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the refresh operation.
     */
    suspend operator fun invoke() {
        repository.refreshNews()
    }
}
