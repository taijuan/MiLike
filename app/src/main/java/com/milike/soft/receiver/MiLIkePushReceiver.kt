package com.milike.soft.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface

class MiLIkePushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("zuiweng",intent.toString())
        if (context != null && intent != null) {
            when (intent.action) {
                JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION, JPushInterface.ACTION_NOTIFICATION_OPENED -> {

                }
            }
        }
    }
}