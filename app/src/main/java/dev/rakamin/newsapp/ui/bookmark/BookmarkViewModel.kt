package dev.rakamin.newsapp.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dev.rakamin.newsapp.data.local.NewsDatabase
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.repository.BookmarkRepository
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookmarkRepository

    val bookmarkedArticles: LiveData<List<Article>>

    init {
        val bookmarkDao = NewsDatabase.getDatabase(application).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        bookmarkedArticles = repository.allBookmarks.map { list ->
            list.map { it.toArticle() }
        }
    }

    fun isBookmarked(url: String): LiveData<Boolean> = repository.isBookmarked(url)

    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            repository.toggleBookmark(article)
        }
    }
}
