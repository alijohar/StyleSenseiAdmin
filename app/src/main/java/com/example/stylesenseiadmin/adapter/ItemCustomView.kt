package com.example.stylesenseiadmin.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.model.ItemResults
import com.google.android.material.card.MaterialCardView

internal class ItemCustomView(context: Context?) : FrameLayout(context!!) {
    var textView: TextView
    var badgeCount: TextView
    var imageView: ImageView
    var selectedImage: ImageView
    var cardView: MaterialCardView
    var badge: MaterialCardView

    fun display(src: ItemResults, isSelected: Boolean) {
        textView.text = src.name
        Glide.with(context).load(src.images.split(",").first()).into(imageView)
        if (src.added_attr_count>0){
            badge.visibility = View.VISIBLE
            badgeCount.text = src.added_attr_count.toString()
        }else{
            badge.visibility = View.GONE
        }
        display(isSelected)
    }

    fun display(isSelected: Boolean) {
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                cardView.strokeColor = Color.WHITE
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                cardView.strokeColor = Color.BLACK

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                cardView.strokeColor = Color.BLACK
            }
        }
        if (isSelected){
            cardView.alpha = 1.0F
            selectedImage.visibility = View.VISIBLE
        }else{
            cardView.alpha = 0.6F
            selectedImage.visibility = View.GONE
        }


    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item, this)
        textView = rootView.findViewById<View>(R.id.item_name) as TextView
        imageView = rootView.findViewById<View>(R.id.grid_image) as ImageView
        selectedImage = rootView.findViewById<View>(R.id.selected_img) as ImageView
        cardView = rootView.findViewById<View>(R.id.cardView) as MaterialCardView
        badge = rootView.findViewById<View>(R.id.badge) as MaterialCardView
        badgeCount = rootView.findViewById<View>(R.id.badge_count) as TextView
    }
}