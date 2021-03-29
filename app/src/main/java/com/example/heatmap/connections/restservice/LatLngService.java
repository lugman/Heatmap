package com.example.heatmap.connections.restservice;

import java.util.concurrent.TimeUnit;

import data.model.GooglePlace;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class LatLngService implements LatLngApi {
    private String URL;
    private LatLngApi latLngApi;
    private static LatLngService instance;

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

    public static LatLngService getInstance(){
        if (instance == null){
            instance = new LatLngService();
        }
        return instance;
    }

    @Override
    public Call<ResponseBody> getLatLng(String apiKey, String placeId) {
        return latLngApi.getLatLng(apiKey, placeId);
    }
}
