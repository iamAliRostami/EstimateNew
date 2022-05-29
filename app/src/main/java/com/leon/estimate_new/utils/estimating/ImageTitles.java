package com.leon.estimate_new.utils.estimating;

import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
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
import com.leon.estimate_new.tables.ImageDataTitle;
import com.leon.estimate_new.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageTitles extends BaseAsync {
    private final Object object;

    public ImageTitles(Context context, Object... view) {
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
        final Call<ImageDataTitle> call = abfaService.getTitle(getApplicationComponent()
                .SharedPreferenceModel().getStringData(TOKEN_FOR_FILE.getValue()));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new GetImageTitles(object), new GetImageTitlesIncomplete(object),
                new GetErrorRedirect(activity));
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class GetImageTitles implements ICallback<ImageDataTitle> {
    private final Object object;

    GetImageTitles(Object object) {
        this.object = object;
    }

    @Override
    public void execute(Response<ImageDataTitle> response) {
        if (response.body() != null && response.body().success) {
            ((ShowDocumentFragment) object).setTitles(response.body());
        } else {
            new CustomToast().error(((ShowDocumentFragment) object).requireContext()
                    .getString(R.string.error_call_backup), Toast.LENGTH_LONG);
            ((ShowDocumentFragment) object).dismiss();
        }
    }
}

class GetImageTitlesIncomplete implements ICallbackIncomplete<ImageDataTitle> {
    private final Object object;

    GetImageTitlesIncomplete(Object object) {
        this.object = object;
    }

    @Override
    public void executeIncomplete(Response<ImageDataTitle> response) {
        new CustomToast().warning(((ShowDocumentFragment) object).requireContext()
                .getString(R.string.error_not_auth), Toast.LENGTH_LONG);
        ((ShowDocumentFragment) object).dismiss();
    }
}