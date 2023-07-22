package com.yong.taximeter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.yong.taximeter.R
import com.yong.taximeter.util.CostType
import com.yong.taximeter.util.MeterStatus
import com.yong.taximeter.util.MeterUtil

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

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun initView() {
        MeterUtil.init(this)
        updateView()
    }

    private fun updateView() {
        tvCost.text = String.format(resources.getString(R.string.tv_meter_info_cost), MeterUtil.cost)
        tvCounter.text = String.format(resources.getString(R.string.tv_meter_info_counter), MeterUtil.counter)
        tvDistance.text = String.format(resources.getString(R.string.tv_meter_info_distance), MeterUtil.distance)
        tvSpeed.text = String.format(resources.getString(R.string.tv_meter_info_speed), MeterUtil.speed)

        when(MeterUtil.costType) {
            CostType.BASE_COST ->
                tvCostType.text = String.format(resources.getString(R.string.tv_meter_info_cost_type), "Base Cost")
            CostType.DISTANCE_COST ->
                tvCostType.text = String.format(resources.getString(R.string.tv_meter_info_cost_type), "Distance Cost")
            CostType.TIME_COST ->
                tvCostType.text = String.format(resources.getString(R.string.tv_meter_info_cost_type), "Time Cost")
        }

        when(MeterUtil.status) {
            MeterStatus.NOT_DRIVING ->
                tvStatus.text = String.format(resources.getString(R.string.tv_meter_info_status), "Not Driving")
            MeterStatus.DRIVING ->
                tvStatus.text = String.format(resources.getString(R.string.tv_meter_info_status), "Driving")
            MeterStatus.GPS_UNSTABLE ->
                tvStatus.text = String.format(resources.getString(R.string.tv_meter_info_status), "Unstable GPS")
        }

        tvPremiumNight.visibility = if(MeterUtil.isPrmNight) View.VISIBLE else View.GONE
        tvPremiumOutcity.visibility = if(MeterUtil.isPrmOutcity) View.VISIBLE else View.GONE
    }
}