package id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events ORDER BY begin_time DESC")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: FavoriteEventEntity)

    @Delete
    suspend fun deleteFavoriteEvent(event: FavoriteEventEntity)

    @Query("DELETE FROM favorite_events WHERE id = :eventId")
    suspend fun deleteFavoriteEventById(eventId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_events WHERE id = :eventId)")
    fun isFavorite(eventId: Int): LiveData<Boolean>
}
