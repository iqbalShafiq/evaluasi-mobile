package id.usecase.assessment.data

import id.usecase.assessment.domain.StudentRepository
import id.usecase.core.domain.utils.DataResult
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
            val student = dataSource.upsertStudent(students)
            return@withContext DataResult.Success(student)
        }
    }

    override suspend fun upsertStudents(students: List<Student>): DataResult<List<Student>> {
        return withContext(dispatcher) {
            val students = dataSource.upsertStudents(students)
            return@withContext DataResult.Success(students)
        }
    }

    override fun getTotalStudent(): Flow<DataResult<Int>> {
        return flow {
            emit(DataResult.Loading)
            val totalStudent: Int = dataSource.getTotalStudent()
            emit(DataResult.Success(totalStudent))
        }.flowOn(dispatcher)
    }

    override fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>> {
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