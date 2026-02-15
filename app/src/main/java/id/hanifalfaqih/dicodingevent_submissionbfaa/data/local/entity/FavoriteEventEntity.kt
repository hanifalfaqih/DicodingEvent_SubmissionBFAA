package id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "summary")
    val summary: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "image_logo")
    val imageLogo: String,

    @ColumnInfo(name = "media_cover")
    val mediaCover: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "owner_name")
    val ownerName: String,

    @ColumnInfo(name = "city_name")
    val cityName: String,

    @ColumnInfo(name = "quota")
    val quota: Int,

    @ColumnInfo(name = "registrants")
    val registrants: Int,

    @ColumnInfo(name = "begin_time")
    val beginTime: String,

    @ColumnInfo(name = "end_time")
    val endTime: String,

    @ColumnInfo(name = "link")
    val link: String
)
