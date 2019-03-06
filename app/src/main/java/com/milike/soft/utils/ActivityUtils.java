package com.milike.soft.utils;

import com.milike.soft.base.BaseActivity;

import java.util.ArrayList;

public class ActivityUtils {
    public static final ArrayList<BaseActivity> activityList = new ArrayList<>();

    public static boolean isActivityExistsInStack(Class<? extends BaseActivity> clazz) {
        for (BaseActivity a : activityList) {
            if (a != null && a.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
