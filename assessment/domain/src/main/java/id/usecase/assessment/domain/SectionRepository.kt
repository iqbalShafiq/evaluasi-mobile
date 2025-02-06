package id.usecase.assessment.domain

import id.usecase.core.domain.assessment.utils.DataResult
import id.usecase.core.domain.assessment.model.section.Section
import kotlinx.coroutines.flow.Flow

interface SectionRepository {
    suspend fun upsertSection(sections: List<Section>): DataResult<List<Long>>
    fun getSectionById(sectionId: Int): Flow<DataResult<Section?>>
    fun getSectionsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Section>>>
    fun getSelectedSectionOnAssessment(assessmentId: Int): Flow<DataResult<List<Section>>>
}