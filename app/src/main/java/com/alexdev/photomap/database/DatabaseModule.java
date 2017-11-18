package com.alexdev.photomap.database;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.alexdev.photomap.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @NonNull
    @Singleton
    public AppDatabase provideAppDatabase(@NonNull Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, BuildConfig.APP_DATABASE_NAME).build();

    }

    @Provides
    @NonNull
    public DatabaseManager provideDatabaseManager(@NonNull AppDatabase appDatabase) {
        return new DatabaseManagerImpl(appDatabase);
    }
}
