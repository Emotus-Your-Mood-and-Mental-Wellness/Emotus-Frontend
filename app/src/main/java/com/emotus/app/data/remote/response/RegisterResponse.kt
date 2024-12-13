package com.emotus.app.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(

    @field:SerializedName("accountInfo")
    val accountInfo: AccountInfo,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
) : Parcelable

@Parcelize
data class AccountInfo(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lastLogin")
    val lastLogin: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("profilePhotoUrl")
    val profilePhotoUrl: String
) : Parcelable
