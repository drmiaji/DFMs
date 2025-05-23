package com.drmiaji.fortyahadith.utils

import androidx.recyclerview.widget.DiffUtil
import com.drmiaji.fortyahadith.data.Hadith

class HadithDiffCallback(
    private val oldList: List<Hadith>,
    private val newList: List<Hadith>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}