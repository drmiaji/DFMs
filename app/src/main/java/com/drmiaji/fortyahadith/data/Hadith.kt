package com.drmiaji.fortyahadith.data

data class Hadith(
    val id: Int,
    val title: String,
    val arabic: String,
    val translation: String,
    val transliteration: String,
    val reference: String,
    val body: String
)