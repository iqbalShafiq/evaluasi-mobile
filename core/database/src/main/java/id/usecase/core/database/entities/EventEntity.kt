package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "event_date")
    val eventDate: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)