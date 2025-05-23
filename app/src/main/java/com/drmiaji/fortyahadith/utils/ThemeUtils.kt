package com.drmiaji.fortyahadith.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.drmiaji.fortyahadith.MainActivity

object ThemeUtils {
    private const val PREF_NAME = "theme_pref"
    private const val KEY_THEME_MODE = "theme_mode"

    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"

    fun applyTheme(context: Context) {
        when (getCurrentThemeMode(context)) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    fun saveThemeMode(context: Context, mode: String) {
        if (mode !in setOf(THEME_LIGHT, THEME_DARK, THEME_SYSTEM)) return

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_THEME_MODE, mode) }
        applyTheme(context)
        restartApp(context)
    }

    fun saveThemeModeWithoutRestart(context: Context, mode: String) {
        if (mode !in setOf(THEME_LIGHT, THEME_DARK, THEME_SYSTEM)) return

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_THEME_MODE, mode) }
        applyTheme(context)

        if (context is Activity) {
            context.recreate()  // Smoothly applies theme
        }
    }

    fun getCurrentThemeMode(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM
    }

    private fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}