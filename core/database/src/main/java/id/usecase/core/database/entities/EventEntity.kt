package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.usecase.core.database.utils.EntityPrefix
import id.usecase.core.database.utils.generateEntityId

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String = generateEntityId(EntityPrefix.EVENT),
    val name: String,
    val purpose: String,
    @ColumnInfo(name = "event_date")
    val eventDate: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)