package com.leon.estimate_new.utils.estimating;

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
import com.leon.estimate_new.fragments.dialog.ShowDocumentFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.ImageDataThumbnail;
import com.leon.estimate_new.utils.CustomErrorHandling;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageThumbnailList extends BaseAsync {
    private final String key;
    private final Object object;

    public ImageThumbnailList(Context context, String key, Object... view) {
        super(context, false, view);
        this.key = key;
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
        final Retrofit retrofit = getApplicationComponent().Retrofit();
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        final Call<ImageDataThumbnail> call = abfaService
                .getDocsListThumbnail(getApplicationComponent().SharedPreferenceModel()
                        .getStringData(TOKEN_FOR_FILE.getValue()), key);
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new GetList(object), new GetListIncomplete(object), new GetError());
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class GetList implements ICallback<ImageDataThumbnail> {
    private final Object object;

    GetList(Object object) {
        this.object = object;
    }

    @Override
    public void execute(Response<ImageDataThumbnail> response) {
        if (response.body() != null && response.body().success) {
            ((ShowDocumentFragment) object).setThumbnails(response.body());
        } else {
            new CustomDialogModel(Yellow, ((ShowDocumentFragment) object).requireContext(),
                    ((ShowDocumentFragment) object).requireContext().getString(R.string.download_document)
                            .concat("\n").concat(response.body().error),
                    ((ShowDocumentFragment) object).requireContext().getString(R.string.dear_user),
                    ((ShowDocumentFragment) object).requireContext().getString(R.string.download_document),
                    ((ShowDocumentFragment) object).requireContext().getString(R.string.accepted));
            ((ShowDocumentFragment) object).getProgressBar().setVisibility(View.GONE);
        }
    }
}

class GetListIncomplete implements ICallbackIncomplete<ImageDataThumbnail> {
    private final Object object;

    public GetListIncomplete(Object object) {
        this.object = object;
    }

    @Override
    public void executeIncomplete(Response<ImageDataThumbnail> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(((ShowDocumentFragment) object).requireContext());
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, ((ShowDocumentFragment) object).requireContext(), error,
                ((ShowDocumentFragment) object).requireContext().getString(R.string.dear_user),
                ((ShowDocumentFragment) object).requireContext().getString(R.string.download_document),
                ((ShowDocumentFragment) object).requireContext().getString(R.string.accepted));
        ((ShowDocumentFragment) object).getProgressBar().setVisibility(View.GONE);
    }
}

