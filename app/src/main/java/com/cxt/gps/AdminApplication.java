package com.cxt.gps;

import android.app.Application;

import timber.log.Timber;

public class AdminApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
//        } else {
//            Timber.plant(new CrashReportingTree());
//        }
    }
}
