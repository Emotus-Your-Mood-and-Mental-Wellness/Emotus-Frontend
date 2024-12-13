package com.emotus.app.data.remote.retrofit

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
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("moods")
    suspend fun postPredictMood(
        @Header("Authorization") token: String,
        @Body diaryEntry: MoodResponse
    ): Response<MoodEntry>

    @GET("analytics/trends")
    suspend fun getMoodTrends(
        @Header("Authorization") token: String,
        @Query("period") period: String
    ): Response<MoodTrends>

    @GET("analytics/summary")
    suspend fun getMoodSummary(
        @Header("Authorization") token: String,
        @Query("period") period: String
    ): Response<MoodSummary>

    @GET("moods")
    suspend fun getMoodEntries(
        @Header("Authorization") token: String,
        @Query("period") period: String,
    ): Response<MoodEntries>

    @DELETE("moods/{entryId}")
    suspend fun deleteMood(
        @Header("Authorization") token: String,
        @Path("entryId") entryId: String
    ): Response<Unit>

    @GET("daily-tips")
    suspend fun getDailyTips(
        @Header("Authorization") token: String,
        @Query("period") period: String
//        @Query("endDate") endDate: String
    ): Response<MoodDailyTips>

    @PUT("auth/profile")
    suspend fun putAccount(
        @Header("Authorization") token: String,
        @Body body: Update
    ): Response<Unit>

    @Multipart
    @POST("users/photo")
    suspend fun postPhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?
    ): Response<Unit>

    @POST("auth/login")
    fun postLogin(
        @Body idToken: Token
    ): Call<LoginResponse>

    @GET("auth/profile")
    suspend fun getDataUserAccount(
        @Header("Authorization") token: String
    ): Response<UserAccount>


    @POST("auth/register")
    fun postregister(
        @Body register: Register
    ): Call<RegisterResponse>

}