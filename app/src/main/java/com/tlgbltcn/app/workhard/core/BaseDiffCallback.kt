package com.tlgbltcn.app.workhard.core

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffCallback<out T>(val oldList: List<T>, val newList: List<T>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
}