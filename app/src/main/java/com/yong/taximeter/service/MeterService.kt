package com.yong.taximeter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.yong.taximeter.R
import com.yong.taximeter.activity.MeterActivity
import com.yong.taximeter.util.MeterStatus
import com.yong.taximeter.util.MeterUtil
import com.yong.taximeter.util.PermissionUtil
import kotlin.math.roundToInt

class MeterService: Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!PermissionUtil.checkLocationPermission(this)){
            PermissionUtil.openAppInfo(this)
            stopSelf()
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 0f, this)
        MeterUtil.status = MeterStatus.GPS_UNSTABLE

        initNotification()
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent("UPDATE_METER"))

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        MeterUtil.resetValues()
        locationManager.removeUpdates(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onLocationChanged(location: Location) {
        val curSpeed = location.speed.roundToInt()
        MeterUtil.increaseCost(curSpeed)

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent("UPDATE_METER"))
        initNotification()
    }

    private fun initNotification() {
        val notiIntent = Intent(this, MeterActivity::class.java)
        notiIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()
        notificationBuilder = NotificationCompat.Builder(this, getString(R.string.noti_channel_id))
            .setSmallIcon(R.drawable.ic_noti_taxi)
            .setContentTitle(getString(R.string.noti_service_title))
            .setContentText(String.format(getString(R.string.noti_service_content), MeterUtil.cost, MeterUtil.speed * 3.6, MeterUtil.distance / 1000))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)

        startForeground(1022, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.noti_channel_title)
            val descriptionText = getString(R.string.noti_channel_description)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel(getString(R.string.noti_channel_id), name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}