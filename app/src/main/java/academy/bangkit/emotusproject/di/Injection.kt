package academy.bangkit.emotusproject.di

import academy.bangkit.emotusproject.Utils.Date
import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.data.remote.retrofit.ApiConfig
import android.content.Context

object Injection {
    fun provideRepository(context: Context): Repository {
//        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }

    fun provideDate(): Date {
        return Date() // Mengembalikan instance MyClass
    }
}