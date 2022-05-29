package com.leon.estimate_new.di.module;

import android.app.Activity;

import com.leon.estimate_new.di.view_model.LocationTrackingGoogle;
import com.leon.estimate_new.di.view_model.LocationTrackingGps;
import com.leon.estimate_new.utils.CheckSensor;

import dagger.Module;
import dagger.Provides;

//@Singleton
@Module
public class LocationTrackingModule {
    private LocationTrackingGoogle locationTrackingGoogle;
    private LocationTrackingGps locationTrackingGps;

    public LocationTrackingModule(Activity activity) {
        if (CheckSensor.checkSensor(activity))
            locationTrackingGoogle = LocationTrackingGoogle.getInstance(activity);
        else
            locationTrackingGps = LocationTrackingGps.getInstance();
    }

//    @Singleton
    @Provides
    public LocationTrackingGps providesLocationTrackingGps() {
        return locationTrackingGps;
    }


//    @Singleton
    @Provides
    public LocationTrackingGoogle providesLocationTrackingGoogle() {
        return locationTrackingGoogle;
    }
}
