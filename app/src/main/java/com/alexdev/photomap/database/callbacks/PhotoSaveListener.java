package com.alexdev.photomap.database.callbacks;


import com.alexdev.photomap.models.Photo;

public interface PhotoSaveListener {

    void onPhotoSaveComplete(Photo photo);

}
