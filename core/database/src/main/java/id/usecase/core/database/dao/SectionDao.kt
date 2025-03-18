package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import id.usecase.core.database.entities.SectionEntity

@Dao
interface SectionDao {

    @Transaction
    suspend fun upsertAndGetId(section: List<SectionEntity>): List<String> {
        upsert(section)
        return section.map { it.id }
    }

    @Upsert
    suspend fun upsert(section: List<SectionEntity>)

    @Query("SELECT * FROM sections WHERE id = :id")
    suspend fun getSectionById(id: String): SectionEntity?

    @Query("SELECT * FROM sections WHERE id = :sectionIds")
    suspend fun getSectionsByIds(sectionIds: List<String>): List<SectionEntity>

    @Query("SELECT * FROM sections WHERE class_room_id = :classRoomId")
    suspend fun getSectionsByClassRoomId(classRoomId: String): List<SectionEntity>

    @Query("SELECT * FROM sections WHERE id IN (SELECT sectionId FROM event_section_cross_ref WHERE eventId = :eventId)")
    suspend fun getSelectedSectionOnAssessment(eventId: String): List<SectionEntity>

    @Delete
    suspend fun delete(section: SectionEntity)
}