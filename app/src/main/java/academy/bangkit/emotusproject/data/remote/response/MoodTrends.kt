package academy.bangkit.emotusproject.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoodTrends(

    @field:SerializedName("moodDistribution")
    val moodDistribution: MoodDistribution,

    @field:SerializedName("stressLevelTrend")
    val stressLevelTrend: List<StressLevelTrendItem>,

    @field:SerializedName("timeOfDayAnalysis")
    val timeOfDayAnalysis: TimeOfDayAnalysis,

    @field:SerializedName("commonTriggers")
    val commonTriggers: List<CommonTriggersItem>
) : Parcelable

@Parcelize
data class Sadness(

    @field:SerializedName("percentage")
    val percentage: Int,

    @field:SerializedName("count")
    val count: Int
) : Parcelable

@Parcelize
data class Fear(

    @field:SerializedName("percentage")
    val percentage: Int,

    @field:SerializedName("count")
    val count: Int
) : Parcelable

@Parcelize
data class TimeOfDayAnalysis(

    @field:SerializedName("afternoon")
    val afternoon: Int,

    @field:SerializedName("night")
    val night: Int,

    @field:SerializedName("evening")
    val evening: Int,

    @field:SerializedName("morning")
    val morning: Int
) : Parcelable

@Parcelize
data class Anger(

    @field:SerializedName("percentage")
    val percentage: Int,

    @field:SerializedName("count")
    val count: Int
) : Parcelable

@Parcelize
data class StressLevelTrendItem(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("mood")
    val mood: String,

    @field:SerializedName("stressLevel")
    val stressLevel: String
) : Parcelable

@Parcelize
data class Happy(

    @field:SerializedName("percentage")
    val percentage: Int,

    @field:SerializedName("count")
    val count: Int
) : Parcelable

@Parcelize
data class MoodDistribution(

    @field:SerializedName("Anger")
    val anger: Anger,

    @field:SerializedName("Fear")
    val fear: Fear,

    @field:SerializedName("Happy")
    val happy: Happy,

    @field:SerializedName("Love")
    val love: Love,

    @field:SerializedName("Sadness")
    val sadness: Sadness
) : Parcelable

@Parcelize
data class CommonTriggersItem(

    @field:SerializedName("trigger")
    val trigger: String,

    @field:SerializedName("frequency")
    val frequency: Int
) : Parcelable

@Parcelize
data class Love(

    @field:SerializedName("percentage")
    val percentage: Int,

    @field:SerializedName("count")
    val count: Int
) : Parcelable
