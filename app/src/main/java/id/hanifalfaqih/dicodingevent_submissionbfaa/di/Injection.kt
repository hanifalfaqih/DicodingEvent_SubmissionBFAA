package id.hanifalfaqih.dicodingevent_submissionbfaa.di
import android.content.Context
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.datastore.SettingPreferences
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.datastore.dataStore
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.room.EventDatabase
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit.ApiConfig
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.repository.EventRepository
object Injection {
    fun provideEventRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService, dao)
    }
    fun provideSettingPreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}
