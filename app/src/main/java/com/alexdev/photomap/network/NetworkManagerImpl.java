package com.alexdev.photomap.network;


import android.support.annotation.NonNull;

import com.alexdev.photomap.database.DatabaseManager;

public final class NetworkManagerImpl implements NetworkManager {

    public static final String VK_API_BASE_URL = "https://vk.com";

    private final VkApiService mVkApiService;
    private final DatabaseManager mDatabaseManager;

    public NetworkManagerImpl(@NonNull VkApiService vkApiService,
                              @NonNull DatabaseManager databaseManager) {
        mVkApiService = vkApiService;
        mDatabaseManager = databaseManager;
    }

}
