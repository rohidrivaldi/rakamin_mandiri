package dev.rakamin.newsapp.ui.allnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.repository.NewsRepository
import dev.rakamin.newsapp.utils.Resource
import kotlinx.coroutines.launch

class AllNewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _allNews = MutableLiveData<Resource<List<Article>>>()
    val allNews: LiveData<Resource<List<Article>>> = _allNews

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val articlesList = mutableListOf<Article>()
    private var currentPage = 1
    private var isLastPage = false
    private var currentCategory: String? = null
    var isLoading = false
        private set

    init {
        fetchAllNews()
    }

    fun fetchAllNews(category: String? = null) {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            _allNews.value = Resource.Loading()
            currentPage = 1
            isLastPage = false
            currentCategory = category
            articlesList.clear()

            val result = repository.getAllNews(currentPage, currentCategory)
            when (result) {
                is Resource.Success -> {
                    result.data?.let { articles ->
                        articlesList.addAll(articles)
                        isLastPage = articles.isEmpty()
                    }
                    _allNews.value = Resource.Success(articlesList.toList())
                }
                is Resource.Error -> {
                    _allNews.value = Resource.Error(result.message)
                }
                else -> {}
            }
            isLoading = false
        }
    }

    fun setCategory(category: String?) {
        if (currentCategory != category) {
            fetchAllNews(category)
        }
    }

    fun loadMoreNews() {
        if (isLoading || isLastPage) return

        viewModelScope.launch {
            isLoading = true
            _isLoadingMore.value = true
            currentPage++

            val result = repository.getAllNews(currentPage, currentCategory)
            when (result) {
                is Resource.Success -> {
                    result.data?.let { articles ->
                        if (articles.isEmpty()) {
                            isLastPage = true
                        } else {
                            articlesList.addAll(articles)
                        }
                    }
                    _allNews.value = Resource.Success(articlesList.toList())
                }
                is Resource.Error -> {
                    currentPage--
                }
                else -> {}
            }
            isLoading = false
            _isLoadingMore.value = false
        }
    }
}
