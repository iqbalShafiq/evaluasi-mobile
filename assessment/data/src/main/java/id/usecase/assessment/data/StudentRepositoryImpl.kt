package id.usecase.assessment.data

import id.usecase.assessment.domain.StudentRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class StudentRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : StudentRepository {
    override suspend fun upsertStudent(students: Student): DataResult<Student?> {
        return withContext(dispatcher) {
            try {
                val student = dataSource.upsertStudent(students)
                return@withContext DataResult.Success(student)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override suspend fun upsertStudents(students: List<Student>): DataResult<List<Student>> {
        return withContext(dispatcher) {
            try {
                val students = dataSource.upsertStudents(students)
                return@withContext DataResult.Success(students)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override fun getTotalStudent(): Flow<DataResult<Int>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val totalStudent: Int = dataSource.getTotalStudent()
                emit(DataResult.Success(totalStudent))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val students: List<Student> = dataSource.getStudentsByClassRoomId(classRoomId)
                emit(DataResult.Success(students))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteStudent(student: Student) {
        withContext(dispatcher) {
            try {
                dataSource.deleteStudent(student)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}