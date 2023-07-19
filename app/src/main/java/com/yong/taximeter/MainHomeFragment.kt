package com.yong.taximeter

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources

class MainHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseView = inflater.inflate(R.layout.fragment_main_home, container, false)
        val btnOpenMeter = baseView.findViewById<Button>(R.id.btn_home_openmeter)
        btnOpenMeter.setOnClickListener { startActivity(Intent(context, MeterActivity::class.java)) }

        val imageTaxi = baseView.findViewById<ImageView>(R.id.image_home_taxi)
        imageTaxi.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_main_taxi_light))
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isUsingNightModeResources()) {
            imageTaxi.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_main_taxi_dark))
        }

        return baseView
    }

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }
}