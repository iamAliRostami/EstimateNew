package com.leon.estimate_new.di.module;

import android.content.Context;

import com.leon.estimate_new.di.view_model.FlashViewModel;

import dagger.Module;
import dagger.Provides;

//@Singleton
@Module
public class FlashModule {
    private final FlashViewModel flashViewModel;

    public FlashModule(Context context) {
        this.flashViewModel = new FlashViewModel(context);
    }

//    @Singleton
    @Provides
    public FlashViewModel providesFlashViewModel() {
        return flashViewModel;
    }
}
