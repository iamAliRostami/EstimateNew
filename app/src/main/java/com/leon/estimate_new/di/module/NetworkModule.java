package com.leon.estimate_new.di.module;

import static com.leon.estimate_new.helpers.MyApplication.getPreferenceManager;

import com.google.gson.Gson;
import com.leon.estimate_new.di.view_model.NetworkHelperModel;
import com.leon.estimate_new.enums.SharedReferenceKeys;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

//@Singleton
@Module
public class NetworkModule {
    NetworkHelperModel networkHelperModel;

    public NetworkModule() {
        networkHelperModel = new NetworkHelperModel();
    }

//    @Singleton
    @Provides
    public Gson providesGson() {
        return networkHelperModel.getGson();
    }

//    @Singleton
    @Provides
    public Retrofit providesRetrofit() {
        return networkHelperModel.getInstance(getPreferenceManager()
                .getStringData(SharedReferenceKeys.TOKEN.getValue()));
    }

//    @Singleton
    @Provides
    public NetworkHelperModel providesNetworkHelperModel() {
        return networkHelperModel;
    }
}
