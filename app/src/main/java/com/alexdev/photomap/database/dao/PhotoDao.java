package com.alexdev.photomap.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.alexdev.photomap.database.entities.Photo;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PhotoDao {

    @Insert
    void insert(Photo photo);

    @Query("SELECT * FROM photo WHERE id = :photoId")
    Single<Photo> getById(int photoId);

    @Query("SELECT * FROM photo ORDER BY saving_date DESC")
    Flowable<List<Photo>> getAll();

    @Query("SELECT * FROM photo ORDER BY saving_date DESC LIMIT :limit")
    Flowable<List<Photo>> getAll(int limit);

    @Delete
    void delete(Photo photo);

}
