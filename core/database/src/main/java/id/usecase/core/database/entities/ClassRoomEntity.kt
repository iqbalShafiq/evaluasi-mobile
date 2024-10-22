package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class_rooms")
data class ClassRoomEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val subject: String,
    @ColumnInfo(name = "start_period")
    val startPeriod: Long,
    @ColumnInfo(name = "end_period")
    val endPeriod: Long?,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)