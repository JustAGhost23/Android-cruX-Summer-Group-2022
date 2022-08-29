package com.example.travelwriter.database

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val BASE_URL = "https://travelwriter-1e909-default-rtdb.firebaseio.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object ArticleApi {
    val retrofitService : ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}