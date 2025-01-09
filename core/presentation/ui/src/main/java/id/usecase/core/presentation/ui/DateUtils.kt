package id.usecase.core.presentation.ui

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

fun String.isCurrentMonth(): Boolean {
    val date = this.toLocalDateTime()
    val currentMonth = YearMonth.now()
    return YearMonth.from(date) == currentMonth
}

fun String.isLastMonth(): Boolean {
    val date = this.toLocalDateTime()
    val lastMonth = YearMonth.now().minusMonths(1)
    return YearMonth.from(date) == lastMonth
}

fun String.isOlderThanLastMonth(): Boolean {
    val date = this.toLocalDateTime()
    val lastMonth = YearMonth.now().minusMonths(1)
    return YearMonth.from(date).isBefore(lastMonth)
}

fun formatDate(dateString: String): String {
    // Implement date formatting logic
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}

fun formatTimeAgo(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: return dateString
        val now = System.currentTimeMillis()
        val diff = now - date.time

        when {
            diff < 1000 * 60 -> "Just now"
            diff < 1000 * 60 * 60 -> "${diff / (1000 * 60)}m ago"
            diff < 1000 * 60 * 60 * 24 -> "${diff / (1000 * 60 * 60)}h ago"
            else -> "${diff / (1000 * 60 * 60 * 24)}d ago"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}
