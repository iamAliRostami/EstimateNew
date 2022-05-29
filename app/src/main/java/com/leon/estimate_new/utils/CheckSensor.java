package com.leon.estimate_new.utils;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class CheckSensor {

    public static boolean checkSensor(Context context) {
        return checkGooglePlayServices(context);
    }

    public static boolean checkGooglePlayServices(Context context) {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
//            final String message;
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                message = context.getString(R.string.google_is_available_but_not_installed);
//            } else {
//                message = context.getString(R.string.google_is_not_available);
//            }
//            new CustomToast().warning(message);
            return false;
        }
        return true;
    }
}
