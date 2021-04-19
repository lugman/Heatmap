package com.example.heatmap.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import data.model.GooglePlace;

@Singleton
public class MapsUtils {
    private final GoogleMap map;
    private HeatmapDrawer heatmapDrawer;

    /**
     * Create a singleton instance for MapUtils
     * @param map the map where the utils will plot the data
     */
    public MapsUtils (GoogleMap map) {
        this.map = map;
    }

    /**
     * Sets a marker in the map with the given LatLng and a empty title
     * @param lat the latitude coordinate
     * @param lng the longitude coordinate
     * @return the created marker
     */
    public Marker setMarker(double lat, double lng) {
        return setMarker(new LatLng(lat, lng), "");
    }

    /**
     * Sets a marker in the map with the given LatLng and a empty title
     * @param latLng coordinates of the marker
     * @return the created marker
     */
    public Marker setMarker(LatLng latLng) {
        return setMarker(latLng, "");
    }

    /**
     * Sets a marker in the map with the given latitude, longitude coordinates and the title
     * @param lat the latitude coordinate
     * @param lng the longitude coordinate
     * @param title title of the marker
     * @return the created marker
     */
    public Marker setMarker(double lat, double lng, String title) {
        return setMarker(new LatLng(lat, lng), title);
    }

    /**
     * Sets a marker in the map with the given LatLng and the title
     * @param latLng coordinates of the marker
     * @param title title of the marker
     * @return the created marker
     */
    public Marker setMarker(LatLng latLng, String title) {
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.setMinZoomPreference(14.0f);
        map.setMaxZoomPreference(20.0f);
        return map.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    /**
     * Plot a heatmap with the given places
     * @param googlePlaceList list of places with the data to plot the heatmap
     */
    public void addHeatMap(List<GooglePlace> googlePlaceList) {
        if (heatmapDrawer == null) heatmapDrawer = new HeatmapDrawer(map);

        heatmapDrawer.makeHeatMap(googlePlaceList);
    }

    /**
     * Clears all the plotted data in the map
     */
    public void clearMap() {
        map.clear();
    }

    /**
     * Create a list of places with a random population from 0 to 100 and
     * a LatLng with a maximum of ±.0015 deviation from the given LatLng
     * @param number number of places to create
     * @param aproxZone aprox zone where they will be created
     * @return populated list of places
     */
    public List<GooglePlace> createPlaces(int number, LatLng aproxZone) {
        return createPlaces(number, aproxZone, 0.0015, 100);
    }

    /**
     * Create a list of places with a random population from 0 to 100
     * and LatLng with the given ranges
     * @param number number of places to create
     * @param aproxZone aprox zone where they will be created
     * @param maxDeviation max deviation for LatLng
     * @return populated list of places
     */
    public List<GooglePlace> createPlaces(int number, LatLng aproxZone, double maxDeviation) {
        return createPlaces(number, aproxZone, maxDeviation, 100);
    }

    /**
     * Create a list of places with a random population from the given ranges
     * and a LatLng maximum deviation of ±.0015
     * @param number number of places to create
     * @param aproxZone aprox zone where they will be created
     * @param maxPopulation max population for the place
     * @return populated list of places
     */
    public List<GooglePlace> createPlaces(int number, LatLng aproxZone, int maxPopulation) {
        return createPlaces(number, aproxZone, 0.0015, maxPopulation);
    }

    /**
     * Create a list of places with a random population and LatLng with the given ranges
     * @param number number of places to create
     * @param aproxZone aprox zone where they will be created
     * @param maxDeviation max deviation for LatLng
     * @param maxPopulation max population for the place
     * @return populated list of places
     */
    public List<GooglePlace> createPlaces(int number, LatLng aproxZone,
                                          double maxDeviation, int maxPopulation) {
        List<GooglePlace> googlePlaces = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            GooglePlace place = new GooglePlace();

            float randomLat = (float) (Math.random() * maxDeviation);
            float randomLng = (float) (Math.random() * maxDeviation);
            randomLat = Math.random() > .5 ? randomLat : -randomLat;
            randomLng = Math.random() > .5 ? randomLng : -randomLng;

            place.setLatitude((float) aproxZone.latitude + randomLat);
            place.setLongitude((float) aproxZone.longitude + randomLng);
            place.setCurrentPopularity((int) (Math.random() * maxPopulation));

            googlePlaces.add(place);
        }

        return googlePlaces;
    }
}
