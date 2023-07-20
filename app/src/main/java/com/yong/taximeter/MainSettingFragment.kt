package com.yong.taximeter

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

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
                "pref_location" -> {
                    val locationValue = arrLocationKey[arrLocationValue.indexOf(sharedPreferences.getString(key, "seoul"))]
                    updateCostInfo(locationValue)
                    updateSummary("pref_location", locationValue)
                    Log.d("PREFS", sharedPreferences.getString(key, "seoul").toString())
                }

                "pref_theme" -> {
                    updateSummary("pref_theme", arrThemeKey[arrThemeValue.indexOf(sharedPreferences.getString(key, "horse"))])
                    Log.d("PREFS", sharedPreferences.getString(key, "horse").toString())
                }
            }
        }

    private fun initSummary() {
        val locationValue = arrLocationKey[arrLocationValue.indexOf(pref.getString("pref_location", "seoul"))]
        updateCostInfo(locationValue)
        updateSummary("pref_location", locationValue)
        updateSummary("pref_theme", arrThemeKey[arrThemeValue.indexOf(pref.getString("pref_theme", "horse"))])
        updateSummary("pref_info_version", pref.getString("pref_info_version", "20001022")!!)
    }

    private fun updateCostInfo(location: String) {
        updateSummary("pref_info_cost", "$location COST_INFO")
    }

    private fun updateSummary(key: String, data: String) {
        findPreference<Preference>(key)!!.summary = data
    }
}