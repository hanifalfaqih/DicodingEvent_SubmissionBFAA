package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<EventItem>>>()
    val upcomingEvents: LiveData<Result<List<EventItem>>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Result<List<EventItem>>>()
    val finishedEvents: LiveData<Result<List<EventItem>>> = _finishedEvents

    init {
        loadHomeEvents()
    }

    fun loadHomeEvents() {
        loadUpcomingEvents()
        loadFinishedEvents()
    }

    private fun loadUpcomingEvents() {
        viewModelScope.launch {
            _upcomingEvents.value = Result.Loading
            try {
                val response = repository.getUpcomingEvents()
                if (!response.error) {
                    val limitedEvents = response.listEvents.take(5)
                    _upcomingEvents.value = Result.Success(limitedEvents)
                } else {
                    _upcomingEvents.value = Result.Error(response.message)
                }
            } catch (e: Exception) {
                _upcomingEvents.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun loadFinishedEvents() {
        viewModelScope.launch {
            _finishedEvents.value = Result.Loading
            try {
                val response = repository.getFinishedEvents()
                if (!response.error) {
                    val limitedEvents = response.listEvents.take(5)
                    _finishedEvents.value = Result.Success(limitedEvents)
                } else {
                    _finishedEvents.value = Result.Error(response.message)
                }
            } catch (e: Exception) {
                _finishedEvents.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}
