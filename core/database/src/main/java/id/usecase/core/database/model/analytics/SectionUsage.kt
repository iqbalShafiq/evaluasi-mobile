package id.usecase.core.database.model.analytics

import androidx.room.ColumnInfo

data class SectionUsage(
    @ColumnInfo("section_name")
    val sectionName: String,
    @ColumnInfo("total_assessments")
    val totalAssessments: Int
)