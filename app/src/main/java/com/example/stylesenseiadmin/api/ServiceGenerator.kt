package com.example.stylesenseiadmin.api

import com.example.stylesenseiadmin.util.Keys
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS) // Increase the connection timeout to 60 seconds
    .readTimeout(60, TimeUnit.SECONDS) // Increase the read timeout to 60 seconds
    .writeTimeout(60, TimeUnit.SECONDS) // Increase the write timeout to 60 seconds
    .addInterceptor(loggingInterceptor) // Add the logging interceptor
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(Keys.URL_BASE_ADDRESS)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient) // Set the custom OkHttpClient
    .build()



object OnlineItem {
    val retrofitService : RequestApi by lazy {
        retrofit.create(RequestApi::class.java)
    }
}