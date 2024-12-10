package academy.bangkit.emotusproject.ui.settings

import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.data.remote.response.UserAccount
import academy.bangkit.emotusproject.data.remote.response.UserAccountBody
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.json.JSONObject

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    private val _userResult = MutableLiveData<UserAccount>()
    val userResult: LiveData<UserAccount> = _userResult
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = this._messages
    private val _bool = MutableLiveData<Boolean>()
    val bool: LiveData<Boolean> = this._bool
    fun getUser(userId: String, body: UserAccountBody? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getUserAccount(userId)
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

    fun updateBool(value: Boolean) {
        _bool.value = value
    }

    fun putAccountt(userId: String, body: UserAccountBody) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.putAccounts(userId, body)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {

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

    fun photo(userId: String, file: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.postPhoto(userId, file)
                if (response.isSuccessful) {
                    val loginResponses = response.body()
                    if (loginResponses != null) {
                        withContext(Dispatchers.Main) {

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