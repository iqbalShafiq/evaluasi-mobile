package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import id.usecase.core.database.type_converters.IntListConverter
import id.usecase.core.database.type_converters.StringListConverter

@Entity(tableName = "sections")
data class SectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "topics")
    @TypeConverters(StringListConverter::class)
    val topics: List<String>,
    @ColumnInfo(name = "class_room_id")
    val classRoomId: Int,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)