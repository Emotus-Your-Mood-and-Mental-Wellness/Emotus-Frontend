package academy.bangkit.emotusproject.data

import academy.bangkit.emotusproject.data.remote.response.MoodDailyTips
import academy.bangkit.emotusproject.data.remote.response.MoodEntries
import academy.bangkit.emotusproject.data.remote.response.MoodEntry
import academy.bangkit.emotusproject.data.remote.response.MoodResponse
import academy.bangkit.emotusproject.data.remote.response.MoodSummary
import academy.bangkit.emotusproject.data.remote.response.MoodTrends
import academy.bangkit.emotusproject.data.remote.response.UserAccount
import academy.bangkit.emotusproject.data.remote.response.UserAccountBody
import academy.bangkit.emotusproject.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import retrofit2.Response

class Repository private constructor(private val apiService: ApiService) {

    suspend fun predictMoodResult(userId: String, diaryEntry: MoodResponse): Response<MoodEntry> {
        // Delegate the API call to the ApiService
        return apiService.postPredictMood(userId, diaryEntry)
    }

    suspend fun moodTrendsResult(
        userId: String,
        startDate: String,
        endDate: String
    ): Response<MoodTrends> {
        // Delegate the API call to the ApiService
        return apiService.getMoodTrends(userId, startDate)
    }

    suspend fun userMoodSummary(
        userId: String,
        startDate: String,
        endDate: String? = null
    ): Response<MoodSummary> {
        // Delegate the API call to the ApiService
        return apiService.getMoodSummary(userId, startDate)
    }

    suspend fun allMoodEntries(
        userId: String,
        period: String,
        endDate: String? = null
    ): Response<MoodEntries> {
        // Delegate the API call to the ApiService
        return apiService.getMoodEntries(userId, period)
    }

    suspend fun getUserAccount(
        userId: String,
        body: UserAccountBody? = null
    ): Response<UserAccount> {
        // Delegate the API call to the ApiService
        return apiService.getDataUserAccount(userId)
    }

    suspend fun deleteMoodEntry(
        entryId: String,
        userId: String,
    ): Response<Unit> {
        // Delegate the API call to the ApiService
        return apiService.deleteMood(entryId, userId)
    }

    suspend fun getDailyTips(
        userId: String,
        period: String,
    ): Response<MoodDailyTips> {
        // Delegate the API call to the ApiService
        return apiService.getDailyTips(userId, period)
    }

    suspend fun putAccounts(userId: String, body: UserAccountBody): Response<Unit> {
        return apiService.putAccount(userId, body)
    }

    suspend fun postPhoto(userId: String, file: MultipartBody.Part): Response<Unit> {
        return apiService.postPhoto(userId, file)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
//            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}