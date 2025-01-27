package id.usecase.core.presentation.ui

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

fun String.isCurrentMonth(): Boolean {
    return try {
        // Convert timestamp string to Long and then to LocalDateTime
        val instant = Instant.ofEpochMilli(this.toLong())
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val currentMonth = YearMonth.now()
        YearMonth.from(date) == currentMonth
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun String.isLastMonth(): Boolean {
    return try {
        // Convert timestamp string to Long and then to LocalDateTime
        val instant = Instant.ofEpochMilli(this.toLong())
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val lastMonth = YearMonth.now().minusMonths(1)
        YearMonth.from(date) == lastMonth
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun String.isOlderThanLastMonth(): Boolean {
    return try {
        val instant = when {
            // Handle timestamp in seconds (10 digits)
            this.all { it.isDigit() } && this.length <= 10 -> {
                Instant.ofEpochSecond(this.toLong())
            }
            // Handle timestamp in milliseconds (13 digits)
            this.all { it.isDigit() } -> {
                Instant.ofEpochMilli(this.toLong())
            }
            // Handle formatted date string
            else -> {
                LocalDateTime.parse(
                    this,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ).atZone(ZoneId.systemDefault()).toInstant()
            }
        }

        val date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        val lastMonth = YearMonth.now().minusMonths(1)
        YearMonth.from(date).isBefore(lastMonth)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun formatDate(dateString: String): String {
    return try {
        val instant = when {
            // Handle timestamp
            dateString.all { it.isDigit() } -> {
                val timestamp = dateString.toLong()
                when {
                    // If timestamp length is around 10 (seconds)
                    dateString.length <= 10 -> Instant.ofEpochSecond(timestamp)
                    // If timestamp length is around 13 (milliseconds)
                    else -> Instant.ofEpochMilli(timestamp)
                }
            }
            // Handle formatted date string
            else -> {
                LocalDateTime.parse(
                    dateString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ).atZone(ZoneId.systemDefault()).toInstant()
            }
        }

        instant.atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun formatTimeAgo(dateString: String): String {
    return try {
        val instant = when {
            // Handle timestamp
            dateString.all { it.isDigit() } -> {
                Instant.ofEpochMilli(dateString.toLong())
            }
            // Handle formatted date string
            else -> {
                LocalDateTime.parse(
                    dateString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ).atZone(ZoneId.systemDefault()).toInstant()
            }
        }

        val now = Instant.now()
        val diff = Duration.between(instant, now)

        when {
            diff.seconds < 60 -> "Just now"
            diff.toMinutes() < 60 -> "${diff.toMinutes()}m ago"
            diff.toHours() < 24 -> "${diff.toHours()}h ago"
            diff.toDays() < 7 -> "${diff.toDays()}d ago"
            diff.toDays() < 30 -> "${diff.toDays() / 7}w ago"
            diff.toDays() < 365 -> "${diff.toDays() / 30}mo ago"
            else -> "${diff.toDays() / 365}y ago"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}
