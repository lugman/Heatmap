package com.example.heatmap.presentation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.heatmap.R;
import com.example.heatmap.data.database.GooglePlaceDatabase;
import com.example.heatmap.data.database.SearchPlacesAccess;
import com.example.heatmap.data.model.GooglePlace;
import com.example.heatmap.data.model.SearchPlaces;
import com.example.heatmap.utils.HeatmapDrawer;
import com.example.heatmap.utils.MapsUtils;
import com.example.heatmap.utils.PaintSearch;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class SelectSavedSearchDialogFragment extends DialogFragment {
    private List<SearchPlaces.SearchPlacesWithGooglePlaces> searchPlaces;
    private GoogleMap mMap;
    private MapsUtils mapsUtils;
    private MapsActivity parent;

    public SelectSavedSearchDialogFragment(GoogleMap map, MapsUtils mapsUtils, MapsActivity parent) {
        mMap = map;
        this.mapsUtils = mapsUtils;
        this.parent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SearchPlacesAccess searchPlacesAccess = SearchPlacesAccess.getInstance(getContext(),
                GooglePlaceDatabase.getInstance(getContext()));
        searchPlaces = searchPlacesAccess.getAll();
        // Use the Builder class for convenient dialog construction
        CharSequence[] items = new CharSequence[searchPlaces.size()];
        for (int i = 0; i < searchPlaces.size(); i++) {
            items[i] = searchPlaces.get(i).searchPlaces.getSearchedLocation();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogSelectTitle)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mapsUtils == null) mapsUtils = new MapsUtils(mMap);
                        //mapsUtils.clearHeatMap();
                        mapsUtils.clearMap();
                        List<GooglePlace> googlePlaces = searchPlaces.get(which).googlePlaces;
                        int average = PaintSearch.getAverage(googlePlaces);
                        parent.updateDays(googlePlaces.get(0));
                        LatLng latLng = searchPlaces.get(which).searchPlaces.getLatLng();

                        HeatmapDrawer heatmapDrawer = new HeatmapDrawer(mMap);
                        heatmapDrawer.drawCircle(latLng, average);

                        PaintSearch.CustomInfoWindowAdapter infoWindowAdapter = new PaintSearch.CustomInfoWindowAdapter(LayoutInflater.from(getContext()), average);
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
