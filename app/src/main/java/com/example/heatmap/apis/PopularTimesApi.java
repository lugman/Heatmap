package com.example.heatmap.apis;

import com.example.heatmap.connections.ParametersPT;

import java.util.List;

import com.example.heatmap.data.model.GooglePlace;
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
