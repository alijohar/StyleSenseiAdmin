package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.ExpandableGroup

class ExpandableListAdapter(private val expandableList: List<ExpandableGroup>) :
    BaseExpandableListAdapter() {
    private var filteredExpandableList: List<ExpandableGroup> = expandableList
    private val selectedChildPositions: MutableList<Pair<Int, Int>> = mutableListOf()
    private var onChildClickListener: OnChildClickListener? = null

    interface OnChildClickListener {
        fun onChildClick(groupPosition: Int, childPosition: Int)
    }

    fun setOnChildClickListener(listener: OnChildClickListener) {
        onChildClickListener = listener
    }

    override fun getGroupCount(): Int {
        return filteredExpandableList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return filteredExpandableList[groupPosition].children.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return filteredExpandableList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return filteredExpandableList[groupPosition].children[childPosition]
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

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View
        val holder: GroupViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.group_layout, parent, false)
            holder = GroupViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as GroupViewHolder
        }

        val groupNameTextView: TextView = holder.groupNameTextView
        groupNameTextView.text = filteredExpandableList[groupPosition].groupName

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View
        val holder: ChildViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.child_layout, parent, false)
            holder = ChildViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ChildViewHolder
        }

        val childNameTextView: TextView = holder.childNameTextView
        childNameTextView.text = filteredExpandableList[groupPosition].children[childPosition]

        val childIconImageView: ImageView = holder.childIconImageView

        val isSelected = selectedChildPositions.contains(Pair(groupPosition, childPosition))
        if (isSelected) {
            childIconImageView.setImageResource(R.drawable.round_check_24)
        } else {
            childIconImageView.setImageResource(R.drawable.round_add_24)
        }

        view.setOnClickListener {
            if (isSelected) {
                selectedChildPositions.remove(Pair(groupPosition, childPosition))
                // Handle deselected child item
            } else {
                selectedChildPositions.add(Pair(groupPosition, childPosition))
                // Handle selected child item
            }
            notifyDataSetChanged()
            onChildClickListener?.onChildClick(groupPosition, childPosition)
        }

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

    fun filter(query: String) {
        val filteredList = expandableList.map { group ->
            val filteredChildren = group.children.filter { child ->
                child.contains(query, ignoreCase = true)
            }
            ExpandableGroup(group.groupName, filteredChildren)
        }.filter { group ->
            group.children.isNotEmpty()
        }
        filteredExpandableList = filteredList
        notifyDataSetChanged()
    }

    fun attachChildClickListener(expandableListView: ExpandableListView) {
        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            onChildClickListener?.onChildClick(groupPosition, childPosition)
            true
        }
    }

    private class GroupViewHolder(view: View) {
        val groupNameTextView: TextView = view.findViewById(R.id.groupNameTextView)
    }

    private class ChildViewHolder(view: View) {
        val childNameTextView: TextView = view.findViewById(R.id.childNameTextView)
        val childIconImageView: ImageView = view.findViewById(R.id.childIconImageView)
    }
}
