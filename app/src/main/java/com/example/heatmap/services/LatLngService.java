package com.example.heatmap.services;

import com.example.heatmap.apis.LatLngApi;

import java.util.concurrent.TimeUnit;

import com.example.heatmap.data.model.PlaceSearch;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LatLngService implements LatLngApi {
    private static LatLngService instance;
    private String URL;
    private LatLngApi latLngApi;

    private LatLngService() {
        URL = "https://maps.googleapis.com/maps/api/geocode/";

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        latLngApi = retrofit.create(LatLngApi.class);
    }

    public static LatLngService getInstance() {
        if (instance == null) {
            instance = new LatLngService();
        }
        return instance;
    }

    @Override
    public Call<PlaceSearch> getLatLng(String apiKey, String placeId) {
        return latLngApi.getLatLng(apiKey, placeId);
    }
}
