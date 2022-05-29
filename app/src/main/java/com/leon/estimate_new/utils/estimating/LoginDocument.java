package com.leon.estimate_new.utils.estimating;

import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.PASSWORD_TEMP;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.enums.SharedReferenceKeys.USERNAME_TEMP;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.dialog.ShowDocumentFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.Login;
import com.leon.estimate_new.utils.Crypto;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginDocument extends BaseAsync {
    private final Object object;

    public LoginDocument(Context context, Object... view) {
        super(context, false, view);
        object = view[0];
    }

    @Override
    public void postTask(Object o) {

    }

    @Override
    public void preTask(Object o) {

    }

    @Override
    public void backgroundTask(Activity activity) {
        final Retrofit retrofit = getApplicationComponent().NetworkHelperModel().getInstance();
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        final Call<Login> call = abfaService.login2(getApplicationComponent().SharedPreferenceModel()
                .getStringData(USERNAME_TEMP.getValue()), Crypto.decrypt(getApplicationComponent()
                .SharedPreferenceModel().getStringData(PASSWORD_TEMP.getValue())));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new LoginSuccess(object), new LoginIncomplete(activity), new GetErrorRedirect(activity));
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class LoginSuccess implements ICallback<Login> {
    private final Object object;

    LoginSuccess(Object object) {
        this.object = object;
    }

    @Override
    public void execute(Response<Login> response) {
        if (response.body() != null && response.body().success) {
            getApplicationComponent().SharedPreferenceModel().putData(TOKEN_FOR_FILE.getValue(),
                    response.body().data.token);
            ((ShowDocumentFragment) object).successLogin();
        } else {
            new CustomToast().warning(((ShowDocumentFragment) object).requireContext()
                    .getString(R.string.error_not_auth), Toast.LENGTH_LONG);
            ((ShowDocumentFragment) object).dismiss();
        }
    }
}

class LoginIncomplete implements ICallbackIncomplete<Login> {
    private final Object object;

    LoginIncomplete(Object object) {
        this.object = object;
    }

    @Override
    public void executeIncomplete(Response<Login> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(((ShowDocumentFragment) object).requireContext());
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomToast().error(error, Toast.LENGTH_LONG);
        ((ShowDocumentFragment) object).dismiss();
    }
}

