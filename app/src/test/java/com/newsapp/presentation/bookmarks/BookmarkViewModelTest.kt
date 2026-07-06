package com.newsapp.presentation.bookmarks

import app.cash.turbine.test
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.newsapp.domain.usecase.bookmark.AddBookmarkUseCase
import com.newsapp.domain.usecase.bookmark.ObserveBookmarksUseCase
import com.newsapp.domain.usecase.bookmark.RemoveBookmarkUseCase
import com.newsapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModelTest {

    private val observeBookmarksUseCase: ObserveBookmarksUseCase = mockk()
    private val removeBookmarkUseCase: RemoveBookmarkUseCase = mockk()
    private val addBookmarkUseCase: AddBookmarkUseCase = mockk()
    
    private val testDispatcher = StandardTestDispatcher()

    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var viewModel: BookmarkViewModel

    private val sampleArticles = listOf(
        Article(
            url = "https://example.com/1",
            title = "Title 1",
            author = "Author 1",
            description = "Desc 1",
            content = "Content 1",
            urlToImage = null,
            publishedAt = "2024-03-20T10:00:00Z",
            source = Source(id = "id1", name = "Name 1"),
            isBookmarked = true
        )
    )

    @BeforeEach
    fun setup() {
        every { observeBookmarksUseCase() } returns flowOf(sampleArticles)
        viewModel = BookmarkViewModel(observeBookmarksUseCase, removeBookmarkUseCase, addBookmarkUseCase)
    }

    @Test
    fun `initial state should observe bookmarks and update articles`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.articles).isEqualTo(sampleArticles)
            assertThat(state.bookmarkCount).isEqualTo(sampleArticles.size)
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `OnArticleClicked should send NavigateToDetail effect`() = runTest {
        val article = sampleArticles[0]
        viewModel.effect.test {
            viewModel.onEvent(BookmarkEvent.OnArticleClicked(article))
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(BookmarkSideEffect.NavigateToDetail::class.java)
            assertThat((effect as BookmarkSideEffect.NavigateToDetail).article).isEqualTo(article)
        }
    }

    @Test
    fun `OnDeleteIntent should update state with article to delete`() = runTest {
        val article = sampleArticles[0]
        viewModel.onEvent(BookmarkEvent.OnDeleteIntent(article))
        assertThat(viewModel.state.value.articleToDelete).isEqualTo(article)
    }

    @Test
    fun `OnConfirmDelete should call removeBookmarkUseCase and send ShowUndoSnackbar effect`() = runTest {
        val article = sampleArticles[0]
        coEvery { removeBookmarkUseCase(article) } returns Unit
        
        viewModel.onEvent(BookmarkEvent.OnDeleteIntent(article))
        
        viewModel.effect.test {
            viewModel.onEvent(BookmarkEvent.OnConfirmDelete)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(BookmarkSideEffect.ShowUndoSnackbar::class.java)
            assertThat((effect as BookmarkSideEffect.ShowUndoSnackbar).article).isEqualTo(article)
        }
        
        coVerify { removeBookmarkUseCase(article) }
        assertThat(viewModel.state.value.articleToDelete).isNull()
    }

    @Test
    fun `OnUndoRemove should call addBookmarkUseCase`() = runTest {
        val article = sampleArticles[0]
        coEvery { addBookmarkUseCase(article) } returns Unit
        
        viewModel.onEvent(BookmarkEvent.OnUndoRemove(article))
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { addBookmarkUseCase(article) }
    }
}
