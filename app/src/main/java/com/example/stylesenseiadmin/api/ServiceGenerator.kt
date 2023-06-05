package com.example.stylesenseiadmin.api

import com.example.stylesenseiadmin.util.Keys
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(Keys.URL_BASE_ADDRESS)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()



object OnlineItem {
    val retrofitService : RequestApi by lazy {
        retrofit.create(RequestApi::class.java)
    }
}