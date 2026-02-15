package id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository

import androidx.lifecycle.LiveData
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity.FavoriteEventEntity
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.room.FavoriteEventDao
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.DetailEventResponse
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventResponse
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit.ApiService

class EventRepository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {

    suspend fun getUpcomingEvents(): EventResponse {
        return apiService.getEvents(active = 1)
    }

    suspend fun getFinishedEvents(): EventResponse {
        return apiService.getEvents(active = 0)
    }

    suspend fun searchEvents(query: String): EventResponse {
        return apiService.searchEvents(query = query)
    }

    suspend fun getEventDetail(id: Int): DetailEventResponse {
        return apiService.getEventDetail(id)
    }

    // Favorite operations
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getAllFavoriteEvents()
    }

    suspend fun insertFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.insertFavoriteEvent(event)
    }

    suspend fun deleteFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.deleteFavoriteEvent(event)
    }

    suspend fun deleteFavoriteEventById(eventId: Int) {
        favoriteEventDao.deleteFavoriteEventById(eventId)
    }

    fun isFavorite(eventId: Int): LiveData<Boolean> {
        return favoriteEventDao.isFavorite(eventId)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao)
            }.also { instance = it }
    }
}
