package com.emotus.app.ui.tips

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.MoodDailyTips
import com.emotus.app.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TipsViewModel(private val repository: Repository) : ViewModel() {
    private val _userResult = MutableLiveData<MoodDailyTips>()
    val userResult: LiveData<MoodDailyTips> = _userResult

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun dailyTips(token: String, period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getDailyTips(token, period)
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
}