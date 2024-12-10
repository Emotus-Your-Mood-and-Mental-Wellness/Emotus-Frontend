package academy.bangkit.emotusproject.ui.monitoring

import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.data.remote.response.DataItem
import academy.bangkit.emotusproject.data.remote.response.MoodDistribution
import academy.bangkit.emotusproject.data.remote.response.MoodSummary
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
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

    fun updateEntries(newEntries: List<DataItem>) {
        _entriesResult.value = newEntries // Mengganti seluruh list
    }

    private val _entriesResults = MutableLiveData<Int>()
    val entriesResults: LiveData<Int> = _entriesResults
    fun moodEntries(userId: String, period: String, endDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.allMoodEntries(userId, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

//                            _entriesResult.value = listOf()
                            _entriesResults.value = response.body()!!.data.size
                            _entriesResult.value = response.body()!!.data

                    }
                } else {
                    if (response.errorBody() != null) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        withContext(Dispatchers.Main) {
                            _messages.postValue(jsonObject.toString())
                        }

                    } else {
                        _messages.postValue("Ada yang salah! Segera cek!!!")
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _messages.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            }
        }
    }

    private val _moods = MutableLiveData<List<DataItem>?>()
    val moods: MutableLiveData<List<DataItem>?> = _moods


    fun deleteMood(entryId: String, userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMoodEntry(entryId, userId)
                moodEntries("user10", "daily", "")
                moodTrends("user10", "daily", "")
//                moodEntries("user10", "daily", "")

//                removeMoodFromLocal(entryId)
//                if (response.isSuccessful) {
//                    val loginResponses = response.body()
//                    if (loginResponses != null) {
//                        withContext(Dispatchers.Main) {
//                            _entriesResult.value = response.body()
//                        }
//                    }
//                } else {
//                    if (response.errorBody() != null) {
//                        val jsonObject = JSONObject(response.errorBody()!!.string())
//                        withContext(Dispatchers.Main) {
//                            _messages.postValue(jsonObject.toString())
//                        }
//
//                    } else {
//                        _messages.postValue("Ada yang salah! Segera cek!!!")
//                    }
//
//                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _messages.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            }
        }
    }

    private fun removeMoodFromLocal(entryId: String) {
        val updatedList = _moods.value?.filter { it.id != entryId }
        _moods.value = updatedList // Memperbarui data lokal
    }

    fun moodTrends(userId: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.moodTrendsResult(userId, startDate, endDate)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {
                            _trendResult.value = response.body()!!.moodDistribution
                        }
                    }
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    withContext(Dispatchers.Main) {
                        _messages.postValue(jsonObject.toString())
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _messages.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            }
        }
    }

    fun moodSummary(userId: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.userMoodSummary(userId, startDate)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {
                            _summaryResult.value = response.body()
                        }
                    }
                } else {
                    if (response.errorBody() != null) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        withContext(Dispatchers.Main) {
                            _messages.postValue(jsonObject.toString())
                        }
                    } else {
                        _messages.postValue("Ada yang salah! Segera cek!!!")
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _messages.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            }
        }
    }

}
