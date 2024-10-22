package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessments")
data class AssessmentEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "student_id")
    val studentId: Int,
    @ColumnInfo(name = "event_id")
    val eventId: Int,
    val score: Double?,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)