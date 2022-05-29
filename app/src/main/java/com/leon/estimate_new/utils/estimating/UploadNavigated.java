package com.leon.estimate_new.utils.estimating;

import static com.leon.estimate_new.enums.ProgressType.SHOW;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.helpers.MyApplication.getLocationTracker;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.leon.estimate_new.activities.FinalReportActivity;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.di.view_model.NetworkHelperModel;
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
    final ExaminerDuties examinerDuty;
    final int resultId;
    private final Object object;

    public UploadNavigated(ExaminerDuties examinerDuty, int resultId, Object... view) {
        super(false);
        this.examinerDuty = examinerDuty;
        this.resultId = resultId;
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
        //TODO
        getApplicationComponent().MyDatabase().examinerDutiesDao().updateExamination(true, examinerDuty.trackNumber);
        final CalculationUserInput calculationUserInput = getApplicationComponent().MyDatabase().calculationUserInputDao().getCalculationUserInput(examinerDuty.trackNumber);
        calculationUserInput.accuracy = getLocationTracker(activity).getAccuracy();
        calculationUserInput.y2 = getLocationTracker(activity).getLatitude();
        calculationUserInput.x2 = getLocationTracker(activity).getLongitude();
        calculationUserInput.resultId = resultId;
        calculationUserInput.ready = true;

        getApplicationComponent().MyDatabase().calculationUserInputDao().deleteByTrackNumber(examinerDuty.trackNumber);
        getApplicationComponent().MyDatabase().calculationUserInputDao().insertCalculationUserInput(calculationUserInput);

        final ArrayList<CalculationUserInputSend> calculationUserInputSends = new ArrayList<>();
        calculationUserInputSends.add(new CalculationUserInputSend(calculationUserInput, examinerDuty));

        final Retrofit retrofit = NetworkHelperModel.getInstance(activity);
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = abfaService.setExaminationInfo(calculationUserInputSends);
        HttpClientWrapper.callHttpAsync(call, SHOW.getValue(), activity,
                new Calculation(examinerDuty.trackNumber, object), new CalculationIncomplete(activity), new GetError());
    }

    @Override
    public void backgroundTask(Context context) {
    }
}

class Calculation implements ICallback<SimpleMessage> {
    private final String trackNumber;
    private final Object object;

    Calculation(String trackNumber, Object object) {
        this.trackNumber = trackNumber;
        this.object = object;
    }

    @Override
    public void execute(Response<SimpleMessage> response) {
        if (response.body() != null) {
            new CustomToast().success(response.body().message, Toast.LENGTH_LONG);
        }
        getApplicationComponent().MyDatabase().calculationUserInputDao().updateCalculationUserInput(true, trackNumber);
        ((FinalReportActivity) object).finish();
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
        new CustomToast().error(errorHandling.getErrorMessageDefault(response), Toast.LENGTH_LONG);
//        new CustomDialogModel(Yellow, context, errorHandling.getErrorMessageDefault(response),
//                context.getString(R.string.dear_user), context.getString(R.string.login),
//                context.getString(R.string.accepted));
    }
}