package com.alexdev.photomap.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"social_id"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int social_id;
    @NonNull
    private String first_name;
    @NonNull
    private String last_name;
    @NonNull
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public int getSocial_id() {
        return social_id;
    }

    public void setSocial_id(@NonNull int social_id) {
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
}
