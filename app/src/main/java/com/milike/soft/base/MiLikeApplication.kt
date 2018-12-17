package com.milike.soft.base

import android.app.Application
import com.blankj.utilcode.util.Utils

class MiLikeApplication : Application() {
    override fun onCreate() {
        Utils.init(this)
        super.onCreate()
    }
}