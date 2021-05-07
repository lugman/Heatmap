package com.example.heatmap.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.heatmap.R;
import com.example.heatmap.data.model.GooglePlace;
import com.example.heatmap.databinding.BottomFragmentBinding;
import com.example.heatmap.services.viewmodel.GooglePlaceViewModel;
import com.example.heatmap.utils.MapsUtils;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomFragment extends Fragment {

    private static GooglePlaceViewModel googlePlaceViewModel;
    private static BottomFragment instance;
    private BottomFragmentBinding binding;

    public BottomFragment() {
        // Required empty public constructor
    }
    public static BottomFragment getInstance(){
        if(instance != null ){
            instance = newInstance();
        }
        return instance;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.


     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomFragment newInstance() {
        BottomFragment fragment = new BottomFragment();
        Bundle args = new Bundle();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.bottom_fragment, container, false);
    }

/*
    public void updateDays(GooglePlace newPlace){


        List<GooglePlace.ItemPopularTimes> popTimes = newPlace.getPopulartimes();
        for (int i = 0; i < 7; i++) {
            if(popTimes.get(i) != null) {
                int average = 0;
                int[] data = popTimes.get(i).getData();
                for(int j=0; j<data.length; j++){
                    average = average + data[j];
                }
                average = average / data.length;

                String dayName = popTimes.get(i).getName();
                int viewId = 0;
                switch(dayName){
                    case "Monday": viewId = R.id.pbMonday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Tuesday": viewId = R.id.pbTuesday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Wednesday": viewId = R.id.pbWeds;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Thursday": viewId = R.id.pbThursday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Friday": viewId = R.id.pbFriday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Saturday": viewId = R.id.pbSaturday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                    case "Sunday": viewId = R.id.pbSunday;
                        Log.i("ben tag", dayName +" "+ average);
                        break;
                }
                Log.i("ben view id", " View ID: "+viewId);
              //  ProgressBar bar =  findViewById(viewId);
              // bar.setProgress(average);
            }
        }

    } */
}