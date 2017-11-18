package com.alexdev.photomap.database;


import android.support.annotation.NonNull;

public final class DatabaseManagerImpl implements DatabaseManager {

    private final AppDatabase mDatabase;
    
    public DatabaseManagerImpl(@NonNull AppDatabase database) {
        mDatabase = database;
    }



}
