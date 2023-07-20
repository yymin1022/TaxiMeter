package com.yong.taximeter

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

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
                    val locationValue = arrLocationKey[arrLocationValue.indexOf(sharedPreferences.getString(key, "seoul"))]
                    updateCostInfo(locationValue)
                    updateSummary(key, locationValue)
                }

                PREF_KEY_THEME -> {
                    updateSummary(key, arrThemeKey[arrThemeValue.indexOf(sharedPreferences.getString(key, "horse"))])
                }
            }
        }

    private fun initSummary() {
        val locationValue = arrLocationKey[arrLocationValue.indexOf(pref.getString(PREF_KEY_LOCATION, "seoul"))]
        updateCostInfo(locationValue)
        updateSummary(PREF_KEY_LOCATION, locationValue)
        updateSummary(PREF_KEY_THEME, arrThemeKey[arrThemeValue.indexOf(pref.getString(PREF_KEY_THEME, "horse"))])
        updateSummary(PREF_KEY_INFO_VERSION, pref.getString(PREF_KEY_INFO_VERSION, "20001022")!!)
    }

    private fun updateCostInfo(location: String) {
        updateSummary(PREF_KEY_INFO_COST, "$location COST_INFO")
    }

    private fun updateSummary(key: String, data: String) {
        findPreference<Preference>(key)!!.summary = data
    }
}