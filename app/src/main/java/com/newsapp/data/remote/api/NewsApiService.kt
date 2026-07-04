package com.newsapp.data.remote.api

import com.newsapp.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the News API service.
 * Documentation: https://newsapi.org/docs/endpoints
 */
interface NewsApiService {

    /**
     * Fetches top headlines from the specified country and category.
     * 
     * @param country The 2-letter ISO 3166-1 code of the country you want to get headlines for.
     * @param category The category you want to get headlines for (e.g., business, technology).
     * @param page The page number to fetch (for pagination).
     * @param pageSize The number of results to return per page.
     * @return [NewsResponseDto] containing a list of articles.
     */
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): NewsResponseDto

    /**
     * Searches for articles across the entire News API database.
     * 
     * @param query The search query string (keywords or phrases).
     * @param page The page number to fetch (for pagination).
     * @param pageSize The number of results to return per page.
     * @param language The 2-letter ISO-639-1 code of the language to fetch articles in.
     * @param sortBy The order to sort the articles in (e.g., publishedAt, relevancy).
     * @return [NewsResponseDto] containing a list of articles.
     */
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): NewsResponseDto
}
