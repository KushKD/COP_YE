package com.kushkumardhawan.inspections;

import android.app.Application;

import com.kushkumardhawan.locationmanager.LocationManager;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager.enableLog(true);
    }
}
