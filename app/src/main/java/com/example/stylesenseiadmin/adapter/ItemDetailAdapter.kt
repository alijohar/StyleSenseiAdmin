package com.example.stylesenseiadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.stylesenseiadmin.model.ItemResults

class ItemDetailAdapter(private val context: Context, private val itemResult: ArrayList<String>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return itemResult.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val customView =
            if (convertView == null) ItemDetailCustomView(context) else (convertView as ItemDetailCustomView?)!!
        customView.display(itemResult[position])
        return customView
    }
}