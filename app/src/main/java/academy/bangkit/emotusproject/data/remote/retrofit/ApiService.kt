package academy.bangkit.emotusproject.data.remote.retrofit

import academy.bangkit.emotusproject.data.remote.response.MoodDailyTips
import academy.bangkit.emotusproject.data.remote.response.MoodEntries
import academy.bangkit.emotusproject.data.remote.response.MoodEntry
import academy.bangkit.emotusproject.data.remote.response.MoodResponse
import academy.bangkit.emotusproject.data.remote.response.MoodSummary
import academy.bangkit.emotusproject.data.remote.response.MoodTrends
import academy.bangkit.emotusproject.data.remote.response.UserAccount
import academy.bangkit.emotusproject.data.remote.response.UserAccountBody
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @POST("moods")
    suspend fun postPredictMood(
        @Query("userId") userId: String,
        @Body diaryEntry: MoodResponse
    ): Response<MoodEntry>

    @GET("analytics/trends")
    suspend fun getMoodTrends(
        @Query("userId") userId: String,
        @Query("period") startDate: String
//        @Query("endDate") endDate: String
    ): Response<MoodTrends>

    @GET("analytics/summary")
    suspend fun getMoodSummary(
        @Query("userId") userId: String,
        @Query("period") period: String
//        @Query("endDate") endDate: String
    ): Response<MoodSummary>

    @GET("moods")
    suspend fun getMoodEntries(
        @Query("userId") userId: String,
        @Query("period") period: String,
//        @Query("endDate") endDate: String
    ): Response<MoodEntries>

    @GET("account")
    suspend fun getDataUserAccount(
        @Query("userId") userId: String,
//        @Body body: UserAccountBody
    ): Response<UserAccount>

    @DELETE("moods/{entryId}")
    suspend fun deleteMood(
        @Path("entryId") entryId: String,
        @Query("userId") userId: String,
    ): Response<Unit>

    @GET("daily-tips")
    suspend fun getDailyTips(
        @Query("userId") userId: String,
        @Query("period") period: String,
//        @Query("endDate") endDate: String
    ): Response<MoodDailyTips>

    @PUT("account")
    suspend fun putAccount(
        @Query("userId") userId: String,
        @Body body: UserAccountBody
    ): Response<Unit>

    @Multipart
    @POST("users/photo")
    suspend fun postPhoto(
        @Query("userId") userId: String,
        @Part photo: MultipartBody.Part
    ): Response<Unit>
//    @FormUrlEncoded
//    @POST("login")
//    fun postLogin(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<DicodingStoryResponse>
//
//    @GET("stories")
//    fun getAllStories(@Header("Authorization") token: String): Call<DicodingStoryResponse>
//
//    @Multipart
//    @POST("stories")
//    fun postStory(
//        @Part file: MultipartBody.Part,
//        @Header("Authorization") token: String,
//        @Part("description") description: RequestBody
//    ): Call<DicodingStoryResponse>
//
//    @GET("stories/{id}")
//    suspend fun getDetailStory(
//        @Header("Authorization") token: String,
//        @Path("id") id: String
//    ): MoodResponse<DicodingStoryResponse>

}