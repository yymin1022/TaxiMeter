package com.yong.taximeter.fragment

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.yong.taximeter.R
import com.yong.taximeter.util.PermissionUtil

class WelcomePermissonFragment : Fragment() {
    private lateinit var btnGrantLocation: TextView
    private lateinit var btnGrantNotification: TextView
    private lateinit var btnToNext: TextView
    private var isLocationGranted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_permisson, container, false)
        btnGrantLocation = view.findViewById(R.id.tv_welcome_permission_grant_location)
        btnGrantNotification = view.findViewById(R.id.tv_welcome_permission_grant_notification)
        btnToNext = view.findViewById(R.id.tv_welcome_permission_next)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateView(requireContext())
    }

    private val requestLocationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if(PermissionUtil.checkLocationPermission(requireContext())) {
            isLocationGranted = true
            updateView(requireContext())
        } else {
            PermissionUtil.openAppInfo(requireContext())
        }
    }

    private val requestNotificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(PermissionUtil.checkNotificationPermission(requireContext())) {
            updateView(requireContext())
        } else {
            Toast.makeText(requireContext(), getString(R.string.noti_toast_no_notification), Toast.LENGTH_SHORT).show()
        }
    }

    private val btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.tv_welcome_permission_grant_location -> {
                requestLocationPermission.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
            }

            R.id.tv_welcome_permission_grant_notification -> {
                if(Build.VERSION.SDK_INT >= 33) {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            R.id.tv_welcome_permission_next -> {
                parentFragmentManager.beginTransaction().replace(R.id.layout_welcome_fragment, WelcomeLocationFragment()).commitAllowingStateLoss()
            }
        }
    }

    private fun updateView(context: Context) {
        if(PermissionUtil.checkLocationPermission(context)) {
            btnGrantLocation.isEnabled = false
            btnToNext.isEnabled = true
            btnToNext.setOnClickListener(btnListener)
        } else {
            btnGrantLocation.isEnabled = true
            btnGrantLocation.setOnClickListener(btnListener)
        }

        if(PermissionUtil.checkNotificationPermission(context)) {
            btnGrantNotification.isEnabled = false
        } else {
            btnGrantNotification.isEnabled = true
            btnGrantNotification.setOnClickListener(btnListener)
        }
    }
}