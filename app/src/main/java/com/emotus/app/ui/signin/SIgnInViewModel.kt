package com.emotus.app.ui.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.LoginResponse
import com.emotus.app.data.remote.response.Token
import com.emotus.app.models.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SIgnInViewModel(private val repository: Repository) : ViewModel() {

    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> get() = _login

    fun loginPost(idToken: Token) {
        viewModelScope.launch {
            repository.login(idToken, object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponses = response.body()
                        if (loginResponses != null) {
                            if (_login.value != loginResponses) {
                                _login.value = loginResponses
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Error API", t.localizedMessage)
                }
            })
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}