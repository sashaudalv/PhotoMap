package com.alexdev.photomap.network.responses;


import com.alexdev.photomap.models.Photo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosResponseBody {

    @SerializedName("count")
    @Expose
    private long count;
    @SerializedName("items")
    @Expose
    private List<Photo> photos;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}
