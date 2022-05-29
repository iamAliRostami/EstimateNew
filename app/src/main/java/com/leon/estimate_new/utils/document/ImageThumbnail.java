package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.documents.TakePhotoFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.Uri;
import com.leon.estimate_new.utils.CustomErrorHandling;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageThumbnail extends BaseAsync {
    private final String uri;
    private final Object object;

    public ImageThumbnail(String img, Object... view) {
        super(false);
        this.uri = img;
        this.object = view[0];
    }

    @Override
    public void postTask(Object o) {

    }

    @Override
    public void preTask(Object o) {
    }

    @Override
    public void backgroundTask(Activity activity) {
        final Retrofit retrofit = getApplicationComponent().Retrofit();
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        final Call<ResponseBody> call = abfaService.getDoc(getApplicationComponent().SharedPreferenceModel()
                .getStringData(TOKEN_FOR_FILE.getValue()), new Uri(uri));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new GetImageDoc(object), new GetImageDocIncomplete(object),
                new GetError(activity, object));
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class GetImageDoc implements ICallback<ResponseBody> {
    private final Object object;

    GetImageDoc(Object object) {
        this.object = object;
    }

    @Override
    public void execute(Response<ResponseBody> response) {
        ((TakePhotoFragment) object).setImage(response.body());
    }
}

class GetImageDocIncomplete implements ICallbackIncomplete<ResponseBody> {
    private final Object object;

    public GetImageDocIncomplete(Object object) {
        this.object = object;
    }

    @Override
    public void executeIncomplete(Response<ResponseBody> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(((TakePhotoFragment) object).requireContext());
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, ((TakePhotoFragment) object).requireContext(), error,
                ((TakePhotoFragment) object).requireContext().getString(R.string.dear_user),
                ((TakePhotoFragment) object).requireContext().getString(R.string.download_document),
                ((TakePhotoFragment) object).requireContext().getString(R.string.accepted));
        ((TakePhotoFragment) object).getProgressBar().setVisibility(View.GONE);
    }
}