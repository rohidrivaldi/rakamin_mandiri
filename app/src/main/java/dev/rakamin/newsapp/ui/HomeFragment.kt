package dev.rakamin.newsapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.data.model.Article
import dev.rakamin.newsapp.databinding.FragmentHomeBinding
import dev.rakamin.newsapp.ui.allnews.AllNewsViewModel
import dev.rakamin.newsapp.ui.allnews.NewsAdapter
import dev.rakamin.newsapp.ui.headline.HeadlineAdapter
import dev.rakamin.newsapp.ui.headline.HeadlineViewModel
import dev.rakamin.newsapp.utils.Resource
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val headlineViewModel: HeadlineViewModel by viewModels()
    private val allNewsViewModel: AllNewsViewModel by viewModels()

    private lateinit var headlineAdapter: HeadlineAdapter
    private lateinit var newsAdapter: NewsAdapter

    private val autoSlideHandler = Handler(Looper.getMainLooper())
    private var autoSlideRunnable: Runnable? = null
    private var headlineCount = 0
    private var isAutoSliding = false
    private var isUserScrolling = false

    companion object {
        private const val AUTO_SLIDE_DELAY = 4000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupCarouselNavigation()
        setupCategoryChips()
        setupSwipeRefresh()
        setupScrollListener()
        setupRetryButtons()
        observeHeadlines()
        observeAllNews()
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val category = when (checkedIds.first()) {
                    R.id.chipAll -> null
                    R.id.chipBusiness -> "business"
                    R.id.chipTechnology -> "technology"
                    R.id.chipSports -> "sports"
                    R.id.chipEntertainment -> "entertainment"
                    R.id.chipPolitics -> "politics"
                    else -> null
                }
                allNewsViewModel.setCategory(category)
            }
        }
    }

    private fun setupAdapters() {
        headlineAdapter = HeadlineAdapter { article ->
            openArticleDetail(article)
        }
        binding.vpHeadline.adapter = headlineAdapter
        
        binding.vpHeadline.setPageTransformer(ZoomOutPageTransformer())

        binding.vpHeadline.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        isUserScrolling = true
                        stopAutoSlide()
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        if (isUserScrolling) {
                            isUserScrolling = false
                            startAutoSlide()
                        }
                    }
                }
            }
            
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        newsAdapter = NewsAdapter { article ->
            openArticleDetail(article)
        }
        binding.rvAllNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupCarouselNavigation() {
        binding.btnArrowLeft.setOnClickListener {
            val currentItem = binding.vpHeadline.currentItem
            if (currentItem > 0) {
                binding.vpHeadline.setCurrentItem(currentItem - 1, true)
            } else {
                binding.vpHeadline.setCurrentItem(headlineCount - 1, true)
            }
        }

        binding.btnArrowRight.setOnClickListener {
            val currentItem = binding.vpHeadline.currentItem
            if (currentItem < headlineCount - 1) {
                binding.vpHeadline.setCurrentItem(currentItem + 1, true)
            } else {
                binding.vpHeadline.setCurrentItem(0, true)
            }
        }
    }

    private fun setupIndicators(count: Int) {
        binding.indicatorContainer.removeAllViews()
        headlineCount = count

        for (i in 0 until count) {
            val dot = ImageView(requireContext()).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        if (i == 0) R.drawable.indicator_dot_active else R.drawable.indicator_dot_inactive
                    )
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(6, 0, 6, 0)
                }
                layoutParams = params
            }
            binding.indicatorContainer.addView(dot)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until binding.indicatorContainer.childCount) {
            val dot = binding.indicatorContainer.getChildAt(i) as ImageView
            dot.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == position) R.drawable.indicator_dot_active else R.drawable.indicator_dot_inactive
                )
            )
        }
    }

    private fun startAutoSlide() {
        stopAutoSlide()
        autoSlideRunnable = object : Runnable {
            override fun run() {
                if (headlineCount > 0 && !isUserScrolling && _binding != null) {
                    val currentItem = binding.vpHeadline.currentItem
                    val nextItem = (currentItem + 1) % headlineCount
                    isAutoSliding = true
                    binding.vpHeadline.setCurrentItem(nextItem, true)
                    autoSlideHandler.postDelayed({
                        isAutoSliding = false
                    }, 500)
                }
                autoSlideHandler.postDelayed(this, AUTO_SLIDE_DELAY)
            }
        }
        autoSlideHandler.postDelayed(autoSlideRunnable!!, AUTO_SLIDE_DELAY)
    }

    private fun stopAutoSlide() {
        autoSlideRunnable?.let { 
            autoSlideHandler.removeCallbacks(it)
            autoSlideRunnable = null
        }
        isAutoSliding = false
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(
            R.color.mandiri_blue,
            R.color.mandiri_gold
        )
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun setupScrollListener() {
        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                val totalHeight = v.getChildAt(0).measuredHeight
                val scrollViewHeight = v.measuredHeight

                if (scrollY >= totalHeight - scrollViewHeight - 200) {
                    if (!allNewsViewModel.isLoading) {
                        allNewsViewModel.loadMoreNews()
                    }
                }
            }
        )
    }

    private fun setupRetryButtons() {
        binding.btnRetryHeadline.setOnClickListener {
            headlineViewModel.fetchHeadlines()
        }
        binding.btnRetryAllNews.setOnClickListener {
            allNewsViewModel.fetchAllNews()
        }
    }

    private fun observeHeadlines() {
        headlineViewModel.headlines.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressHeadline.isVisible = true
                    binding.vpHeadline.isVisible = false
                    binding.btnArrowLeft.isVisible = false
                    binding.btnArrowRight.isVisible = false
                    binding.indicatorContainer.isVisible = false
                    binding.layoutErrorHeadline.isVisible = false
                }
                is Resource.Success -> {
                    binding.progressHeadline.isVisible = false
                    binding.swipeRefresh.isRefreshing = false

                    val articles = resource.data?.take(5) ?: emptyList()
                    if (articles.isNotEmpty()) {
                        binding.vpHeadline.isVisible = true
                        binding.btnArrowLeft.isVisible = true
                        binding.btnArrowRight.isVisible = true
                        binding.indicatorContainer.isVisible = true
                        binding.layoutErrorHeadline.isVisible = false
                        headlineAdapter.submitList(articles)
                        setupIndicators(articles.size)
                        startAutoSlide()
                    } else {
                        binding.vpHeadline.isVisible = false
                        binding.btnArrowLeft.isVisible = false
                        binding.btnArrowRight.isVisible = false
                        binding.indicatorContainer.isVisible = false
                        binding.layoutErrorHeadline.isVisible = true
                        binding.tvErrorHeadline.text = getString(R.string.no_news)
                    }
                }
                is Resource.Error -> {
                    binding.progressHeadline.isVisible = false
                    binding.vpHeadline.isVisible = false
                    binding.btnArrowLeft.isVisible = false
                    binding.btnArrowRight.isVisible = false
                    binding.indicatorContainer.isVisible = false
                    binding.layoutErrorHeadline.isVisible = true
                    binding.tvErrorHeadline.text = resource.message
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun observeAllNews() {
        allNewsViewModel.allNews.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (newsAdapter.currentList.isEmpty()) {
                        binding.progressAllNews.isVisible = true
                        binding.rvAllNews.isVisible = false
                        binding.layoutErrorAllNews.isVisible = false
                    }
                }
                is Resource.Success -> {
                    binding.progressAllNews.isVisible = false
                    binding.swipeRefresh.isRefreshing = false

                    val articles = resource.data ?: emptyList()
                    if (articles.isNotEmpty()) {
                        binding.rvAllNews.isVisible = true
                        binding.layoutErrorAllNews.isVisible = false
                        newsAdapter.submitList(articles)
                    } else {
                        binding.rvAllNews.isVisible = false
                        binding.layoutErrorAllNews.isVisible = true
                        binding.tvErrorAllNews.text = getString(R.string.no_news)
                    }
                }
                is Resource.Error -> {
                    binding.progressAllNews.isVisible = false
                    binding.swipeRefresh.isRefreshing = false

                    if (newsAdapter.currentList.isEmpty()) {
                        binding.rvAllNews.isVisible = false
                        binding.layoutErrorAllNews.isVisible = true
                        binding.tvErrorAllNews.text = resource.message
                    }
                }
            }
        }

        allNewsViewModel.isLoadingMore.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoadMore.isVisible = isLoading
        }
    }

    private fun refreshData() {
        stopAutoSlide()
        headlineViewModel.fetchHeadlines()
        allNewsViewModel.fetchAllNews()
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

    override fun onResume() {
        super.onResume()
        if (headlineCount > 0) {
            startAutoSlide()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSlide()
        _binding = null
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 1 -> {
                        val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> {
                        alpha = 0f
                    }
                }
            }
        }
    }
}
