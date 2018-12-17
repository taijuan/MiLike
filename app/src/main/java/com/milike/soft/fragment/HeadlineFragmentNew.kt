package com.milike.soft.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.blankj.utilcode.util.SPUtils
import com.milike.soft.R
import com.milike.soft.base.BaseFragment
import com.milike.soft.base.UrlConstants
import com.milike.soft.utils.MiLikeJavascriptInterface
import com.milike.soft.utils.MiLikeWebViewClient
import com.milike.soft.utils.getWebUrlSuffix
import com.milike.soft.utils.initWebViewSetting
import kotlinx.android.synthetic.main.layout_web_view.*

class HeadlineFragmentNew : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.layout_web_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.settings.initWebViewSetting()
        webView.webViewClient = object : MiLikeWebViewClient() {
            override fun interceptUrlLoading(view: WebView, url: String): Boolean {
                return arrayListOf(
                    "/infmt/zx/",
                    "/infmt/dt/",
                    "/estate/detail/index/",
                    "js://jstojava?cityCode="
                ).any { s -> url.contains(s) }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
        webView.addJavascriptInterface(MiLikeJavascriptInterface(webView), "android")
        loadUrl()
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FADB28"), Color.parseColor("#FFDA00"))
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.setOnRefreshListener {
            loadUrl()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun loadUrl() {
        loading.visibility = View.VISIBLE
        val cityCode = SPUtils.getInstance().getString("cityCode", "sz")
        webView.loadUrl("${UrlConstants.server}$cityCode/infmt/${getWebUrlSuffix()}")
    }
}