package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.Clothing

class ClothingAdapter(private val clothingList: List<Clothing>) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return clothingList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return clothingList[groupPosition].attrs.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return clothingList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return clothingList[groupPosition].attrs[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        // Inflate the group item layout
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.group_item_layout, parent, false)

        // Set the type value for the group item
        val typeTextView = view.findViewById<TextView>(R.id.typeTextView)
        typeTextView.text = clothingList[groupPosition].type

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        // Inflate the child item layout
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.child_item_layout, parent, false)

        // Set the attrs values for the child item
        val attrsTextView = view.findViewById<TextView>(R.id.attrsTextView)
        val attrsMap = clothingList[groupPosition].attrs[childPosition]
        val attrsString = attrsMap.values.flatten().joinToString(", ")
        attrsTextView.text = attrsString

        return view
    }
}
