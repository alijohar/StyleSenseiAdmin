package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stylesenseiadmin.R

class AttrItemAdapter(private val dataMap: Map<String, List<String>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val headerKeys: List<String> = dataMap.keys.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_attr, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val headerIndex = getHeaderIndex(position)
        val headerKey = headerKeys[headerIndex]
        val items = dataMap[headerKey]
        val itemIndex = getItemIndex(headerIndex, position)
        val item = items?.get(itemIndex)
        val itemHolder = holder as ItemViewHolder
        itemHolder.bindItem(item)
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        for (headerKey in headerKeys) {
            itemCount += dataMap[headerKey]?.size ?: 0
        }
        return itemCount
    }

    private fun getHeaderIndex(position: Int): Int {
        var count = 0
        for (index in headerKeys.indices) {
            val itemsCount = dataMap[headerKeys[index]]?.size ?: 0
            count += itemsCount
            if (position < count) {
                return index
            }
        }
        return -1
    }

    private fun getItemIndex(headerIndex: Int, position: Int): Int {
        var count = 0
        for (index in 0 until headerIndex) {
            count += dataMap[headerKeys[index]]?.size ?: 0
        }
        return position - count
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bindItem(item: String?) {
            textView.text = item
        }
    }
}