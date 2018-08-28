package com.hixel.hixel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static SharedPreferences preferences() {
        return mContext.getSharedPreferences("HIXEL", 0);
    }
}
