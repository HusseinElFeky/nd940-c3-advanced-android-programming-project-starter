package com.udacity.loadapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.udacity.loadapp.R
import com.udacity.loadapp.models.ChannelDetails
import com.udacity.loadapp.models.DownloadStatus
import com.udacity.loadapp.ui.DetailActivity

object NotificationUtils {

    private const val CHANNEL_ID_DOWNLOADS = "downloads"
    private const val CHANNEL_NAME_DOWNLOADS = "Downloads"
    private const val REQUEST_CODE_DOWNLOADS = 1000

    fun getDownloadsChannel(context: Context): ChannelDetails {
        return ChannelDetails(
            CHANNEL_ID_DOWNLOADS,
            CHANNEL_NAME_DOWNLOADS,
            context.getString(R.string.channel_downloads_description),
            NotificationManager.IMPORTANCE_HIGH,
            NotificationCompat.PRIORITY_HIGH,
            NotificationCompat.VISIBILITY_PUBLIC
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelDetails: ChannelDetails) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationChannel(
            channelDetails.id,
            channelDetails.name,
            channelDetails.importance
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
            lightColor = context.getColor(R.color.colorAccent)
            description = channelDetails.description
            lockscreenVisibility = channelDetails.visibility
            notificationManager.createNotificationChannel(this)
        }
    }

    fun sendDownloadNotification(
        context: Context,
        fileName: String,
        downloadStatus: DownloadStatus,
        downloadId: Int
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(context, DetailActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        notifyIntent.putExtras(DetailActivity.withExtras(fileName, downloadStatus, downloadId))

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_DOWNLOADS,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val downloadsChannel = getDownloadsChannel(context)

        val notification = NotificationCompat.Builder(context, downloadsChannel.id)
            .setContentTitle(fileName)
            .setContentText(
                if (downloadStatus == DownloadStatus.SUCCESS) {
                    context.getString(R.string.download_complete)
                } else {
                    context.getString(R.string.download_failed)
                }
            )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setAutoCancel(true)
            .setColor(context.getColor(R.color.colorAccent))
            .setLights(context.getColor(R.color.colorAccent), 1000, 3000)
            .setVisibility(downloadsChannel.visibility)
            .setPriority(downloadsChannel.priority)
            .addAction(
                NotificationCompat.Action(
                    null,
                    context.getString(R.string.view_details),
                    pendingIntent
                )
            )
            .build()

        notificationManager.notify(downloadId, notification)
    }

    fun clearNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(notificationId)
    }
}
