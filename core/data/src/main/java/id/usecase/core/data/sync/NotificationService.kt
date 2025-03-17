package id.usecase.core.data.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import id.usecase.core.data.R

/**
 * Service to handle synchronization notifications
 */
class NotificationService(private val context: Context) {

    companion object {
        private const val SYNC_CHANNEL_ID = "sync_notification_channel"
        private const val SYNC_NOTIFICATION_ID = 1001
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createSyncNotificationChannel()
    }

    private fun createSyncNotificationChannel() {
        val channel = NotificationChannel(
            SYNC_CHANNEL_ID,
            "Sinkronisasi Data",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifikasi proses sinkronisasi data"
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Show a synchronization notification
     * @param isSyncing Whether synchronization is in progress
     * @param itemCount Number of items being synchronized
     */
    fun showSyncNotification(isSyncing: Boolean, itemCount: Int = 0) {
        val activityClass = try {
            Class.forName("com.example.evaluasimobile.MainActivity")
        } catch (e: ClassNotFoundException) {
            null
        }

        val pendingIntent = if (activityClass != null) {
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, activityClass),
                PendingIntent.FLAG_IMMUTABLE
            )
        } else null

        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(R.drawable.evaluasi_icon) // Make sure this icon exists in your resources
            .setContentTitle(if (isSyncing) "Sinkronisasi Sedang Berlangsung" else "Sinkronisasi Selesai")
            .setContentText(
                when {
                    isSyncing && itemCount > 0 -> "Sedang mengupload $itemCount data"
                    isSyncing -> "Sedang menyinkronkan data ke server"
                    else -> "Data berhasil disinkronkan"
                }
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isSyncing)
            .setAutoCancel(!isSyncing)

        pendingIntent?.let { notification.setContentIntent(it) }

        notificationManager.notify(SYNC_NOTIFICATION_ID, notification.build())
    }

    /**
     * Cancel the synchronization notification
     */
    fun cancelSyncNotification() {
        notificationManager.cancel(SYNC_NOTIFICATION_ID)
    }
}