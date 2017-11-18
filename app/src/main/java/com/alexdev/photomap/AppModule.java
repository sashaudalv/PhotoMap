package com.alexdev.photomap;


import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @NonNull
    private Context mContext;

    public AppModule(@NonNull Context context) {
        mContext = context;
    }

    @Provides
    @NonNull
    @Singleton
    public Context provideContext() {
        return mContext;
    }
}
