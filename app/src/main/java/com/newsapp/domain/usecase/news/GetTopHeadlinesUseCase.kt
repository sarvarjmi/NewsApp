package com.newsapp.domain.usecase.news

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case to fetch the top breaking news headlines from the repository.
 *
 * This Use Case encapsulates the business logic for retrievingheadlines, 
 * abstracting the data source and pagination details from the presentation layer.
 * 
 * Design Decisions:
 * 1. **Single Responsibility**: This class has one job: providing headlines.
 * 2. **Operator Invoke**: Enables the syntax `getTopHeadlinesUseCase(category)` 
 *    in ViewModels, making it feel like a first-class function call.
 * 3. **Paging Integration**: Returns a [Flow] of [PagingData], allowing the UI 
 *    to reactively handle infinite scrolling and state management.
 * 4. **Android-Free**: This class uses only standard Kotlin and pure Kotlin 
 *    libraries (like `kotlinx-coroutines` and `paging-common`), ensuring it is 
 *    fully unit-testable without an emulator or Robolectric.
 */
class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the business logic to retrieve headlines.
     *
     * @param category The news category to fetch (e.g., "business", "technology").
     *                 If null, general headlines are returned.
     * @return A [Flow] of [PagingData] containing [Article] domain models.
     */
    operator fun invoke(category: String? = null): Flow<PagingData<Article>> {
        return repository.getTopHeadlines(category)
    }
}
