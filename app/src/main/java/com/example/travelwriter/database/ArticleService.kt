package com.example.travelwriter.database

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val BASE_URL = "https://travelwriter-1e909-default-rtdb.firebaseio.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object ArticleApi {
    val retrofitService : ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}