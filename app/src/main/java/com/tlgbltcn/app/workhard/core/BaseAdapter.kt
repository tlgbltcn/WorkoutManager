package com.tlgbltcn.app.workhard.core

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.generalmobile.assistant.base.BaseDiffCallback
import com.tlgbltcn.app.workhard.R

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = getViewHolder(parent, viewType)

    open fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = BaseViewHolder(createBinding(parent, viewType))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<*>).binding.root.setTag(R.string.position, position)
        bind(holder.binding, position)
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    protected abstract fun bind(binding: ViewDataBinding, position: Int)

    open fun setList(list: List<T>) {
        updateList(list)
    }

    open fun getDiffCallBack(list: List<T>): DiffUtil.Callback = BaseDiffCallback(items, list)

    open fun updateList(list: List<T>) {
        val result = DiffUtil.calculateDiff(getDiffCallBack(list), true)

        items.clear()
        items.addAll(list)

        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size
}