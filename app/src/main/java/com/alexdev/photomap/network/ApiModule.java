package com.alexdev.photomap.network;


import android.support.annotation.NonNull;

import com.alexdev.photomap.database.DatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @NonNull
    @Singleton
    public VkApiService provideVkApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkManagerImpl.VK_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(VkApiService.class);
    }

    @Provides
    @NonNull
    public NetworkManager provideNetworkManager(@NonNull VkApiService vkApiService,
                                                @NonNull DatabaseManager databaseManager) {
        return new NetworkManagerImpl(vkApiService, databaseManager);
    }

}
