package com.example.travelwriter.articleFragment

import androidx.lifecycle.*

class ArticleFragmentViewModel(): ViewModel() {}
class ArticleFragmentViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}