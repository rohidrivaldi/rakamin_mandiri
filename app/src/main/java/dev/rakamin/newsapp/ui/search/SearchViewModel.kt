package dev.rakamin.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.repository.NewsRepository
import dev.rakamin.newsapp.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _searchResults = MutableLiveData<Resource<List<Article>>>()
    val searchResults: LiveData<Resource<List<Article>>> = _searchResults

    fun search(query: String) {
        _searchResults.value = Resource.Loading()
        viewModelScope.launch {
            val result = repository.searchNews(query)
            _searchResults.postValue(result)
        }
    }
}
