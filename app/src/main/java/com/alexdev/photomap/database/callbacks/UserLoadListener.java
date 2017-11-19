package com.alexdev.photomap.database.callbacks;


import com.alexdev.photomap.models.User;

public interface UserLoadListener {

    void onUserLoadComplete(User user);

    void onUserLoadError();

}
