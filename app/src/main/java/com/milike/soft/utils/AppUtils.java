package com.milike.soft.utils;

import android.app.Application;

public final class AppUtils {

    private static Application sApplication;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        sApplication = app;
    }

    public static Application getApp() {
        return sApplication;
    }
}
