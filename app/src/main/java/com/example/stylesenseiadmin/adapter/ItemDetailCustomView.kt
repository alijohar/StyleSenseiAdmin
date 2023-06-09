package com.example.stylesenseiadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.ItemResults
import com.google.android.material.card.MaterialCardView

class ItemDetailCustomView(context: Context?) : FrameLayout(context!!) {
    var imageView: ImageView

    fun display(src: String) {
        Glide.with(context).load(src).into(imageView)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_detail, this)
        imageView = rootView.findViewById<View>(R.id.grid_image) as ImageView
    }
}