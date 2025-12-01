package dev.rakamin.newsapp.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.FragmentBookmarkBinding
import dev.rakamin.newsapp.ui.DetailActivity
import dev.rakamin.newsapp.ui.allnews.NewsAdapter

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeBookmarks()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            openArticleDetail(article)
        }
        binding.rvBookmarks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun observeBookmarks() {
        viewModel.bookmarkedArticles.observe(viewLifecycleOwner) { articles ->
            if (articles.isEmpty()) {
                binding.rvBookmarks.isVisible = false
                binding.layoutEmpty.isVisible = true
            } else {
                binding.rvBookmarks.isVisible = true
                binding.layoutEmpty.isVisible = false
                newsAdapter.submitList(articles)
            }
        }
    }

    private fun openArticleDetail(article: Article) {
        val intent = DetailActivity.newIntent(
            requireContext(),
            title = article.title,
            description = article.description,
            content = article.content,
            imageUrl = article.urlToImage,
            source = article.source?.name,
            author = article.author,
            date = article.publishedAt,
            url = article.url
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
