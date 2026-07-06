package com.newsapp.core.deeplink

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LinkGeneratorTest {

    private val linkGenerator = LinkGenerator()

    @Test
    fun `generateArticleLink should return valid HTTPS app link`() {
        val articleUrl = "https://example.com/news/1"
        val result = linkGenerator.generateArticleLink(articleUrl)
        
        assertThat(result).contains("https://newsapp.com/article/")
        assertThat(result).contains("https%3A%2F%2Fexample.com%2Fnews%2F1")
    }
}
