package academy.bangkit.emotusproject.ui.home

import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.data.remote.response.MoodDistribution
import academy.bangkit.emotusproject.data.remote.response.MoodEntries
import academy.bangkit.emotusproject.data.remote.response.MoodEntry
import academy.bangkit.emotusproject.data.remote.response.MoodResponse
import academy.bangkit.emotusproject.data.remote.response.MoodSummary
import academy.bangkit.emotusproject.data.remote.response.MoodTrends
import academy.bangkit.emotusproject.data.remote.response.UserAccount
import academy.bangkit.emotusproject.data.remote.response.UserAccountBody
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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

    private val _messageResult = MutableLiveData<String>()
    val messageResult: LiveData<String> = this._messageResult
    private val rs = MutableLiveData<MoodTrends?>()
    val ss: LiveData<MoodTrends?> get() = rs
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadings = MutableLiveData<Boolean>()
    val isLoadings: LiveData<Boolean> = _isLoadings

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = this._message
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = this._messages
    private val _messagess = MutableLiveData<String>()
    val messagess: LiveData<String> = this._messagess

    fun predictMood(userId: String, body: MoodResponse) {
        viewModelScope.launch {
            try {
                val response = repository.predictMoodResult(userId, body)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

                            _result.value = response.body()

                    }
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    withContext(Dispatchers.Main) {
                        _messageResult.value = jsonObject.toString()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _messageResult.value = e.localizedMessage
                }
            }
        }
    }

    fun moodTrends(userId: String, startDate: String, endDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.moodTrendsResult(userId, startDate, endDate)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

                            _trendResult.value = response.body()!!.moodDistribution
                            rs.postValue(response.body())

                    }
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    withContext(Dispatchers.Main) {
                        _message.postValue(jsonObject.toString())
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }

            }
        }
    }

    fun moodSummary(userId: String, startDate: String, endDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.userMoodSummary(userId, startDate)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

                            _summaryResult.value = response.body()

                    }
                } else {
                    if (response.errorBody() != null) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        withContext(Dispatchers.Main) {
                            _message.postValue(jsonObject.toString())
                        }
                    } else {
                        _message.postValue("Ada yang salah! Segera cek!!!")
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _message.value = e.localizedMessage ?: "Ada yang salah! Segera cek!!!"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoadings.value = false
                }
            }
        }
    }

    fun moodEntries(userId: String, period: String, endDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.allMoodEntries(userId, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

                            _entriesResult.value = response.body()

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

    fun getUser(userId: String, body: UserAccountBody? = null) {
        viewModelScope.launch {
            try {
                val response = repository.getUserAccount(userId)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {

                            _userResult.value = response.body()

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