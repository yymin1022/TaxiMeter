package com.yong.taximeter.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.yong.taximeter.util.MeterUtil

class MeterService: Service(), LocationListener {
    private lateinit var locationManager: LocationManager

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

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        val curSpeed = location.speed.toInt()
        MeterUtil.increaseCost(curSpeed)

        val intent = Intent("UPDATE_METER")
        sendBroadcast(intent)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}