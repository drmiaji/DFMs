package com.drmiaji.fortyahadith.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import com.drmiaji.fortyahadith.R
import com.drmiaji.fortyahadith.activity.About
import com.drmiaji.fortyahadith.activity.BaseActivity
import com.drmiaji.fortyahadith.activity.SettingsActivity
import com.drmiaji.fortyahadith.data.Hadith
import com.drmiaji.fortyahadith.utils.ThemeUtils
import com.drmiaji.fortyahadith.utils.loadHadiths
import kotlin.math.abs

class WebViewActivity : BaseActivity() {

    override fun getLayoutResource() = R.layout.activity_webview
    private lateinit var webView: WebView
    private var currentIndex = 0
    private lateinit var chapters: List<Hadith> // all chapters

    private val gestureDetector by lazy {
        GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (abs(diffX) > abs(diffY) && abs(diffX) > 100 && abs(velocityX) > 100) {
                    if (diffX > 0) loadPreviousChapter()
                    else loadNextChapter()
                    return true
                }
                return false
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onActivityReady(savedInstanceState: Bundle?) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra("title") ?: "Reading"
        }

        setCustomFontToTitle(toolbar)
        val iconColor = ContextCompat.getColor(this, R.color.toolbar_icon_color)
        toolbar.navigationIcon?.let { drawable ->
            val wrapped = DrawableCompat.wrap(drawable).mutate()
            DrawableCompat.setTint(wrapped, iconColor)
            toolbar.navigationIcon = wrapped
        }

        webView = findViewById(R.id.webview) // <-- Use class property, not local variable!
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar?.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar?.visibility = View.GONE
            }
        }

        webView.settings.apply {
            javaScriptEnabled = false
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
            textZoom = 110
        }

        webView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) } // <-- Attach gesture detector

        val externalUrl = intent.getStringExtra("url")
        val hadithId = intent.getIntExtra("hadith_id", -1)

        if (externalUrl != null) {
            webView.loadUrl(externalUrl)
        } else if (hadithId != -1) {
            chapters = loadHadiths(this) // <-- Load all hadiths
            currentIndex = chapters.indexOfFirst { it.id == hadithId }.takeIf { it >= 0 } ?: 0

            if (chapters.isNotEmpty() && currentIndex in chapters.indices) {
                loadCurrentChapter()
            } else {
                webView.loadData("<h2>Hadith not found</h2>", "text/html", "utf-8")
            }
        } else {
            val fileName = intent.getStringExtra("fileName") ?: "chapter1.html"
            val themeMode = ThemeUtils.getCurrentThemeMode(this)
            val themeClass = when (themeMode) {
                ThemeUtils.THEME_DARK -> "dark"
                ThemeUtils.THEME_LIGHT -> "light"
                else -> {
                    val nightModeFlags = resources.configuration.uiMode and
                            android.content.res.Configuration.UI_MODE_NIGHT_MASK
                    if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES)
                        "dark" else "light"
                }
            }
            val baseHtml = assets.open("contents/base.html").bufferedReader().use { it.readText() }
            val contentHtml =
                assets.open("contents/topics/$fileName").bufferedReader().use { it.readText() }
            val fullHtml = baseHtml
                .replace("{{CONTENT}}", contentHtml)
                .replace("{{THEME}}", themeClass)
                .replace("{{STYLE}}", "")

            webView.loadDataWithBaseURL(
                "file:///android_asset/contents/",
                fullHtml,
                "text/html",
                "utf-8",
                null
            )
        }
    }

    private fun loadCurrentChapter() {
        if (chapters.isNotEmpty() && currentIndex in chapters.indices) {
            val hadith = chapters[currentIndex]
            val themeMode = ThemeUtils.getCurrentThemeMode(this)
            val themeClass = when (themeMode) {
                ThemeUtils.THEME_DARK -> "dark"
                ThemeUtils.THEME_LIGHT -> "light"
                else -> {
                    val nightModeFlags = resources.configuration.uiMode and
                            android.content.res.Configuration.UI_MODE_NIGHT_MASK
                    if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES)
                        "dark" else "light"
                }
            }
            val html = """
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="stylesheet" type="text/css" href="file:///android_asset/contents/style.css">
                </head>
                <body class="$themeClass">
                    <h2>${hadith.title}</h2>
                    <div class="arabic">${hadith.arabic}</div>
                    <div class="transliteration">${hadith.transliteration}</div>
                    <div class="translation">${hadith.translation}</div>
                    <div class="reference">${hadith.reference}</div>
                </body>
                </html>
            """.trimIndent()
            webView.loadDataWithBaseURL(
                "file:///android_asset/",
                html,
                "text/html",
                "utf-8",
                null
            )
        }
    }

    private fun loadPreviousChapter() {
        if (currentIndex > 0) {
            currentIndex--
            loadCurrentChapter()
        }
    }

    private fun loadNextChapter() {
        if (chapters.isNotEmpty() && currentIndex < chapters.size - 1) {
            currentIndex++
            loadCurrentChapter()
        }
    }

    private fun setCustomFontToTitle(toolbar: Toolbar) {
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView && view.text == toolbar.title) {
                val typeface = ResourcesCompat.getFont(this, R.font.solaimanlipi)
                view.typeface = typeface
                break
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        menu.findItem(R.id.action_search)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.share -> {
                val myIntent = Intent(Intent.ACTION_SEND)
                myIntent.setType("text/plain")
                val shareSub: String? = getString(R.string.share_subject)
                val shareBody: String? = getString(R.string.share_message)
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(myIntent, "Share using!"))
            }
            R.id.more_apps -> {
                val moreApp = Intent(Intent.ACTION_VIEW)
                moreApp.setData("https://play.google.com/store/apps/dev?id=5204491413792621474".toUri())
                startActivity(moreApp)
            }
            R.id.action_about_us -> {
                startActivity(Intent(this, About::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}