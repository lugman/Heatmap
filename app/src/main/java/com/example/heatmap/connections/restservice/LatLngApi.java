package com.example.heatmap.connections.restservice;

import data.model.PlaceSearch;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LatLngApi {

    @GET("json")
    Call<PlaceSearch> getLatLng(
            @Query("key") String apiKey,
            @Query("place_id") String placeId);
}
