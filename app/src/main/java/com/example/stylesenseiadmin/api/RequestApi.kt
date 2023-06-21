package com.example.stylesenseiadmin.api

import com.example.stylesenseiadmin.model.ClothingResponse
import com.example.stylesenseiadmin.model.ItemModel
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RequestApi {
    @POST("/api/v1/product/")
    fun getItem(@Body body: RequestBody):Deferred<ItemModel>

    @POST("/api/v1/product/add-attr")
    fun addAttr(@Body body: RequestBody):Deferred<String>


    @GET("/api/v1/product/get-unique-attrs")
    fun getAttrs(): Deferred<Map<String, List<String>>>

    @GET("")
    fun getClothingData(): Deferred<ClothingResponse>
}