package com.yong.taximeter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.yong.taximeter.R

class WelcomeLocationFragment : Fragment() {
    private lateinit var btnNext: TextView
    private lateinit var radioLocation1: RadioGroup
    private lateinit var radioLocation2: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_location, container, false)
        btnNext = view.findViewById(R.id.tv_welcome_location_next)
        btnNext.setOnClickListener(btnListener)

        radioLocation1 = view.findViewById(R.id.radio_welcome_location_1)
        radioLocation2 = view.findViewById(R.id.radio_welcome_location_2)
        radioLocation1.setOnCheckedChangeListener(radioListener1)
        radioLocation2.setOnCheckedChangeListener(radioListener2)

        val radioSeoul = view.findViewById<RadioButton>(R.id.radio_location_seoul)
        radioSeoul.isChecked = true

        return view
    }

    private val btnListener = View.OnClickListener { view ->
        if(view.id == R.id.tv_welcome_location_next) {
            val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val prefEditor = pref.edit()
            prefEditor.putBoolean("welcome", true)
            prefEditor.apply()
            requireActivity().finish()
        }
    }

    private val radioListener1 = RadioGroup.OnCheckedChangeListener { radioGroup, checkedID ->
        if(checkedID > 0) {
            val checkedRadio = radioGroup.findViewById<RadioButton>(checkedID)
            if(checkedRadio.isChecked) {
                radioLocation2.clearCheck()
                updatePrefs(checkedID)
            }
        }
    }

    private val radioListener2 = RadioGroup.OnCheckedChangeListener { radioGroup, checkedID ->
        if(checkedID > 0) {
            val checkedRadio = radioGroup.findViewById<RadioButton>(checkedID)
            if(checkedRadio.isChecked) {
                radioLocation1.clearCheck()
                updatePrefs(checkedID)
            }
        }
    }
    
    private fun updatePrefs(checkedID: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val prefEditor = pref.edit()
        
        var selectedLocation = "seoul"
        when(checkedID) {
                R.id.radio_location_busan -> selectedLocation = "busan"
                R.id.radio_location_chungbuk -> selectedLocation = "chungbuk"
                R.id.radio_location_chungnam -> selectedLocation = "chungnam"
                R.id.radio_location_daegu -> selectedLocation = "daegu"
                R.id.radio_location_daejeon -> selectedLocation = "daejeon"
                R.id.radio_location_gangwon -> selectedLocation = "gangwon"
                R.id.radio_location_gwangju -> selectedLocation = "gwangju"
                R.id.radio_location_gyeongbuk -> selectedLocation = "gyeongbuk"
                R.id.radio_location_gyeonggi -> selectedLocation = "gyeonggi"
                R.id.radio_location_gyeongnam -> selectedLocation = "gyeongnam"
                R.id.radio_location_incheon -> selectedLocation = "incheon"
                R.id.radio_location_jeju -> selectedLocation = "jeju"
                R.id.radio_location_jeonbuk -> selectedLocation = "jeonbuk"
                R.id.radio_location_jeonnam -> selectedLocation = "jeonnam"
                R.id.radio_location_ulsan -> selectedLocation = "ulsan"
                R.id.radio_location_seoul -> selectedLocation = "seoul"
        }

        prefEditor.putString("pref_location", selectedLocation)
        prefEditor.putString("pref_theme", "horse")
        prefEditor.apply()
    }
}