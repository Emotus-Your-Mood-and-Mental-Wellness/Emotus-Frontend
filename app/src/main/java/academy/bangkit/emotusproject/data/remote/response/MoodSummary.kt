package academy.bangkit.emotusproject.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodSummary(

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("entries")
    val entries: List<EntriesItem>,

    @field:SerializedName("endDate")
    val endDate: String,

    @field:SerializedName("thingsToDo")
    val thingsToDo: List<String>,

    @field:SerializedName("helpfulHint")
    val helpfulHint: String,

    @field:SerializedName("feelInspire")
    val feelInspire: String,

    @field:SerializedName("dominantMood")
    val dominantMood: String,

    @field:SerializedName("dominantStressLevel")
    val dominantStressLevel: String,

    @field:SerializedName("thoughtfulSuggestions")
    val thoughtfulSuggestions: List<String>,

    @field:SerializedName("totalEntries")
    val totalEntries: Int,

    @field:SerializedName("startDate")
    val startDate: String,

    @field:SerializedName("sympathyMessage")
    val sympathyMessage: String
) : Parcelable

@Parcelize
data class EntriesItem(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("mood")
    val mood: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("stressLevel")
    val stressLevel: String
) : Parcelable
