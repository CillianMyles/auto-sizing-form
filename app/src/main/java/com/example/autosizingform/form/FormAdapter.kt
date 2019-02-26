package com.example.autosizingform.form

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.autosizingform.R
import com.example.autosizingform.form.StringExt.notNullOrEmpty

/**
 * Created by Cillian Myles on 26/02/2019.
 * Copyright (c) 2019 Cillian Myles. All rights reserved.
 */

class FormAdapter : RecyclerView.Adapter<FormHolder>(), FormListener {

    companion object {
        private val TAG = FormAdapter::class.java.simpleName
        private const val EMPTY = ""
        private const val MIN_SIZE = 1
        private const val MAX_SIZE = 3
    }

    private var list: MutableList<String> = emptyList()

    private var recycler: RecyclerView? = null // TODO: remove !!

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FormHolder {
        val inflated = LayoutInflater.from(parent.context)
                .inflate(R.layout.form_list_item, parent, false)
        val holder = FormHolder(inflated)
        inflated.tag = holder
        return holder
    }

    override fun onBindViewHolder(holder: FormHolder, position: Int) {
        holder.listener = this
        holder.paused = true
        holder.show(list[position])
        holder.paused = false
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) { // TODO: remove !!
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    override fun onItemCleared(layoutPosition: Int) {
        Log.e(TAG, "onItemCleared - layoutPosition: $layoutPosition") // TODO: remove
        if (size > MIN_SIZE) {
            list.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }
    }

    override fun onItemRemoved(layoutPosition: Int) {
        Log.e(TAG, "onItemRemoved - layoutPosition: $layoutPosition") // TODO: remove
        if (size > MIN_SIZE) {
            list.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }
    }

    override fun onNewItemNeeded(generatedByPosition: Int) {
        Log.e(TAG, "onNewItemNeeded - generatedByPosition: $generatedByPosition") // TODO: remove
        if (size < MAX_SIZE && lastIsNonEmpty()) { // TODO: fix check - list does not have up to date values
            val newSize = size + 1
            val newLastIndex = newSize - 1
            list.add(EMPTY)
            notifyItemInserted(newLastIndex)
        }
    }

    /*
     * Private / Internal
     */

    private val size
        get() = list.size

    private val lastIndex
        get() = size - 1

    private fun setListImpl(list: MutableList<String>?, maxSize: Int = MAX_SIZE) {
        this.list =
                if (list == null || list.isEmpty()) {
                    emptyList()
                } else if (list.size > maxSize) {
                    list.subList(0, maxSize)
                } else {
                    list
                }
    }

    private fun emptyList(size: Int = MIN_SIZE): MutableList<String> {
        return MutableList(size) { EMPTY }
    }

    private fun lastIsNonEmpty(): Boolean { // TODO: remove !!
        return (recycler?.findViewHolderForAdapterPosition(lastIndex) as FormHolder?)?.extract().notNullOrEmpty()
    }
}
