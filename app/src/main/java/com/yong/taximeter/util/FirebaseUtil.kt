package com.yong.taximeter.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yong.taximeter.R

object FirebaseUtil {
    fun getLatestVersion(context: Context) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        var chkVersion: String
        val curVersion = pref.getString("pref_info_version", "20001022")

        val db = Firebase.firestore
        val docRef = db.collection("cost").document("version")
        docRef.get()
            .addOnSuccessListener { document ->
                chkVersion = document.get("data").toString()

                if(curVersion!! < chkVersion) {
                    updateCostInfo(context, chkVersion)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.resources.getString(R.string.firebase_toast_fail), Toast.LENGTH_SHORT).show()
                Log.e("VerCheckErr", exception.message.toString())
            }
    }

    private fun updateCostInfo(context: Context, version: String) {
        val db = Firebase.firestore
        val docRef = db.collection("cost").document("info")
        docRef.get()
            .addOnSuccessListener { document ->
                for(costInfo in (document.get("data") as List<*>)) {
                    val costInfoItem = costInfo as Map<*, *>
                    val costInfoCity = costInfoItem["city"].toString()
                    val costInfoData = costInfoItem["data"] as Map<*, *>

                    val pref = context.getSharedPreferences("pref_cost_${costInfoCity}", Context.MODE_PRIVATE)
                    val prefEditor = pref.edit()

                    costInfoData.forEach {
                        prefEditor.putInt(it.key.toString(), (it.value as Long).toInt())
                    }
                    prefEditor.apply()
                }

                val pref = PreferenceManager.getDefaultSharedPreferences(context)
                val prefEditor = pref.edit()
                prefEditor.putString("pref_info_version", version)
                prefEditor.apply()

                Toast.makeText(context, context.resources.getString(R.string.firebase_toast_update), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.resources.getString(R.string.firebase_toast_fail), Toast.LENGTH_SHORT).show()
                Log.e("DBUpdateErr", exception.message.toString())
            }
    }
}