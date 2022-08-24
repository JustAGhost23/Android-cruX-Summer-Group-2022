package com.example.travelwriter.draftsFragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.travelwriter.database.Article
import com.example.travelwriter.database.ArticleDAO
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DraftsFragmentViewModel(
    private val database: ArticleDAO
): ViewModel() {
    private val currentDraft = MutableLiveData<Article>()

    val articleList = database.getAllArticles()

    private val _navigateToAddArticle = MutableLiveData<Boolean>()
    val navigateToAddArticle: LiveData<Boolean>
        get() = _navigateToAddArticle
    fun navigatedToAddArticle() {
        _navigateToAddArticle.value = false
    }
    init {
        _navigateToAddArticle.value = false
    }
    fun deleteArticleWithId(articleId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteArticleWithId(articleId)
            }
        }
    }
    fun openArticleWithId(articleId: Int, sharedPrefs: SharedPreferences) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentDraft.postValue(database.getArticle(articleId))
            }
        }
        _navigateToAddArticle.value = true
        saveArticleId(articleId, sharedPrefs)
    }
    private fun saveArticleId(articleId: Int, sharedPrefs: SharedPreferences) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sharedPrefs.edit()?.putInt("articleID", articleId)?.apply()
            }
        }
    }
}

class DraftsFragmentViewModelFactory(
    private val database: ArticleDAO
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DraftsFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DraftsFragmentViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}