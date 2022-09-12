package com.example.travelwriter.articleFragment

import androidx.lifecycle.*
import com.example.travelwriter.database.ArticleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleFragmentViewModel: ViewModel() {
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain
    fun navigatedToMain() {
        _navigateToMain.value = false
    }
    init {
        _navigateToMain.value = false
    }
    fun delete(user: String, id: String) {
        deleteArticle(user, id)
    }
    private fun deleteArticle(user: String, id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ArticleApi.retrofitService.deleteArticle(user, id)
            }
        }
        _navigateToMain.value = true
    }
}
class ArticleFragmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}