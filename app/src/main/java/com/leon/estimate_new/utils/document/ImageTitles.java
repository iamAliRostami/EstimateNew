package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.activities.DocumentActivity;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
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
                new GetImageTitles(activity, object), new GetImageTitlesIncomplete(activity),
                new GetErrorRedirect(activity));
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class GetImageTitles implements ICallback<ImageDataTitle> {
    private final Activity activity;
    private final Object object;

    GetImageTitles(Activity activity, Object object) {
        this.activity = activity;
        this.object = object;
    }

    @Override
    public void execute(Response<ImageDataTitle> response) {
        if (response.body() != null && response.body().success) {
            ((DocumentActivity) object).setTitles(response.body());
            //TODO
//            images.addAll(CustomFile.loadImage(dataBase, trackNumber, billId, title, context));
//            imageViewAdapter.notifyDataSetChanged();
        } else {
            new CustomToast().error(activity.getString(R.string.error_call_backup),
                    Toast.LENGTH_LONG);
            activity.finish();
        }
    }
}

class GetImageTitlesIncomplete implements ICallbackIncomplete<ImageDataTitle> {
    private final Activity activity;

    GetImageTitlesIncomplete(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void executeIncomplete(Response<ImageDataTitle> response) {
        new CustomToast().warning(activity.getString(R.string.error_not_auth), Toast.LENGTH_LONG);
        activity.finish();
    }
}