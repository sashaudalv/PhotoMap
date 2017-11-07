package com.alexdev.photomap.models;


import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private static final String URL_PATTERN = "https://vk.com/id%d";

    private final int id;
    private final String first_name;
    private final String last_name;
    private final String avatar;
    private final String url;

    public User(int id, String firstName, String lastName, String avatar, String url) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        this.avatar = avatar;
        this.url = url;
    }

    @SuppressLint("DefaultLocale")
    public User(int id, String firstName, String lastName, String avatar) {
        this(id, firstName, lastName, avatar, String.format(URL_PATTERN, id));
    }

    private User(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        avatar = in.readString();
        url = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getName() {
        return first_name + ' ' + last_name;
    }

    public String getUrl() {
        return url;
    }

    public String getAvatar() {
        return avatar;
    }

    @SuppressLint("DefaultLocale")
    public static String getUrlByID(int userId) {
        return String.format(URL_PATTERN, userId);
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
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(avatar);
        dest.writeString(url);
    }
}
