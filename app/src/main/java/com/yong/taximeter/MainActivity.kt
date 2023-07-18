package com.yong.taximeter

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.yong.taximeter.databinding.ActivityMainBinding

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

        setSupportActionBar(binding.toolbar)

        val mainBottomNavigationView = findViewById<BottomNavigationView>(R.id.menu_main_bottom)
        mainBottomNavigationView.setOnItemSelectedListener(NavListner())

        val mainFragmentTransaction = mainFragmentManager.beginTransaction()
        mainFragmentTransaction.replace(R.id.layout_main_fragment, mainHomeFragment).commitAllowingStateLoss()
    }

    inner class NavListner : OnItemSelectedListener, NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val mainFragmentTransaction = mainFragmentManager.beginTransaction()

            when(item.itemId) {
                R.id.menu_main_donate -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainDonateFragment).commitAllowingStateLoss()
                R.id.menu_main_home -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainHomeFragment).commitAllowingStateLoss()
                R.id.menu_main_setting -> mainFragmentTransaction.replace(R.id.layout_main_fragment, mainSettingFragment).commitAllowingStateLoss()
            }

            return true
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}