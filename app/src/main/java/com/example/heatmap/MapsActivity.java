package com.example.heatmap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.heatmap.connections.restservice.LatLngService;
import com.example.heatmap.connections.restservice.ParametersPT;
import com.example.heatmap.connections.restservice.PopularTimesService;
import com.example.heatmap.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.model.GooglePlace;

import java.io.IOException;

import static android.content.ContentValues.TAG;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String apiKey = "AIzaSyBC5WXRHQ7fKL96InPGDLXPrpztFFYcpLg";
    private ActivityMapsBinding binding;
    private GoogleMap mMap;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());

        initializePlaces(apiKey);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);



        /*
                    EJEMPLO DE LLAMADA A LA API.
        */

        PopularTimesService populartimesService =  PopularTimesService.getInstance();

        Call<List<GooglePlace>> response2 = populartimesService.get(new ParametersPT(new String[]{"bar"},new double[]{48.132986, 11.566126},new double[]{48.142199, 11.580047,},60,90));
        Call<GooglePlace> response  = populartimesService.get_id(new ParametersPT("ChIJSYuuSx9awokRyrrOFTGg0GY"));

        response2.enqueue(new Callback<List<GooglePlace>>() {
            @Override
            public void onResponse(Call<List<GooglePlace>> call, Response<List<GooglePlace>> response) {

                Log.d("Response List", response.body().get(0).getLatitude().toString());
            }

            @Override
            public void onFailure(Call<List<GooglePlace>> call, Throwable t) {

            }

        });

        response.enqueue(new Callback<GooglePlace>() {
            @Override
            public void onResponse(Call<GooglePlace> call, Response<GooglePlace> response) {
                Log.d("Response", String.valueOf(response.body().getTimeWait()));
            }

            @Override
            public void onFailure(Call<GooglePlace> call, Throwable t) {
                Log.d("Response",t.getLocalizedMessage());
            }
        });




    }

    private void initializePlaces(String apiKey) {
        // Initialize Places client
        Places.initialize(getApplicationContext(), apiKey);
        placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(binding.autocompleteFragment.getId());

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng(place.getId())));
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private LatLng getLatLng(String placeId) {
        LatLngService latLngService = LatLngService.getInstance();
        Call<ResponseBody> response  = latLngService.getLatLng(apiKey, placeId);

        try {
            //TODO: cambiar execute por enqueue
            Response<ResponseBody> responseHttp = response.execute();
            Double[] latLng = arrayLatLng(responseHttp.body().string());
            return new LatLng(latLng[0],latLng[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Double[] arrayLatLng(String response) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        JSONArray arrayJson = responseJson.getJSONArray("results");
        JSONObject location = arrayJson.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        return new Double[]{location.getDouble("lat"),
                            location.getDouble("lng")};
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
        googlePlace.setLatitude(39.48305714751131f);
        googlePlace.setLongitude(-0.34783486024869137f);
        googlePlace.setCurrentPopularity(30);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        googlePlace = new GooglePlace();

        googlePlace.setLatitude(39.48232417821347f);
        googlePlace.setLongitude(-0.3487664561099621f);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);
        googlePlace.setCurrentPopularity(30);

        googlePlace = new GooglePlace();

        googlePlace.setLatitude(39.48281274006481f);
        googlePlace.setLongitude(-0.3468889099088923f);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);
        googlePlace.setCurrentPopularity(30);

        googlePlace = new GooglePlace();

        googlePlace.setLatitude(39.483831256278556f);
        googlePlace.setLongitude(-0.3468567234025883f);
        googlePlace.setCurrentPopularity(20);
        //heatmapDrawer.drawCircle(googlePlace);
        googlePlaces.add(googlePlace);

        heatmapDrawer.makeHeatMap(googlePlaces);
    }
}