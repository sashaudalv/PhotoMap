package com.alexdev.photomap.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"social_id"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int user_id;
    private long social_id;
    @NonNull
    private String first_name;
    @NonNull
    private String last_name;
    @NonNull
    private String avatar;

    public User() {

    }

    @Ignore
    public User(com.alexdev.photomap.models.User userModel) {
        user_id = userModel.getId();
        social_id = userModel.getSocialId();
        first_name = userModel.getFirstName();
        last_name = userModel.getLastName();
        avatar = userModel.getAvatar();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getSocial_id() {
        return social_id;
    }

    public void setSocial_id(long social_id) {
        this.social_id = social_id;
    }

    @NonNull
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(@NonNull String first_name) {
        this.first_name = first_name;
    }

    @NonNull
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(@NonNull String last_name) {
        this.last_name = last_name;
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    @Ignore
    public com.alexdev.photomap.models.User asUserModel() {
        return new com.alexdev.photomap.models.User(user_id, social_id, first_name, last_name, avatar);
    }
}
