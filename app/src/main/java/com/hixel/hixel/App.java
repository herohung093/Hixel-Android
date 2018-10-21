package com.hixel.hixel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.facebook.stetho.Stetho;
import com.hixel.hixel.di.component.DaggerAppComponent;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Entry point for the application, creates the ActivityInjector for every subsequent activity,
 * and enables Dagger, and Stetho.
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

        Stetho.initializeWithDefaults(this);
        Timber.plant(new Timber.DebugTree());

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
