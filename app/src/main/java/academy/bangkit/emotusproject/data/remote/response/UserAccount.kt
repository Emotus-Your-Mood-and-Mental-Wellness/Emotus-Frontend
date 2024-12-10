package academy.bangkit.emotusproject.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAccount(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("profilePhotoUrl")
    val profilePhotoUrl: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("username")
    val username: String
) : Parcelable

@Parcelize
data class UserAccountBody(


    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("password")
    val password: String


) : Parcelable
