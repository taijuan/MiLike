package com.milike.soft.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.Utils
import com.gyf.barlibrary.ImmersionBar
import com.milike.soft.BuildConfig
import com.umeng.analytics.MobclickAgent

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
        ImmersionBar.with(this).barColor(android.R.color.transparent).statusBarDarkFont(true).init()
        LocalBroadcastManager.getInstance(Utils.getApp()).registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction("${BuildConfig.APPLICATION_ID}.refresh")
        })
    }

    override fun onResume() {
        MobclickAgent.onResume(this)
        super.onResume()
    }

    override fun onPause() {
        MobclickAgent.onPause(this)
        super.onPause()
    }

    override fun onDestroy() {
        ImmersionBar.with(this).destroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    open fun loadUrl() {}
}
