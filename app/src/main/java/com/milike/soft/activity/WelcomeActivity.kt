package com.milike.soft.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import cn.jpush.android.api.JPushInterface
import com.milike.soft.BuildConfig
import com.milike.soft.base.BaseActivity
import com.milike.soft.utils.SPUtils
import com.milike.soft.utils.initDisplayCutout

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.initDisplayCutout {
            val uuid = JPushInterface.getRegistrationID(this)
            Log.e("zuiweng", uuid)
            SPUtils.getInstance().put("uuid", uuid)
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        val permissions = getPermissions()
        val b = permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
        if (b) {
            goHomeOrGuide()
        } else {
            ActivityCompat.requestPermissions(this, getPermissions(), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {
            goHomeOrGuide()
        }
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

    private fun getPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
