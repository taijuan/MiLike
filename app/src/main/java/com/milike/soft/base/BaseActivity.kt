package com.milike.soft.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.milike.soft.R
import com.umeng.analytics.MobclickAgent

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).barColor(R.color.white).statusBarDarkFont(true).init()
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
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}
