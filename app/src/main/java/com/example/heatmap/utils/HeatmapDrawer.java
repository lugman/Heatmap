package com.example.heatmap.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.example.heatmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import data.model.GooglePlace;

public class HeatmapDrawer {
    private GoogleMap mMap;
    private Circle circle;
    private static HeatmapDrawer heatmapDrawer;

    public HeatmapDrawer(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public static HeatmapDrawer getInstance(GoogleMap mMap){
        if (heatmapDrawer == null){
            heatmapDrawer = new HeatmapDrawer(mMap);
        }
        return  heatmapDrawer;
    }

    private void addHeatMap(List<WeightedLatLng> weightedLatLngs) {

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngs)
                .radius(50)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider((TileProvider) provider));
    }

    public void makeHeatMap(List<GooglePlace> googlePlaces) {
        List<WeightedLatLng> weightedLatLngs = new ArrayList<>();
        for (int i = 0; i < googlePlaces.size(); i++) {
            float lat = googlePlaces.get(i).getLatitude();
            float lng = googlePlaces.get(i).getLongitude();
            int popularity = googlePlaces.get(i).getCurrentPopularity();
            weightedLatLngs.add(new WeightedLatLng(new LatLng(lat, lng), popularity));
        }
        addHeatMap(weightedLatLngs);
    }

    public void drawCircle(LatLng latLng, int popularity) {
        if (circle != null) circle.remove();

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);

        //Formato de color: 0x aarrggbb siendo aa el alfa que determina la transparencia
        //Obtener el color entre verde y rojo usando popularity

        // Rojo transparente
        int  color = ColorUtils.blendARGB(0x44ffff00, 0x44ff0000, (float) 0.6);
        if (popularity < 33) {
            // Verde transparente
            color = ColorUtils.blendARGB(0x4400ff00, 0x44ff6600, (float)  00.1);
        }else if(popularity < 66){
            // Naranja transparente
            color = ColorUtils.blendARGB(0x44ffff00, 0x44ff0000, (float) 0.01);
        }
        circleOptions.fillColor(color);
        //quitar el borde del circulo
        circleOptions.strokeColor(0);
        Log.d("Color", String.valueOf(popularity));
        circleOptions.radius(600);



        circle = mMap.addCircle(circleOptions);


/*         Marker melbourne = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Darwin Marker 3")
                .snippet("z-index 3")
                .zIndex(3)
                 .infoWindowAnchor(100,100)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amu_bubble_shadow)));*/

    }
    public void drawCircle(GooglePlace googlePlace) {
        LatLng latLng = new LatLng(googlePlace.getLatitude(), googlePlace.getLongitude());
        int popularity = googlePlace.getCurrentPopularity();
        drawCircle(latLng, popularity);
    }

}
