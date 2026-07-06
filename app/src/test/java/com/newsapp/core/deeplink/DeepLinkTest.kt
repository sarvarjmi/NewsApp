package com.newsapp.core.deeplink

import android.net.Uri
import com.newsapp.presentation.navigation.RouteParser
import com.newsapp.presentation.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DeepLinkTest {

    private lateinit var routeParser: RouteParser
    private lateinit var validator: DeepLinkValidator
    private lateinit var uriParser: UriParser

    @Before
    fun setup() {
        validator = DeepLinkValidator()
        uriParser = UriParser()
        routeParser = RouteParser(validator, uriParser)
    }

    @Test
    fun `valid article app link should parse to ArticleDeepLink route`() {
        val uri = Uri.parse("https://newsapp.com/article/http%3A%2F%2Fexample.com")
        val route = routeParser.parse(uri)
        
        assertTrue(route is Routes.ArticleDeepLink)
        assertEquals("http://example.com", (route as Routes.ArticleDeepLink).url)
    }

    @Test
    fun `valid search app link should parse to Search route`() {
        val uri = Uri.parse("https://newsapp.com/search?q=android")
        val route = routeParser.parse(uri)
        
        assertTrue(route is Routes.Search)
        assertEquals("android", (route as Routes.Search).query)
    }

    @Test
    fun `valid custom scheme article link should parse to ArticleDeepLink route`() {
        val uri = Uri.parse("newsapp://article/https%3A%2F%2Fapnews.com%2F123")
        val route = routeParser.parse(uri)
        
        assertTrue(route is Routes.ArticleDeepLink)
        assertEquals("https://apnews.com/123", (route as Routes.ArticleDeepLink).url)
    }

    @Test
    fun `valid home link should parse to Home route`() {
        val uri = Uri.parse("https://newsapp.com/home")
        val route = routeParser.parse(uri)
        assertEquals(Routes.Home, route)
    }

    @Test
    fun `custom scheme home link should be valid`() {
        val uri = Uri.parse("newsapp://home")
        val route = routeParser.parse(uri)
        assertEquals(Routes.Home, route)
    }

    @Test
    fun `untrusted domain should be invalid`() {
        val uri = Uri.parse("https://malicious.com/article/123")
        val route = routeParser.parse(uri)
        
        assertEquals(null, route)
    }

    @Test
    fun `malformed article link should return null`() {
        val uri = Uri.parse("https://newsapp.com/article") // Missing ID
        val route = routeParser.parse(uri)
        
        assertEquals(null, route)
    }
}
