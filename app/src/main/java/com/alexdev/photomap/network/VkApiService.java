package com.alexdev.photomap.network;


import com.alexdev.photomap.models.User;
import com.alexdev.photomap.network.responses.PhotosResponseBody;
import com.alexdev.photomap.network.responses.Response;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApiService {

    @GET("users.get")
    Observable<Response<List<User>>> getUsers(@Query("user_ids") long[] userIds,
                                              @Query("fields") String[] fields,
                                              @Query("v") String apiVersion,
                                              @Query("access_token") String accessToken);

    @GET("photos.search")
    Observable<Response<PhotosResponseBody>> getPhotosByLocation(@Query("lat") double latitude,
                                                                 @Query("long") double longitude,
                                                                 @Query("sort") int sortType,
                                                                 @Query("count") int limit,
                                                                 @Query("radius") int radius,
                                                                 @Query("v") String apiVersion,
                                                                 @Query("access_token") String accessToken);

}
