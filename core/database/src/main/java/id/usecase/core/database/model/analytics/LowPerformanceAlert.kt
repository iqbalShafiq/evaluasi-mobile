package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class LowPerformanceAlert(
    @ColumnInfo(name = "student_name")
    val studentName: String,
    @ColumnInfo(name = "student_identifier")
    val studentIdentifier: Int,
    @ColumnInfo(name = "average_score")
    val averageScore: Float,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long
)
