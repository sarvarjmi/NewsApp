package com.newsapp.core.deeplink

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DeepLinkValidatorTest {

    private val validator = DeepLinkValidator()

    @Test
    fun `isValid should return true for valid HTTPS app link`() {
        val uri = Uri.parse("https://newsapp.com/article/123")
        assertThat(validator.isValid(uri)).isTrue()
    }

    @Test
    fun `isValid should return true for valid custom scheme link`() {
        val uri = Uri.parse("newsapp://home")
        assertThat(validator.isValid(uri)).isTrue()
    }

    @Test
    fun `isValid should return false for unknown domain`() {
        val uri = Uri.parse("https://randomsite.com/article/123")
        assertThat(validator.isValid(uri)).isFalse()
    }

    @Test
    fun `isValid should return false for invalid scheme`() {
        val uri = Uri.parse("ftp://newsapp.com/article/123")
        assertThat(validator.isValid(uri)).isFalse()
    }

    @Test
    fun `isValidArticleId should return true for valid ID`() {
        assertThat(validator.isValidArticleId("https://example.com/123")).isTrue()
    }

    @Test
    fun `isValidArticleId should return false for short ID`() {
        assertThat(validator.isValidArticleId("12")).isFalse()
    }

    @Test
    fun `isValidArticleId should return false for blank ID`() {
        assertThat(validator.isValidArticleId("  ")).isFalse()
    }
}
