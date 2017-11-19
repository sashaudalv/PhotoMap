package com.alexdev.photomap.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Photo implements Parcelable {

    private transient final int id;
    @SerializedName("photo_604")
    @Expose
    private final String url;
    @SerializedName("owner_id")
    @Expose
    private final long ownerSocialId;
    @SerializedName("text")
    @Expose
    @Nullable
    private final String text;
    @SerializedName("lat")
    @Expose
    private final double latitude;
    @SerializedName("long")
    @Expose
    private final double longitude;
    @SerializedName("date")
    @Expose
    private final long date;
    private transient long savingDate;
    private transient boolean isInFavorites;

    public Photo(int id, String url, long ownerSocialId, @Nullable String text, double latitude,
                 double longitude, long date, long savingDate, boolean isInFavorites) {
        this.id = id;
        this.url = url;
        this.ownerSocialId = ownerSocialId;
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.savingDate = savingDate;
        this.isInFavorites = isInFavorites;
    }

    public Photo(String url, long ownerSocialId, @Nullable String text, double latitude,
                 double longitude, long date) {
        this(0, url, ownerSocialId, text, latitude, longitude, date, 0L, false);
    }

    private Photo(Parcel in) {
        id = in.readInt();
        url = in.readString();
        ownerSocialId = in.readLong();
        text = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        date = in.readLong();
        savingDate = in.readLong();
        isInFavorites = in.readByte() != 0;
    }


    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public long getOwnerSocialId() {
        return ownerSocialId;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getDate() {
        return date;
    }

    public long getSavingDate() {
        return savingDate;
    }

    public boolean getIsInFavorites() {
        return isInFavorites;
    }

    public void setIsInFavorites(boolean isInFavorites) {
        this.isInFavorites = isInFavorites;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeLong(ownerSocialId);
        dest.writeString(text);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(date);
        dest.writeLong(savingDate);
        dest.writeByte((byte) (isInFavorites ? 1 : 0));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Photo)) {
            return false;
        }
        Photo rhs = ((Photo) other);
        return (ownerSocialId == rhs.ownerSocialId) && ObjectsCompat.equals(url, rhs.url)
                && ObjectsCompat.equals(text, rhs.text);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (int) (31 * result + ownerSocialId);
        return 31 * result + Arrays.hashCode(new Object[]{url, text});
    }
}
