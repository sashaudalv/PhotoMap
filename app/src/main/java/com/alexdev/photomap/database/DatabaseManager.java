package com.alexdev.photomap.database;


import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.alexdev.photomap.database.callbacks.FavoritesLoadListener;
import com.alexdev.photomap.database.callbacks.PhotoLoadListener;
import com.alexdev.photomap.database.callbacks.PhotoSaveListener;
import com.alexdev.photomap.database.callbacks.UserLoadListener;
import com.alexdev.photomap.database.callbacks.UserSaveListener;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;

public interface DatabaseManager {

    void saveUser(User user);

    void saveUser(User user, UserSaveListener listener);

    void savePhoto(Photo photo, @Nullable PhotoSaveListener listener);

    void saveToFavorites(User user, Photo photo);

    void saveToFavorites(Pair<User, Photo> pair);

    void deleteFromFavorites(User user, Photo photo);

    void deleteFromFavorites(Pair<User, Photo> pair);

    void getUser(long userSocialId, UserLoadListener listener);

    void getPhoto(int photoId, PhotoLoadListener listener);

    void getFavorites(FavoritesLoadListener listener);

}
