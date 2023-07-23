package com.yong.taximeter.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.yong.taximeter.R
import com.yong.taximeter.activity.MeterActivity
import com.yong.taximeter.util.MeterUtil
import kotlin.math.roundToInt

class MeterService: Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Error", Toast.LENGTH_SHORT).show()
            return super.onStartCommand(intent, flags, startId)
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0f, this)

        initNotification()
        MeterUtil.init(this)

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
    }

    private fun initNotification() {
        val notiIntent = Intent(this, MeterActivity::class.java)
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()
        notificationBuilder = NotificationCompat.Builder(this, "CHANNEL")
            .setSmallIcon(R.drawable.ic_horse_1)
            .setContentTitle("Taxi Meter")
            .setContentText("Taxi Meter is Running")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)

        startForeground(1022, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CHANNEL"
            val descriptionText = "CHANNEL DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel("CHANNEL", name, importance).apply {
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