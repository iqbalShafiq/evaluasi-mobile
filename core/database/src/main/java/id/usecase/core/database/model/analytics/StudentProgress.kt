package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class StudentProgress(
    @ColumnInfo(name = "student_name")
    val studentName: String,
    @ColumnInfo(name = "progress_percentage")
    val progressPercentage: Float,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long
)
