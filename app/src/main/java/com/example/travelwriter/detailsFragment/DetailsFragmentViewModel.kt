package com.example.travelwriter.detailsFragment

import androidx.lifecycle.*

class DetailsFragmentViewModel: ViewModel() {

}
class DetailsFragmentViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}