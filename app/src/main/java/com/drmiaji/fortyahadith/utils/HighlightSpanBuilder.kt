package com.drmiaji.fortyahadith.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.drmiaji.fortyahadith.R

class HighlightSpanBuilder(private val context: Context) {
    fun getHighlightedText(text: String, searchTerm: String): SpannableString {
        if (searchTerm.isEmpty()) {
            return SpannableString(text)
        }

        val spannableString = SpannableString(text)
        val highlightColor = ContextCompat.getColor(context, R.color.secondary)

        var startIndex = text.indexOf(searchTerm, ignoreCase = true)
        while (startIndex >= 0) {
            val endIndex = startIndex + searchTerm.length
            spannableString.setSpan(
                BackgroundColorSpan(highlightColor),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // You can also add a bold span for better visibility
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            startIndex = text.indexOf(searchTerm, endIndex, ignoreCase = true)
        }

        return spannableString
    }
}