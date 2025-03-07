package id.usecase.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import id.usecase.core.database.model.analytics.CategoryAnalysis
import id.usecase.core.database.model.analytics.CategoryScore
import id.usecase.core.database.model.analytics.LowPerformanceAlert
import id.usecase.core.database.model.analytics.MonthlyScore
import id.usecase.core.database.model.analytics.PerformanceScore
import id.usecase.core.database.model.analytics.SectionScore
import id.usecase.core.database.model.analytics.SectionUsage
import id.usecase.core.database.model.analytics.StudentProgress

@Dao
interface AnalyticsDao {
    // For Performance Trend
    @Query(
        """
        SELECT 
            strftime('%m', datetime(events.event_date/1000, 'unixepoch')) as month,
            AVG(COALESCE(assessments.score, 0.0)) as average_score
        FROM events 
        LEFT JOIN assessments ON events.id = assessments.event_id
        WHERE events.category_id IN (
            SELECT id FROM categories WHERE class_room_id = :classRoomId
        )
        GROUP BY month
        ORDER BY month
    """
    )
    suspend fun getPerformanceTrendByClassRoom(classRoomId: Int): List<MonthlyScore>

    // For Category Distribution
    @Query(
        """
        SELECT 
            c.name as category_name,
            COUNT(a.id) as total_assessments
        FROM categories c
        LEFT JOIN events e ON c.id = e.category_id
        LEFT JOIN assessments a ON e.id = a.event_id
        WHERE c.class_room_id = :classRoomId
        GROUP BY c.id, c.name
        ORDER BY c.percentage ASC
    """
    )
    suspend fun getCategoryDistributionByClassRoom(classRoomId: Int): List<CategoryScore>

    // For Performance Distribution
    @Query(
        """
        SELECT 
            CASE 
                WHEN score < 50 THEN 'Poor'
                WHEN score >= 50 AND score < 70 THEN 'Bad'
                WHEN score >= 70 AND score < 85 THEN 'Good'
                ELSE 'Great'
            END as performance_level,
            AVG(score) as average_score
        FROM assessments
        WHERE event_id IN (
            SELECT id FROM events WHERE category_id IN (
                SELECT id FROM categories WHERE class_room_id = :classRoomId
            )
        )
        AND score IS NOT NULL
        GROUP BY performance_level
    """
    )
    suspend fun getPerformanceDistributionByClassRoom(classRoomId: Int): List<PerformanceScore>

    // For Student Progress
    @Query(
        """
        SELECT 
            s.name as student_name,
            AVG(COALESCE(a.score, 0.0)) as progress_percentage,
            MAX(a.created_time) as last_updated
        FROM students s
        LEFT JOIN assessments a ON s.id = a.student_id
        WHERE a.event_id IN (
            SELECT id FROM events WHERE category_id IN (
                SELECT id FROM categories WHERE class_room_id = :classRoomId
            )
        )
        GROUP BY s.id, s.name
    """
    )
    suspend fun getStudentProgressByClassRoom(classRoomId: Int): List<StudentProgress>

    // For Category Analysis
    @Query(
        """
        SELECT 
            c.name as category_name,
            AVG(COALESCE(a.score, 0.0)) as average_score
        FROM categories c
        LEFT JOIN events e ON c.id = e.category_id
        LEFT JOIN assessments a ON e.id = a.event_id
        WHERE c.class_room_id = :classRoomId
        GROUP BY c.id, c.name
        ORDER BY c.percentage ASC
    """
    )
    suspend fun getCategoryAnalysisByClassRoom(classRoomId: Int): List<CategoryAnalysis>

    // For Low Performance Students
    @Query(
        """
        SELECT 
            s.name as student_name,
            s.identifier as student_identifier,
            AVG(COALESCE(a.score, 0.0)) as average_score,
            MAX(a.created_time) as last_updated
        FROM students s
        LEFT JOIN assessments a ON s.id = a.student_id
        WHERE a.event_id IN (
            SELECT id FROM events WHERE category_id IN (
                SELECT id FROM categories WHERE class_room_id = :classRoomId
            )
        )
        GROUP BY s.id, s.name
        HAVING average_score < 50
    """
    )
    suspend fun getLowPerformanceStudentsByClassRoomId(classRoomId: Int): List<LowPerformanceAlert>

    // Section score distribution by class room
    @Query(
        """
        SELECT
            s.name as section_name,
            AVG(COALESCE(a.score, 0.0)) as average_score
        FROM sections s
        LEFT JOIN event_section_cross_ref es ON s.id = es.sectionId
        LEFT JOIN assessments a ON es.eventId = a.event_id
        WHERE s.class_room_id = :classRoomId
        GROUP BY s.id, s.name
    """
    )
    suspend fun getSectionScoreDistributionByClassRoomId(classRoomId: Int): List<SectionScore>

    // Section usage by class room
    @Query(
        """
        SELECT
            s.name as section_name,
            (COUNT(DISTINCT a.id) * 100.0 / (SELECT COUNT(*) FROM events WHERE class_room_id = :classRoomId)) as total_assessments
        FROM sections s
        LEFT JOIN event_section_cross_ref es ON s.id = es.sectionId
        LEFT JOIN events a ON es.eventId = a.id
        WHERE s.class_room_id = :classRoomId
    GROUP BY s.id, s.name
    """
    )
    suspend fun getSectionUsageByClassRoomId(classRoomId: Int): List<SectionUsage>
}