package id.usecase.core.domain.assessment.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toFormattedDate(): String {
    val instant = Instant.ofEpochSecond(this)
    val formatter = DateTimeFormatter
        .ofPattern("dd MMMM yyyy HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    val formattedDate = formatter.format(instant)

    return formattedDate
}