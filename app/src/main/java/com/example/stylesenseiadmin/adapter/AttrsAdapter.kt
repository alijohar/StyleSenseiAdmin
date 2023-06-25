package com.example.stylesenseiadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stylesenseiadmin.R

class AttrsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val attrsTextView: TextView = itemView.findViewById(R.id.attrsTextView)
}

class AttrsAdapter(private val attrsList: List<String>) :
    RecyclerView.Adapter<AttrsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttrsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.attrs_item_layout, parent, false)
        return AttrsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AttrsViewHolder, position: Int) {
        val attr = attrsList[position]
        holder.attrsTextView.text = attr
    }

    override fun getItemCount(): Int {
        return attrsList.size
    }
}