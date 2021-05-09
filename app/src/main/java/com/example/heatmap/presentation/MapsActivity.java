package com.example.heatmap.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String apiKey; // = BuildConfig.API_KEY;
    private ActivityMapsBinding binding;
    private GoogleMap mMap;
    private Marker lastMarker;
    private PlacesClient placesClient;
    private MapsUtils mapsUtils;
    private PlacesUtils placesUtils;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomFragContainer;
    private Button button;
    private GooglePlaceViewModel googlePlaceViewModel;
    private int[] dayAverage;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        if (sharedPrefs.getString("apiKey", "").equals("")) keyDialog();
        else {
            apiKey = sharedPrefs.getString("apiKey", "");
            initializePlaces(apiKey);
        }

        FloatingActionButton but = binding.preferencesButton;

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyDialog();
            }
        });

        PaintSearch.setContext(this);

        BottomFragment bottomFrag = BottomFragment.newInstance();

        //Create and initialize the fragment

        bottomFragContainer = findViewById(R.id.infoFragmentHolder);
        Fragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.infoFragmentHolder, BottomFragment.class, null).commit();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomFragContainer);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                Log.i("State of frag: ", "" + newState);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
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

    public void updateDays(GooglePlace newPlace) {
        List<GooglePlace.ItemPopularTimes> popTimes = newPlace.getPopulartimes();
        dayAverage = new int[7];
        for (int i = 0; i < 7; i++) {
            if (popTimes.get(i) != null) {
                int average = 0;
                int[] data = popTimes.get(i).getData();
                int notZeroEntry = 0;
                for (int j = 0; j < data.length; j++) {
                    average += data[j];
                    if (data[j] > 0) notZeroEntry++;
                }
                if (notZeroEntry == 0) {
                    for (int j = 0; j < 5; j++) {
                        average += dayAverage[j];
                    }
                    average /= 5;
                } else {
                    average /= notZeroEntry;
                }
                dayAverage[i] = average;

                String dayName = popTimes.get(i).getName();
                int viewId = 0;
                switch (dayName) {
                    case "Monday":
                        viewId = R.id.pbMonday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Tuesday":
                        viewId = R.id.pbTuesday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Wednesday":
                        viewId = R.id.pbWeds;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Thursday":
                        viewId = R.id.pbThursday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Friday":
                        viewId = R.id.pbFriday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Saturday":
                        viewId = R.id.pbSaturday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                    case "Sunday":
                        viewId = R.id.pbSunday;
                        Log.i("ben_tag", dayName + " " + average);
                        break;
                }
                Log.i("ben_view id", " View ID: " + viewId);
                ProgressBar bar = findViewById(viewId);
                bar.setProgress(average);
            }
        }

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);
    }

    private void getLatLng(String placeId) {
        if (placesUtils == null)
            placesUtils = new PlacesUtils(apiKey, mMap);

        placesUtils.getLatLng(placeId);
    }

    public void keyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Google Api key");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setText(sharedPrefs.getString("apiKey", ""));
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                apiKey = input.getText().toString();
                editor.putString("apiKey", apiKey);
                initializePlaces(apiKey);

                editor.apply();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!sharedPrefs.getString("apiKey", "").equals("")) dialog.cancel();
                else Toast.makeText(MapsActivity.this, "Pon un apikey", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void openSavedMenu(View view) {
        SelectSavedSearchDialogFragment dialog = new SelectSavedSearchDialogFragment(mMap, mapsUtils, this);
        dialog.show(getSupportFragmentManager(), "selectSaved");
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

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            requestPermissions( new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 100);
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng myLocation = new LatLng(location.getLongitude(), location.getLatitude());

        mapsUtils.moveCamera(myLocation);
    }
}