package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class PerformanceScore(
    @ColumnInfo(name = "performance_level")
    val performanceLevel: String,
    @ColumnInfo(name = "average_score")
    val averageScore: Float
)
