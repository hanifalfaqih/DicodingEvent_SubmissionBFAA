package id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit

import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.DetailEventResponse
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int? = null
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): DetailEventResponse
}
