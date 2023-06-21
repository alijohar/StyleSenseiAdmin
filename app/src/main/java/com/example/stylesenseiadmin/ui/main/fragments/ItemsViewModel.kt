package com.example.stylesenseiadmin.ui.main.fragments

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.stylesenseiadmin.model.ClothingResponse
import com.example.stylesenseiadmin.model.ItemResults
import com.example.stylesenseiadmin.util.ApiHelper

class ItemsViewModel : ViewModel() {
    var apiHelper:ApiHelper? = ApiHelper()
    val _results = MutableLiveData<List<ItemResults>>()
    val results: LiveData<List<ItemResults>>
        get() = _results

    val _localResults = MutableLiveData<ClothingResponse>()
    val localResults: LiveData<ClothingResponse>
        get() = _localResults

    val _fail = MutableLiveData<String>()
    val fail: LiveData<String>
        get() = _fail

    val _addAttrResult = MutableLiveData<String>()
    val addAttrResult: LiveData<String>
        get() = _addAttrResult

    val _attrs = MutableLiveData<Map<String, List<String>>>()
    val attrs: LiveData<Map<String, List<String>>>
        get() = _attrs

    init {
        getOnlineItems()
        getAllAttrs()
    }

    private fun getAllAttrs() {
        apiHelper?.getAttr(_attrs)
    }

    fun getAttrsLocally(context: Context){
        apiHelper?.getAttrLocally(_localResults, context)
    }

    fun getOnlineItems(attrsString:String = "") {
        apiHelper?.getItem(attrsString, _results, _fail)
    }

    fun addAttr(ids:ArrayList<Int>, key:String, value:String){
        apiHelper?.addAttr(_addAttrResult, key, value, ids)
    }

    override fun onCleared() {
        super.onCleared()
        apiHelper = null
    }

    companion object{
        @JvmStatic
        @BindingAdapter("loadImage")
        fun setImage(image: ImageView, item: ItemResults){
            Glide.with(image.context)
                .load(item.pictures.split(",").first())
                .into(image)

        }
    }
}