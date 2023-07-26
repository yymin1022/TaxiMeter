package com.yong.taximeter.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class CostType {
    BASE_COST,
    DISTANCE_COST,
    TIME_COST
}

enum class MeterStatus {
    NOT_DRIVING,
    DRIVING,
    GPS_UNSTABLE
}

enum class MeterTheme {
    THEME_CIRCLE,
    THEME_HORSE
}

object MeterUtil {
    private lateinit var pref: SharedPreferences
    private var lastUpdateTime = 0L

    private var costBase = 0
    private var costRunPer = 0
    private var costTimePer = 0
    private var distBase = 0
    private var percCity = 0
    private var percNight1 = 0
    private var percNight2 = 0
    private var percNightEnd1 = 0
    private var percNightEnd2 = 0
    private var percNightStart1 = 0
    private var percNightStart2 = 0

    var cost = 0
    var costType = CostType.BASE_COST
    var counter = 0
    var distance = 0.0
    var speed = 0f
    var status = MeterStatus.NOT_DRIVING
    var theme = MeterTheme.THEME_HORSE

    var isDriving = false
    var isPrmNight = false
    var isPrmOutcity = false

    fun init(context: Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        val locationPrefs = pref.getString("pref_location", "seoul")
        theme = if(pref.getString("pref_theme", "horse").equals("circle")) MeterTheme.THEME_CIRCLE else MeterTheme.THEME_HORSE

        pref = context.getSharedPreferences("pref_cost_${locationPrefs}", Context.MODE_PRIVATE)!!
        costBase = pref.getInt("cost_base", 0)
        costRunPer = pref.getInt("cost_run_per", 131)
        costTimePer = pref.getInt("cost_time_per", 30)
        distBase = pref.getInt("dist_base", 1600)
        percCity = pref.getInt("perc_city", 20)
        percNight1 = pref.getInt("perc_night_1", 20)
        percNight2 = pref.getInt("perc_night_2", 40)
        percNightEnd1 = pref.getInt("perc_night_end_1", 4)
        percNightEnd2 = pref.getInt("perc_night_end_2", 2)
        percNightStart1 = pref.getInt("perc_night_start_1", 22)
        percNightStart2 = pref.getInt("perc_night_start_2", 23)

        resetValues()
    }

    fun resetValues() {
        cost = costBase
        costType = CostType.BASE_COST
        counter = distBase
        distance = 0.0
        speed = 0f
        status = MeterStatus.NOT_DRIVING

        isPrmNight = false
        isPrmOutcity = false
    }

    fun increaseCost(curSpeed: Int) {
        val curTime = System.currentTimeMillis()
        if(lastUpdateTime == 0L) {
            lastUpdateTime = curTime
        }

        val deltaTime = (curTime - lastUpdateTime).toInt() / 1000f
        lastUpdateTime = curTime

        speed = curSpeed.toFloat()
        status = MeterStatus.DRIVING

        counter -= (speed * deltaTime).toInt()
        distance += speed * deltaTime

        if(speed < 4.2) {
            counter -= (costRunPer / costTimePer * deltaTime).toInt()

            if(costType == CostType.DISTANCE_COST) {
                costType = CostType.TIME_COST
            }
        }else{
            if(costType == CostType.TIME_COST) {
                costType = CostType.DISTANCE_COST
            }
        }

        if(counter <= 0) {
            cost += 100
            counter = costRunPer

            if(isPrmNight) {
                val curH = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
                if((curH >= 20 && curH >= percNightStart1) || (curH <= 5 && curH < percNightEnd1)) {
                    cost += if((curH >= 20 && curH >= percNightStart2) || (curH <= 5 && curH < percNightEnd2)) percNight2 else percNight1
                }
            }

            if(isPrmOutcity) {
                cost += percCity
            }

            if(costType == CostType.BASE_COST) {
                costType = CostType.DISTANCE_COST
            }
        }
    }

    fun applyBaseCostNightPremium(isEnabled: Boolean) {
        val curH = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
        var premiumCost = 0
        if((curH >= 20 && curH >= percNightStart1) || (curH <= 5 && curH < percNightEnd1)) {
            premiumCost = if((curH >= 20 && curH >= percNightStart2) || (curH <= 5 && curH < percNightEnd2)) {
                costBase * percNight2 / 100
            } else {
                costBase * percNight1 / 100
            }
        }

        if(isEnabled) {
            cost += premiumCost
        } else {
            cost -= premiumCost
        }
    }
}