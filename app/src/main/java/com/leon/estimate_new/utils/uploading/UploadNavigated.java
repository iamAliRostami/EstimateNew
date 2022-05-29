package com.leon.estimate_new.utils.uploading;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
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
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.CalculationUserInput;
import com.leon.estimate_new.tables.CalculationUserInputSend;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.utils.CustomErrorHandling;
import com.leon.estimate_new.utils.CustomToast;
import com.leon.estimate_new.utils.SimpleMessage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadNavigated extends BaseAsync {

    public UploadNavigated(Context context, Object... view) {
        super(context, false, view);
    }

    @Override
    public void postTask(Object o) {
        ((Button) o).setEnabled(true);
    }

    @Override
    public void preTask(Object o) {
        ((Button) o).setEnabled(false);
    }

    @Override
    public void backgroundTask(Activity activity) {
        final ArrayList<CalculationUserInput> calculationUserInputs =
                new ArrayList<>(getApplicationComponent().MyDatabase().calculationUserInputDao()
                        .getCalculationUserInput());
        if (calculationUserInputs.size() > 0) {
            final ArrayList<CalculationUserInputSend> calculationUserInputSends = new ArrayList<>();
            for (int i = 0; i < calculationUserInputs.size(); i++) {
                final ExaminerDuties examinerDuties = getApplicationComponent().MyDatabase()
                        .examinerDutiesDao().examinerDutiesByTrackNumber(calculationUserInputs.get(i).trackNumber);
                final CalculationUserInputSend calculationUserInputSend =
                        new CalculationUserInputSend(calculationUserInputs.get(i), examinerDuties);
                calculationUserInputSends.add(calculationUserInputSend);
            }
            final Retrofit retrofit = getApplicationComponent().Retrofit();
            final IAbfaService abfaService = retrofit.create(IAbfaService.class);
            final Call<SimpleMessage> call = abfaService.setExaminationInfo(calculationUserInputSends);
            HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                    new Calculation(calculationUserInputs, activity),
                    new CalculationIncomplete(activity), new GetError());
        } else {
            getApplicationComponent().CustomProgressModel().cancelDialog();
            new CustomToast().warning(activity.getString(R.string.empty_masir), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void backgroundTask(Context context) {
    }
}

class Calculation implements ICallback<SimpleMessage> {
    private final ArrayList<CalculationUserInput> calculationUserInputSends;
    private final Activity activity;

    Calculation(ArrayList<CalculationUserInput> calculationUserInputSends, Activity activity) {
        this.calculationUserInputSends = calculationUserInputSends;
        this.activity = activity;
    }

    @Override
    public void execute(Response<SimpleMessage> response) {
        for (CalculationUserInput calculationUserInput : calculationUserInputSends) {
            getApplicationComponent().MyDatabase().calculationUserInputDao()
                    .updateCalculationUserInput(true, calculationUserInput.trackNumber);
        }
        if (getApplicationComponent().MyDatabase().imagesDao().getUnsentImage() > 0)
            new LoginDocument(activity).execute(activity);
    }
}

class CalculationIncomplete implements ICallbackIncomplete<SimpleMessage> {
    private final Context context;

    CalculationIncomplete(Context context) {
        this.context = context;
    }

    @Override
    public void executeIncomplete(Response<SimpleMessage> response) {
        final CustomErrorHandling errorHandling = new CustomErrorHandling(context);
        final String error = errorHandling.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, context, error, context.getString(R.string.dear_user),
                context.getString(R.string.download), context.getString(R.string.accepted));
    }
}

