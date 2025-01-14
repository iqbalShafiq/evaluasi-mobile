package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class MonthlyScore(
    val month: Float,
    @ColumnInfo(name = "average_score")
    val averageScore: Float
)
