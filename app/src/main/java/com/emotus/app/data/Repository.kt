package com.emotus.app.data

import com.emotus.app.data.remote.response.LoginResponse
import com.emotus.app.data.remote.response.MoodDailyTips
import com.emotus.app.data.remote.response.MoodEntries
import com.emotus.app.data.remote.response.MoodEntry
import com.emotus.app.data.remote.response.MoodResponse
import com.emotus.app.data.remote.response.MoodSummary
import com.emotus.app.data.remote.response.MoodTrends
import com.emotus.app.data.remote.response.Register
import com.emotus.app.data.remote.response.RegisterResponse
import com.emotus.app.data.remote.response.Token
import com.emotus.app.data.remote.response.Update
import com.emotus.app.data.remote.response.UserAccount
import com.emotus.app.data.remote.retrofit.ApiService
import com.emotus.app.models.UserModel
import com.emotus.app.models.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun predictMoodResult(userId: String, diaryEntry: MoodResponse): Response<MoodEntry> {
        // Delegate the API call to the ApiService
        return apiService.postPredictMood(userId, diaryEntry)
    }

    suspend fun moodTrendsResult(
        token: String,
        startDate: String
    ): Response<MoodTrends> {
        // Delegate the API call to the ApiService
        return apiService.getMoodTrends(token, startDate)
    }

    suspend fun userMoodSummary(
        userId: String,
        startDate: String
    ): Response<MoodSummary> {
        // Delegate the API call to the ApiService
        return apiService.getMoodSummary(userId, startDate)
    }

    suspend fun allMoodEntries(
        userId: String,
        period: String
    ): Response<MoodEntries> {
        // Delegate the API call to the ApiService
        return apiService.getMoodEntries(userId, period)
    }

    suspend fun getUserAccount(
        token: String
    ): Response<UserAccount> {
        // Delegate the API call to the ApiService
        return apiService.getDataUserAccount(token)
    }

    suspend fun deleteMoodEntry(
        entryId: String,
        token: String,
    ): Response<Unit> {
        // Delegate the API call to the ApiService
        return apiService.deleteMood(token = token, entryId = entryId)
    }

    suspend fun getDailyTips(
        userId: String,
        period: String,
    ): Response<MoodDailyTips> {
        // Delegate the API call to the ApiService
        return apiService.getDailyTips(userId, period)
    }

    suspend fun putAccounts(token: String, body: Update): Response<Unit> {
        return apiService.putAccount(token, body)
    }

    suspend fun postPhoto(userId: String, file: MultipartBody.Part? = null): Response<Unit> {
        return apiService.postPhoto(userId, file)
    }

    fun login(idToken: Token, callback: Callback<LoginResponse>) {
        return apiService.postLogin(idToken).enqueue(callback)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(text: Register, callback: Callback<RegisterResponse>) {
        return apiService.postregister(text).enqueue(callback)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}