package com.newsapp.presentation.search

import com.newsapp.domain.model.Article

sealed interface SearchEvent {
    data class OnQueryChanged(val query: String) : SearchEvent
    data class OnBookmarkToggled(val article: Article) : SearchEvent
}
