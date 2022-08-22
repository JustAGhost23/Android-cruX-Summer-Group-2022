package com.example.travelwriter.addArticleFragment

import androidx.lifecycle.*
import com.example.travelwriter.database.Article
import com.example.travelwriter.database.ArticleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddArticleFragmentViewModel(
    private val database: ArticleDAO,
    private val articleId: Int,
    private val user: String
): ViewModel() {

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

        if(articleId == -1) {
            createNewArticle()
        }
        else {
            viewModelScope.launch {
                currentDraft.value = database.getArticle(articleId)
            }
        }
    }

    private suspend fun insertArticle(article: Article) {
        withContext(Dispatchers.IO){
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
            viewModelScope.launch {
                insertArticle(currentDraft.value!!)
            }
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
}

class AddArticleFragmentViewModelFactory(
    private val database: ArticleDAO,
    private val articleId: Int,
    private val user: String
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddArticleFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddArticleFragmentViewModel(database, articleId, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}