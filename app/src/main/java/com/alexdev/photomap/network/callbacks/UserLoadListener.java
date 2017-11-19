package com.alexdev.photomap.network.callbacks;


import com.alexdev.photomap.models.User;

public interface UserLoadListener extends VisibleListener {

    void onUserLoadComplete(User user);

    void onUserLoadError();

}
