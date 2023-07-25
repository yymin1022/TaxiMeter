package com.yong.taximeter.fragment

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
import com.yong.taximeter.R
import com.yong.taximeter.activity.MeterActivity
import com.yong.taximeter.util.Util

class MainHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseView = inflater.inflate(R.layout.fragment_main_home, container, false)
        val btnOpenMeter = baseView.findViewById<Button>(R.id.btn_home_openmeter)
        btnOpenMeter.setOnClickListener { startActivity(Intent(context, MeterActivity::class.java)) }

        val imageTaxi = baseView.findViewById<ImageView>(R.id.image_home_taxi)
        imageTaxi.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_main_taxi_light
        ))
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Util.isUsingNightModeResources(requireContext())) {
            imageTaxi.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_main_taxi_dark
            ))
        }

        return baseView
    }
}