package dev.rakamin.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.model.Source

@Entity(tableName = "bookmarked_articles")
data class BookmarkedArticle(
    @PrimaryKey
    val url: String,
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val bookmarkedAt: Long = System.currentTimeMillis()
) {
    fun toArticle(): Article {
        return Article(
            source = Source(id = null, name = sourceName),
            author = author,
            title = title,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )
    }

    companion object {
        fun fromArticle(article: Article): BookmarkedArticle {
            return BookmarkedArticle(
                url = article.url ?: "",
                sourceName = article.source?.name,
                author = article.author,
                title = article.title,
                description = article.description,
                urlToImage = article.urlToImage,
                publishedAt = article.publishedAt,
                content = article.content
            )
        }
    }
}
