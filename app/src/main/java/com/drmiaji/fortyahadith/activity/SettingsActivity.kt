package com.drmiaji.fortyahadith.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.drmiaji.fortyahadith.R
import com.drmiaji.fortyahadith.utils.SettingsFragment
import com.drmiaji.fortyahadith.utils.ThemeUtils
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.applyTheme(this) // Keep your existing theme application
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings) // Set the new layout with toolbar

        // Set up the toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val titleTextView = findViewById<TextView>(R.id.toolbar_title)

        setSupportActionBar(toolbar)
        // Show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Hide default title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set custom title
        titleTextView.text = getString(R.string.app_name) // Use a string resource or "Settings"

        // Optional: Tint the back arrow (navigation icon) if needed
        val navIconColor = ContextCompat.getColor(this, R.color.nav_icon_color)
        toolbar.navigationIcon?.let { originalDrawable ->
            val wrappedDrawable = DrawableCompat.wrap(originalDrawable).mutate()
            DrawableCompat.setTint(wrappedDrawable, navIconColor)
            toolbar.navigationIcon = wrappedDrawable
        }

        // Add the settings fragment if this is the first creation
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }

    // Handle back button clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}