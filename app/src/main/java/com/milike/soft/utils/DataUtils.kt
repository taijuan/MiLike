package com.milike.soft.utils

import android.net.Uri
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SPUtils

fun getWebUrlSuffix(url: String): String {
    val uri = Uri.parse(url)
    val query = uri.queryParameterNames
    val builder = Uri.Builder()
        .scheme(uri.scheme)
        .authority(uri.authority)
        .path(uri.path)
        .query(uri.query)
    if (!query.contains("flag")) {
        builder.appendQueryParameter("flag", "milike")
    }
    if (!query.contains("mid")) {
        builder.appendQueryParameter("mid", DeviceUtils.getAndroidID())
    }
    if (!query.contains("tm")) {
        builder.appendQueryParameter("tm", System.currentTimeMillis().toString())
    }
    if (!query.contains("appPlatform")) {
        builder.appendQueryParameter("appPlatform", "android")
    }
    if (!query.contains("initial")) {
        builder.appendQueryParameter("initial", (SPUtils.getInstance().getInt("versionCode") == -1).toString())
    }
    if (!query.contains("uuid")) {
        builder.appendQueryParameter("uuid", SPUtils.getInstance().getString("uuid"))
    }
    if (!query.contains("token")) {
        builder.appendQueryParameter("token", SPUtils.getInstance().getString("token"))
    }
    return builder.toString()
}
