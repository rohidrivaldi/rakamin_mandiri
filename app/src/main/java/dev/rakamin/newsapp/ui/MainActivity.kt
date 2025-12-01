package dev.rakamin.newsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.databinding.ActivityMainBinding
import dev.rakamin.newsapp.ui.saved.SavedActivity
import dev.rakamin.newsapp.ui.search.SearchActivity
import dev.rakamin.newsapp.utils.LocaleHelper
import dev.rakamin.newsapp.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun attachBaseContext(newBase: Context) {
        val lang = PreferenceManager(newBase).language
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupFab()
        loadHomeFragment()
    }

    private fun setupFab() {
        binding.fabSaved.setOnClickListener {
            startActivity(SavedActivity.newIntent(this))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        updateThemeIcon(menu.findItem(R.id.action_theme))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(SearchActivity.newIntent(this))
                true
            }
            R.id.action_theme -> {
                toggleTheme(item)
                true
            }
            R.id.action_language -> {
                showLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme(item: MenuItem) {
        val newDarkMode = !preferenceManager.isDarkMode
        preferenceManager.isDarkMode = newDarkMode
        
        // Apply and recreate
        AppCompatDelegate.setDefaultNightMode(
            if (newDarkMode) AppCompatDelegate.MODE_NIGHT_YES 
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun updateThemeIcon(item: MenuItem) {
        item.setIcon(
            if (preferenceManager.isDarkMode) R.drawable.ic_light_mode
            else R.drawable.ic_dark_mode
        )
        item.setTitle(
            if (preferenceManager.isDarkMode) R.string.light_mode
            else R.string.dark_mode
        )
    }

    private fun showLanguageDialog() {
        val languages = arrayOf(
            getString(R.string.indonesian),
            getString(R.string.english)
        )
        val currentIndex = if (preferenceManager.language == PreferenceManager.LANG_INDONESIAN) 0 else 1

        AlertDialog.Builder(this)
            .setTitle(R.string.select_language)
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                val newLang = if (which == 0) PreferenceManager.LANG_INDONESIAN else PreferenceManager.LANG_ENGLISH
                if (newLang != preferenceManager.language) {
                    preferenceManager.language = newLang
                    dialog.dismiss()
                    showRestartDialog()
                } else {
                    dialog.dismiss()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showRestartDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.app_restart_required)
            .setPositiveButton(R.string.restart) { _, _ ->
                recreate()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }
}
