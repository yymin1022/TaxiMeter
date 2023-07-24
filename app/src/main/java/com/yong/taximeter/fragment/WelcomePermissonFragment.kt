package com.yong.taximeter.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.yong.taximeter.R

class WelcomePermissonFragment : Fragment() {
    private lateinit var btnGrantLocation: TextView
    private lateinit var btnGrantNotification: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_permisson, container, false)
        btnGrantLocation = view.findViewById(R.id.tv_welcome_permission_grant_location)
        btnGrantNotification = view.findViewById(R.id.tv_welcome_permission_grant_notification)

        return view
    }

    private fun checkLocationPermission(context: Context): Boolean {
        if(Build.VERSION.SDK_INT >= 29) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        if(Build.VERSION.SDK_INT >= 33) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        
        return true
    }
}