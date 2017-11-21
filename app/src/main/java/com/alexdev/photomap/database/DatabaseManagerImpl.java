package com.alexdev.photomap.database;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.alexdev.photomap.database.callbacks.FavoritesLoadListener;
import com.alexdev.photomap.database.callbacks.PhotoLoadListener;
import com.alexdev.photomap.database.callbacks.PhotoSaveListener;
import com.alexdev.photomap.database.callbacks.UserLoadListener;
import com.alexdev.photomap.database.callbacks.UserSaveListener;
import com.alexdev.photomap.database.entities.UserPhotoPair;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class DatabaseManagerImpl implements DatabaseManager {

    private final AppDatabase mDatabase;

    public DatabaseManagerImpl(@NonNull AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public void saveUser(User user) {
        com.alexdev.photomap.database.entities.User userEntity = new com.alexdev.photomap.database.entities.User(user);
        Completable.fromAction(() -> mDatabase.userDao().insertUser(userEntity))
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();
    }

    @Override
    public void saveUser(User user, UserSaveListener listener) {
        com.alexdev.photomap.database.entities.User userEntity = new com.alexdev.photomap.database.entities.User(user);
        Completable.fromAction(() -> mDatabase.userDao().insertUser(userEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe(listener::onUserSaveComplete);

    }

    @Override
    public void savePhoto(Photo photo, @Nullable PhotoSaveListener listener) {
        com.alexdev.photomap.database.entities.Photo photoEntity = new com.alexdev.photomap.database.entities.Photo(photo);
        if (photoEntity.getSaving_date() == 0) photoEntity.setSavingDateAsCurrentTime();
        Single.fromCallable(() -> mDatabase.photoDao().insertPhoto(photoEntity))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        photoId -> {
                            if (listener != null) getPhoto(photoId.intValue(), listener::onPhotoSaveComplete);
                        }
                );
    }

    @Override
    public void saveToFavorites(User user, Photo photo) {
        com.alexdev.photomap.database.entities.User userEntity = new com.alexdev.photomap.database.entities.User(user);
        Completable.fromAction(() -> mDatabase.userDao().insertUser(userEntity))
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe(() -> savePhoto(photo, null));
    }

    @Override
    public void saveToFavorites(Pair<User, Photo> pair) {
        saveToFavorites(pair.first, pair.second);
    }

    @Override
    public void deleteFromFavorites(User user, Photo photo) {
        com.alexdev.photomap.database.entities.User userEntity = new com.alexdev.photomap.database.entities.User(user);
        com.alexdev.photomap.database.entities.Photo photoEntity = new com.alexdev.photomap.database.entities.Photo(photo);
        Completable.fromAction(() -> mDatabase.photoDao().deletePhoto(photoEntity))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Completable.fromAction(() -> mDatabase.userDao().deleteUser(userEntity))
                                .subscribeOn(Schedulers.io())
                                .onErrorComplete()
                                .subscribe()
                );
    }

    @Override
    public void deleteFromFavorites(Pair<User, Photo> pair) {
        deleteFromFavorites(pair.first, pair.second);
    }

    @Override
    public void getUser(long userSocialId, UserLoadListener listener) {
        mDatabase.userDao().getUserBySocialId(userSocialId)
                .subscribeOn(Schedulers.io())
                .map(com.alexdev.photomap.database.entities.User::asUserModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listener::onUserLoadComplete,
                        error -> listener.onUserLoadError()
                );
    }

    @Override
    public void getPhoto(int photoId, PhotoLoadListener listener) {
        mDatabase.photoDao().getPhotoById(photoId)
                .subscribeOn(Schedulers.io())
                .map(com.alexdev.photomap.database.entities.Photo::asPhotoModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listener::onPhotoLoadComplete
                );
    }

    @Override
    public void getFavorites(FavoritesLoadListener listener) {
        mDatabase.photoDao().getAllUserAndPhotoPairs()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .toObservable()
                .flatMapIterable(pairs -> pairs)
                .map(UserPhotoPair::getAsPair)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pairs -> {
                            if (listener.isListenerVisible())
                                listener.onFavoritesLoadComplete(pairs);
                        }
                );
    }

}
