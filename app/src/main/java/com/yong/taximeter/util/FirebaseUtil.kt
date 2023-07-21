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
        var curVersion = pref.getString("pref_info_version", "00000000")

        val db = Firebase.firestore
        val docRef = db.collection("cost").document("version")
        docRef.get()
            .addOnSuccessListener { document ->
                chkVersion = document.get("data").toString()

                if(curVersion!! < chkVersion) {
                    Toast.makeText(context, context.resources.getString(R.string.firebase_toast_update), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, context.resources.getString(R.string.firebase_toast_fail), Toast.LENGTH_SHORT).show()
                Log.e("VerCheck", exception.message.toString())
            }
    }
}