package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.dialog.HighQualityFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackError;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.Uri;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageHighQuality extends BaseAsync {
    private final String uri;

    public ImageHighQuality(Context context, String uri, Object... view) {
        super(context, false, view);
        this.uri = uri;
    }

    @Override
    public void postTask(Object o) {

    }

    @Override
    public void preTask(Object o) {

    }

    @Override
    public void backgroundTask(Activity activity) {

    }

    @Override
    public void backgroundTask(Context context) {
        final Retrofit retrofit = getApplicationComponent().Retrofit();
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        final Call<ResponseBody> call = abfaService.getDoc(getApplicationComponent()
                .SharedPreferenceModel().getStringData(TOKEN_FOR_FILE.getValue()), new Uri(uri));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), context, new GetImageDocHigh(context),
                new GetImageDocHighIncomplete(context), new GetHighError(context));
    }
}

class GetImageDocHigh implements ICallback<ResponseBody> {
    private final Context context;

    GetImageDocHigh(Context context) {
        this.context = context;
    }

    @Override
    public void execute(Response<ResponseBody> response) {
        if (response.body() != null) {
            final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            if (bitmap != null)
                ShowFragmentDialogOnce(context, "HIGH_QUALITY_FRAGMENT",
                        HighQualityFragment.newInstance(bitmap));
        }
    }
}

class GetImageDocHighIncomplete implements ICallbackIncomplete<ResponseBody> {
    private final Context context;

    GetImageDocHighIncomplete(Context context) {
        this.context = context;
    }

    @Override
    public void executeIncomplete(Response<ResponseBody> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, context, error,
                context.getString(R.string.dear_user),
                context.getString(R.string.download_document),
                context.getString(R.string.accepted));
    }
}

class GetHighError implements ICallbackError {
    private final Context context;

    GetHighError(Context context) {
        this.context = context;
    }

    @Override
    public void executeError(Throwable t) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageTotal(t);
        new CustomToast().error(error, Toast.LENGTH_LONG);
    }
}