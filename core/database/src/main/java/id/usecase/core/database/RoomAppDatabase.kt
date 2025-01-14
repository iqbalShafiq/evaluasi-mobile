package id.usecase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.usecase.core.database.dao.AssessmentDao
import id.usecase.core.database.dao.CategoryDao
import id.usecase.core.database.dao.ClassRoomDao
import id.usecase.core.database.dao.EventDao
import id.usecase.core.database.dao.StudentDao
import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.CategoryEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.EventEntity
import id.usecase.core.database.entities.StudentEntity
import id.usecase.core.database.type_converters.IntListConverter

@Database(
    entities = [
        AssessmentEntity::class,
        CategoryEntity::class,
        EventEntity::class,
        StudentEntity::class,
        ClassRoomEntity::class
    ],
    version = 1
)
@TypeConverters(IntListConverter::class)
abstract class RoomAppDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun studentDao(): StudentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun classRoomDao(): ClassRoomDao
}