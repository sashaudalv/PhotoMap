package com.alexdev.photomap.database.callbacks;


import android.support.v4.util.Pair;

import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;

import java.util.List;

public interface FavoritesVisibleListener extends VisibleListener {

    void onFavoritesLoadComplete(List<Pair<User, Photo>> favoriteList);

}
