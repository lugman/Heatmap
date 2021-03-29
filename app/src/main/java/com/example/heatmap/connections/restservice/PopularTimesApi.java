package com.example.heatmap.connections.restservice;

import java.util.List;

import data.model.GooglePlace;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PopularTimesApi {

    @Headers({"Content-Type: application/json"})
    @POST("getpopulartimes")
    Call<List<GooglePlace>> get(@Body ParametersPT body);

    @Headers({"Content-Type: application/json"})
    @POST("getpopulartimes")
    Call<GooglePlace> get_id(@Body ParametersPT body);

}
