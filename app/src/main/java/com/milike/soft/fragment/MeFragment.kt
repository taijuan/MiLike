package com.milike.soft.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.milike.soft.BuildConfig.DNS
import com.milike.soft.R
import com.milike.soft.base.BaseFragment
import com.milike.soft.utils.*
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.view_web.*

class MeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_web, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewContent.setPadding(0, BarUtils.getBarHeightToDP(), 0, 0)
        webView.settings.initWebViewSetting()
        webView.webViewClient = object : MiLikeWebViewClient() {
            override fun onStart(view: WebView, url: String) {
                loading?.visibility = View.VISIBLE
            }

            override fun onEnd(view: WebView, url: String) {
                loading?.visibility = View.GONE
            }

            override fun interceptUrlLoading(view: WebView, url: String): Boolean {
                return arrayListOf(
                    "/mysub.htm",
                    "/myNeed/1",
                    "/queryuserinfo",
                    "/set.htm",
                    "/link",
                    "js://jstojava?cityCode=",
                    "/join.htm"
                ).any { s -> url.contains(s) }
            }
        }
        webView.addJavascriptInterface(MiLikeJavascriptInterface(webView), "android")
        loadUrl()
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FADB28"), Color.parseColor("#FFDA00"))
        swipeRefreshLayout.setOnRefreshListener {
            loadUrl()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    override fun loadUrl() {
        val cityCode = SPUtils.getInstance().getString("cityCode", "sz")
        webView.loadUrl(getWebUrlSuffix("$DNS$cityCode/user"))
    }

    override fun onResume() {
        webView.onResume()
        super.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        webView?.also {
            webViewContent?.removeView(it)
            it.removeAllViews()
            it.destroy()
        }
        super.onDestroy()
    }
}