package com.alexdev.photomap.network;


import android.support.annotation.NonNull;

import com.alexdev.photomap.BuildConfig;
import com.alexdev.photomap.database.DatabaseManager;
import com.alexdev.photomap.database.callbacks.UserSaveListener;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.network.callbacks.PhotosLoadListener;
import com.alexdev.photomap.network.callbacks.UserLoadListener;
import com.alexdev.photomap.network.responses.PhotosResponseBody;
import com.alexdev.photomap.network.responses.Response;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class NetworkManagerImpl implements NetworkManager {

    public static final String VK_API_BASE_URL = "https://api.vk.com/method/";
    private static final String VK_API_SERVICE_KEY = BuildConfig.VK_API_SERVICE_KEY;
    private static final String VK_API_VERSION = BuildConfig.VK_API_VERSION;
    private static final String[] VK_USER_EXTRA_FIELDS = {"photo_400_orig"};
    private static final int PHOTOS_LOAD_LIMIT = 100;

    private enum VkSortTypes {BY_DATE, BY_LIKES_COUNT}

    private final VkApiService mVkApiService;
    private final DatabaseManager mDatabaseManager;

    public NetworkManagerImpl(@NonNull VkApiService vkApiService,
                              @NonNull DatabaseManager databaseManager) {
        mVkApiService = vkApiService;
        mDatabaseManager = databaseManager;
    }

    @Override
    public void loadUser(long userSocialId, UserLoadListener listener) {
        mVkApiService.getUsers(new long[]{userSocialId}, VK_USER_EXTRA_FIELDS, VK_API_VERSION, VK_API_SERVICE_KEY)
                .subscribeOn(Schedulers.io())
                .map(Response::getResponseBody)
                .flatMapIterable(users -> users)
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            if (listener.isListenerVisible()) listener.onUserLoadComplete(user);
                        },
                        error -> {
                            if (listener.isListenerVisible()) listener.onUserLoadError();
                        }
                );
    }

    @Override
    public void loadAndSaveUser(long userSocialId, UserSaveListener listener) {
        mVkApiService.getUsers(new long[]{userSocialId}, VK_USER_EXTRA_FIELDS, VK_API_VERSION, VK_API_SERVICE_KEY)
                .subscribeOn(Schedulers.io())
                .map(Response::getResponseBody)
                .flatMapIterable(users -> users)
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> mDatabaseManager.saveUser(user, listener)
                );
    }

    @Override
    public void loadPhotosForLocation(LatLng coordinates, int radius, PhotosLoadListener listener) {
        mVkApiService.getPhotosByLocation(coordinates.latitude, coordinates.longitude,
                VkSortTypes.BY_LIKES_COUNT.ordinal(), PHOTOS_LOAD_LIMIT, radius, VK_API_VERSION, VK_API_SERVICE_KEY)
                .subscribeOn(Schedulers.io())
                .map(Response::getResponseBody)
                .map(PhotosResponseBody::getPhotos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoList -> {
                            if (listener.isListenerVisible()) listener.onPhotosLoadComplete(photoList);
                        },
                        error -> {
                            if (listener.isListenerVisible()) listener.onPhotosLoadError();
                        }
                );
    }

    @Override
    public void loadUniquePhotosForLocation(LatLng coordinates, int radius, List<Photo> actualPhotoList,
                                            PhotosLoadListener listener) {
        mVkApiService.getPhotosByLocation(coordinates.latitude, coordinates.longitude,
                VkSortTypes.BY_LIKES_COUNT.ordinal(), PHOTOS_LOAD_LIMIT, radius, VK_API_VERSION, VK_API_SERVICE_KEY)
                .subscribeOn(Schedulers.io())
                .map(Response::getResponseBody)
                .map(PhotosResponseBody::getPhotos)
                .observeOn(Schedulers.computation())
                .flatMapIterable(photoList -> photoList)
                .filter(photo -> !actualPhotoList.contains(photo))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoList -> {
                            if (listener.isListenerVisible()) listener.onPhotosLoadComplete(photoList);
                        },
                        error -> {
                            if (listener.isListenerVisible()) listener.onPhotosLoadError();
                        }
                );
    }

}
