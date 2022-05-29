package com.leon.estimate_new.utils.document;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.DialogType.YellowRedirect;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TOKEN_FOR_FILE;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackError;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.AddDocument;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDocumentRadif extends BaseAsync {
    private final ExaminerDuties examinerDuty;

    public AddDocumentRadif(Context context, ExaminerDuties examinerDuty, Object... view) {
        super(context, false, view);
        this.examinerDuty = examinerDuty;
    }

    @Override
    public void postTask(Object o) {

    }

    @Override
    public void preTask(Object o) {
        ((Button) o).setEnabled(false);
    }

    @Override
    public void backgroundTask(Activity activity) {
        final Retrofit retrofit = getApplicationComponent().Retrofit();
        final IAbfaService iAbfaService = retrofit.create(IAbfaService.class);
        final Call<AddDocument> call = iAbfaService.addDocument(getApplicationComponent()
                        .SharedPreferenceModel().getStringData(TOKEN_FOR_FILE.getValue()),
                new AddDocument(examinerDuty.trackNumber, examinerDuty.firstName,
                        examinerDuty.sureName, examinerDuty.address, examinerDuty.zoneId));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new AddDocumentSuccess(activity), new AddDocumentIncomplete(activity), new GetError(activity));
    }

    @Override
    public void backgroundTask(Context context) {

    }

    class AddDocumentSuccess implements ICallback<AddDocument> {
        private final Context context;

        AddDocumentSuccess(Context context) {
            this.context = context;
        }

        @Override
        public void execute(Response<AddDocument> response) {
            if (response.body() != null && response.body().success) {
                new CustomToast().success(context.getString(R.string.add_successful), Toast.LENGTH_LONG);
            } else {
                new CustomDialogModel(Yellow, context, context.getString(R.string.error_add_document)
                        .concat("\n").concat(response.body().error), context.getString(R.string.dear_user),
                        context.getString(R.string.add_document), context.getString(R.string.accepted));
            }
        }
    }

    class AddDocumentIncomplete implements ICallbackIncomplete<AddDocument> {
        private final Context context;

        public AddDocumentIncomplete(Context context) {
            this.context = context;
        }

        @Override
        public void executeIncomplete(Response<AddDocument> response) {
            final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
            final String error = errorHandling.getErrorMessageDefault(response);
            new CustomDialogModel(Yellow, context, error, context.getString(R.string.dear_user),
                    context.getString(R.string.add_document), context.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        private final Context context;

        GetError(Context context) {
            this.context = context;
        }

        @Override
        public void executeError(Throwable t) {
            final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
            final String error = errorHandling.getErrorMessageTotal(t);
            new CustomDialogModel(YellowRedirect, context, error,
                    context.getString(R.string.dear_user), context.getString(R.string.add_document),
                    context.getString(R.string.accepted));
        }
    }
}
