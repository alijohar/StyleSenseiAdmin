package com.example.stylesenseiadmin.api

import com.example.stylesenseiadmin.model.ItemModel
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestApi {
    @POST("/api/v1/product")
    fun getItem(@Body body:String):Deferred<ItemModel>
}