package com.drmiaji.fortyahadith.utils

import android.content.Context
import com.drmiaji.fortyahadith.data.Hadith
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun loadHadiths(context: Context): List<Hadith> {
    return try {
        val json = context.assets.open("hadith.json").bufferedReader().use { it.readText() }
        Gson().fromJson(json, object : TypeToken<List<Hadith>>() {}.type)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}