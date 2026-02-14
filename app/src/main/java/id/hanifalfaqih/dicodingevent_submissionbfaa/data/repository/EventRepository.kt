package id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository

import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.DetailEventResponse
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventResponse
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit.ApiService

class EventRepository(private val apiService: ApiService) {

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

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService)
            }.also { instance = it }
    }
}
