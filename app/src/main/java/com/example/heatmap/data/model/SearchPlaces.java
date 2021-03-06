package com.example.heatmap.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Entity(tableName = "searchplaces_table")
public class SearchPlaces {
    @PrimaryKey(autoGenerate = true)
    private long id;

    public long getId(){return id;}
    public void setId(long id){this.id = id;}

    @ColumnInfo(name = "search_location")
    private String searchedLocation;

    public String getSearchedLocation() {
        return searchedLocation;
    }

    public void setSearchedLocation(String searchedLocation) {
        this.searchedLocation = searchedLocation;
    }

    @ColumnInfo(name = "search_latLng")
    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public SearchPlaces(){
    }

    public static class SearchPlacesWithGooglePlaces{
        @Embedded public SearchPlaces searchPlaces;
        @Relation(
                parentColumn = "id",
                entityColumn = "searchPlacesId"
        )
        public List<GooglePlace> googlePlaces;
    }
}


