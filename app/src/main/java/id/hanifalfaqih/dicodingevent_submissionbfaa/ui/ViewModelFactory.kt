package id.hanifalfaqih.dicodingevent_submissionbfaa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail.DetailViewModel
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.finished.FinishedViewModel
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.home.HomeViewModel
import id.hanifalfaqih.dicodingevent_submissionbfaa.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(repository: EventRepository): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository)
            }.also { instance = it }
    }
}
