package com.alexdev.photomap.database.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = {"saving_date"})})
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String url;
    @ForeignKey(entity = User.class,
            parentColumns = {"social_id"},
            childColumns = {"owner_social_id"},
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true)
    private int owner_social_id;
    @Nullable
    private String text;
    private long date;
    private long saving_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public int getOwner_social_id() {
        return owner_social_id;
    }

    public void setOwner_social_id(int owner_social_id) {
        this.owner_social_id = owner_social_id;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSaving_date() {
        return saving_date;
    }

    public void setSaving_date(long saving_date) {
        this.saving_date = saving_date;
    }
}
