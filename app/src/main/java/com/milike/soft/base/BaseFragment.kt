package com.milike.soft.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.milike.soft.BuildConfig
import com.milike.soft.utils.AppUtils

abstract class BaseFragment : Fragment() {
    private val broadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                loadUrl()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(AppUtils.getApp()).registerReceiver(broadcastReceiver, IntentFilter().apply {
            addAction("${BuildConfig.APPLICATION_ID}.refresh")
        })
    }

    override fun onDestroyView() {
        LocalBroadcastManager.getInstance(AppUtils.getApp()).unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }

    abstract fun loadUrl()
}