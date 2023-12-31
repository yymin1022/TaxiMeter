package com.yong.taximeter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.yong.taximeter.R
import com.yong.taximeter.fragment.WelcomePermissonFragment

class WelcomeActivity : AppCompatActivity() {
    private var welcomeFragmentManager = supportFragmentManager
    private var welcomePermissonFragment = WelcomePermissonFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        this.onBackPressedDispatcher.addCallback(this, backButtonCallback)

        val welcomeFragmentTransaction = welcomeFragmentManager.beginTransaction()
        welcomeFragmentTransaction.replace(R.id.layout_welcome_fragment, welcomePermissonFragment).commitAllowingStateLoss()
    }

    private val backButtonCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }
}