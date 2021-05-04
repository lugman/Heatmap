package com.example.heatmap.services.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.heatmap.data.model.GooglePlace;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GooglePlaceViewModel extends ViewModel {
    private MutableLiveData<List<GooglePlace>> googlePlaces = new MutableLiveData<>();
    public void setGooglePlace(GooglePlace googlePlaces) {
        this.googlePlaces.setValue(Arrays.asList(googlePlaces));
    }
    public void setGooglePlace(List<GooglePlace> googlePlaces) {
        this.googlePlaces.setValue(googlePlaces);
    }
    public LiveData<List<GooglePlace>> getGooglePlace() {return googlePlaces; }
}
