package com.yong.taximeter.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yong.taximeter.R

const val PREF_KEY_LOCATION = "pref_location"
const val PREF_KEY_THEME = "pref_theme"
const val PREF_KEY_INFO_COST = "pref_info_cost"
const val PREF_KEY_INFO_VERSION = "pref_info_version"
class MainSettingFragment : PreferenceFragmentCompat() {
    private lateinit var pref: SharedPreferences
    private lateinit var arrLocationKey: Array<String>
    private lateinit var arrLocationValue: Array<String>
    private lateinit var arrThemeKey: Array<String>
    private lateinit var arrThemeValue: Array<String>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_items)

        arrLocationKey = resources.getStringArray(R.array.pref_location)
        arrLocationValue = resources.getStringArray(R.array.pref_location_value)
        arrThemeKey = resources.getStringArray(R.array.pref_theme)
        arrThemeValue = resources.getStringArray(R.array.pref_theme_value)

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pref.registerOnSharedPreferenceChangeListener(prefListener)

        initSummary()
    }

    private var prefListener = OnSharedPreferenceChangeListener {
        sharedPreferences, key ->
            when(key) {
                PREF_KEY_LOCATION -> {
                    val locationValue = sharedPreferences.getString(key, "seoul")!!
                    val locationKey = arrLocationKey[arrLocationValue.indexOf(locationValue)]
                    updateCostInfo(locationValue)
                    updateSummary(key, locationKey)
                }

                PREF_KEY_THEME -> {
                    updateSummary(key, arrThemeKey[arrThemeValue.indexOf(sharedPreferences.getString(key, "horse"))])
                }
            }
        }

    private fun initSummary() {
        val locationValue = pref.getString(PREF_KEY_LOCATION, "seoul")!!
        val locationKey = arrLocationKey[arrLocationValue.indexOf(locationValue)]
        updateCostInfo(locationValue)
        updateSummary(PREF_KEY_LOCATION, locationKey)
        updateSummary(
            PREF_KEY_THEME, arrThemeKey[arrThemeValue.indexOf(pref.getString(
                PREF_KEY_THEME, "horse"))])
        updateSummary(PREF_KEY_INFO_VERSION, pref.getString(PREF_KEY_INFO_VERSION, "20001022")!!)
    }

    private fun updateCostInfo(location: String) {
        val pref = requireContext().getSharedPreferences("pref_cost_${location}", Context.MODE_PRIVATE)!!

        val costBase = pref.getInt("cost_base", 0)
        val costRunPer = pref.getInt("cost_run_per", 131)
        val costTimePer = pref.getInt("cost_time_per", 30)
        val distBase = pref.getInt("dist_base", 1600)
        val percCity = pref.getInt("perc_city", 20)
        val percNight1 = pref.getInt("perc_night_1", 20)
        val percNight2 = pref.getInt("perc_night_2", 40)
        val percNightEnd1 = pref.getInt("perc_night_end_1", 4)
        val percNightEnd2 = pref.getInt("perc_night_end_2", 2)
        val percNightStart1 = pref.getInt("perc_night_start_1", 22)
        val percNightStart2 = pref.getInt("perc_night_start_2", 23)

        val strCostInfo = if(percNight1 != percNight2) {
            String.format(resources.getString(R.string.pref_summary_cost_1),
                costBase, (distBase.toFloat()).div(1000), costRunPer, costTimePer, percCity,
                percNight1, percNightStart1, percNightEnd1, percNight2, percNightStart2, percNightEnd2)
        }else{
            String.format(resources.getString(R.string.pref_summary_cost_2),
                costBase, (distBase.toFloat()).div(1000), costRunPer, costTimePer, percCity,
                percNight1, percNightStart1, percNightEnd1)
        }

        updateSummary(PREF_KEY_INFO_COST, strCostInfo)
    }

    private fun updateSummary(key: String, data: String) {
        findPreference<Preference>(key)!!.summary = data
    }
}