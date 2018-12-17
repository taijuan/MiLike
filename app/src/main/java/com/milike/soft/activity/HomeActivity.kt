package com.milike.soft.activity

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.BuildConfig
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.fragment.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    private val locationListener: BDAbstractLocationListener by lazy {
        object : BDAbstractLocationListener() {
            override fun onReceiveLocation(it: BDLocation?) {
                it?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    Log.e("zuiweng", "经度=" + latitude.toString() + " 纬度=" + longitude.toString())
                    val city = it.city
                    Log.e("zuiweng", "城市=$city")
                    if (it.locType == BDLocation.TypeGpsLocation || it.locType == BDLocation.TypeNetWorkLocation) {
                        val positionInfo =
                            "&location=" + latitude.toString() + "," + longitude.toString() + "&city=" + it.city
                        SPUtils.getInstance().put("position", positionInfo)
                    }
                }
            }
        }
    }
    private val locationClient: LocationClient by lazy {
        LocationClient(this).apply {
            registerLocationListener(locationListener)
            val option = LocationClientOption()
            option.setIsNeedAddress(true)
            option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            option.setCoorType("bd09ll")
            option.setScanSpan(1000)
            option.isOpenGps = true
            option.isLocationNotify = true
            option.setIgnoreKillProcess(false)
            option.SetIgnoreCacheException(false)
            option.setWifiCacheTimeOut(5 * 60 * 1000)
            option.setEnableSimulateGps(false)
            locOption = option
            start()
        }
    }
    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        SPUtils.getInstance().put("versionCode", BuildConfig.VERSION_CODE)
        viewPager.adapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = when (position) {
                0 -> HomeFragmentNew()
                1 -> HeadlineFragmentNew()
                2 -> ConsultantFragmentNew()
                else -> MeFragmentNew()
            }

            override fun getCount() = 4
        }
        viewPager.offscreenPageLimit = 4
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.removeAllTabs()
        tabItem(R.string.page1, R.drawable.home_selected, R.drawable.home)
        tabItem(R.string.page2, R.drawable.toutiao_selected, R.drawable.toutiao)
        tabItem(R.string.page3, R.drawable.advisor_selected, R.drawable.advisor)
        tabItem(R.string.page4, R.drawable.me_selected, R.drawable.me)
    }

    private fun tabItem(name: Int, select: Int, normal: Int) {
        tabLayout.addTab(tabLayout.newTab().setCustomView(TextView(this@HomeActivity).apply {
            text = resources.getString(name)
            gravity = Gravity.CENTER
            textSize = 10f
            setCompoundDrawablesWithIntrinsicBounds(null, StateListDrawable().apply {
                addState(
                    intArrayOf(android.R.attr.state_selected),
                    ContextCompat.getDrawable(this@HomeActivity, select)
                )
                addState(intArrayOf(android.R.attr.state_pressed), ContextCompat.getDrawable(this@HomeActivity, select))
                addState(intArrayOf(), ContextCompat.getDrawable(this@HomeActivity, normal))
            }, null, null)
        }))
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
            System.exit(0)
        }
    }

    override fun onDestroy() {
        locationClient.unRegisterLocationListener(locationListener)
        super.onDestroy()
    }

}
