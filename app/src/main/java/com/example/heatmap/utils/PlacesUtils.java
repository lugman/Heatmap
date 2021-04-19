package com.example.heatmap.utils;

import android.util.Log;

import com.example.heatmap.services.LatLngService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import data.model.PlaceSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesUtils {
    private final String apiKey;
    private final GoogleMap map;
    private Marker lastMarker;
    private MapsUtils mapsUtils;

    public PlacesUtils (String apiKey, GoogleMap map) {
        this.apiKey = apiKey;
        this.map = map;
    }

    public void removeLastMarker() { lastMarker.remove(); }

    public void getLatLng (String placeId) { getLatLng(placeId, "");}

    public void getLatLng (String placeId, String name) {
        LatLngService latLngService = LatLngService.getInstance();
        Call<PlaceSearch> response  = latLngService.getLatLng(apiKey, placeId);
        mapsUtils = new MapsUtils(map);

        try {
            response.enqueue(new Callback<PlaceSearch>() {
                @Override
                public void onResponse(Call<PlaceSearch> call, Response<PlaceSearch> response) {
                    try {
                        PlaceSearch placeResponse = response.body();

                        Double[] latLng = arrayLatLng(placeResponse);
                        LatLng placeLatLng = new LatLng(latLng[0],latLng[1]);
                        Log.i("LatLng", latLng[0] + " " + latLng[1]);

                        if (lastMarker != null) removeLastMarker();
                        lastMarker = mapsUtils.setMarker(placeLatLng, name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<PlaceSearch> call, Throwable t) {
                    Log.e("HTTP_Call_Error", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double[] arrayLatLng(PlaceSearch response) throws JSONException {
        PlaceSearch.Location location = response.results.get(0).geometry.location;

        return new Double[]{location.lat, location.lng};
    }
}
