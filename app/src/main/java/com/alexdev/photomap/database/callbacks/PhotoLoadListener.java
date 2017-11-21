package com.alexdev.photomap.database.callbacks;


import com.alexdev.photomap.models.Photo;

public interface PhotoLoadListener {

    void onPhotoLoadComplete(Photo photo);

}
