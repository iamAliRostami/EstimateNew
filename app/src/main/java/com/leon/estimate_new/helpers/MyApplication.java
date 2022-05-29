package com.leon.estimate_new.helpers;

import static com.leon.estimate_new.helpers.Constants.DATABASE_NAME;
import static com.leon.estimate_new.helpers.Constants.FONT_NAME;
import static com.leon.estimate_new.helpers.Constants.TOAST_TEXT_SIZE;
import static com.leon.estimate_new.helpers.Constants.activityComponent;
import static com.leon.estimate_new.helpers.Constants.appContext;
import static com.leon.estimate_new.helpers.Constants.applicationComponent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.leon.estimate_new.di.component.ActivityComponent;
import com.leon.estimate_new.di.component.ApplicationComponent;
import com.leon.estimate_new.di.component.DaggerActivityComponent;
import com.leon.estimate_new.di.component.DaggerApplicationComponent;
import com.leon.estimate_new.di.module.CustomDialogModule;
import com.leon.estimate_new.di.module.CustomProgressModule;
import com.leon.estimate_new.di.module.FlashModule;
import com.leon.estimate_new.di.module.LocationTrackingModule;
import com.leon.estimate_new.di.module.MyDatabaseModule;
import com.leon.estimate_new.di.module.NetworkModule;
import com.leon.estimate_new.di.module.SharedPreferenceModule;
import com.leon.estimate_new.enums.SharedReferenceNames;
import com.leon.estimate_new.infrastructure.ILocationTracking;
import com.leon.estimate_new.infrastructure.ISharedPreferenceManager;
import com.leon.estimate_new.utils.CheckSensor;

import es.dmoral.toasty.Toasty;

public class MyApplication extends Application {

    public static String getDBName() {
        return DATABASE_NAME;
    }

    public static Context getContext() {
        return appContext;
    }

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public static void setActivityComponent(Activity activity) {
        activityComponent = DaggerActivityComponent
                .builder()
                .customDialogModule(new CustomDialogModule(activity))
                .locationTrackingModule(new LocationTrackingModule(activity))
                .build();
    }

    public static ILocationTracking getLocationTracker(Activity activity) {
        return CheckSensor.checkSensor(activity) ?
                activityComponent.LocationTrackingGoogle() :
                activityComponent.LocationTrackingGps();
    }

    public static ISharedPreferenceManager getPreferenceManager() {
        return MyApplication.getApplicationComponent().SharedPreferenceModel();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        Toasty.Config.getInstance()
                .tintIcon(true)
                .setToastTypeface(Typeface.createFromAsset(appContext.getAssets(), FONT_NAME))
                .setTextSize(TOAST_TEXT_SIZE)
                .allowQueue(true).apply();
        setApplicationComponent();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void setApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .networkModule(new NetworkModule())
                .flashModule(new FlashModule(appContext))
                .customProgressModule(new CustomProgressModule())
                .myDatabaseModule(new MyDatabaseModule(appContext))
                .sharedPreferenceModule(new SharedPreferenceModule(appContext, SharedReferenceNames.ACCOUNT))
                .build();
        applicationComponent.inject(this);
    }
}
