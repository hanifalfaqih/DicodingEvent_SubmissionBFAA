package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity.FavoriteEventEntity
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

    fun isFavorite(eventId: Int): LiveData<Boolean> {
        return repository.isFavorite(eventId)
    }

    fun addToFavorite(event: EventItem) {
        viewModelScope.launch {
            val favoriteEvent = FavoriteEventEntity(
                id = event.id,
                name = event.name,
                summary = event.summary,
                description = event.description,
                imageLogo = event.imageLogo,
                mediaCover = event.mediaCover,
                category = event.category,
                ownerName = event.ownerName,
                cityName = event.cityName,
                quota = event.quota,
                registrants = event.registrants,
                beginTime = event.beginTime,
                endTime = event.endTime,
                link = event.link
            )
            repository.insertFavoriteEvent(favoriteEvent)
        }
    }

    fun removeFromFavorite(eventId: Int) {
        viewModelScope.launch {
            repository.deleteFavoriteEventById(eventId)
        }
    }
}
