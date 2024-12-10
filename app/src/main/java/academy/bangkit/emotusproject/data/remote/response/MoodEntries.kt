package academy.bangkit.emotusproject.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodEntries(
    val total: Int,
    val data: List<DataItem>
) : Parcelable

@Parcelize
data class DataItem(
    val createdAt: String,
    val mood: String,
    val thingsToDo: List<String>,
    val thoughtfulSuggestions: List<String>,
    val id: String,
    val diaryEntry: String,
    val stressLevel: String,
    val userId: String,
    val predictedMood: String,
    val sympathyMessage: String,
    val updatedAt: String
) : Parcelable
