package com.emotus.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emotus.app.data.Repository
import com.emotus.app.di.Injection
import com.emotus.app.ui.home.HomeViewModel
import com.emotus.app.ui.monitoring.MonitoringViewModel
import com.emotus.app.ui.settings.SettingsViewModel
import com.emotus.app.ui.signin.SIgnInViewModel
import com.emotus.app.ui.signup.SignUpViewModel
import com.emotus.app.ui.tips.TipsViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SIgnInViewModel::class.java) -> {
                SIgnInViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MonitoringViewModel::class.java) -> {
                MonitoringViewModel(repository) as T
            }

            modelClass.isAssignableFrom(TipsViewModel::class.java) -> {
                TipsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}