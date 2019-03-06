package com.milike.soft.utils

import android.annotation.SuppressLint
import android.provider.Settings


fun getWebUrlSuffix(url: String): String {
    val a = url.contains("?")
    return StringBuilder(url).append(if (a) "&" else "?")
        .append("flag").append("=").append("milike").append("&")
        .append("mid").append("=").append(getAndroidID()).append("&")
        .append("tm").append("=").append(System.currentTimeMillis()).append("&")
        .append("appPlatform").append("=").append("android").append("&")
        .append("initial").append("=").append(SPUtils.getInstance().getInt("versionCode") == -1).append("&")
        .append("uuid").append("=").append(SPUtils.getInstance().getString("uuid")).append("&")
        .append("token").append("=").append(SPUtils.getInstance().getString("token"))
        .toString()
}

@SuppressLint("HardwareIds")
fun getAndroidID(): String {
    return Settings.Secure.getString(
        AppUtils.getApp().contentResolver,
        Settings.Secure.ANDROID_ID
    ) ?: ""
}