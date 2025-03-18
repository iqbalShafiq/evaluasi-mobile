package id.usecase.core.domain.sync

interface SyncableEntity {
    val id: String
    val lastModifiedTime: Long
    fun toNetworkModel(): Any
}