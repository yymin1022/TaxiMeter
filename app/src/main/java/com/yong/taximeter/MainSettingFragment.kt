package com.yong.taximeter

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class MainSettingFragment : PreferenceFragmentCompat() {
    private lateinit var pref: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_items)

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pref.registerOnSharedPreferenceChangeListener(prefListener)
    }

    var prefListener = OnSharedPreferenceChangeListener {
        sharedPreferences, key ->
            when(key) {
                "pref_location" -> Log.d("PREFS", sharedPreferences.getString(key, "seoul").toString())
                "pref_theme" -> Log.d("PREFS", sharedPreferences.getString(key, "horse").toString())
            }
        }
}