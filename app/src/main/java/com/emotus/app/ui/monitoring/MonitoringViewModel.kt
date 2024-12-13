package com.emotus.app.ui.monitoring

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.DataItem
import com.emotus.app.data.remote.response.MoodDistribution
import com.emotus.app.data.remote.response.MoodSummary
import com.emotus.app.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MonitoringViewModel(private val repository: Repository) : ViewModel() {
    private val _entriesResult = MutableLiveData<List<DataItem>>()
    val entriesResult: LiveData<List<DataItem>> = _entriesResult
    private val _messages = MutableLiveData<String>()
    val message: LiveData<String> = this._messages

    private val _summaryResult = MutableLiveData<MoodSummary>()
    val summaryResult: LiveData<MoodSummary> = _summaryResult

    private val _trendResult = MutableLiveData<MoodDistribution>()
    val trendResult: LiveData<MoodDistribution> = _trendResult

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun moodEntries(userId: String, period: String) {
        viewModelScope.launch {
            try {
                val response = repository.allMoodEntries(userId, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _entriesResult.value = response.body()!!.data
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun deleteMood(entryId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMoodEntry(entryId = entryId, token = token)
                moodEntries(token, "daily")
                moodTrends(token, "daily")
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun moodTrends(userId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.moodTrendsResult(userId, token)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        _trendResult.value = response.body()!!.moodDistribution
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

    fun moodSummary(userId: String, startDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.userMoodSummary(userId, startDate)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                            _summaryResult.value = response.body()
                    }
                }
            } catch (e: Exception) {
                Log.e("Error API", e.localizedMessage)
            }
        }
    }

}
