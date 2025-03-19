package id.usecase.core.domain.utils

import java.util.UUID

fun generateEntityId(prefix: String): String {
    return "$prefix-${UUID.randomUUID()}"
}

// Constants for prefixes
object EntityPrefix {
    const val CLASS_ROOM = "CR"
    const val STUDENT = "ST"
    const val CATEGORY = "CT"
    const val SECTION = "SC"
    const val EVENT = "EV"
    const val ASSESSMENT = "AS"
    const val EVENT_SECTION = "ES"
}