package com.example.push_notification_android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.push_notification_android"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    //generate the notification
    //attach the notification created with the custom layout
    //show the notification


    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    fun generateNotification(title: String, msg: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        //channel id, channel name
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId).setSmallIcon(R.drawable.meta)
                .setAutoCancel(true).setVibrate(
                    longArrayOf(1000, 1000, 1000, 1000)
                ).setOnlyAlertOnce(true).setContentIntent(pendingIntent)

        builder = builder.setContent(
            getRemoteView(title, msg)
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        notificationManager.notify(0, builder.build())


    }

    private fun getRemoteView(title: String, msg: String): RemoteViews? {
        val remoteView = RemoteViews("com.example.push_notification_android", R.layout.notification)
        remoteView.setTextViewText(
            R.id.tvTitle, title
        )
        remoteView.setTextViewText(
            R.id.tvMsg, msg
        )
        remoteView.setImageViewResource(
            R.id.img, R.drawable.meta
        )
        return remoteView
    }
}