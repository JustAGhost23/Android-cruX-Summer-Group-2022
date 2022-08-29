package com.example.travelwriter.detailsFragment

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.travelwriter.database.ArticleApi
import com.example.travelwriter.database.ArticleDAO
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragmentViewModel(
    private val database: ArticleDAO,
    private val sharedPrefs: SharedPreferences
): ViewModel() {
    var postedArticleCount = MutableLiveData<Int>()

    val articleCount = database.getArticleCount()
    init {
        getArticlesFromFirebase()
    }
    private fun getArticlesFromFirebase() {
        ArticleApi.retrofitService.getArticleCount(sharedPrefs.getString("user", null)!!).enqueue( object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val jsonObj = JSONObject(response.body()!!)
                val map = jsonObj.toMap()
                postedArticleCount.value = map.size
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failed")
            }
        })
    }
    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
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
class DetailsFragmentViewModelFactory(
    private val database: ArticleDAO,
    private val sharedPrefs: SharedPreferences
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsFragmentViewModel(database, sharedPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}