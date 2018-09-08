package com.hixel.hixel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.hixel.hixel.di.component.DaggerAppComponent;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

/**
 *
 */

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @SuppressLint("StaticFieldLeak")
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    public static SharedPreferences preferences() {
        return context.getSharedPreferences("HIXEL", 0);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
