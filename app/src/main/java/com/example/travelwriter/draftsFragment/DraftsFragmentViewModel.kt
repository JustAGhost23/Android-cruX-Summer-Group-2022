package com.example.travelwriter.draftsFragment

import androidx.lifecycle.*
import com.example.travelwriter.database.ArticleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DraftsFragmentViewModel(
    private val database: ArticleDAO
): ViewModel() {
    fun deleteArticleWithId(articleId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteArticleWithId(articleId)
            }
        }
    }
    val articleList = database.getAllArticles()
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