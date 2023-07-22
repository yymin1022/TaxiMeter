package com.yong.taximeter.activity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.yong.taximeter.R

class MeterActivity : AppCompatActivity() {
    private lateinit var tvCost: TextView
    private lateinit var tvCostType: TextView
    private lateinit var tvCounter: TextView
    private lateinit var tvDistance: TextView
    private lateinit var tvPremiumNight: TextView
    private lateinit var tvPremiumOutcity: TextView
    private lateinit var tvSpeed: TextView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meter)

        tvCost = findViewById(R.id.tv_meter_info_cost)
        tvCostType = findViewById(R.id.tv_meter_info_cost_type)
        tvCounter = findViewById(R.id.tv_meter_info_counter)
        tvDistance = findViewById(R.id.tv_meter_info_distance)
        tvPremiumNight = findViewById(R.id.tv_meter_premium_night)
        tvPremiumOutcity = findViewById(R.id.tv_meter_premium_outcity)
        tvSpeed = findViewById(R.id.tv_meter_info_speed)
        tvStatus = findViewById(R.id.tv_meter_info_status)

        initView()
    }

    private fun initView() {
        tvCost.text = String.format(resources.getString(R.string.tv_meter_info_cost), 0)
        tvCostType.text = String.format(resources.getString(R.string.tv_meter_info_cost_type), 0)
        tvCounter.text = String.format(resources.getString(R.string.tv_meter_info_counter), 0)
        tvDistance.text = String.format(resources.getString(R.string.tv_meter_info_distance), 0)
        tvSpeed.text = String.format(resources.getString(R.string.tv_meter_info_speed), 0)
        tvStatus.text = String.format(resources.getString(R.string.tv_meter_info_status), "Not Driving")

        tvPremiumNight.visibility = View.GONE
        tvPremiumOutcity.visibility = View.GONE
    }
}