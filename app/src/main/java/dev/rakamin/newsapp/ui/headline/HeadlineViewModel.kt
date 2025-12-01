package dev.rakamin.newsapp.ui.headline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.repository.NewsRepository
import dev.rakamin.newsapp.utils.Resource
import kotlinx.coroutines.launch

class HeadlineViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _headlines = MutableLiveData<Resource<List<Article>>>()
    val headlines: LiveData<Resource<List<Article>>> = _headlines

    init {
        fetchHeadlines()
    }

    fun fetchHeadlines() {
        viewModelScope.launch {
            _headlines.value = Resource.Loading()
            val result = repository.getHeadlines()
            _headlines.value = result
        }
    }
}
