package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.Clothing

import android.content.Context

class ClothingAdapter(private val context: Context, private val attrs: List<Map<String, List<String>>>) :
    BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return attrs.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return attrs[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return attrs[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val attributeMap = attrs[groupPosition]
        val attributeKey = attributeMap.keys.toTypedArray()[childPosition]
        return attributeMap[attributeKey]!!
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

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.group_item_layout, null)
        }

        val groupTextView: TextView = convertView!!.findViewById(R.id.typeTextView)
        val attributeMap = attrs[groupPosition]
        val attributeKey = attributeMap.keys.toTypedArray()[0]
        groupTextView.text = attributeKey

        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.child_item_layout, null)
        }

        val childTextView: TextView = convertView!!.findViewById(R.id.attrsTextView)
        val attributeMap = attrs[groupPosition]
        val attributeKey = attributeMap.keys.toTypedArray()[childPosition]
        val attributeValues = attributeMap[attributeKey]!!

        val valuesStringBuilder = StringBuilder()
        for (value in attributeValues) {
            valuesStringBuilder.append(value).append(", ")
        }
        valuesStringBuilder.delete(valuesStringBuilder.length - 2, valuesStringBuilder.length)

        childTextView.text = valuesStringBuilder.toString()

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}


