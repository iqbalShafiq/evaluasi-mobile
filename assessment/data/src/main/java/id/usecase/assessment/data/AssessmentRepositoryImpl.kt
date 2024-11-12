package id.usecase.assessment.data

import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AssessmentRepositoryImpl(
    private val dataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : AssessmentRepository {
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

    override fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val students: List<Student> = dataSource.getStudentsByClassRoomId(classRoomId)
                if (students.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any student yet")
                        )
                    )
                    return@flow
                }
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

    override suspend fun upsertCategories(categories: List<Category>): DataResult<List<Category>> {
        return withContext(dispatcher) {
            try {
                val insertedIds = dataSource
                    .upsertCategories(categories)
                    .map { it.toInt() }
                val insertedCategories = dataSource.getCategoriesByIds(insertedIds)
                return@withContext DataResult.Success(insertedCategories)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override suspend fun upsertCategory(category: Category): DataResult<Category?> {
        return withContext(dispatcher) {
            try {
                val id = dataSource
                    .upsertCategory(category)
                    .toInt()
                val category = dataSource.getCategoryById(id)

                return@withContext DataResult.Success(category)
            } catch (e: Exception) {
                throw e
                return@withContext DataResult.Error(e)
            }
        }
    }

    override fun getCategoriesByClassRoomId(classRoomId: Int): Flow<DataResult<List<Category>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val categories: List<Category> = dataSource.getCategoriesByClassRoomId(classRoomId)
                emit(DataResult.Success(categories))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getCategoryById(categoryId: Int): Flow<DataResult<Category?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val category: Category? = dataSource.getCategoryById(categoryId)
                if (category == null) {
                    emit(
                        DataResult.Error(
                            Exception("Category not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(category))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteCategory(category: Category) {
        withContext(dispatcher) {
            try {
                dataSource.deleteCategory(category)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertEvent(event: Event) {
        withContext(dispatcher) {
            try {
                dataSource.upsertEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val events: List<Event> = dataSource.getEventsByClassRoomId(classRoomId)
                if (events.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any event yet")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(events))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getEventsByCategoryId(categoryId: Int): Flow<DataResult<List<Event>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val events: List<Event> = dataSource.getEventsByCategoryId(categoryId)
                if (events.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any event yet")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(events))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getEventById(eventId: Int): Flow<DataResult<Event?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val event: Event? = dataSource.getEventById(eventId)
                if (event == null) {
                    emit(
                        DataResult.Error(
                            Exception("Event not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(event))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteEvent(event: Event) {
        withContext(dispatcher) {
            try {
                dataSource.deleteEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            try {
                dataSource.upsertAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessments: List<Assessment> =
                    dataSource.getAssessmentsByEventId(eventId)
                if (assessments.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any assessment yet")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(assessments))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getAssessmentById(assessmentId: Int): Flow<DataResult<Assessment?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessment: Assessment? = dataSource.getAssessmentById(assessmentId)
                if (assessment == null) {
                    emit(
                        DataResult.Error(
                            Exception("Assessment not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(assessment))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            try {
                dataSource.deleteAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

}