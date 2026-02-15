package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity.FavoriteEventEntity
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<FavoriteEventEntity>> = repository.getAllFavoriteEvents()
    fun deleteFavorite(event: FavoriteEventEntity) {
        viewModelScope.launch {
            repository.deleteFavoriteEvent(event)
        }
    }
}
