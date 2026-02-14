package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.finished

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

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<Result<List<EventItem>>>()
    val events: LiveData<Result<List<EventItem>>> = _events

    private var searchJob: Job? = null

    init {
        loadFinishedEvents()
    }

    fun loadFinishedEvents() {
        viewModelScope.launch {
            _events.value = Result.Loading
            try {
                val response = repository.getFinishedEvents()
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
            delay(1000) // Debounce
            if (query.isEmpty()) {
                loadFinishedEvents()
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
