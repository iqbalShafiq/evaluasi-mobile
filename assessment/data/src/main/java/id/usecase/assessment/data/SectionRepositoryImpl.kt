package id.usecase.assessment.data

import id.usecase.assessment.domain.SectionRepository
import id.usecase.core.data.sync.SyncService
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.section.Section
import id.usecase.core.domain.sync.EntityType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SectionRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val syncService: SyncService,
    private val dispatcher: CoroutineDispatcher
) : SectionRepository {
    override suspend fun upsertSection(sections: List<Section>): DataResult<List<String>> {
        return withContext(dispatcher) {
            val sectionId = dataSource.upsertSection(sections)
            val result = dataSource.getSectionByIds(sectionId)
            if (result.isNotEmpty()) {
                syncService.markMultipleForSync(
                    entities = result,
                    entityType = EntityType.SECTION
                )
            }

            DataResult.Success(sectionId)
        }
    }

    override fun getSectionById(sectionId: String): Flow<DataResult<Section?>> {
        return flow {
            emit(DataResult.Loading)
            val section: Section? = dataSource.getSectionById(sectionId)
            emit(DataResult.Success(section))
        }.flowOn(dispatcher)
    }

    override fun getSectionsByClassRoomId(classRoomId: String): Flow<DataResult<List<Section>>> {
        return flow {
            emit(DataResult.Loading)
            val sections: List<Section> = dataSource.getSectionsByClassRoomId(classRoomId)
            emit(DataResult.Success(sections))
        }.flowOn(dispatcher)
    }

    override fun getSelectedSectionOnAssessment(assessmentId: String): Flow<DataResult<List<Section>>> {
        return flow {
            emit(DataResult.Loading)
            val sections: List<Section> = dataSource.getSelectedSectionOnAssessment(assessmentId)
            emit(DataResult.Success(sections))
        }.flowOn(dispatcher)
    }
}