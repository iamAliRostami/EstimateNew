package com.leon.estimate_new.utils.uploading;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.tables.UploadImage;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomFile;
import com.leon.estimate_new.utils.CustomToast;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadDocuments extends BaseAsync {

    public UploadDocuments(Context context, Object... view) {
        super(context, false, view);
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
        final Images images = CustomFile.getImage(getApplicationComponent().MyDatabase().imagesDao()
                .getImage(), activity);
        if (images != null) {
            final MultipartBody.Part body = CustomFile.bitmapToFile(images.bitmap, activity);
            final Call<UploadImage> call;
            if (images.trackingNumber.length() > 0)
                call = abfaService.uploadDocNew(getApplicationComponent().SharedPreferenceModel()
                                .getStringData(TOKEN_FOR_FILE.getValue()), body,
                        Integer.parseInt(images.docId), images.trackingNumber);
            else
                call = abfaService.uploadDoc(getApplicationComponent().SharedPreferenceModel()
                                .getStringData(TOKEN_FOR_FILE.getValue()), body,
                        Integer.parseInt(images.docId), images.billId);
            HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                    new DocumentsSuccess(activity, images.imageId),
                    new DocumentsIncomplete(activity), new GetError());
        }
    }

    @Override
    public void backgroundTask(Context context) {
    }
}

class DocumentsSuccess implements ICallback<UploadImage> {
    private final Activity activity;
    private final int imageId;

    DocumentsSuccess(Activity activity, int imageId) {
        this.activity = activity;
        this.imageId = imageId;
    }

    @Override
    public void execute(Response<UploadImage> response) {
        if (response.body() != null && response.body().success) {
            getApplicationComponent().MyDatabase().imagesDao().deleteByID(imageId);
            if (getApplicationComponent().MyDatabase().imagesDao().getUnsentImage() > 0)
                new UploadDocuments(activity).execute(activity);
        } else
            new CustomDialogModel(Yellow, activity, activity.getString(R.string.error_upload)
                    .concat("\n").concat(response.body().error),
                    activity.getString(R.string.dear_user),
                    activity.getString(R.string.upload_image),
                    activity.getString(R.string.accepted));
    }
}

class DocumentsIncomplete implements ICallbackIncomplete<UploadImage> {
    private final Context context;

    DocumentsIncomplete(Context context) {
        this.context = context;
    }

    @Override
    public void executeIncomplete(Response<UploadImage> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomToast().error(error, Toast.LENGTH_LONG);
    }
}