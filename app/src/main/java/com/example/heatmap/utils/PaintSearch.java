package com.example.heatmap.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heatmap.R;
import com.example.heatmap.connections.ParametersPT;
import com.example.heatmap.services.PopularTimesService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.heatmap.data.model.GooglePlace;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaintSearch {
    private final GoogleMap map;
    private static Context context;
    private PopularTimesService populartimesService;
    private AlertDialog alertDialog;
    private Marker marker;

    public PaintSearch(GoogleMap map, Marker marker) {
        this.map = map;
        this.marker = marker;
    }

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public void drawHeat(String placeId, LatLng placeLatLng) {
        //Buscamos actividad en este sitio
        populartimesService = PopularTimesService.getInstance();

        Call<GooglePlace> response = populartimesService.get_id(new ParametersPT(placeId));
        response.enqueue(new Callback<GooglePlace>() {
            @Override
            public void onResponse(Call<GooglePlace> call, Response<GooglePlace> response) {

                MapsUtils mapsUtils = new MapsUtils(map);
                GooglePlace googlePlace = response.body();
                List<GooglePlace> googlePlaces = new ArrayList<>();


                if(googlePlace.getPopulartimes()==null || googlePlace.getPopulartimes().size()==0){
                    showAlertNotFound(placeLatLng, googlePlace);
                }else{
                    List<GooglePlace> oneElement = new ArrayList<>();
                    oneElement.add(googlePlace);
                    googlePlace = setCurrentHour(oneElement).get(0);
                    googlePlaces.add(googlePlace);
                    mapsUtils.setMarker(placeLatLng, googlePlace.getName());
                    mapsUtils.addHeatMap(googlePlaces);
                    HeatmapDrawer  heatmapDrawer = HeatmapDrawer.getInstance(map);
                    heatmapDrawer.drawCircle(placeLatLng,googlePlace.getCurrentPopularity());
                    setMarker(googlePlace.getCurrentPopularity());

                }
            }

            @Override
            public void onFailure(Call<GooglePlace> call, Throwable t) {
                Log.d("Response", t.getLocalizedMessage());
            }
        });
    }
    public  void showAlertNotFound(LatLng placeLatLng, GooglePlace googlePlace){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.alertview, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
        ((TextView)dialogView.findViewById(R.id.title)).setText(R.string.titleNotFound);
        ((TextView)dialogView.findViewById(R.id.description)).setText(R.string.descNotFound);
        ((TextView)dialogView.findViewById(R.id.infoExtra)).setText(R.string.infoNotFound);
        ((Button)dialogView.findViewById(R.id.buttonOk)).setText(R.string.Continue);
        ((Button)dialogView.findViewById(R.id.buttonCancel)).setText(R.string.Cancel);

        dialogView.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                searchPlaces(placeLatLng,googlePlace);
                LoaderOn();
            }
        });
        dialogView.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public  void searchPlaces(LatLng placeLatLng, GooglePlace googlePlace){


        PopularTimesService populartimesService = PopularTimesService.getInstance();
        MapsUtils mapsUtils = new MapsUtils(map);

        GooglePlace googlePlacesRandom = mapsUtils.createPlaces(1,placeLatLng, 0.1).get(0);



       Call<List<GooglePlace>> response2 = populartimesService.get(new ParametersPT(TypesUtils.getTypes(),
               new double[]{placeLatLng.latitude, placeLatLng.longitude}, new double[]{googlePlacesRandom.getLatitude(),googlePlacesRandom.getLongitude()},
                20, 600));


        response2.enqueue(new Callback<List<GooglePlace>>() {
            @Override
            public void onResponse(Call<List<GooglePlace>> call, Response<List<GooglePlace>> response) {



                List<GooglePlace> googlePlaces =  response.body();


                Log.d("Response searchPlaces", String.valueOf(googlePlaces));

                if (googlePlaces != null && googlePlaces.size() != 0){
                    googlePlaces = setCurrentHour(googlePlaces);

                    int average = 0;

                    for (GooglePlace item : googlePlaces ){
                        average+=item.getCurrentPopularity();
                    }
                    average = average/googlePlaces.size();

                    googlePlace.setCurrentPopularity(average);

                    List<GooglePlace> googlePlacesAverage =  new ArrayList<>();

                    googlePlacesAverage.add(googlePlace);
                    // mapsUtils.addHeatMap(googlePlacesAverage);

                    HeatmapDrawer  heatmapDrawer = HeatmapDrawer.getInstance(map);
                    heatmapDrawer.drawCircle(placeLatLng,googlePlace.getCurrentPopularity());
                    setMarker(googlePlace.getCurrentPopularity());
                }else {
                    Toast.makeText(context,"Lo sentimos, no hemos podido encontrar información para este lugar :(",Toast.LENGTH_LONG).show();;
                }
                LoaderOff();



            }

            @Override
            public void onFailure(Call<List<GooglePlace>> call, Throwable t) {
                LoaderOff();
                Log.e("Response searchPlaces", t.getMessage());

            }

        });
    }
    public void setMarker(int popularity){
        CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(LayoutInflater.from(context),popularity);
        map.setInfoWindowAdapter(infoWindowAdapter);
        marker.showInfoWindow();
    }

    List<GooglePlace> setCurrentHour(List<GooglePlace> googlePlaces){

        Calendar calendar = Calendar.getInstance();
        String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

        for (GooglePlace gplc:googlePlaces){
            for (GooglePlace.ItemPopularTimes itemPopularTimes:gplc.getPopulartimes()){
                if(itemPopularTimes.getName().equals( days[calendar.get(Calendar.DAY_OF_WEEK) - 1] )){
                    int hour= calendar.get(Calendar.HOUR_OF_DAY);
                    gplc.setCurrentPopularity(itemPopularTimes.getData()[hour]);
                    Log.d("Popularity hour:", String.valueOf(itemPopularTimes.getData()[hour]));
                }


            }
        }
        return googlePlaces;
    }

    public static class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private LayoutInflater inflater;
        private  int popularity;

        public CustomInfoWindowAdapter(LayoutInflater inflater,int popularity){
            this.inflater = inflater;
            this.popularity = popularity;
        }

        @Override
        public View getInfoContents(final Marker m) {
            return null;
        }


        @Override
        public View getInfoWindow(Marker m) {
            View v = inflater.inflate(R.layout.infowindow_layout, null);
            ((TextView)v.findViewById(R.id.info_window_placas)).setText("Público: "+popularity+"% ");
            return v;
        }

    }

    void LoaderOn() {
        ((Activity) context).findViewById(R.id.material_design_ball_clip_rotate_multiple_loader).setVisibility(View.VISIBLE);
    }
    void LoaderOff() {
        ((Activity) context).findViewById(R.id.material_design_ball_clip_rotate_multiple_loader).setVisibility(View.INVISIBLE);
    }



}
