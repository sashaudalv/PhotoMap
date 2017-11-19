package com.alexdev.photomap.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class User implements Parcelable {

    private static final String URL_PATTERN = "https://vk.com/id%d";

    private transient final int id;
    @SerializedName("id")
    @Expose
    private final long socialId;
    @SerializedName("first_name")
    @Expose
    @NonNull
    private final String firstName;
    @SerializedName("last_name")
    @Expose
    @NonNull
    private final String lastName;
    @SerializedName("photo_400_orig")
    @Expose
    @NonNull
    private final String avatar;

    public User(int id, long socialId, @NonNull String firstName, @NonNull String lastName, @NonNull String avatar) {
        this.id = id;
        this.socialId = socialId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public User(long socialId, @NonNull String firstName, @NonNull String lastName, @NonNull String avatar) {
        this(0, socialId, firstName, lastName, avatar);
    }

    private User(Parcel in) {
        id = in.readInt();
        socialId = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        avatar = in.readString();
    }

    public int getId() {
        return id;
    }

    public long getSocialId() {
        return socialId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + ' ' + lastName;
    }

    public String getUrl() {
        return String.format(Locale.getDefault(), URL_PATTERN, socialId);
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    public static String getUrlBySocialID(long userSocialId) {
        return String.format(Locale.getDefault(), URL_PATTERN, userSocialId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(socialId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(avatar);
    }
}
