package com.example.travelwriter.database

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ArticleApiService {

    @GET(".json?")
    fun getArticles(): Call<String>

    @GET("{user}.json?")
    fun getArticleCount(@Path("user") user: String): Call<String>

    @PATCH(".json?")
    suspend fun postArticleWithUser(@Body body: String): Response<String>

    @PATCH("{user}.json?")
    suspend fun postArticle(@Path("user") user: String, @Body body: String): Response<String>

    @DELETE("{user}/{id}.json?")
    suspend fun deleteArticle(@Path("user") user: String, @Path("id") id: String): Response<String>
}

