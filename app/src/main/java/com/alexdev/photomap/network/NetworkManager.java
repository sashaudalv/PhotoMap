package com.alexdev.photomap.network;


import com.alexdev.photomap.database.callbacks.UserSaveListener;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.network.callbacks.PhotosLoadListener;
import com.alexdev.photomap.network.callbacks.UserLoadListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface NetworkManager {

    void loadUser(long userSocialId, UserLoadListener listener);

    void loadAndSaveUser(long userSocialId, UserSaveListener listener);

    void loadPhotosForLocation(LatLng coordinates, int radius, PhotosLoadListener listener);

    void loadUniquePhotosForLocation(LatLng coordinates, int radius, List<Photo> actualPhotoList, PhotosLoadListener listener);

}
