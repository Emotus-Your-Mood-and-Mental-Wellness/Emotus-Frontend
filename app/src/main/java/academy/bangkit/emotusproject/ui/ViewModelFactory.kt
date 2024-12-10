package academy.bangkit.emotusproject.ui

import academy.bangkit.emotusproject.data.Repository
import academy.bangkit.emotusproject.di.Injection
import academy.bangkit.emotusproject.ui.home.HomeViewModel
import academy.bangkit.emotusproject.ui.monitoring.MonitoringViewModel
import academy.bangkit.emotusproject.ui.settings.SettingsViewModel
import academy.bangkit.emotusproject.ui.tips.TipsViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
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