package com.example.stylesenseiadmin.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.stylesenseiadmin.api.OnlineItem
import com.example.stylesenseiadmin.model.ClothingResponse
import com.example.stylesenseiadmin.model.ItemResults
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class ApiHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getItem(
        attrsString: String,
        array: MutableLiveData<List<ItemResults>>,
        fail: MutableLiveData<String>
    ) {

        Log.i("AJC", "attributes: {$attrsString},")
        val mediaType = "application/json".toMediaType()
        val requestBody = """
        {
            "attributes": {$attrsString},
            "limit": 100,
            "offset": 0
        }
    """.trimIndent().toRequestBody(mediaType)

        val deferredList = OnlineItem.retrofitService.getItem(requestBody)
        uiScope.launch {
            try {
                val listResult = deferredList.await()
                val result = listResult.result
                if (result.isNotEmpty()) {
                    array.value = result
                } else {
                    // Handle the situation where 'result' or 'resource' is null
                }
            } catch (e: Exception) {
                fail.value = e.message.toString()
            }
        }
    }


    fun addAttr(
        addAttrResult: MutableLiveData<String>,
        key: String,
        value: String,
        ids: List<Int>
    ) {
        val jsonArray = JSONArray().apply {
            for (id in ids) {
                put(id)
            }
        }
        val jsonObject = JSONObject().apply {
            val attributes = JSONObject().apply {
                put(key, value)
            }
            put("attributes", attributes)
            put("product_ids", jsonArray)
        }
        val jsonString = jsonObject.toString()
        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaType())

        val deferredList = OnlineItem.retrofitService.addAttr(requestBody)
        uiScope.launch {
            try {
                val result = deferredList.await()
                if (result.isNotEmpty()) {
                    addAttrResult.value = result
                }
            } catch (e: Exception) {
                addAttrResult.value = e.message.toString()
            }
        }
    }

    fun getAttr(_attrs: MutableLiveData<Map<String, List<String>>>) {
        val deferredList = OnlineItem.retrofitService.getAttrs()
        uiScope.launch {
            try {
                val result = deferredList.await()
                if (result.isNotEmpty()) {
                    _attrs.value = result
                }
            } catch (e: Exception) {
                Log.i("AJC", e.message.toString())
            }
        }
    }

    fun getAttrLocally(_attrs: MutableLiveData<ClothingResponse>, context: Context) {
        uiScope.launch {
            val jsonString = context.assets.open("clothing_data.json").bufferedReader().use {
                it.readText()
            }

            _attrs.value = Gson().fromJson(jsonString, ClothingResponse::class.java)
        }
    }
}