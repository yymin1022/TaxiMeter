package com.yong.taximeter.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firestore.v1.TargetOrBuilder
import com.yong.taximeter.R

object PermissionUtil {
    fun checkPermission(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    fun openAppInfo(context: Context) {
        Toast.makeText(context, context.getString(R.string.noti_toast_no_permission), Toast.LENGTH_LONG).show()
        val permissionIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:com.yong.taximeter")
        }
        context.startActivity(permissionIntent)
    }
}