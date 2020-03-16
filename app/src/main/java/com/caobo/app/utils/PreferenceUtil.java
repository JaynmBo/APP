package com.caobo.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public final static String PRE_SHARE_APP = "pre_share_app";

    public static void write(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void write(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static void write(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static String readString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int readInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static Boolean readBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static Boolean readBooleanDefTrue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, true);
    }

    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_SHARE_APP,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

}
