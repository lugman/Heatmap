package com.example.heatmap.utils;

import androidx.core.graphics.ColorUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

import data.model.GooglePlace;

public class HeatmapDrawer {
    private GoogleMap mMap;
    private TileOverlay mOverlay;

    public HeatmapDrawer(GoogleMap mMap) {
        this.mMap = mMap;
    }

    private void addHeatMap(List<WeightedLatLng> weightedLatLngs) {

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngs)
                .radius(50)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider((TileProvider) provider));
    }

    public void clearHeatMap(){
        mOverlay.remove();
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
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        //Formato de color: 0x aarrggbb siendo aa el alfa que determina la transparencia
        //Obtener el color entre verde y rojo usando popularity
        int color = 0x4400ff00;
        if (popularity < 10) {
            color = ColorUtils.blendARGB(0x4400ff00, 0x44ff6600, (float) popularity / 10);
        } else {
            color = ColorUtils.blendARGB(0x44ffff00, 0x44ff0000, (float) (popularity - 10) / 20);
        }
        circleOptions.fillColor(color);
        //quitar el borde del circulo
        circleOptions.strokeColor(0);
        circleOptions.radius(100);
        mMap.addCircle(circleOptions);
    }

    public void drawCircle(GooglePlace googlePlace) {
        LatLng latLng = new LatLng(googlePlace.getLatitude(), googlePlace.getLongitude());
        int popularity = googlePlace.getCurrentPopularity();
        drawCircle(latLng, popularity);
    }

}
