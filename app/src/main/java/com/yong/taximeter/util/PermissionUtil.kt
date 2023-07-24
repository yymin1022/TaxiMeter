package com.yong.taximeter.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yong.taximeter.R

object PermissionUtil {
    fun openAppInfo(context: Context) {
        Toast.makeText(context, context.getString(R.string.noti_toast_no_permission), Toast.LENGTH_LONG).show()
        val permissionIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:com.yong.taximeter")
        }
        context.startActivity(permissionIntent)
    }

    fun checkLocationPermission(context: Context): Boolean {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    fun checkNotificationPermission(context: Context): Boolean {
        if(Build.VERSION.SDK_INT >= 33) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }
}