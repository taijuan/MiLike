package com.milike.soft.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.milike.soft.BuildConfig
import com.milike.soft.utils.AppUtils

abstract class LazyLoadBaseFragment : androidx.fragment.app.Fragment() {

    private var mIsFirstVisible = true
    private var isViewCreated = false
    private val broadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                loadUrl()
            }

        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        onFragmentVisible()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        onFragmentVisible()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        onFragmentVisible()
    }

    private fun onFragmentVisible() {
        if (!isHidden && userVisibleHint && isViewCreated && mIsFirstVisible) {
            onFragmentFirstVisible()
            LocalBroadcastManager.getInstance(AppUtils.getApp())
                .registerReceiver(broadcastReceiver, IntentFilter().apply {
                    addAction("${BuildConfig.APPLICATION_ID}.refresh")
                })
            mIsFirstVisible = false
        }
    }

    protected abstract fun onFragmentFirstVisible()

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        mIsFirstVisible = true
        LocalBroadcastManager.getInstance(AppUtils.getApp()).unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }

    abstract fun loadUrl()
}