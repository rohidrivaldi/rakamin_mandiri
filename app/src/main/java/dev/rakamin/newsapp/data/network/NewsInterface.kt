package dev.rakamin.newsapp.data.network

import dev.rakamin.newsapp.data.model.ArticleResponse
import dev.rakamin.newsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("v2/everything")
    suspend fun getHeadlines(
        @Query("q") query: String = Constants.DEFAULT_QUERY,
        @Query("pageSize") pageSize: Int = 10,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<ArticleResponse>

    @GET("v2/everything")
    suspend fun getAllNews(
        @Query("q") query: String = Constants.DEFAULT_QUERY,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<ArticleResponse>

    @GET("v2/everything")
    suspend fun getNewsByCategory(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = Constants.PAGE_SIZE,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<ArticleResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 50,
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<ArticleResponse>
}
