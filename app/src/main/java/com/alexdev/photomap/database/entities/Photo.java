package com.alexdev.photomap.database.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(indices = {@Index(value = {"saving_date"})},
        foreignKeys = {@ForeignKey(entity = User.class,
                parentColumns = {"social_id"},
                childColumns = {"owner_social_id"},
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.RESTRICT)})
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private int photo_id;
    @NonNull
    private String url;
    private long owner_social_id;
    @Nullable
    private String text;
    private double latitude;
    private double longitude;
    private long date;
    private long saving_date;

    public Photo() {

    }

    @Ignore
    public Photo(com.alexdev.photomap.models.Photo photoModel) {
        photo_id = photoModel.getId();
        url = photoModel.getUrl();
        owner_social_id = photoModel.getOwnerSocialId();
        text = photoModel.getText();
        latitude = photoModel.getLatitude();
        longitude = photoModel.getLongitude();
        date = photoModel.getDate();
        saving_date = photoModel.getSavingDate();
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public long getOwner_social_id() {
        return owner_social_id;
    }

    public void setOwner_social_id(long owner_social_id) {
        this.owner_social_id = owner_social_id;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    @Ignore
    public void setSavingDateAsCurrentTime() {
        saving_date = System.currentTimeMillis();
    }

    @Ignore
    public com.alexdev.photomap.models.Photo asPhotoModel() {
        return new com.alexdev.photomap.models.Photo(photo_id, url, owner_social_id, text, latitude,
                longitude, date, saving_date, true);
    }

}
