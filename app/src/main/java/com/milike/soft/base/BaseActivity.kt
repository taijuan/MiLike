package com.milike.soft.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.milike.soft.BuildConfig
import com.milike.soft.R
import com.milike.soft.utils.ActivityUtils
import com.milike.soft.utils.AppUtils
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {
    private val broadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                loadUrl()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.activityList.add(this)
        LocalBroadcastManager.getInstance(AppUtils.getApp()).registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction("${BuildConfig.APPLICATION_ID}.refresh")
        })
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        ActivityUtils.activityList.remove(this)
        super.onDestroy()
    }

    open fun loadUrl() {}

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(R.layout.activity_base)
        contentView.removeAllViews()
        val view = View.inflate(this, layoutResID, null)
        contentView.addView(view)
    }
}
