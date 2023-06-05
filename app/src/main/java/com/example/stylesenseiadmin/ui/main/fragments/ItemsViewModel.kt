package com.example.stylesenseiadmin.ui.main.fragments

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.stylesenseiadmin.model.ItemResults
import com.example.stylesenseiadmin.util.ApiHelper

class ItemsViewModel : ViewModel() {
    var apiHelper = ApiHelper()
    val _results = MutableLiveData<List<ItemResults>>()
    val results: LiveData<List<ItemResults>>
        get() = _results
    init {
        getOnlineItems()
    }

    private fun getOnlineItems() {
        apiHelper.getItem(_results)
    }

    companion object{
        @JvmStatic
        @BindingAdapter("loadImage")
        fun setImage(image: ImageView, item: ItemResults){
            Glide.with(image.context)
                .load(item.images.split(",").first())
                .into(image)

        }
    }
}