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
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE user_id = :userId")
    Single<User> getUserById(int userId);

    @Query("SELECT * FROM user WHERE social_id = :socialId")
    Single<User> getUserBySocialId(long socialId);

    @Delete
    void deleteUser(User user);

}
