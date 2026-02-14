package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventDetail = MutableLiveData<Result<EventItem>>()
    val eventDetail: LiveData<Result<EventItem>> = _eventDetail

    fun loadEventDetail(id: Int) {
        viewModelScope.launch {
            _eventDetail.value = Result.Loading
            try {
                val response = repository.getEventDetail(id)
                if (!response.error) {
                    _eventDetail.value = Result.Success(response.event)
                } else {
                    _eventDetail.value = Result.Error(response.message)
                }
            } catch (e: Exception) {
                _eventDetail.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}
