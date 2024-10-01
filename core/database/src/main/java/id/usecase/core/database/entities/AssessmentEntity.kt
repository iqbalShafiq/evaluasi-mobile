package id.usecase.core.database.entities

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class AssessmentEntity: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var studentEntity: StudentEntity? = null
    var eventEntity: AssessmentEventEntity? = null
    var score: Double? = null
    var createdTime: RealmInstant = RealmInstant.now()
    var lastModifiedTime: RealmInstant = RealmInstant.now()
}