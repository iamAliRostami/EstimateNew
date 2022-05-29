package com.leon.estimate_new.utils.document;

import android.app.Activity;
import android.widget.Toast;

import com.leon.estimate_new.infrastructure.ICallbackError;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;

class GetErrorRedirect implements ICallbackError {
    private final Activity activity;

    GetErrorRedirect(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void executeError(Throwable t) {
        final CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(activity);
        final String error = customErrorHandlingNew.getErrorMessageTotal(t);
        new CustomToast().error(error, Toast.LENGTH_LONG);
        activity.finish();
    }
}
