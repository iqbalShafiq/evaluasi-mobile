package id.usecase.assessment.domain

import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import kotlinx.coroutines.flow.Flow

interface ClassRoomRepository {
    suspend fun upsertClassRoom(classRoom: ClassRoom): DataResult<ClassRoom?>
    fun getClassRooms(): Flow<DataResult<List<ClassRoom>>>
    fun searchClassRooms(query: String): Flow<DataResult<List<ClassRoom>>>
    fun getClassRoomById(classRoomId: String): Flow<DataResult<ClassRoom?>>
    suspend fun deleteClassRoom(classRoom: ClassRoom): DataResult<Unit>
}