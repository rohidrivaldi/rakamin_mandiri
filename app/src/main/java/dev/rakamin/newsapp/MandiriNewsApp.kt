package dev.rakamin.newsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dev.rakamin.newsapp.utils.PreferenceManager

class MandiriNewsApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Apply saved theme on app start
        val prefs = PreferenceManager(this)
        val mode = if (prefs.isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
