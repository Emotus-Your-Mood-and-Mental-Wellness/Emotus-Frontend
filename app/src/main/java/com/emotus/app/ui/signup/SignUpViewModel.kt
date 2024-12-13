package com.emotus.app.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.response.Register
import com.emotus.app.data.remote.response.RegisterResponse
import com.emotus.app.models.UserModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel(private val repository: Repository) : ViewModel() {

    private val _login = MutableLiveData<RegisterResponse>()
    val login: LiveData<RegisterResponse> get() = _login
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun regis(text: Register) {
        repository.register(text, object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Error API", t.localizedMessage)
            }
        })
    }
}