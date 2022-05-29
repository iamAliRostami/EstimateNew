package com.leon.estimate_new.di.component;

import com.google.gson.Gson;
import com.leon.estimate_new.di.module.CustomProgressModule;
import com.leon.estimate_new.di.module.FlashModule;
import com.leon.estimate_new.di.module.MyDatabaseModule;
import com.leon.estimate_new.di.module.NetworkModule;
import com.leon.estimate_new.di.module.SharedPreferenceModule;
import com.leon.estimate_new.di.view_model.CustomProgressModel;
import com.leon.estimate_new.di.view_model.FlashViewModel;
import com.leon.estimate_new.di.view_model.NetworkHelperModel;
import com.leon.estimate_new.di.view_model.SharedPreferenceManagerModel;
import com.leon.estimate_new.helpers.MyApplication;
import com.leon.estimate_new.utils.MyDatabase;

import dagger.Component;
import retrofit2.Retrofit;

//@Singleton
@Component(modules = {FlashModule.class, MyDatabaseModule.class, SharedPreferenceModule.class,
        NetworkModule.class, CustomProgressModule.class})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

    FlashViewModel FlashViewModel();

    MyDatabase MyDatabase();

    SharedPreferenceManagerModel SharedPreferenceModel();

    Gson Gson();

    Retrofit Retrofit();

    NetworkHelperModel NetworkHelperModel();

    CustomProgressModel CustomProgressModel();
}
