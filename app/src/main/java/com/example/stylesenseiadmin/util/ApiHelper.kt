package com.example.stylesenseiadmin.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.stylesenseiadmin.api.OnlineItem
import com.example.stylesenseiadmin.model.ItemResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ApiHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getItem(array: MutableLiveData<List<ItemResults>>){
        val deferredList = OnlineItem.retrofitService.getItem(Keys.TEMP_BODY_JSON)
        uiScope.launch {
            try {
                val listResult = deferredList.await()
                array.value = listResult.itemResults
            }catch (e:java.lang.Exception){
                Log.i("AJC", e.message.toString())
            }
        }
    }
}