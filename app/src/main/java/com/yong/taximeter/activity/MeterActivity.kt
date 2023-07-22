package com.yong.taximeter.activity

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.yong.taximeter.R
import com.yong.taximeter.util.CostType
import com.yong.taximeter.util.MeterStatus
import com.yong.taximeter.util.MeterTheme
import com.yong.taximeter.util.MeterUtil


class MeterActivity : AppCompatActivity() {
    private lateinit var ivRunner: ImageView
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

        ivRunner = findViewById(R.id.iv_meter_runner)
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
        ivRunner.setImageDrawable(updateRunner(MeterUtil.speed, MeterUtil.theme))

        tvCost.text = String.format(resources.getString(R.string.tv_meter_info_cost), MeterUtil.cost)
        tvCounter.text = String.format(resources.getString(R.string.tv_meter_info_counter), MeterUtil.counter)
        tvDistance.text = String.format(resources.getString(R.string.tv_meter_info_distance), MeterUtil.distance)
        tvSpeed.text = String.format(resources.getString(R.string.tv_meter_info_speed), MeterUtil.speed)

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