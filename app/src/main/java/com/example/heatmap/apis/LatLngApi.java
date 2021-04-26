package com.example.heatmap.apis;

import data.model.PlaceSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LatLngApi {

    @GET("json")
    Call<PlaceSearch> getLatLng(
            @Query("key") String apiKey,
            @Query("place_id") String placeId);
}
