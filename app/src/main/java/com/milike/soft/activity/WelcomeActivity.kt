package com.milike.soft.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.BuildConfig
import com.milike.soft.base.BaseActivity

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uuid = JPushInterface.getRegistrationID(this)
        Log.e("zuiweng", uuid)
        SPUtils.getInstance().put("uuid", uuid)
        PermissionUtils.permission(
            PermissionConstants.PHONE,
            PermissionConstants.LOCATION,
            PermissionConstants.STORAGE,
            PermissionConstants.SMS
        ).callback(object :
            PermissionUtils.SimpleCallback {
            override fun onGranted() {
                goHomeOrGuide()
            }

            override fun onDenied() {
                PermissionUtils.launchAppDetailsSettings()
            }
        }).request()
    }

    private fun goHomeOrGuide() {
        window.decorView.postDelayed({
            val versionCode = SPUtils.getInstance().getInt("versionCode")
            if (BuildConfig.VERSION_CODE > versionCode) {
                startActivity(Intent(this@WelcomeActivity, GuideActivity::class.java).apply {
                    putExtra("url", intent.getStringExtra("url"))
                })
                finish()
            } else {
                startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java).apply {
                    putExtra("url", intent.getStringExtra("url"))
                })
                finish()
            }
        }, 3000)
    }
}
