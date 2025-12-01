package dev.rakamin.newsapp.ui.allnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.ItemNewsBinding
import dev.rakamin.newsapp.utils.DateFormatter

class NewsAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(article: Article) {
            binding.apply {
                tvNewsTitle.text = article.title ?: "-"
                tvNewsSource.text = article.source?.name ?: "-"
                tvNewsDate.text = DateFormatter.formatDate(article.publishedAt)
                tvNewsDescription.text = article.description ?: "-"

                Glide.with(ivNews.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivNews)
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
