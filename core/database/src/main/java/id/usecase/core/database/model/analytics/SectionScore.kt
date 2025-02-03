package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class SectionScore(
    @ColumnInfo("section_name")
    val sectionName: String,
    @ColumnInfo("average_score")
    val averageScore: Double
)