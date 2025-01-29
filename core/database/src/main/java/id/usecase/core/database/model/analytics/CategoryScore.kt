package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class CategoryScore(
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "total_assessments")
    val totalAssessments: Int
)
