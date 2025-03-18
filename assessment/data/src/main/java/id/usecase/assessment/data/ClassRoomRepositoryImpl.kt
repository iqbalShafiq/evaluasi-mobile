package id.usecase.assessment.data

import id.usecase.assessment.domain.ClassRoomRepository
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ClassRoomRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : ClassRoomRepository {
    override suspend fun upsertClassRoom(classRoom: ClassRoom): DataResult<ClassRoom?> {
        return withContext(dispatcher) {
            val newClassRoom = dataSource.upsertClassRoom(classRoom)
            return@withContext DataResult.Success(newClassRoom)
        }
    }

    override fun getClassRooms(): Flow<DataResult<List<ClassRoom>>> {
        return flow {
            emit(DataResult.Loading)
            val classRooms: List<ClassRoom> = dataSource.getClassRooms()
            emit(DataResult.Success(classRooms))
        }.flowOn(dispatcher)
    }

    override fun searchClassRooms(query: String): Flow<DataResult<List<ClassRoom>>> {
        return flow {
            emit(DataResult.Loading)
            val classRooms: List<ClassRoom> = dataSource.searchClassRooms(query)
            emit(DataResult.Success(classRooms))
        }.flowOn(dispatcher)
    }

    override fun getClassRoomById(classRoomId: String): Flow<DataResult<ClassRoom?>> {
        return flow {
            emit(DataResult.Loading)
            val classRoom: ClassRoom = dataSource.getClassRoomById(classRoomId)
                ?: throw Exception("Class room not found")

            emit(DataResult.Success(classRoom))
        }.flowOn(dispatcher)
    }

    override suspend fun deleteClassRoom(classRoom: ClassRoom): DataResult<Unit> {
        return withContext(dispatcher) {
            dataSource.deleteClassRoom(classRoom)
            return@withContext DataResult.Success(Unit)
        }
    }
}