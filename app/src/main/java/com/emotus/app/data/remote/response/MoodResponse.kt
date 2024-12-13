package com.emotus.app.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodResponse(

    @field:SerializedName("diaryEntry")
    val diaryEntry: String,
) : Parcelable

@Parcelize
data class MoodEntry(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("thingsToDo")
    val thingsToDo: List<String>,

    @field:SerializedName("thoughtfulSuggestions")
    val thoughtfulSuggestions: List<String>,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("diaryEntry")
    val diaryEntry: String,

    @field:SerializedName("stressLevel")
    val stressLevel: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("predictedMood")
    val predictedMood: String,

    @field:SerializedName("sympathyMessage")
    val sympathyMessage: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable
