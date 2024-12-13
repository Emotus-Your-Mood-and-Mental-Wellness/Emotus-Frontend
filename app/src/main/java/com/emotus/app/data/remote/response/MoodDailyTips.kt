package com.emotus.app.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodDailyTips(

    @field:SerializedName("entriesCount")
    val entriesCount: Int,

    @field:SerializedName("Quick Activity")
    val quickActivity: String,

    @field:SerializedName("Relaxation Exercise")
    val relaxationExercise: String,

    @field:SerializedName("dominantMood")
    val dominantMood: String,

    @field:SerializedName("Take a Moment for Yourself")
    val takeAMomentForYourself: String,

    @field:SerializedName("stressLevel")
    val stressLevel: String,

    @field:SerializedName("Kind Reminder")
    val kindReminder: String
) : Parcelable
