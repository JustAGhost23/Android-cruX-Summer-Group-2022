package com.example.travelwriter.database

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArticleApiService {

    @GET(".json?")
    fun getArticles(): Call<String>

    @GET("{user}.json?")
    fun getArticleCount(@Path("user") user: String): Call<String>

    @POST("{user}.json?")
    fun addNewUser(@Path("user") user: String, @Body body: RequestBody)
}

