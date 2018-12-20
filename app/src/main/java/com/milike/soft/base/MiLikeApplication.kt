package com.milike.soft.base

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.milike.soft.BuildConfig
import com.umeng.commonsdk.UMConfigure

class MiLikeApplication : Application() {
    override fun onCreate() {
        Utils.init(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        UMConfigure.setLogEnabled(true)
        UMConfigure.init(this, BuildConfig.UMENG_APPKEY, BuildConfig.UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, "")
        super.onCreate()
    }
}