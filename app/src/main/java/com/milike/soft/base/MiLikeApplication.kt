package com.milike.soft.base

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.milike.soft.BuildConfig
import com.milike.soft.utils.AppUtils
import com.milike.soft.utils.FixBugFinalizeTimeOutUtils
import com.squareup.leakcanary.LeakCanary
import com.umeng.commonsdk.UMConfigure

class MiLikeApplication : Application() {
    override fun onCreate() {
        FixBugFinalizeTimeOutUtils.fix()
        initLeakCanary()
        AppUtils.init(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        UMConfigure.setLogEnabled(true)
        UMConfigure.init(this, BuildConfig.UMENG_APPKEY, BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, "")
        super.onCreate()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}