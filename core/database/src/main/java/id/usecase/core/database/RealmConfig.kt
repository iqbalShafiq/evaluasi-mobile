package id.usecase.core.database

import id.usecase.core.database.entities.AssessmentCategoryEntity
import id.usecase.core.database.entities.AssessmentEntity
import id.usecase.core.database.entities.AssessmentEventEntity
import id.usecase.core.database.entities.ClassRoomEntity
import id.usecase.core.database.entities.StudentEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmConfig {
    private val config = RealmConfiguration.create(
        schema = setOf(
            ClassRoomEntity::class,
            StudentEntity::class,
            AssessmentEntity::class,
            AssessmentEventEntity::class,
            AssessmentCategoryEntity::class
        )
    )

    private var realm: Realm? = null

    fun getRealm(): Realm {
        if (realm != null) return realm!!
        else {
            realm = Realm.open(config)
            return realm!!
        }
    }
}