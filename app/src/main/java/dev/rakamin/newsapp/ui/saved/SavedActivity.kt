package dev.rakamin.newsapp.ui.saved

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.ActivitySavedBinding
import dev.rakamin.newsapp.ui.DetailActivity
import dev.rakamin.newsapp.ui.allnews.NewsAdapter
import dev.rakamin.newsapp.ui.bookmark.BookmarkViewModel
import dev.rakamin.newsapp.utils.LocaleHelper
import dev.rakamin.newsapp.utils.PreferenceManager

class SavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBinding
    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun attachBaseContext(newBase: Context) {
        val lang = PreferenceManager(newBase).language
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeBookmarks()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.nav_saved)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            openArticleDetail(article)
        }
        binding.rvSaved.apply {
            layoutManager = LinearLayoutManager(this@SavedActivity)
            adapter = newsAdapter
        }
    }

    private fun observeBookmarks() {
        viewModel.bookmarkedArticles.observe(this) { articles ->
            if (articles.isEmpty()) {
                binding.rvSaved.isVisible = false
                binding.layoutEmpty.isVisible = true
            } else {
                binding.rvSaved.isVisible = true
                binding.layoutEmpty.isVisible = false
                newsAdapter.submitList(articles)
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

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SavedActivity::class.java)
        }
    }
}
