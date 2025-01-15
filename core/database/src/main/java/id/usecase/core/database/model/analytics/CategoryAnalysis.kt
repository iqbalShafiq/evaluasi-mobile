package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class CategoryAnalysis(
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "average_score")
    val averageScore: Float,
    @ColumnInfo(name = "total_assessments")
    val totalAssessments: Int
)
