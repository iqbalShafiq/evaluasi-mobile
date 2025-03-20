package id.usecase.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.usecase.core.domain.utils.EntityPrefix
import id.usecase.core.domain.utils.generateEntityId

@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey
    val id: String = generateEntityId(EntityPrefix.ASSESSMENT),
    @ColumnInfo(name = "student_id")
    val studentId: String,
    @ColumnInfo(name = "event_id")
    val eventId: String,
    val score: Double,
    val comment: String?,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "last_modified_time")
    val lastModifiedTime: Long
)