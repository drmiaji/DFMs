package com.drmiaji.fortyahadith.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drmiaji.fortyahadith.R
import com.drmiaji.fortyahadith.activity.About
import com.drmiaji.fortyahadith.activity.BaseActivity
import com.drmiaji.fortyahadith.activity.SettingsActivity
import com.drmiaji.fortyahadith.adapter.HadithAdapter
import com.drmiaji.fortyahadith.data.Hadith
import com.drmiaji.fortyahadith.models.ChapterItem
import com.drmiaji.fortyahadith.utils.loadHadiths
import com.drmiaji.fortyahadith.viewmodel.ChapterViewModel
import com.google.android.material.appbar.MaterialToolbar


class ChapterListActivity : BaseActivity() {
    private lateinit var adapter: HadithAdapter
    private var allHadiths: List<Hadith> = emptyList()

    override fun getLayoutResource() = R.layout.activity_chapter_list

    override fun onActivityReady(savedInstanceState: Bundle?) {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val titleTextView = findViewById<TextView>(R.id.toolbar_title)
        allHadiths = loadHadiths(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        titleTextView.text = getString(R.string.app_name)
        val typeface = ResourcesCompat.getFont(this, R.font.solaimanlipi)
        titleTextView.typeface = typeface

        val iconColor = ContextCompat.getColor(this, R.color.toolbar_icon_color)
        toolbar.navigationIcon?.let { drawable ->
            val wrapped = DrawableCompat.wrap(drawable).mutate()
            DrawableCompat.setTint(wrapped, iconColor)
            toolbar.navigationIcon = wrapped
        }

        val recyclerView = findViewById<RecyclerView>(R.id.chapter_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HadithAdapter(allHadiths) { hadith ->
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("hadith_id", hadith.id)
            intent.putExtra("title", hadith.title)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun filterHadiths(query: String) {
        val filtered = if (query.isBlank()) {
            allHadiths
        } else {
            allHadiths.filter { it.title.contains(query, ignoreCase = true) }
        }
        adapter.updateData(filtered, query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        val iconColor = ContextCompat.getColor(this, R.color.toolbar_icon_color)
        for (i in 0 until menu.size) {
            val menuItem = menu[i]
            menuItem.icon?.let { icon ->
                DrawableCompat.setTint(DrawableCompat.wrap(icon), iconColor)
            }
        }

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as? androidx.appcompat.widget.SearchView
        // Change the text color
        val searchEditText = searchView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.setTextColor(Color.WHITE) // Your desired color
        searchEditText?.setHintTextColor(Color.GRAY) // Optional: hint color
        searchView?.queryHint = "Search hadiths..."
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterHadiths(newText.orEmpty())
                return true
            }
        })
        return true
    }

    // Handle the back button click
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