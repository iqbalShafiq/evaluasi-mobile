package id.usecase.assessment.data

import id.usecase.assessment.domain.AssessmentRepository
import id.usecase.core.domain.assessment.DataResult
import id.usecase.core.domain.assessment.LocalAssessmentDataSource
import id.usecase.core.domain.assessment.model.assessment.Assessment
import id.usecase.core.domain.assessment.model.assessment.category.Category
import id.usecase.core.domain.assessment.model.assessment.event.Event
import id.usecase.core.domain.assessment.model.classroom.ClassRoom
import id.usecase.core.domain.assessment.model.student.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AssessmentRepositoryImpl(
    private val assessmentDataSource: LocalAssessmentDataSource,
    private val dispatcher: CoroutineDispatcher
) : AssessmentRepository {
    override suspend fun upsertClassRoom(classRoom: ClassRoom) {
        try {
            assessmentDataSource.upsertClassRoom(classRoom)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getClassRooms(): Flow<DataResult<List<ClassRoom>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val classRooms: List<ClassRoom> = assessmentDataSource.getClassRooms()
                if (classRooms.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any class room yet")
                        )
                    )
                    return@flow
                }

                emit(DataResult.Success(classRooms))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override fun getClassRoomById(classRoomId: Int): Flow<DataResult<ClassRoom?>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val classRoom: ClassRoom? = assessmentDataSource.getClassRoomById(classRoomId)
                if (classRoom == null) {
                    emit(
                        DataResult.Error(
                            Exception("Class room not found")
                        )
                    )
                    return@flow
                }
                emit(DataResult.Success(classRoom))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun deleteClassRoom(classRoom: ClassRoom) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.deleteClassRoom(classRoom)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun insertStudent(students: Student) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.insertStudent(students)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun insertStudents(students: List<Student>) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.insertStudents(students)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getStudentsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Student>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val students: List<Student> = assessmentDataSource.getStudentsByClassRoomId(classRoomId)
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
                assessmentDataSource.deleteStudent(student)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertCategory(category: Category) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.upsertCategory(category)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getCategoriesByClassRoomId(classRoomId: Int): Flow<DataResult<List<Category>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val categories: List<Category> = assessmentDataSource.getCategoriesByClassRoomId(classRoomId)
                if (categories.isEmpty()) {
                    emit(
                        DataResult.Error(
                            Exception("Haven't create any category yet")
                        )
                    )
                    return@flow
                }
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
                val category: Category? = assessmentDataSource.getCategoryById(categoryId)
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
                assessmentDataSource.deleteCategory(category)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertEvent(event: Event) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.upsertEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getEventsByClassRoomId(classRoomId: Int): Flow<DataResult<List<Event>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val events: List<Event> = assessmentDataSource.getEventsByClassRoomId(classRoomId)
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
                val events: List<Event> = assessmentDataSource.getEventsByCategoryId(categoryId)
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
                val event: Event? = assessmentDataSource.getEventById(eventId)
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
                assessmentDataSource.deleteEvent(event)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertAssessment(assessment: Assessment) {
        withContext(dispatcher) {
            try {
                assessmentDataSource.upsertAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAssessmentsByEventId(eventId: Int): Flow<DataResult<List<Assessment>>> {
        return flow {
            try {
                emit(DataResult.Loading)
                val assessments: List<Assessment> = assessmentDataSource.getAssessmentsByEventId(eventId)
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
                val assessment: Assessment? = assessmentDataSource.getAssessmentById(assessmentId)
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
                assessmentDataSource.deleteAssessment(assessment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

}