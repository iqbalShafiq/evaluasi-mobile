package id.usecase.core.database.entities

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ClassRoomEntity : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var subject: String = ""
    var studentEntities: RealmSet<StudentEntity> = realmSetOf()
    var startPeriod: RealmInstant = RealmInstant.now()
    var endPeriod: RealmInstant? = null
    var createdTime: RealmInstant = RealmInstant.now()
    var lastModifiedTime: RealmInstant = RealmInstant.now()
}