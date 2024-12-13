package com.emotus.app.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User
) : Parcelable

@Parcelize
data class Token(

    @field:SerializedName("idToken")
    val idToken: String

) : Parcelable

@Parcelize
data class User(

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("profilePhotoUrl")
    val profilePhotoUrl: String
) : Parcelable
