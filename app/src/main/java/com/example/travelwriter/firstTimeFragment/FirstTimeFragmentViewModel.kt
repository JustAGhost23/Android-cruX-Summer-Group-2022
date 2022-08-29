package com.example.travelwriter.firstTimeFragment

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.travelwriter.database.ArticleApi
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstTimeFragmentViewModel(
    private val sharedPrefs: SharedPreferences
): ViewModel() {
    private var _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain
    fun navigatedToMain() {
        _navigateToMain.value = false
    }
    init {
        _navigateToMain.value = false
    }
    fun buttonClicked(data: String) {
        viewModelScope.launch {
            sharedPrefs.edit()?.putString("user", data)?.apply()
        }
        _navigateToMain.value=true
    }
    fun emptyName() {
        viewModelScope.launch {
            sharedPrefs.edit()?.putString("user", null)?.apply()
        }
    }
}
class FirstTimeFragmentViewModelFactory(
    private val sharedPrefs: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstTimeFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FirstTimeFragmentViewModel(sharedPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}