package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.usecase.core.database.utils.EntityPrefix
import id.usecase.core.database.utils.generateEntityId

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String = generateEntityId(EntityPrefix.CATEGORY),
    val name: String,
    val percentage: Double,
    @ColumnInfo(name = "class_room_id")
    val classRoomId: String,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)