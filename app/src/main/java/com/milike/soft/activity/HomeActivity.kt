package com.milike.soft.activity

import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.jpush.android.api.JPushInterface
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.manager.DownloadManager
import com.azhon.appupdate.utils.Constant
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.BuildConfig
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.fragment.ConsultantFragmentNew
import com.milike.soft.fragment.HeadlineFragmentNew
import com.milike.soft.fragment.HomeFragmentNew
import com.milike.soft.fragment.MeFragmentNew
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


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
                        locationClient.unRegisterLocationListener(locationListener)
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
        Log.e("zuiweng", JPushInterface.getRegistrationID(this) + "   11")
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
        getAppVersion()
    }

    private fun showUpdateDialog(apkUrl: String, desc: String, code: Int) {
        DownloadManager.getInstance(this)
            .setApkName("MiLike.apk")
            .setApkUrl(apkUrl)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setShowNewerToast(true)
            .setApkVersionCode(code)
            .setAuthorities(BuildConfig.APPLICATION_ID)
            .setApkDescription(desc)
            .setConfiguration(
                UpdateConfiguration()
                    .setEnableLog(true)
                    .setJumpInstallPage(true)
                    .setDialogButtonTextColor(Color.WHITE)
                    .setBreakpointDownload(true)
                    .setShowNotification(true)
                    .setForcedUpgrade(false)
            )
            .download()
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
        }
    }

    override fun onDestroy() {
        locationClient.unRegisterLocationListener(locationListener)
        super.onDestroy()
    }

    private fun getAppVersion() {
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).submit {
            try {
                val url =
                    URL("http://api.fir.im/apps/latest/5bf6bd49ca87a83627a27824?api_token=65e3d3d8ed721bed0eb86feeaed2d867")
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.readTimeout = Constant.HTTP_TIME_OUT
                con.connectTimeout = Constant.HTTP_TIME_OUT
                con.setRequestProperty("Accept-Encoding", "identity")
                if (con.responseCode == HttpURLConnection.HTTP_OK) {
                    val json = JSONObject(String(con.inputStream.readBytes()))
                    val installUrl = json.getString("direct_install_url")
                    val versionCode = json.getInt("version")
                    val xx = json.getString("changelog")
                    runOnUiThread {
                        showUpdateDialog(installUrl, xx, versionCode)
                    }
                }
                con.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
