package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import id.usecase.core.database.type_converters.IntListConverter
import id.usecase.core.domain.utils.EntityPrefix
import id.usecase.core.domain.utils.generateEntityId

@Entity(tableName = "class_rooms")
data class ClassRoomEntity (
    @PrimaryKey
    val id: String = generateEntityId(EntityPrefix.CLASS_ROOM),
    @ColumnInfo(name = "teacher_id")
    val teacherId: String,
    val name: String,
    val subject: String,
    val description: String,
    @ColumnInfo(name = "start_period")
    val startPeriod: Long,
    @ColumnInfo(name = "long_period")
    val longPeriod: Long?,
    @ColumnInfo(name = "schedule")
    @TypeConverters(IntListConverter::class)
    val schedule: List<Int>,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)