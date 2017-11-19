package com.alexdev.photomap.network.callbacks;


import com.alexdev.photomap.models.Photo;

import java.util.List;

public interface PhotosLoadListener extends VisibleListener {

    void onPhotosLoadComplete(List<Photo> photoList);

    void onPhotosLoadError();

}
