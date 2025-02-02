package id.usecase.assessment.data

import id.usecase.assessment.domain.SectionRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.section.Section
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SectionRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : SectionRepository {
    override suspend fun upsertSection(sections: List<Section>): DataResult<List<Long>> {
        return withContext(dispatcher) {
            val sectionId = dataSource.upsertSection(sections)
            DataResult.Success(sectionId)
        }
    }

    override fun getSectionById(sectionId: Int): Flow<DataResult<Section?>> {
        return flow {
            emit(DataResult.Loading)
            val section: Section? = dataSource.getSectionById(sectionId)
            emit(DataResult.Success(section))
        }.flowOn(dispatcher)
    }

    override fun getSectionsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Section>>> {
        return flow {
            emit(DataResult.Loading)
            val sections: List<Section> = dataSource.getSectionsByClassRoomId(classRoomId)
            emit(DataResult.Success(sections))
        }.flowOn(dispatcher)
    }

    override fun getSelectedSectionOnAssessment(assessmentId: Int): Flow<DataResult<List<Section>>> {
        return flow {
            emit(DataResult.Loading)
            val sections: List<Section> = dataSource.getSelectedSectionOnAssessment(assessmentId)
            emit(DataResult.Success(sections))
        }.flowOn(dispatcher)
    }
}