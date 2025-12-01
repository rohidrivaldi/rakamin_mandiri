package dev.rakamin.newsapp.data.repository

import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.network.NewsApiClient
import dev.rakamin.newsapp.utils.Constants
import dev.rakamin.newsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository {

    private val api = NewsApiClient.newsInterface

    suspend fun getHeadlines(): Resource<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getHeadlines()
                if (response.isSuccessful) {
                    val articles = response.body()?.articles?.filter {
                        !it.title.isNullOrEmpty() && it.title != "[Removed]"
                    } ?: emptyList()
                    Resource.Success(articles)
                } else {
                    Resource.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }

    suspend fun getAllNews(page: Int, category: String? = null): Resource<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val query = if (category.isNullOrEmpty() || category == "all") {
                    Constants.DEFAULT_QUERY
                } else {
                    "$category AND ${Constants.DEFAULT_QUERY}"
                }
                val response = api.getNewsByCategory(query = query, page = page)
                if (response.isSuccessful) {
                    val articles = response.body()?.articles?.filter {
                        !it.title.isNullOrEmpty() && it.title != "[Removed]"
                    } ?: emptyList()
                    Resource.Success(articles)
                } else {
                    Resource.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }

    suspend fun searchNews(query: String): Resource<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchNews(query = query)
                if (response.isSuccessful) {
                    val articles = response.body()?.articles?.filter {
                        !it.title.isNullOrEmpty() && it.title != "[Removed]"
                    } ?: emptyList()
                    Resource.Success(articles)
                } else {
                    Resource.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }
}
