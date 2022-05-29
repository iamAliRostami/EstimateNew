package com.leon.estimate_new.di.view_model;

import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.utils.PermissionManager.isNetworkAvailable;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.estimate_new.R;
import com.leon.estimate_new.enums.ProgressType;
import com.leon.estimate_new.infrastructure.ICallback;
import com.leon.estimate_new.infrastructure.ICallbackError;
import com.leon.estimate_new.infrastructure.ICallbackIncomplete;
import com.leon.estimate_new.utils.CustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpClientWrapper {
    public static Call call;
    public static CustomProgressModel progressCancelable;
    public static boolean cancel;

    public static <T> void callHttpAsync(final Call<T> call, final int progressType,
                                         final Context context,
                                         final ICallback<T> callback,
                                         final ICallbackIncomplete<T> callbackIncomplete,
                                         final ICallbackError callbackError) {
        cancel = false;
        CustomProgressModel progress = getApplicationComponent().CustomProgressModel();
        try {
            if (progressType == ProgressType.SHOW.getValue()) {
                progress.show(context, context.getString(R.string.waiting));
            } else if (progressType == ProgressType.SHOW_CANCELABLE.getValue()) {
                progress.show(context, true, context.getString(R.string.waiting));
            } else if (progressType == ProgressType.SHOW_CANCELABLE_REDIRECT.getValue()) {
                progress.show(context, true, context.getString(R.string.waiting));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
        }

        if (isNetworkAvailable(context)) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (!cancel) {
                        if (progress.getDialog() != null)
                            try {
                                progress.getDialog().dismiss();
                            } catch (Exception e) {
                                new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
                            }
                        if (response.isSuccessful()) {
                            callback.execute(response);
                        } else {
                            ((Activity) context).runOnUiThread(() -> callbackIncomplete.executeIncomplete(response));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    if (!cancel) {
                        ((Activity) context).runOnUiThread(() -> callbackError.executeError(t));
                        if (progress.getDialog() != null)
                            try {
                                progress.getDialog().dismiss();
                            } catch (Exception e) {
                                new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
                            }
                    }
                }
            });
            HttpClientWrapper.call = call;
        } else {
            if (progress.getDialog() != null)
                try {
                    progress.getDialog().dismiss();
                } catch (Exception e) {
                    new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
                }
            new CustomToast().warning(context.getString(R.string.turn_internet_on));
        }
    }

    public static <T> void callHttpAsync(final Call<T> call,
                                         final Context context,
                                         final ICallback<T> callback,
                                         final ICallbackIncomplete<T> callbackIncomplete,
                                         final ICallbackError callbackError) {
        cancel = false;

        if (isNetworkAvailable(context)) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (!cancel) {
                        if (response.isSuccessful()) {
                            callback.execute(response);
                        } else {
                            ((Activity) context).runOnUiThread(() -> callbackIncomplete.executeIncomplete(response));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    if (!cancel) {
                        ((Activity) context).runOnUiThread(() -> callbackError.executeError(t));
                    }
                }
            });
            HttpClientWrapper.call = call;
        } else {
            new CustomToast().warning(context.getString(R.string.turn_internet_on));
        }
    }

    public static <T> void callHttpAsyncProgressDismiss(Call<T> call, int progressType,
                                                        final Context context,
                                                        final ICallback<T> callback,
                                                        final ICallbackIncomplete<T> callbackIncomplete,
                                                        final ICallbackError callbackError) {

        progressCancelable = getApplicationComponent().CustomProgressModel();
        if (progressType == ProgressType.SHOW.getValue()) {
            progressCancelable.show(context, context.getString(R.string.waiting));
        } else if (progressType == ProgressType.SHOW_CANCELABLE.getValue()) {
            progressCancelable.show(context, true, context.getString(R.string.waiting));
        } else if (progressType == ProgressType.SHOW_CANCELABLE_REDIRECT.getValue()) {
            progressCancelable.show(context, true, context.getString(R.string.waiting));
        }
        if (isNetworkAvailable(context)) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (progressCancelable.getDialog() != null)
                        progressCancelable.getDialog().dismiss();
                    if (response.isSuccessful()) {
                        callback.execute(response);
                    } else {
                        ((Activity) context).runOnUiThread(() -> callbackIncomplete.executeIncomplete(response));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    if (progressCancelable.getDialog() != null)
                        progressCancelable.getDialog().dismiss();
                    ((Activity) context).runOnUiThread(() -> callbackError.executeError(t));
                }
            });
            HttpClientWrapper.call = call;
        } else {
            if (progressCancelable.getDialog() != null)
                progressCancelable.getDialog().dismiss();
            new CustomToast().warning(context.getString(R.string.turn_internet_on));
        }
    }
}
