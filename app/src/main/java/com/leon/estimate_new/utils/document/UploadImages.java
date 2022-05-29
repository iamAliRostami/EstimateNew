package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.SHOW_CANCELABLE;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.helpers.Constants.IMAGE_FILE_NAME;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.utils.CustomFile.bitmapToFile;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.documents.TakePhotoFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackError;
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

public class UploadImages extends BaseAsync {
    private final Object object;

    public UploadImages(Object... view) {
        super(false);
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
        final MultipartBody.Part body = bitmapToFile(((TakePhotoFragment) object)
                .documentActivity.getBitmap(), activity);
        final Call<UploadImage> call;
        if (((TakePhotoFragment) object).documentActivity.isNew())
            call = abfaService.uploadDocNew(getApplicationComponent().SharedPreferenceModel()
                            .getStringData(TOKEN_FOR_FILE.getValue()), body,
                    ((TakePhotoFragment) object).getTitle().id, ((TakePhotoFragment) object)
                            .documentActivity.getTrackNumber());
        else
            call = abfaService.uploadDoc(getApplicationComponent().SharedPreferenceModel()
                            .getStringData(TOKEN_FOR_FILE.getValue()), body,
                    ((TakePhotoFragment) object).getTitle().id,
                    ((TakePhotoFragment) object).documentActivity.getBillId());
        HttpClientWrapper.callHttpAsync(call, SHOW_CANCELABLE.getValue(), activity,
                new UploadImageDoc(object, activity), new UploadImageIncomplete(object, activity),
                new UploadImageError(activity, object));
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class UploadImageDoc implements ICallback<UploadImage> {
    private final Object object;
    private final Context context;

    UploadImageDoc(Object object, Context context) {
        this.object = object;
        this.context = context;
    }

    @Override
    public void execute(Response<UploadImage> response) {
        if (response.body() != null && response.body().success) {
            new CustomToast().success(context.getString(R.string.upload_success), Toast.LENGTH_LONG);
            final Images image = new Images(IMAGE_FILE_NAME,
                    ((TakePhotoFragment) object).documentActivity.getBillId(),
                    ((TakePhotoFragment) object).documentActivity.getTrackNumber(),
                    ((TakePhotoFragment) object).getTitle().id,
                    ((TakePhotoFragment) object).getTitle().title,
                    ((TakePhotoFragment) object).documentActivity.getBitmap(), true);
            ((TakePhotoFragment) object).addImage(image);
        } else {
            new CustomDialogModel(Yellow, context, context.getString(R.string.error_upload)
                    .concat("\n").concat(response.body().error), context.getString(R.string.dear_user),
                    context.getString(R.string.upload_image), context.getString(R.string.accepted));
            CustomFile.saveTempBitmap(((TakePhotoFragment) object).documentActivity.getBitmap(),
                    context, ((TakePhotoFragment) object).documentActivity.getBillId(),
                    ((TakePhotoFragment) object).documentActivity.getTrackNumber(),
                    ((TakePhotoFragment) object).getTitle().id,
                    ((TakePhotoFragment) object).getTitle().title,
                    ((TakePhotoFragment) object).documentActivity.isNew());
        }
    }
}

class UploadImageIncomplete implements ICallbackIncomplete<UploadImage> {
    private final Object object;
    private final Context context;

    UploadImageIncomplete(Object object, Context context) {
        this.object = object;
        this.context = context;
    }

    @Override
    public void executeIncomplete(Response<UploadImage> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, context, error, context.getString(R.string.dear_user),
                context.getString(R.string.upload_image), context.getString(R.string.accepted));
        CustomFile.saveTempBitmap(((TakePhotoFragment) object).documentActivity.getBitmap(),
                context, ((TakePhotoFragment) object).documentActivity.getBillId(),
                ((TakePhotoFragment) object).documentActivity.getTrackNumber(),
                ((TakePhotoFragment) object).getTitle().id,
                ((TakePhotoFragment) object).getTitle().title,
                ((TakePhotoFragment) object).documentActivity.isNew());
    }
}

class UploadImageError implements ICallbackError {
    private final Context context;
    private final Object object;

    public UploadImageError(Context context, Object object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public void executeError(Throwable t) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageTotal(t);
        new CustomToast().error(error, Toast.LENGTH_LONG);

        CustomFile.saveTempBitmap(((TakePhotoFragment) object).documentActivity.getBitmap(),
                context, ((TakePhotoFragment) object).documentActivity.getBillId(),
                ((TakePhotoFragment) object).documentActivity.getTrackNumber(),
                ((TakePhotoFragment) object).getTitle().id,
                ((TakePhotoFragment) object).getTitle().title,
                ((TakePhotoFragment) object).documentActivity.isNew());

    }
}