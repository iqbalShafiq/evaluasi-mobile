package id.usecase.core.database

import io.realm.kotlin.types.RealmInstant
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset

fun String.toObjectId() = ObjectId(hexString = this)

fun LocalDateTime.toRealmInstant() = RealmInstant.from(
    epochSeconds = this.toEpochSecond(ZoneOffset.UTC),
    nanosecondAdjustment = this.nano
)

fun RealmInstant.toJavaInstant(): LocalDateTime = LocalDateTime.ofEpochSecond(
    this.epochSeconds,
    this.nanosecondsOfSecond,
    ZoneOffset.UTC
)