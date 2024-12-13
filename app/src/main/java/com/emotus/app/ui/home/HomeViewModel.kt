package com.emotus.app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.MoodDistribution
import com.emotus.app.data.remote.response.MoodEntries
import com.emotus.app.data.remote.response.MoodEntry
import com.emotus.app.data.remote.response.MoodResponse
import com.emotus.app.data.remote.response.MoodSummary
import com.emotus.app.data.remote.response.UserAccount
import com.emotus.app.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _result = MutableLiveData<MoodEntry>()
    val result: LiveData<MoodEntry> = _result

    private val _trendResult = MutableLiveData<MoodDistribution>()
    val trendResult: LiveData<MoodDistribution> = _trendResult

    private val _summaryResult = MutableLiveData<MoodSummary>()
    val summaryResult: LiveData<MoodSummary> = _summaryResult

    private val _entriesResult = MutableLiveData<MoodEntries>()
    val entriesResult: LiveData<MoodEntries> = _entriesResult

    private val _userResult = MutableLiveData<UserAccount>()
    val userResult: LiveData<UserAccount> = _userResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadings = MutableLiveData<Boolean>()
    val isLoadings: LiveData<Boolean> = _isLoadings

    fun predictMood(token: String, body: MoodResponse) {
        viewModelScope.launch {
            try {
                val response = repository.predictMoodResult(token, body)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _result.value = response.body()
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun getUser(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getUserAccount(token)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {
                            _userResult.value = response.body()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun moodTrends(token: String, period: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.moodTrendsResult(token, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _trendResult.value = response.body()!!.moodDistribution
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            } finally {
                    _isLoading.value = false
            }
        }
    }

    fun moodSummary(token: String, period: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.userMoodSummary(token, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _summaryResult.value = response.body()
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoadings.value = false
                }
            }
        }
    }

    fun moodEntries(token: String, period: String) {
        viewModelScope.launch {
            try {
                val response = repository.allMoodEntries(token, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _entriesResult.value = response.body()
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

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}