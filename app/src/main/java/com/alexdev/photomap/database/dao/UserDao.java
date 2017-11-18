package com.alexdev.photomap.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.alexdev.photomap.database.entities.User;

import io.reactivex.Single;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE id = :userId")
    Single<User> getById(int userId);

    @Query("SELECT * FROM user WHERE social_id = :socialId")
    Single<User> getBySocialId(int socialId);

    @Delete
    void delete(User user);

}
