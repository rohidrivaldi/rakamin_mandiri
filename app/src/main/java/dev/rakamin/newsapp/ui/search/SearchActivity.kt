package dev.rakamin.newsapp.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.ActivitySearchBinding
import dev.rakamin.newsapp.ui.DetailActivity
import dev.rakamin.newsapp.ui.allnews.NewsAdapter
import dev.rakamin.newsapp.utils.Resource

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerView()
        observeResults()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener { finish() }
        
        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            binding.btnClear.isVisible = false
        }

        binding.etSearch.addTextChangedListener { text ->
            binding.btnClear.isVisible = !text.isNullOrEmpty()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        // Auto focus and show keyboard
        binding.etSearch.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            openArticleDetail(article)
        }
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = newsAdapter
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        if (query.isNotEmpty()) {
            hideKeyboard()
            viewModel.search(query)
        }
    }

    private fun observeResults() {
        viewModel.searchResults.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvSearchResults.isVisible = false
                    binding.layoutEmpty.isVisible = false
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val articles = resource.data ?: emptyList()
                    if (articles.isNotEmpty()) {
                        binding.rvSearchResults.isVisible = true
                        binding.layoutEmpty.isVisible = false
                        newsAdapter.submitList(articles)
                    } else {
                        binding.rvSearchResults.isVisible = false
                        binding.layoutEmpty.isVisible = true
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    binding.rvSearchResults.isVisible = false
                    binding.layoutEmpty.isVisible = true
                    binding.tvEmpty.text = resource.message
                }
            }
        }
    }

    private fun openArticleDetail(article: Article) {
        val intent = DetailActivity.newIntent(
            this,
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

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
}
