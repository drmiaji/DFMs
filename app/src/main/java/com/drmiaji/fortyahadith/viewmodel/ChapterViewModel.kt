package com.drmiaji.fortyahadith.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.drmiaji.fortyahadith.models.ChapterItem
import org.json.JSONArray
import java.io.BufferedReader

class ChapterViewModel(application: Application) : AndroidViewModel(application) {
    private val _chapters = MutableLiveData<List<ChapterItem>>()
    val chapters: LiveData<List<ChapterItem>> get() = _chapters

    init {
        loadIndexFromAssets()
    }

    private fun loadIndexFromAssets() {
        val assetManager = getApplication<Application>().assets
        val inputStream = assetManager.open("contents/index.json")
        val json = inputStream.bufferedReader().use(BufferedReader::readText)

        val jsonArray = JSONArray(json)
        val chapterList = mutableListOf<ChapterItem>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            // Defensive parsing: skip item if "path" or "title" is missing
            if (obj.has("title") && obj.has("path")) {
                chapterList.add(
                    ChapterItem(
                        title = obj.getString("title"),
                        file = obj.getString("path") // ✅ changed from "file"
                    )
                )
            }
        }
        _chapters.value = chapterList
    }
}