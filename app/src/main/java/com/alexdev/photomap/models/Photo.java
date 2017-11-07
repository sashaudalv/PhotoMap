package com.alexdev.photomap.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class Photo implements Parcelable {

    private final int id;
    private final String url;
    private final int owner_id;
    @Nullable
    private final String text;
    private final long date;
    private final long saving_date;
    private boolean is_in_favorites;

    public Photo(int id, String url, int ownerId, @Nullable String text, long date, long saving_date, boolean is_in_favorites) {
        this.id = id;
        this.url = url;
        this.owner_id = ownerId;
        this.text = text;
        this.date = date;
        this.saving_date = saving_date;
        this.is_in_favorites = is_in_favorites;
    }

    public Photo(int id, String url, int ownerId, @Nullable String text, long date) {
        this(id, url, ownerId, text, date, 0L, false);
    }

    private Photo(Parcel in) {
        id = in.readInt();
        url = in.readString();
        owner_id = in.readInt();
        text = in.readString();
        date = in.readLong();
        saving_date = in.readLong();
        is_in_favorites = in.readByte() != 0;
    }


    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return owner_id;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }

    public long getSavingDate() {
        return saving_date;
    }

    public boolean getIsInFavorites() {
        return is_in_favorites;
    }

    public void setIsInFavorites(boolean isInFavorites) {
        is_in_favorites = isInFavorites;
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
        dest.writeInt(owner_id);
        dest.writeString(text);
        dest.writeLong(date);
        dest.writeLong(saving_date);
        dest.writeByte((byte) (is_in_favorites ? 1 : 0));
    }
}
