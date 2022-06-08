package com.leon.estimate_new.di.component;


import com.leon.estimate_new.di.module.CustomDialogModule;
import com.leon.estimate_new.di.module.LocationTrackingModule;
import com.leon.estimate_new.di.view_model.LocationTrackingGoogle;
import com.leon.estimate_new.di.view_model.LocationTrackingGps;
import com.leon.estimate_new.utils.custom_dialog.LovelyStandardDialog;

import dagger.Component;

@Component(modules = {CustomDialogModule.class, LocationTrackingModule.class})
public interface ActivityComponent {

    LovelyStandardDialog LovelyStandardDialog();

    LocationTrackingGps LocationTrackingGps();

    LocationTrackingGoogle LocationTrackingGoogle();

}
