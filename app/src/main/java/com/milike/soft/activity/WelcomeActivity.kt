package com.milike.soft.activity

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.BuildConfig
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.utils.initWebViewSetting
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        webView.isEnabled = false
        webView.settings.initWebViewSetting()
        webView.loadUrl("file:///android_asset/start.html")
        PermissionUtils.permission(PermissionConstants.PHONE, PermissionConstants.LOCATION).callback(object :
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
                startActivity(Intent(this@WelcomeActivity, GuideActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
