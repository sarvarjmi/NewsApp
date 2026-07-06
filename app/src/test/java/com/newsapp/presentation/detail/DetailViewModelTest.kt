package com.newsapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.newsapp.core.deeplink.LinkGenerator
import com.newsapp.domain.usecase.bookmark.ObserveBookmarkStatusUseCase
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.news.GetArticleFromCacheUseCase
import com.newsapp.util.MainDispatcherRule
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()
    private val observeBookmarkStatusUseCase: ObserveBookmarkStatusUseCase = mockk()
    private val getArticleFromCacheUseCase: GetArticleFromCacheUseCase = mockk()
    private val linkGenerator: LinkGenerator = mockk()
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DetailViewModel

    @BeforeEach
    fun setup() {
        viewModel = DetailViewModel(
            toggleBookmarkUseCase,
            observeBookmarkStatusUseCase,
            getArticleFromCacheUseCase,
            linkGenerator,
            savedStateHandle
        )
    }

    @Test
    fun `init with article should update state and observe bookmark status`() = runTest {
        val article = TestData.sampleArticle
        every { observeBookmarkStatusUseCase(article.url) } returns flowOf(true)
        
        viewModel.init(article)
        
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.article).isEqualTo(article)
            assertThat(state.isBookmarked).isTrue()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `initWithUrl should fetch article from cache and then init`() = runTest {
        val article = TestData.sampleArticle
        coEvery { getArticleFromCacheUseCase(article.url) } returns article
        every { observeBookmarkStatusUseCase(article.url) } returns flowOf(false)
        
        viewModel.initWithUrl(article.url)
        
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.article).isEqualTo(article)
            assertThat(state.isLoading).isFalse()
            assertThat(state.isBookmarked).isFalse()
        }
    }

    @Test
    fun `OnShareClicked should generate app link and send ShareArticle effect`() = runTest {
        val article = TestData.sampleArticle
        val appLink = "https://newsapp.com/article/1"
        every { linkGenerator.generateArticleLink(article.url) } returns appLink
        
        viewModel.effect.test {
            viewModel.onEvent(DetailEvent.OnShareClicked(article))
            
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(DetailSideEffect.ShareArticle::class.java)
            val shareEffect = effect as DetailSideEffect.ShareArticle
            assertThat(shareEffect.url).isEqualTo(appLink)
        }
    }

    @Test
    fun `OnBackClicked should send NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onEvent(DetailEvent.OnBackClicked)
            assertThat(awaitItem()).isEqualTo(DetailSideEffect.NavigateBack)
        }
    }
}
