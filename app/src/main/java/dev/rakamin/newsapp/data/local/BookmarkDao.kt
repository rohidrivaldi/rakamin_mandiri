package dev.rakamin.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarked_articles ORDER BY bookmarkedAt DESC")
    fun getAllBookmarks(): LiveData<List<BookmarkedArticle>>

    @Query("SELECT * FROM bookmarked_articles WHERE url = :url")
    suspend fun getBookmarkByUrl(url: String): BookmarkedArticle?

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_articles WHERE url = :url)")
    fun isBookmarked(url: String): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_articles WHERE url = :url)")
    suspend fun isBookmarkedSync(url: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(article: BookmarkedArticle)

    @Delete
    suspend fun deleteBookmark(article: BookmarkedArticle)

    @Query("DELETE FROM bookmarked_articles WHERE url = :url")
    suspend fun deleteByUrl(url: String)
}
