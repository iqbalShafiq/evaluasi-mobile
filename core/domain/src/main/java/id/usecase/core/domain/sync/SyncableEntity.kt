package id.usecase.core.domain.sync

interface SyncableEntity {
    val id: Int
    val lastModifiedTime: Long
    fun toNetworkModel(): Any
}