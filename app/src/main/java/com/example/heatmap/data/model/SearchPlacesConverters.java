package com.example.heatmap.data.model;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
public class SearchPlacesConverters {
    @TypeConverter
    public static LatLng latLngFromString(String value) {
        Type latLngType = new TypeToken<LatLng>() {}.getType();
        return new Gson().fromJson(value, latLngType);
    }

    @TypeConverter
    public static String fromLatLng(LatLng latLng) {
        Gson gson = new Gson();
        String json = gson.toJson(latLng);
        return json;
    }
}
