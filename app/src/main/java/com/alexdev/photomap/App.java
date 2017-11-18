package com.alexdev.photomap;

import android.app.Application;
import android.content.Context;

import com.alexdev.photomap.database.DatabaseModule;
import com.alexdev.photomap.network.ApiModule;


public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .databaseModule(new DatabaseModule())
                .build();
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
