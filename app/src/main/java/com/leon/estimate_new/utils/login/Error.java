package com.leon.estimate_new.utils.login;

import android.app.Activity;
import android.widget.Toast;

import com.leon.estimate_new.infrastructure.ICallbackError;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;


class Error implements ICallbackError {
    private final Activity activity;

    public Error(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void executeError(Throwable t) {
        CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(activity);
        String error = customErrorHandlingNew.getErrorMessageTotal(t);
        new CustomToast().error(error, Toast.LENGTH_LONG);
    }
}
