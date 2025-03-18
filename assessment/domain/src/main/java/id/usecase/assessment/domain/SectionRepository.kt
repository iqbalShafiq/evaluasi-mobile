package id.usecase.assessment.domain

import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.section.Section
import kotlinx.coroutines.flow.Flow

interface SectionRepository {
    suspend fun upsertSection(sections: List<Section>): DataResult<List<String>>
    fun getSectionById(sectionId: String): Flow<DataResult<Section?>>
    fun getSectionsByClassRoomId(classRoomId: String): Flow<DataResult<List<Section>>>
    fun getSelectedSectionOnAssessment(assessmentId: String): Flow<DataResult<List<Section>>>
}