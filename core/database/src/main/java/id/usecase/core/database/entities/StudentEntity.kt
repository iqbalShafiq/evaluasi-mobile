package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val identifier: Int,
    @ColumnInfo(name = "class_room_id")
    val classRoomId: Int,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)