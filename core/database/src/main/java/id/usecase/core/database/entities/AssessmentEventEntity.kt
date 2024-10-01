package id.usecase.core.database.entities

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class AssessmentEventEntity : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var eventDate: RealmInstant = RealmInstant.now()
    var categoryEntity: AssessmentCategoryEntity? = null
    var createdTime: RealmInstant = RealmInstant.now()
    var lastModifiedTime: RealmInstant = RealmInstant.now()
}