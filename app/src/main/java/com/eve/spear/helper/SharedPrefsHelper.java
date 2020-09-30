package com.eve.spear.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    public static SharedPrefsHelper instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefsHelper(Context context) {
        sharedPreferences =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static SharedPrefsHelper getInstance() {
        return instance;
    }

    public static SharedPrefsHelper newInstance(Context context) {
        return instance = instance == null ? new SharedPrefsHelper(context) : instance;
    }

    public SharedPreferences getPref() {
        return sharedPreferences;
    }

}
