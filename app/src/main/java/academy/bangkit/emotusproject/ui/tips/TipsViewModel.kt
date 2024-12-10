package academy.bangkit.emotusproject.ui.tips

import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.data.remote.response.MoodDailyTips
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TipsViewModel(private val repository: Repository) : ViewModel() {
    private val _userResult = MutableLiveData<MoodDailyTips>()
    val userResult: LiveData<MoodDailyTips> = _userResult

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = this._messages

    fun dailyTips(userId: String, period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getDailyTips(userId, period)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {
                            _userResult.value = response.body()
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