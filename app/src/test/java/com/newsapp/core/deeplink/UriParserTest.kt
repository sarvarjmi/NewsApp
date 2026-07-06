package com.newsapp.core.deeplink

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UriParserTest {

    private val uriParser = UriParser()

    @Test
    fun `getPathSegments should return all segments`() {
        val uri = Uri.parse("https://newsapp.com/article/123/edit")
        val segments = uriParser.getPathSegments(uri)
        assertThat(segments).containsExactly("article", "123", "edit")
    }

    @Test
    fun `getQueryParameter should return correct value`() {
        val uri = Uri.parse("https://newsapp.com/search?q=android&src=web")
        val query = uriParser.getQueryParameter(uri, "q")
        assertThat(query).isEqualTo("android")
    }

    @Test
    fun `getQueryParameter should return null if parameter missing`() {
        val uri = Uri.parse("https://newsapp.com/search?src=web")
        val query = uriParser.getQueryParameter(uri, "q")
        assertThat(query).isNull()
    }
}
