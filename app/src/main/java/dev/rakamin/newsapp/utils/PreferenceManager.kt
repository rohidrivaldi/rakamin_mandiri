package dev.rakamin.newsapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "mandiri_news_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
        const val LANG_INDONESIAN = "id"
        const val LANG_ENGLISH = "en"
    }

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            prefs.edit().putBoolean(KEY_DARK_MODE, value).commit()
        }

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, LANG_INDONESIAN) ?: LANG_INDONESIAN
        set(value) {
            prefs.edit().putString(KEY_LANGUAGE, value).apply()
        }

    fun applyTheme(isDark: Boolean = isDarkMode) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun isEnglish(): Boolean = language == LANG_ENGLISH
}
