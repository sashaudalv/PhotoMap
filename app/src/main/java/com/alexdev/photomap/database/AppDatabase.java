package com.alexdev.photomap.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alexdev.photomap.BuildConfig;
import com.alexdev.photomap.database.dao.PhotoDao;
import com.alexdev.photomap.database.dao.UserDao;
import com.alexdev.photomap.database.entities.Photo;
import com.alexdev.photomap.database.entities.User;


@Database(entities = {User.class, Photo.class}, version = BuildConfig.APP_DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract PhotoDao photoDao();

}
