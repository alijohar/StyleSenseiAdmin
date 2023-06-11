package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stylesenseiadmin.R

class AttrItemAdapter(private val dataMap: Map<String, List<String>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val headerKeys: List<String> = dataMap.keys.toList()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            VIEW_TYPE_HEADER -> inflater.inflate(R.layout.header_attr, parent, false)
            else -> inflater.inflate(R.layout.item_attr, parent, false)
        }
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            else -> ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_HEADER) {
            val headerHolder = holder as HeaderViewHolder
            val headerKey = headerKeys[position]
            headerHolder.bindHeader(headerKey)
        } else {
            val itemHolder = holder as ItemViewHolder
            val headerIndex = getHeaderIndex(position)
            val headerKey = headerKeys[headerIndex]
            val items = dataMap[headerKey]
            val itemIndex = getItemIndex(headerIndex, position)
            if (items != null && itemIndex >= 0 && itemIndex < items.size) {
                val item = items[itemIndex]
                itemHolder.bindItem(item)
            }
        }
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        for (headerKey in headerKeys) {
            itemCount += dataMap[headerKey]?.size ?: 0
            itemCount++ // Add one for the header
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderPosition(position)) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    private fun isHeaderPosition(position: Int): Boolean {
        var count = 0
        for (headerKey in headerKeys) {
            if (position == count) {
                return true
            }
            count += dataMap[headerKey]?.size ?: 0
            count++ // Increment for the header
            if (position < count) {
                return false
            }
        }
        return false
    }

    private fun getHeaderIndex(position: Int): Int {
        var count = 0
        for (index in headerKeys.indices) {
            val itemsCount = dataMap[headerKeys[index]]?.size ?: 0
            count += itemsCount
            count++ // Increment for the header
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
            count++ // Increment for the header
        }
        return position - count - 1
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

        fun bindHeader(headerKey: String) {
            headerTextView.text = headerKey
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bindItem(item: String) {
            textView.text = item
        }
    }
}
