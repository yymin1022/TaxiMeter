package com.yong.taximeter.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.fsn.cauly.CaulyAdInfoBuilder
import com.fsn.cauly.CaulyAdView
import com.yong.taximeter.R
import com.yong.taximeter.service.MeterService
import com.yong.taximeter.util.CostType
import com.yong.taximeter.util.MeterStatus
import com.yong.taximeter.util.MeterTheme
import com.yong.taximeter.util.MeterUtil
import com.yong.taximeter.util.PermissionUtil
import kotlin.math.roundToInt

class MeterActivity : AppCompatActivity() {
    private lateinit var btnPrmNight: AppCompatButton
    private lateinit var btnPrmOutcity: AppCompatButton
    private lateinit var btnStart: AppCompatButton
    private lateinit var btnStop: AppCompatButton
    private lateinit var ivRunner: ImageView
    private lateinit var tvCost: TextView
    private lateinit var tvCostType: TextView
    private lateinit var tvCounter: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvPremiumNight: TextView
    private lateinit var tvPremiumOutcity: TextView
    private lateinit var tvSpeed: TextView
    private lateinit var tvStatus: TextView

    private var statusReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.action != null && intent.action.equals("METER_STATUS")) {
                updateView()
            }
        }
    }

    private var updateReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.action != null && intent.action.equals("UPDATE_METER")) {
                updateView()
            }
        }
    }

    private var btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.btn_meter_premium_night -> {
                if(MeterUtil.isPrmNight) {
                    MeterUtil.isPrmNight = false
                    btnPrmNight.text = resources.getString(R.string.btn_meter_premium_night_off)
                } else {
                    MeterUtil.isPrmNight = true
                    btnPrmNight.text = resources.getString(R.string.btn_meter_premium_night_on)
                }
            }
            R.id.btn_meter_premium_outcity -> {
                if(MeterUtil.isPrmOutcity) {
                    MeterUtil.isPrmOutcity = false
                    btnPrmOutcity.text = resources.getString(R.string.btn_meter_premium_outcity_off)
                } else {
                    MeterUtil.isPrmOutcity = true
                    btnPrmOutcity.text = resources.getString(R.string.btn_meter_premium_outcity_on)
                }
            }
            R.id.btn_meter_start -> {
                if(!MeterUtil.isDriving) {
                    MeterUtil.isDriving = true
                    startService(Intent(this, MeterService::class.java))
                }
            }
            R.id.btn_meter_stop -> {
                if(MeterUtil.isDriving) {
                    MeterUtil.isDriving = false
                    showFinishDialog()
                    stopService(Intent(this, MeterService::class.java))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meter)

        btnPrmNight = findViewById(R.id.btn_meter_premium_night)
        btnPrmOutcity = findViewById(R.id.btn_meter_premium_outcity)
        btnStart = findViewById(R.id.btn_meter_start)
        btnStop = findViewById(R.id.btn_meter_stop)

        ivRunner = findViewById(R.id.iv_meter_runner)
        tvCost = findViewById(R.id.tv_meter_info_cost)
        tvCostType = findViewById(R.id.tv_meter_info_cost_type)
        tvCounter = findViewById(R.id.tv_meter_info_counter)
        tvDistance = findViewById(R.id.tv_meter_info_distance)
        tvPremiumNight = findViewById(R.id.tv_meter_premium_night)
        tvPremiumOutcity = findViewById(R.id.tv_meter_premium_outcity)
        tvSpeed = findViewById(R.id.tv_meter_info_speed)
        tvStatus = findViewById(R.id.tv_meter_info_status)

        if(!PermissionUtil.checkLocationPermission(this)) {
            PermissionUtil.openAppInfo(this)
            finish()
        }

        initView()
    }

    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(statusReceiver, IntentFilter("METER_STATUS"))
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, IntentFilter("UPDATE_METER"))

        updateView()
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(statusReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver)
    }

    private fun showFinishDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.noti_alert_title))
        alertDialog.setMessage(String.format(getString(R.string.noti_alert_message), MeterUtil.cost, MeterUtil.distance))
        alertDialog.setPositiveButton(getString(R.string.noti_alert_close)) { dialog, _ ->
            updateView()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun initView() {
        if(!MeterUtil.isDriving) {
            MeterUtil.init(this)
        }

        initCauly()
        updateView()

        btnPrmNight.setOnClickListener(btnListener)
        btnPrmOutcity.setOnClickListener(btnListener)
        btnStart.setOnClickListener(btnListener)
        btnStop.setOnClickListener(btnListener)
    }

    private fun initCauly() {
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if(!pref.getBoolean("ad_remove", false)) {
            val caulyLayout = findViewById<LinearLayout>(R.id.layout_meter_cauly)
            val caulyAdInfo = CaulyAdInfoBuilder("ymwH9YIJ")
                .bannerHeight(CaulyAdInfoBuilder.FIXED)
                .effect("Circle")
                .enableDefaultBannerAd(true)
                .setBannerSize(320, 50)
                .build()

            val caulyAdView = CaulyAdView(this)
            caulyAdView.setAdInfo(caulyAdInfo)
            caulyLayout.addView(caulyAdView)
        }
    }

    private fun updateView() {
        ivRunner.setImageDrawable(updateRunner(MeterUtil.speed, MeterUtil.theme))

        tvCost.text = String.format(resources.getString(R.string.tv_meter_info_cost), MeterUtil.cost)
        tvCounter.text = String.format(resources.getString(R.string.tv_meter_info_counter), MeterUtil.counter)
        tvDistance.text = String.format(resources.getString(R.string.tv_meter_info_distance), MeterUtil.distance / 1000)
        tvSpeed.text = String.format(resources.getString(R.string.tv_meter_info_speed), (MeterUtil.speed * 3.6).roundToInt())

        when(MeterUtil.costType) {
            CostType.BASE_COST ->
                tvCostType.text = resources.getString(R.string.tv_meter_info_cost_type_base)
            CostType.DISTANCE_COST ->
                tvCostType.text = resources.getString(R.string.tv_meter_info_cost_type_distance)
            CostType.TIME_COST ->
                tvCostType.text = resources.getString(R.string.tv_meter_info_cost_type_time)
        }

        when(MeterUtil.status) {
            MeterStatus.NOT_DRIVING ->
                tvStatus.text = resources.getString(R.string.tv_meter_info_status_not_driving)
            MeterStatus.DRIVING ->
                tvStatus.text = resources.getString(R.string.tv_meter_info_status_driving)
            MeterStatus.GPS_UNSTABLE ->
                tvStatus.text = resources.getString(R.string.tv_meter_info_status_gps_unstable)
        }

        tvPremiumNight.visibility = if(MeterUtil.isPrmNight) View.VISIBLE else View.GONE
        tvPremiumOutcity.visibility = if(MeterUtil.isPrmOutcity) View.VISIBLE else View.GONE
    }

    private fun updateRunner(speed: Int, theme: MeterTheme): Drawable {
        val animDrawable = AnimationDrawable()
        when(theme) {
            MeterTheme.THEME_CIRCLE -> {
                if(speed > 50) {
                    for (i in 0..10) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_1)!!, 12)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_2)!!, 13)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_3)!!, 12)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_4)!!, 13)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_5)!!, 12)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_6)!!, 13)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_7)!!, 12)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_8)!!, 13)
                    }
                } else if(speed > 30) {
                    for (i in 0..7) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_1)!!, 20)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_2)!!, 20)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_3)!!, 21)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_4)!!, 21)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_5)!!, 21)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_6)!!, 21)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_7)!!, 21)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_8)!!, 21)
                    }
                } else if(speed > 15) {
                    for (i in 0..3) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_1)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_2)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_3)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_4)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_5)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_6)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_7)!!, 41)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_8)!!, 41)
                    }
                } else if(speed > 0) {
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_1)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_2)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_3)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_4)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_5)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_6)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_7)!!, 125)
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_8)!!, 125)
                } else {
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_circle_1)!!, 1000)
                }
            }

            MeterTheme.THEME_HORSE -> {
                if(speed > 50) {
                    for (i in 0..7) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 47)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_2)!!, 47)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_3)!!, 48)
                    }
                } else if(speed > 30) {
                    for (i in 0..5) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 66)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_2)!!, 67)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_3)!!, 67)
                    }
                } else if(speed > 20) {
                    for (i in 0..4) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 83)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_2)!!, 83)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_3)!!, 84)
                    }
                } else if(speed > 10) {
                    for (i in 0..3) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 111)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_2)!!, 111)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_3)!!, 111)
                    }
                } else if(speed > 0) {
                    for (i in 0..2) {
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 166)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_2)!!, 166)
                        animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_3)!!, 167)
                    }
                } else {
                    animDrawable.addFrame(AppCompatResources.getDrawable(applicationContext, R.drawable.ic_horse_1)!!, 1000)
                }
            }
        }

        animDrawable.start()
        return animDrawable
    }
}