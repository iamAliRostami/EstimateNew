package com.leon.estimate_new.utils.login;

import android.app.Activity;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.LoginFeedBack;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

class Incomplete implements ICallbackIncomplete<LoginFeedBack> {
    private final Activity activity;

    public Incomplete(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void executeIncomplete(Response<LoginFeedBack> response) {
        CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(activity);
        String error = customErrorHandlingNew.getErrorMessageDefault(response);
        if (response.code() == 401 || response.code() == 400) {
            error = activity.getString(R.string.error_is_not_match);
            if (response.errorBody() != null) {
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    error = jObjError.getString("message");
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new CustomToast().warning(error, Toast.LENGTH_LONG);
    }
}
