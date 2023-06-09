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

class ApiHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getItem(array: MutableLiveData<List<ItemResults>>, fail: MutableLiveData<String>){
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
    }}