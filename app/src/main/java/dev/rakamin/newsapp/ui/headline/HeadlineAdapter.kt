package dev.rakamin.newsapp.ui.headline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.ItemHeadlineBinding
import dev.rakamin.newsapp.utils.DateFormatter

class HeadlineAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, HeadlineAdapter.HeadlineViewHolder>(HeadlineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val binding = ItemHeadlineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeadlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HeadlineViewHolder(
        private val binding: ItemHeadlineBinding
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
                tvHeadlineTitle.text = article.title ?: "-"
                tvHeadlineSource.text = article.source?.name ?: "-"
                tvHeadlineDate.text = DateFormatter.formatDate(article.publishedAt)

                Glide.with(ivHeadline.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivHeadline)
            }
        }
    }

    private class HeadlineDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
