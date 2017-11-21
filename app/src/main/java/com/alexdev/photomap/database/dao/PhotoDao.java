package com.alexdev.photomap.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.alexdev.photomap.database.entities.Photo;
import com.alexdev.photomap.database.entities.UserPhotoPair;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PhotoDao {

    @Insert
    Long insertPhoto(Photo photo);

    @Query("SELECT * FROM photo WHERE photo_id = :photoId")
    Single<Photo> getPhotoById(int photoId);

    @Query("SELECT * FROM photo ORDER BY saving_date DESC")
    Single<List<Photo>> getAllPhotos();

    @Query("SELECT * FROM photo ORDER BY saving_date DESC LIMIT :limit")
    Single<List<Photo>> getAllPhotos(int limit);

    @Query("SELECT * FROM photo " +
            "INNER JOIN user ON owner_social_id = social_id " +
            "ORDER BY saving_date DESC")
    Single<List<UserPhotoPair>> getAllUserAndPhotoPairs();

    @Delete
    void deletePhoto(Photo photo);

}
