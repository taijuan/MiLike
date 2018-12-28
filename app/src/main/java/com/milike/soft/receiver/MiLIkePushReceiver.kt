package com.milike.soft.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.Utils
import com.milike.soft.BuildConfig
import com.milike.soft.activity.HomeActivity
import com.milike.soft.activity.WebActivity
import org.json.JSONObject

class MiLIkePushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (intent.action) {
                JPushInterface.ACTION_NOTIFICATION_OPENED -> {
                    if (!TextUtils.isEmpty(intent.getStringExtra(JPushInterface.EXTRA_EXTRA))) {
                        try {
                            val json = JSONObject(intent.getStringExtra(JPushInterface.EXTRA_EXTRA))
                            val url = json.getString("url")
                            if (!TextUtils.isEmpty(url)) {
                                filterActionToIntent(url)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }

    private fun filterActionToIntent(url: String) {
        if (ActivityUtils.isActivityExistsInStack(HomeActivity::class.java)) {
            Utils.getApp().startActivity(Intent(Utils.getApp(), WebActivity::class.java).apply {
                putExtra("url", url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } else {
            Utils.getApp().startActivity(IntentUtils.getLaunchAppIntent(BuildConfig.APPLICATION_ID).apply {
                putExtra("url", url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}