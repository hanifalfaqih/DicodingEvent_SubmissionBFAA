package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<Result<List<EventItem>>>()
    val events: LiveData<Result<List<EventItem>>> = _events

    private var searchJob: Job? = null

    init {
        loadUpcomingEvents()
    }

    fun loadUpcomingEvents() {
        viewModelScope.launch {
            _events.value = Result.Loading
            try {
                val response = repository.getUpcomingEvents()
                if (!response.error) {
                    _events.value = Result.Success(response.listEvents)
                } else {
                    _events.value = Result.Error(response.message)
                }
            } catch (e: Exception) {
                _events.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun searchEvents(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            if (query.isEmpty()) {
                loadUpcomingEvents()
                return@launch
            }

            _events.value = Result.Loading
            try {
                val response = repository.searchEvents(query)
                if (!response.error) {
                    _events.value = Result.Success(response.listEvents)
                } else {
                    _events.value = Result.Error(response.message)
                }
            } catch (e: Exception) {
                _events.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}
