package com.milike.soft.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.manager.DownloadManager
import com.azhon.appupdate.utils.Constant
import com.milike.soft.BuildConfig
import com.milike.soft.BuildConfig.DNS
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.fragment.ConsultantFragment
import com.milike.soft.fragment.HeadlineFragment
import com.milike.soft.fragment.HomeFragment
import com.milike.soft.fragment.MeFragment
import com.milike.soft.utils.SPUtils
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeActivity : BaseActivity() {

    private var exitTime: Long = 0
    private val singleExecutorService: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        SPUtils.getInstance().put("versionCode", BuildConfig.VERSION_CODE)
        viewPager.adapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = when (position) {
                0 -> HomeFragment()
                1 -> HeadlineFragment()
                2 -> ConsultantFragment()
                else -> MeFragment()
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
        filterActionToIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        filterActionToIntent()
        super.onNewIntent(intent)
    }

    private fun filterActionToIntent() {
        val url = intent.getStringExtra("url")
        if (!TextUtils.isEmpty(url)) {
            startActivity(Intent(this, WebActivity::class.java).apply {
                putExtra("url", url)
            })
        }
    }

    private fun showUpdateDialog(apkUrl: String, desc: String, code: Int, isForceUpdate: Boolean) {
        DownloadManager.getInstance(this)
            .setApkName("MiLike.apk")
            .setApkUrl(apkUrl)
            .setSmallIcon(R.drawable.ic_launcher)
            .setShowNewerToast(false)
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
                    .setForcedUpgrade(isForceUpdate)
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

    private fun getAppVersion() {
        singleExecutorService.submit {
            try {
                val url = URL("${DNS}getAppUpdate")
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.readTimeout = Constant.HTTP_TIME_OUT
                con.connectTimeout = Constant.HTTP_TIME_OUT
                con.setRequestProperty("Accept-Encoding", "identity")
                if (con.responseCode == HttpURLConnection.HTTP_OK) {
                    val json = JSONObject(String(con.inputStream.readBytes())).getJSONObject("vl")
                    val installUrl = json.getString("apkUrl")
                    val versionCode = json.getInt("versionName")
                    val desc = json.getString("updateDesc")
                    val isForceUpdate = json.getBoolean("isCompelUpdate")
                    runOnUiThread {
                        showUpdateDialog(installUrl, desc, versionCode, isForceUpdate)
                    }
                }
                con.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        if (singleExecutorService.isShutdown) {
            singleExecutorService.shutdownNow()
        }
        super.onDestroy()
    }
}
