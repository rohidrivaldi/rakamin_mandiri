package dev.rakamin.newsapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.data.model.Source
import dev.rakamin.newsapp.databinding.ActivityDetailBinding
import dev.rakamin.newsapp.ui.bookmark.BookmarkViewModel
import dev.rakamin.newsapp.utils.DateFormatter
import dev.rakamin.newsapp.utils.LocaleHelper
import dev.rakamin.newsapp.utils.PreferenceManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var preferenceManager: PreferenceManager
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private var isBookmarked = false
    private var bookmarkMenuItem: MenuItem? = null

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_CONTENT = "extra_content"
        private const val EXTRA_IMAGE_URL = "extra_image_url"
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_AUTHOR = "extra_author"
        private const val EXTRA_DATE = "extra_date"
        private const val EXTRA_URL = "extra_url"

        fun newIntent(
            context: Context,
            title: String?,
            description: String?,
            content: String?,
            imageUrl: String?,
            source: String?,
            author: String?,
            date: String?,
            url: String?
        ): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_DESCRIPTION, description)
                putExtra(EXTRA_CONTENT, content)
                putExtra(EXTRA_IMAGE_URL, imageUrl)
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_AUTHOR, author)
                putExtra(EXTRA_DATE, date)
                putExtra(EXTRA_URL, url)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = PreferenceManager(newBase).language
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(this)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadData()
        setupButtons()
        observeBookmarkState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        bookmarkMenuItem = menu.findItem(R.id.action_bookmark)
        updateBookmarkIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                toggleBookmark()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeBookmarkState() {
        val url = intent.getStringExtra(EXTRA_URL) ?: return
        bookmarkViewModel.isBookmarked(url).observe(this) { bookmarked ->
            isBookmarked = bookmarked
            updateBookmarkIcon()
        }
    }

    private fun updateBookmarkIcon() {
        bookmarkMenuItem?.setIcon(
            if (isBookmarked) R.drawable.ic_bookmark_filled
            else R.drawable.ic_bookmark_outline
        )
    }

    private fun toggleBookmark() {
        val article = createArticleFromIntent()
        bookmarkViewModel.toggleBookmark(article)
        
        val message = if (isBookmarked) {
            R.string.bookmark_removed
        } else {
            R.string.bookmark_added
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createArticleFromIntent(): Article {
        return Article(
            source = Source(id = null, name = intent.getStringExtra(EXTRA_SOURCE)),
            author = intent.getStringExtra(EXTRA_AUTHOR),
            title = intent.getStringExtra(EXTRA_TITLE),
            description = intent.getStringExtra(EXTRA_DESCRIPTION),
            url = intent.getStringExtra(EXTRA_URL),
            urlToImage = intent.getStringExtra(EXTRA_IMAGE_URL),
            publishedAt = intent.getStringExtra(EXTRA_DATE),
            content = intent.getStringExtra(EXTRA_CONTENT)
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun loadData() {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val content = intent.getStringExtra(EXTRA_CONTENT)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val source = intent.getStringExtra(EXTRA_SOURCE)
        val author = intent.getStringExtra(EXTRA_AUTHOR)
        val date = intent.getStringExtra(EXTRA_DATE)

        binding.collapsingToolbar.title = title ?: getString(R.string.detail_title)
        binding.tvDetailTitle.text = title ?: "-"
        binding.tvDetailDescription.text = description ?: getString(R.string.no_description)
        
        val contentText = content?.replace(Regex("\\[\\+\\d+ chars\\]"), "")?.trim()
        binding.tvDetailContent.text = if (contentText.isNullOrEmpty()) {
            getString(R.string.no_content)
        } else {
            contentText
        }

        binding.chipSource.text = source ?: getString(R.string.unknown_source)
        binding.tvDetailAuthor.text = "${getString(R.string.author)}: ${author ?: getString(R.string.unknown_author)}"
        binding.tvDetailDate.text = DateFormatter.formatDate(date)

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivDetailImage)
    }

    private fun setupButtons() {
        val url = intent.getStringExtra(EXTRA_URL)

        binding.btnReadFull.setOnClickListener {
            url?.let {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }

        binding.btnShare.setOnClickListener {
            val title = intent.getStringExtra(EXTRA_TITLE)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, "$title\n\n${getString(R.string.read_full_article)}: $url")
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }
    }
}
