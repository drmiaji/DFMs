package com.drmiaji.fortyahadith.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.drmiaji.fortyahadith.data.Hadith
import com.drmiaji.fortyahadith.databinding.ItemChapterBinding
import com.drmiaji.fortyahadith.utils.HadithDiffCallback

class HadithAdapter(
    private var items: List<Hadith>,
    private val onClick: (Hadith) -> Unit
) : RecyclerView.Adapter<HadithAdapter.ViewHolder>() {

    fun stripHtml(html: String): String {
        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        // Remove leading/trailing and internal blank lines
        return spanned.replace(Regex("[\\r\\n]+"), " ").trim()
    }

    private var searchQuery: String = ""

    inner class ViewHolder(private val binding: ItemChapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Hadith) {
            val context = binding.root.context
            val typeface = Typeface.createFromAsset(context.assets, "font/solaimanlipi.ttf")
            // Highlight the search term if present
            if (searchQuery.isNotBlank()) {
                binding.chapterTitle.text = highlightSearch(item.title, searchQuery)
            } else {
                binding.chapterTitle.text = item.title
            }
            binding.chapterTitle.text = stripHtml(item.title)
            binding.chapterTitle.setTypeface(typeface, Typeface.BOLD)
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newList: List<Hadith>, query: String = "") {
        val diffCallback = HadithDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newList
        searchQuery = query
        diffResult.dispatchUpdatesTo(this)
    }

    // Helper to highlight search query
    private fun highlightSearch(text: String, query: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        val start = text.indexOf(query, ignoreCase = true)
        if (start >= 0) {
            builder.setSpan(
                ForegroundColorSpan(Color.RED), // or your highlight color
                start, start + query.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                StyleSpan(Typeface.BOLD),
                start, start + query.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return builder
    }
}