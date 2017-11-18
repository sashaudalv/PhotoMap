package com.alexdev.photomap;


import com.alexdev.photomap.database.DatabaseModule;
import com.alexdev.photomap.network.ApiModule;
import com.alexdev.photomap.ui.favorites.FavoritesFragment;
import com.alexdev.photomap.ui.map.MapFragment;
import com.alexdev.photomap.ui.photo.PhotoViewerActivity;
import com.alexdev.photomap.ui.user.UserDetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(MapFragment mapFragment);

    void inject(FavoritesFragment favoritesFragment);

    void inject(PhotoViewerActivity photoViewerActivity);

    void inject(UserDetailsActivity userDetailsActivity);

}
