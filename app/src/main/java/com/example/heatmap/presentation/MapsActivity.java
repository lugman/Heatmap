package com.example.heatmap.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.heatmap.BuildConfig;
import com.example.heatmap.R;
import com.example.heatmap.data.model.GooglePlace;
import com.example.heatmap.connections.ParametersPT;
import com.example.heatmap.databinding.BottomFragmentBinding;
import com.example.heatmap.services.PopularTimesService;
import com.example.heatmap.databinding.ActivityMapsBinding;
import com.example.heatmap.services.viewmodel.GooglePlaceViewModel;
import com.example.heatmap.utils.HeatmapDrawer;
import com.example.heatmap.utils.MapsUtils;
import com.example.heatmap.utils.PaintSearch;
import com.example.heatmap.utils.PlacesUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


import static android.content.ContentValues.TAG;

import com.example.heatmap.data.database.GooglePlaceAccess;
import com.example.heatmap.data.database.GooglePlaceDatabase;
import com.example.heatmap.data.model.SearchPlaces;
import com.example.heatmap.data.database.SearchPlacesAccess;


import com.google.android.material.bottomsheet.BottomSheetBehavior;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // TODO: Change api key
    private final String apiKey = BuildConfig.API_KEY;
    private ActivityMapsBinding binding;
    private GoogleMap mMap;
    private Marker lastMarker;
    private PlacesClient placesClient;
    private MapsUtils mapsUtils;
    private PlacesUtils placesUtils;
    private  BottomSheetBehavior bottomSheetBehavior;
    private View bottomFragContainer;
    private Button button;
    private GooglePlaceViewModel googlePlaceViewModel;
    private int[] dayAverage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());

        PaintSearch.setContext(this);

        testGooglePlaceDb();

        initializePlaces(apiKey);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);



        //ejemploLlamadaApi();

       BottomFragment bottomFrag = BottomFragment.newInstance();
    //    bottomFrag.show(getSupportFragmentManager(),"tag");


        //Create and initialize the fragment

        bottomFragContainer = findViewById(R.id.infoFragmentHolder);
        Fragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.infoFragmentHolder, BottomFragment.class, null).commit();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomFragContainer);


        //BottomFragmentBinding binding2 = BottomFragmentBinding.inflate(getLayoutInflater());
       // binding2.tvMonday.setText("Set test 2");
       // bottomSheetBehavior.isFitToContents = false;
      //  bottomSheetBehavior.halfExpandedRatio = 0.6f;



       // Log.i("tag binding name",binding.tvInfoLocale.getText().toString());

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                Log.i("State of frag: ", ""+newState);
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();

                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        googlePlaceViewModel = new ViewModelProvider(this).get(GooglePlaceViewModel.class);
        LiveData<List<GooglePlace>> googlePlaceList = googlePlaceViewModel.getGooglePlace();
        Observer<List<GooglePlace>> observer = new Observer<List<GooglePlace>>() {
            @Override
            public void onChanged(List<GooglePlace> googlePlaceList) {
               BottomFragment botFragClass = new BottomFragment();
             //  botFragClass.updateDays(googlePlaceList.get(0));
                updateDays(googlePlaceList.get(0));
            }
        };
        googlePlaceList.observeForever(observer);
    }

    public void updateDays(GooglePlace newPlace){
        List<GooglePlace.ItemPopularTimes> popTimes = newPlace.getPopulartimes();
        dayAverage = new int[7];
        for (int i = 0; i < 7; i++) {
            if(popTimes.get(i) != null) {
                int average = 0;
                int[] data = popTimes.get(i).getData();
                int notZeroEntry = 0;
                for(int j=0; j<data.length; j++){
                    average += data[j];
                    if (data[j] > 0) notZeroEntry++;
                }
                if (notZeroEntry == 0) {
                    for (int j = 0; j < 5; j++) {
                        average += dayAverage[j];
                    }
                    average /= 5;
                }
                else {
                    average /= notZeroEntry;
                }
                dayAverage[i] = average;

                String dayName = popTimes.get(i).getName();
                int viewId = 0;
                switch(dayName){
                    case "Monday": viewId = R.id.pbMonday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Tuesday": viewId = R.id.pbTuesday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Wednesday": viewId = R.id.pbWeds;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Thursday": viewId = R.id.pbThursday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Friday": viewId = R.id.pbFriday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Saturday": viewId = R.id.pbSaturday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                    case "Sunday": viewId = R.id.pbSunday;
                        Log.i("ben_tag", dayName +" "+ average);
                        break;
                }
                Log.i("ben_view id", " View ID: "+viewId);
                ProgressBar bar =  findViewById(viewId);
                bar.setProgress(average);
            }
        }

    }



    private void testGooglePlaceDb() {
        GooglePlaceAccess googlePlaceAccess = GooglePlaceAccess.getInstance(this, GooglePlaceDatabase.getInstance(this));
        List<GooglePlace> googlePlaces =
                googlePlaceAccess.getAll();

        if(googlePlaces.size() == 0){
            //Llenar db con valores de testeo
            GooglePlace googlePlace = new GooglePlace();
            googlePlace.setLatitude(39.48305714751131f);
            googlePlace.setLongitude(-0.34783486024869137f);
            googlePlace.setCurrentPopularity(15);
            googlePlace.setId("f");
            googlePlace.setName("Efe");
            googlePlaceAccess.add(googlePlace);

            googlePlace = new GooglePlace();

            googlePlace.setLatitude(39.48232417821347f);
            googlePlace.setLongitude(-0.3487664561099621f);
            googlePlace.setCurrentPopularity(10);
            googlePlace.setId("f");
            googlePlace.setName("Efe");
            googlePlaceAccess.add(googlePlace);

            googlePlace = new GooglePlace();

            googlePlace.setLatitude(39.48281274006481f);
            googlePlace.setLongitude(-0.3468889099088923f);
            googlePlace.setCurrentPopularity(30);
            googlePlace.setId("f");
            googlePlace.setName("Efe");
            googlePlaceAccess.add(googlePlace);

            googlePlace = new GooglePlace();

            googlePlace.setLatitude(39.483831256278556f);
            googlePlace.setLongitude(-0.3468567234025883f);
            googlePlace.setCurrentPopularity(20);
            googlePlace.setId("f");
            googlePlace.setName("Efe");
            googlePlaceAccess.add(googlePlace);
        }

        googlePlaces = googlePlaceAccess.getAll();

        Log.d("test db", googlePlaces.size() + "");
        for (GooglePlace googlePlace : googlePlaces) {
            Log.d("test db", googlePlace.getLatitude()+ ", " +googlePlace.getLongitude());
        }
    }

    private void ejemploLlamadaApi(){
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
                getLatLng(place.getId());

            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void getLatLng(String placeId) {
        if (placesUtils == null)
            placesUtils = new PlacesUtils(apiKey, mMap);

        placesUtils.getLatLng(placeId);
    }


    private void clearDb() {
        GooglePlaceAccess googlePlaceAccess = GooglePlaceAccess.getInstance(this,
                GooglePlaceDatabase.getInstance(this));
        SearchPlacesAccess searchPlacesAccess = SearchPlacesAccess.getInstance(this,
                GooglePlaceDatabase.getInstance(this));
        googlePlaceAccess.clearTable();
        searchPlacesAccess.clearTable();
    }

    public void openSavedMenu(View view) {
        SelectSavedSearchDialogFragment dialog = new SelectSavedSearchDialogFragment(mMap, mapsUtils);
        dialog.show(getSupportFragmentManager(), "selectSaved");
    }

    public static class SelectSavedSearchDialogFragment extends DialogFragment {
        private List<SearchPlaces.SearchPlacesWithGooglePlaces> searchPlaces;
        private GoogleMap mMap;
        private MapsUtils mapsUtils;

        public SelectSavedSearchDialogFragment(GoogleMap map, MapsUtils mapsUtils){
            mMap = map;
            this.mapsUtils = mapsUtils;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            SearchPlacesAccess searchPlacesAccess = SearchPlacesAccess.getInstance(getContext(),
                    GooglePlaceDatabase.getInstance(getContext()));
            searchPlaces = searchPlacesAccess.getAll();
            // Use the Builder class for convenient dialog construction
            CharSequence[] items = new CharSequence[searchPlaces.size()];
            for(int i = 0; i < searchPlaces.size(); i++){
                items[i] = searchPlaces.get(i).searchPlaces.getSearchedLocation();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialogSelectTitle)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(mapsUtils == null) mapsUtils = new MapsUtils(mMap);
                            //mapsUtils.clearHeatMap();
                            mapsUtils.clearMap();
                            List<GooglePlace> googlePlaces = searchPlaces.get(which).googlePlaces;
                            int average = PaintSearch.getAverage(googlePlaces);
                            LatLng latLng = searchPlaces.get(which).searchPlaces.getLatLng();

                            HeatmapDrawer heatmapDrawer = new HeatmapDrawer(mMap);
                            heatmapDrawer.drawCircle(latLng, average);

                            PaintSearch.CustomInfoWindowAdapter infoWindowAdapter = new PaintSearch.CustomInfoWindowAdapter(LayoutInflater.from(getContext()),average);
                            mMap.setInfoWindowAdapter(infoWindowAdapter);
                            String name = searchPlaces.get(which).searchPlaces.getSearchedLocation();
                            Marker lastMarker = mapsUtils.setMarker(latLng, name);
                            lastMarker.showInfoWindow();
                            //mapsUtils.addHeatMap(searchPlaces.get(which).googlePlaces);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

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

        mapsUtils = new MapsUtils(mMap);

        LatLng etsinf = new LatLng(39.48305714751131, -0.34783486024869137);
        mapsUtils.setMarker(etsinf, "Etsinf");

        List<GooglePlace> googlePlaces = mapsUtils.createPlaces(15, etsinf);

        mapsUtils.addHeatMap(googlePlaces);

        //testGooglePlaceDb();
        // clearDb();

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        /*
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
        */
    }
}