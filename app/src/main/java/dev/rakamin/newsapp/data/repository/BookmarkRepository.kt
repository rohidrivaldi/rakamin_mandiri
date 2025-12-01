package dev.rakamin.newsapp.data.repository

import androidx.lifecycle.LiveData
import dev.rakamin.newsapp.data.local.BookmarkDao
import dev.rakamin.newsapp.data.local.BookmarkedArticle
import dev.rakamin.newsapp.data.model.Article

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    val allBookmarks: LiveData<List<BookmarkedArticle>> = bookmarkDao.getAllBookmarks()

    fun isBookmarked(url: String): LiveData<Boolean> = bookmarkDao.isBookmarked(url)

    suspend fun isBookmarkedSync(url: String): Boolean = bookmarkDao.isBookmarkedSync(url)

    suspend fun addBookmark(article: Article) {
        val bookmarkedArticle = BookmarkedArticle.fromArticle(article)
        bookmarkDao.insertBookmark(bookmarkedArticle)
    }

    suspend fun removeBookmark(article: Article) {
        article.url?.let { bookmarkDao.deleteByUrl(it) }
    }

    suspend fun toggleBookmark(article: Article): Boolean {
        val url = article.url ?: return false
        return if (bookmarkDao.isBookmarkedSync(url)) {
            bookmarkDao.deleteByUrl(url)
            false
        } else {
            bookmarkDao.insertBookmark(BookmarkedArticle.fromArticle(article))
            true
        }
    }
}
