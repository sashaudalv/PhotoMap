package com.alexdev.photomap.database.entities;


import android.arch.persistence.room.Embedded;
import android.support.v4.util.Pair;

public class UserPhotoPair {

    @Embedded
    private com.alexdev.photomap.database.entities.User user;
    @Embedded
    private com.alexdev.photomap.database.entities.Photo photo;

    public com.alexdev.photomap.database.entities.User getUser() {
        return user;
    }

    public void setUser(com.alexdev.photomap.database.entities.User user) {
        this.user = user;
    }

    public com.alexdev.photomap.database.entities.Photo getPhoto() {
        return photo;
    }

    public void setPhoto(com.alexdev.photomap.database.entities.Photo photo) {
        this.photo = photo;
    }

    public Pair<com.alexdev.photomap.models.User, com.alexdev.photomap.models.Photo> getAsPair() {
        return new Pair<>(user.asUserModel(), photo.asPhotoModel());
    }

}
