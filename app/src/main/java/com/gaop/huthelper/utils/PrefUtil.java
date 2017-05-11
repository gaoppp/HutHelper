package com.gaop.huthelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 偏好设置工具
 * Created by gaop1 on 2016/8/30.
 */
public class PrefUtil {
    public static final String PREF_NAME = "config";
    //得到缓存
    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                 Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }
    //设置缓存
    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
}
