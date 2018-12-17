package com.milike.soft.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.milike.soft.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).barColor(R.color.white).statusBarDarkFont(true).init()
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}
