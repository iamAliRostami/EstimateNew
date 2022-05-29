package com.leon.estimate_new.di.module;

import android.content.Context;

import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.utils.custom_dialog.LovelyStandardDialog;

import dagger.Module;
import dagger.Provides;

//@Singleton
@Module
public class CustomDialogModule {
    private final LovelyStandardDialog lovelyStandardDialog;

    public CustomDialogModule(Context context) {
        CustomDialogModel customDialogModel = new CustomDialogModel(context);
        this.lovelyStandardDialog = customDialogModel.getLovelyStandardDialog();
    }

//    @Singleton
    @Provides
    public LovelyStandardDialog provideLovelyStandardDialog() {
        return lovelyStandardDialog;
    }
}
