package com.yong.taximeter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseView = inflater.inflate(R.layout.fragment_main_home, container, false)
        val btnOpenMeter = baseView.findViewById<Button>(R.id.btn_home_openmeter)
        btnOpenMeter.setOnClickListener { startActivity(Intent(context, MeterActivity::class.java)) }

        return baseView
    }
}