package id.usecase.core.domain.sync

object EntityDependencyManager {
    // Higher number = higher priority
    private val priorityMap = mapOf(
        EntityType.CLASS_ROOM to 5,
        EntityType.CATEGORY to 4,
        EntityType.STUDENT to 4,
        EntityType.SECTION to 3,
        EntityType.EVENT to 2,
        EntityType.ASSESSMENT to 1,
        EntityType.EVENT_SECTION to 1
    )

    // Dependencies (what needs to be synced first)
    private val dependencyMap = mapOf(
        EntityType.CATEGORY to listOf(EntityType.CLASS_ROOM),
        EntityType.STUDENT to listOf(EntityType.CLASS_ROOM),
        EntityType.SECTION to listOf(EntityType.CLASS_ROOM),
        EntityType.EVENT to listOf(EntityType.CATEGORY),
        EntityType.ASSESSMENT to listOf(EntityType.STUDENT, EntityType.EVENT),
        EntityType.EVENT_SECTION to listOf(EntityType.EVENT, EntityType.SECTION)
    )

    fun getPriority(entityType: EntityType): Int {
        return priorityMap[entityType] ?: 0
    }

    fun getDependencies(entityType: EntityType): List<EntityType> {
        return dependencyMap[entityType] ?: emptyList()
    }
}