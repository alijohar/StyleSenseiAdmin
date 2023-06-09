package com.example.stylesenseiadmin.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.stylesenseiadmin.api.OnlineItem
import com.example.stylesenseiadmin.model.ItemResults
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

    fun getItem(array: MutableLiveData<List<ItemResults>>, fail: MutableLiveData<String>) {
        val mediaType = "text/plain".toMediaType()
        val requestBody = "{\n    \"type\": 0,\n    \"limit\": 100\n}".toRequestBody(mediaType)
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


    fun addAttr(addAttrResult: MutableLiveData<String>, key: String, value: String, ids: List<Int>) {
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
        Log.i("AJC", jsonString)
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
}