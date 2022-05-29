package com.leon.estimate_new.utils.estimating;

import static com.leon.estimate_new.enums.DialogType.Yellow;
import static com.leon.estimate_new.enums.ProgressType.NOT_SHOW;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;

import com.leon.estimate_new.R;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.di.view_model.NetworkHelperModel;
import com.leon.estimate_new.fragments.dialog.ShowFragmentDialog;
import com.leon.estimate_new.fragments.dialog.ValueFragment;
import com.leon.estimate_new.fragments.forms.BaseInfoFragment;
import com.leon.estimate_new.infrastructure.IAbfaService;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.utils.CustomErrorHandling;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetArzeshdaraei extends BaseAsync {
    private final Object object;
    private final String zoneId;

    public GetArzeshdaraei(Context context, String zoneId, Object... o) {
        super(context, false, o);
        this.object = o[0];
        this.zoneId = zoneId;
    }

    @Override
    public void postTask(Object o) {
    }

    @Override
    public void preTask(Object o) {
    }

    @Override
    public void backgroundTask(Activity activity) {
        final Retrofit retrofit = NetworkHelperModel.getInstance(activity);
        final IAbfaService arzeshdaraei = retrofit.create(IAbfaService.class);
        Call<Arzeshdaraei> call = arzeshdaraei.getArzeshDaraii(Integer.parseInt(zoneId));
        HttpClientWrapper.callHttpAsync(call, NOT_SHOW.getValue(), activity,
                new ArzeshdaraeiSucceed(activity, object), new ArzeshdaraeiIncomplete(activity), new GetError());
    }

    @Override
    public void backgroundTask(Context context) {

    }
}

class ArzeshdaraeiSucceed implements ICallback<Arzeshdaraei> {
    private final Context context;
    private final Object object;

    public ArzeshdaraeiSucceed(Context context, Object object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public void execute(Response<Arzeshdaraei> response) {
        Arzeshdaraei arzeshdaraei = response.body();
        if (arzeshdaraei != null && arzeshdaraei.formulas != null &&
                arzeshdaraei.formulas.size() > 0 && arzeshdaraei.blocks != null
                && arzeshdaraei.blocks.size() > 0) {
            for (int i = 0; i < arzeshdaraei.formulas.size(); i++)
                getApplicationComponent().MyDatabase().formulaDao().insertFormula(arzeshdaraei.formulas.get(i));
            for (int i = 0; i < arzeshdaraei.blocks.size(); i++)
                getApplicationComponent().MyDatabase().blockDao().insertBlock(arzeshdaraei.blocks.get(i));
            for (int i = 0; i < arzeshdaraei.zaribs.size(); i++)
                getApplicationComponent().MyDatabase().zaribDao().insertZarib(arzeshdaraei.zaribs.get(i));
            ((BaseInfoFragment) object).setArzeshdaraei(arzeshdaraei);
            ShowFragmentDialog.ShowFragmentDialogOnce(context, "VALUE_FRAGMENT",
                    ValueFragment.newInstance(((BaseInfoFragment) object)));
        } else {
            new CustomDialogModel(Yellow, context, context.getString(R.string.error_value),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.arzesh_mantaghe),
                    context.getString(R.string.accepted));
        }
    }
}

class ArzeshdaraeiIncomplete implements ICallbackIncomplete<Arzeshdaraei> {
    private final Context context;

    public ArzeshdaraeiIncomplete(Context context) {
        this.context = context;
    }

    @Override
    public void executeIncomplete(Response<Arzeshdaraei> response) {
        CustomErrorHandling customErrorHandlingNew = new CustomErrorHandling(context);
        final String error = customErrorHandlingNew.getErrorMessageDefault(response);
        new CustomDialogModel(Yellow, context, error, context.getString(R.string.dear_user),
                context.getString(R.string.arzesh_mantaghe), context.getString(R.string.accepted));
    }
}

