package com.yong.taximeter.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.yong.taximeter.fragment.MainDonateFragment
import com.yong.taximeter.fragment.MainHomeFragment
import com.yong.taximeter.fragment.MainSettingFragment
import com.yong.taximeter.R
import com.yong.taximeter.databinding.ActivityMainBinding
import com.yong.taximeter.util.FirebaseUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mainFragmentManager = supportFragmentManager
    private var mainDonateFragment = MainDonateFragment()
    private var mainHomeFragment = MainHomeFragment()
    private var mainSettingFragment = MainSettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainBottomNavigationView = findViewById<BottomNavigationView>(R.id.menu_main_bottom)
        mainBottomNavigationView.setOnApplyWindowInsetsListener(null)
        mainBottomNavigationView.setOnItemSelectedListener(NavListener())
        mainBottomNavigationView.selectedItemId = R.id.menu_main_home

        val mainFragmentTransaction = mainFragmentManager.beginTransaction()
        mainFragmentTransaction.replace(R.id.layout_main_fragment, mainHomeFragment)
            .commitAllowingStateLoss()

        FirebaseUtil.getLatestVersion(this)

        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (!pref.getBoolean("welcome", false)) {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
        }
    }

    inner class NavListener : NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val mainFragmentTransaction = mainFragmentManager.beginTransaction()

            when(item.itemId) {
                R.id.menu_main_donate -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainDonateFragment).commitAllowingStateLoss()
                R.id.menu_main_home -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainHomeFragment).commitAllowingStateLoss()
                R.id.menu_main_setting -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainSettingFragment).commitAllowingStateLoss()
            }

            return true
        }
    }
}