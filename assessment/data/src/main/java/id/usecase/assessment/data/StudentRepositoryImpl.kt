package id.usecase.assessment.data

import id.usecase.assessment.domain.StudentRepository
import id.usecase.core.data.sync.SyncService
import id.usecase.core.domain.utils.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.student.Student
import id.usecase.core.domain.sync.EntityType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class StudentRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val syncService: SyncService,
    private val dispatcher: CoroutineDispatcher
) : StudentRepository {
    override suspend fun upsertStudent(students: Student): DataResult<Student?> {
        return withContext(dispatcher) {
            val student = dataSource.upsertStudent(students)
            if (student != null) {
                syncService.markForSync(
                    entity = student,
                    entityType = EntityType.STUDENT
                )
            }

            return@withContext DataResult.Success(student)
        }
    }

    override suspend fun upsertStudents(students: List<Student>): DataResult<List<Student>> {
        return withContext(dispatcher) {
            val result = dataSource.upsertStudents(students)
            if (result.isNotEmpty()) {
                syncService.markMultipleForSync(
                    entities = result,
                    entityType = EntityType.STUDENT
                )
            }

            return@withContext DataResult.Success(result)
        }
    }

    override fun getTotalStudent(): Flow<DataResult<Int>> {
        return flow {
            emit(DataResult.Loading)
            val totalStudent: Int = dataSource.getTotalStudent()
            emit(DataResult.Success(totalStudent))
        }.flowOn(dispatcher)
    }

    override fun getStudentsByClassRoomId(classRoomId: String): Flow<DataResult<List<Student>>> {
        return flow {
            emit(DataResult.Loading)
            val students: List<Student> = dataSource.getStudentsByClassRoomId(classRoomId)
            emit(DataResult.Success(students))
        }.flowOn(dispatcher)
    }

    override suspend fun deleteStudent(student: Student) {
        withContext(dispatcher) {
            dataSource.deleteStudent(student)
        }
    }
}