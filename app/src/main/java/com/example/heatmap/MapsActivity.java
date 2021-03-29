package com.example.heatmap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.heatmap.connections.restservice.ParametersPT;
import com.example.heatmap.connections.restservice.PopularTimesService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import data.model.GooglePlace;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        /*
                    EJEMPLO DE LLAMADA A LA API.
        */

        PopularTimesService populartimesService = new PopularTimesService();

        Call<ResponseBody> response2 = populartimesService.get(new ParametersPT(new String[]{"bar"},new double[]{48.132986, 11.566126},new double[]{48.142199, 11.580047,},60,90));
        Call<ResponseBody> response  = populartimesService.get_id(new ParametersPT("ChIJSYuuSx9awokRyrrOFTGg0GY"));

        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("Response", response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Response", "No va");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Response", "ERROR "+t.toString());
            }
        });

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("Response", response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Response", "No va");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng etsinf = new LatLng(39.48305714751131, -0.34783486024869137);
        mMap.addMarker(new MarkerOptions().position(etsinf).title("Etsinf"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(etsinf));
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(20.0f);

        List<GooglePlace> googlePlaces = new ArrayList<>();
        HeatmapDrawer heatmapDrawer = new HeatmapDrawer(mMap);
        GooglePlace googlePlace = new GooglePlace();
        List<Float> coords = new ArrayList<>();
        coords.add(39.48305714751131f);
        coords.add(-0.34783486024869137f);
        googlePlace.setCoords(coords);
        googlePlace.setCurrentPopularity(30);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        googlePlace = new GooglePlace();
        coords = new ArrayList<>();
        coords.add(39.48232417821347f);
        coords.add(-0.3487664561099621f);
        googlePlace.setCoords(coords);
        googlePlace.setCurrentPopularity(5);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        googlePlace = new GooglePlace();
        coords = new ArrayList<>();
        coords.add(39.48281274006481f);
        coords.add(-0.3468889099088923f);
        googlePlace.setCoords(coords);
        googlePlace.setCurrentPopularity(15);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        googlePlace = new GooglePlace();
        coords = new ArrayList<>();
        coords.add(39.483831256278556f);
        coords.add(-0.3468567234025883f);
        googlePlace.setCoords(coords);
        googlePlace.setCurrentPopularity(20);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        heatmapDrawer.makeHeatMap(googlePlaces);
    }
}