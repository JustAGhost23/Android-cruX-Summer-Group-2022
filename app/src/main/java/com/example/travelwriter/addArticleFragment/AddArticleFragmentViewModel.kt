package com.example.travelwriter.addArticleFragment

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.travelwriter.database.Article
import com.example.travelwriter.database.ArticleApi
import com.example.travelwriter.database.ArticleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddArticleFragmentViewModel(
    private val database: ArticleDAO,
    private val articleId: Int,
    private val user: String,
    private val sharedPrefs: SharedPreferences
): ViewModel() {
    var articleString = MutableLiveData<String>()
    private var sentString = MutableLiveData<String>()
    var userListString = MutableLiveData<String>()
    private var userList = MutableLiveData<List<String>>()

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain
    fun navigatedToMain() {
        _navigateToMain.value = false
    }

    private val _navigateToDrafts = MutableLiveData<Boolean>()
    val navigateToDrafts: LiveData<Boolean>
        get() = _navigateToDrafts
    fun navigatedToDrafts() {
        _navigateToDrafts.value = false
    }

    private val _validInput = MutableLiveData<Boolean>()
    val validInput: LiveData<Boolean>
        get() = _validInput

    val currentDraft = MutableLiveData<Article>()

    init {
        _navigateToMain.value = false
        _navigateToDrafts.value = false
        _validInput.value = true
        userList.value = arrayListOf()
        getUserArticles()
        getUsers()

        if(articleId == -1) {
            createNewArticle()
        }
        else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    currentDraft.postValue(database.getArticle(articleId))
                }
            }
        }
    }
    private fun getUserArticles() {
        ArticleApi.retrofitService.getArticleCount(sharedPrefs.getString("user", null)!!).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                articleString.postValue(response.body())
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failed")
            }
        })
    }
    private fun getUsers() {
        ArticleApi.retrofitService.getArticles().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                userListString.postValue(response.body())
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failed")
            }
        })
    }
    private fun postArticleToFirebase(article: Article) {
        val completeUserList: List<String>? = userList.value
        if (completeUserList != null) {
            if(sharedPrefs.getString("user", null)!! in completeUserList){
                val string = sentString.value
                val newString = string?.substring(0, (string.length) - 1)
                val jsonMainString = newString + ", \"${article.id}\": {\"title\": \"${article.title}\", \"body\": \"${article.body}\"}}"
                postArticleWithoutUser(article.author, jsonMainString)
            }
            else {
                val jsonMainString = "{\"${article.author}\": {\"${article.id}\": {\"title\": \"${article.title}\", \"body\": \"${article.body}\"}}}"
                postArticleWithUser(jsonMainString)
            }
        }
    }
    private fun postArticleWithUser(jsonString: String) {
        viewModelScope.launch{
            ArticleApi.retrofitService.postArticleWithUser(jsonString)
        }
    }
    private fun postArticleWithoutUser(user: String, jsonString: String) {
        viewModelScope.launch{
            ArticleApi.retrofitService.postArticle(user, jsonString)
        }
    }
    fun sendString(string: String) {
        sentString.postValue(string)
    }
    fun stringToUserList(string: String) {
        val jsonObj = JSONObject(string)
        val map = jsonObj.toMap()
        val usersList: List<String> = map.keys.toList()
        userList.postValue(usersList)
    }
    private suspend fun insertArticle(article: Article) {
        withContext(Dispatchers.IO) {
            database.insertArticle(article)
        }
    }
    private fun createNewArticle() {
        viewModelScope.launch {
            val newArticle = Article()
            newArticle.author = user
            currentDraft.value = newArticle
        }
    }
    fun updateTitle(title: String) {
        currentDraft.value!!.title = title
    }
    fun updateBody(body: String) {
        currentDraft.value!!.body = body
    }
    fun loadTitle(articleId: Int): String {
        if(articleId == -1) {
            currentDraft.value!!.title = ""
        }
        else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    currentDraft.postValue(database.getArticle(articleId))
                }
            }
        }
        return currentDraft.value!!.title
    }
    fun loadBody(articleId: Int): String {
        if(articleId == -1) {
            currentDraft.value!!.body = ""
        }
        else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    currentDraft.postValue(database.getArticle(articleId))
                }
            }
        }
        return currentDraft.value!!.body
    }
    fun createPost() {
        if (currentDraft.value!!.title.isBlank()) {
            _validInput.value = false
            return
        }
        else {
            _validInput.value = true
            postArticleToFirebase(currentDraft.value!!)
            _navigateToMain.value = true
            return
        }
    }
    fun saveAsDraft() {
        if (currentDraft.value!!.title.isBlank()) {
            _validInput.value = false
            return
        }
        else {
            _validInput.value = true
            viewModelScope.launch {
                insertArticle(currentDraft.value!!)
            }
            _navigateToDrafts.value = true
            return
        }
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

class AddArticleFragmentViewModelFactory(
    private val database: ArticleDAO,
    private val articleId: Int,
    private val user: String,
    private val sharedPrefs: SharedPreferences
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddArticleFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddArticleFragmentViewModel(database, articleId, user, sharedPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}