package com.leon.estimate_new.di.module;

import android.content.Context;

import com.leon.estimate_new.di.view_model.SharedPreferenceManagerModel;
import com.leon.estimate_new.enums.SharedReferenceNames;

import dagger.Module;
import dagger.Provides;

//@Singleton
@Module
public class SharedPreferenceModule {
    private final SharedPreferenceManagerModel sharedPreferencemanagerModel;

    public SharedPreferenceModule(Context context, SharedReferenceNames sharedReferenceNames) {
        sharedPreferencemanagerModel = SharedPreferenceManagerModel.getInstance(context, sharedReferenceNames.getValue());
    }

//    @Singleton
    @Provides
    public SharedPreferenceManagerModel providesSharedPreferenceModel() {
        return sharedPreferencemanagerModel;
    }
}
