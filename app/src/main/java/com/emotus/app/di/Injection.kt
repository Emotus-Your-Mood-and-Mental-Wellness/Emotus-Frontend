package com.emotus.app.di

import android.content.Context
import com.emotus.app.data.Repository
import com.emotus.app.data.remote.retrofit.ApiConfig
import com.emotus.app.models.UserPreference
import com.emotus.app.models.dataStore
import com.emotus.app.utils.Date

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }

    fun provideDate(): Date {
        return Date() // Mengembalikan instance MyClass
    }
}