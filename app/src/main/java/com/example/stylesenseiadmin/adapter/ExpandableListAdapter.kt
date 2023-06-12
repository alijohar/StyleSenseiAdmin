package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.ExpandableGroup

class ExpandableListAdapter(private val expandableList: List<ExpandableGroup>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return expandableList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return expandableList[groupPosition].children.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return expandableList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return expandableList[groupPosition].children[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: GroupViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.group_layout, parent, false)
            holder = GroupViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as GroupViewHolder
        }

        val groupNameTextView: TextView = holder.groupNameTextView
        groupNameTextView.text = expandableList[groupPosition].groupName

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ChildViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.child_layout, parent, false)
            holder = ChildViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ChildViewHolder
        }

        val childNameTextView: TextView = holder.childNameTextView
        childNameTextView.text = expandableList[groupPosition].children[childPosition]

        return view
    }

    override fun getGroupTypeCount(): Int {
        return 1
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return 0
    }

    override fun getGroupType(groupPosition: Int): Int {
        return 0
    }

    private class GroupViewHolder(view: View) {
        val groupNameTextView: TextView = view.findViewById(R.id.groupNameTextView)
    }

    private class ChildViewHolder(view: View) {
        val childNameTextView: TextView = view.findViewById(R.id.childNameTextView)
    }
}
