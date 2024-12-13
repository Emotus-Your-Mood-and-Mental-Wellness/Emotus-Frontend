package com.emotus.app.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.Update
import com.emotus.app.data.remote.response.UserAccount
import com.emotus.app.models.UserModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    private val _userResult = MutableLiveData<UserAccount>()
    val userResult: LiveData<UserAccount> = _userResult
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = this._messages

    fun getUser(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserAccount(token)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                            _userResult.value = response.body()
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun putAccount(token: String, body: Update ) {
        viewModelScope.launch {
            try {
                val response = repository.putAccounts(token, body)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                            getUser(token)
                    }
                }

            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun photo(token: String, file: MultipartBody.Part? = null) {
        viewModelScope.launch {
            try {
                val response = repository.postPhoto(token, file)
                if (response.isSuccessful) {
                    getUser(token)
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}