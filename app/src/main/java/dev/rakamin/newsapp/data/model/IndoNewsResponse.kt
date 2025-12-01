package dev.rakamin.newsapp.data.model

import com.google.gson.annotations.SerializedName

data class IndoNewsResponse(
    @SerializedName("message", alternate = ["messages"])
    val message: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("data")
    val data: List<IndoArticle>?
)

data class IndoArticle(
    @SerializedName("title")
    val title: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("contentSnippet", alternate = ["description"])
    val description: String?,
    @SerializedName("isoDate")
    val isoDate: String?,
    @SerializedName("image")
    val image: Any?
) {
    fun getImageUrl(): String? {
        return when (image) {
            is String -> image as String
            is Map<*, *> -> {
                val imageMap = image as Map<*, *>
                imageMap["large"]?.toString() ?: imageMap["small"]?.toString()
            }
            else -> null
        }
    }
    
    fun toArticle(sourceName: String): Article {
        return Article(
            source = Source(id = null, name = sourceName),
            author = null,
            title = title,
            description = description,
            url = link,
            urlToImage = getImageUrl(),
            publishedAt = isoDate,
            content = description
        )
    }
}
