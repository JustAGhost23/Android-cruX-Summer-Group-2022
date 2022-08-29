package com.example.travelwriter.mainFragment

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.travelwriter.database.Article
import com.example.travelwriter.database.ArticleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragmentViewModel(): ViewModel() {
    val postedArticleListString = MutableLiveData<String>()

    val postedArticleList = MutableLiveData<MutableList<Article>>()

    private val _navigateToArticle = MutableLiveData<Boolean>()
    val navigateToArticle: LiveData<Boolean>
        get() = _navigateToArticle
    fun navigatedToArticle() {
        _navigateToArticle.value = false
    }
    init {
        postedArticleList.value = arrayListOf()
        getArticlesFromFirebase()
        _navigateToArticle.value = false
    }

    fun openArticleWithId(article: Article, sharedPrefs: SharedPreferences) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sharedPrefs.edit().putString("postedArticleTitle", article.title).apply()
                sharedPrefs.edit().putString("postedArticleBody", article.body).apply()
            }
        }
        _navigateToArticle.value = true
    }
    private fun getArticlesFromFirebase() {
        //viewModelScope.launch {
        //    withContext(Dispatchers.IO) {
        //       database.nukeTable()
        //    }
        //}
        ArticleApi.retrofitService.getArticles().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                postedArticleListString.postValue(response.body())
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failed")
            }
        })
    }
    fun stringToMutableList(string: String) {
        val jsonObj = JSONObject(string)
        val map = jsonObj.toMap()
        val articleList: MutableList<Article> = arrayListOf()
        val userList: List<String> = map.keys.toList()
        for (i in userList.indices) {
            val article = Article()
            article.author = userList[i]
            val articleMap = jsonObj.getJSONObject(article.author).toMap()
            val articlesList: List<String> = articleMap.keys.toList()
            for (j in articlesList.indices) {
                val newArticle: Article = article.copy()
                newArticle.id = articlesList[j].toInt()
                val eachArticleMap = jsonObj.getJSONObject(article.author)
                    .getJSONObject(newArticle.id.toString()).toMap()
                newArticle.title = eachArticleMap["title"].toString()
                newArticle.body = eachArticleMap["body"].toString()
                articleList.add(newArticle)
            }
        }
        postedArticleList.value = articleList
    }
    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {

        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}
class MainFragmentViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}