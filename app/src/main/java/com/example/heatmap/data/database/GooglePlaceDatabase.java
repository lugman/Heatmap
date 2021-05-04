package com.example.heatmap.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.heatmap.data.dao.GooglePlaceDao;
import com.example.heatmap.data.dao.SearchPlacesDao;
import com.example.heatmap.data.model.GooglePlace;
import com.example.heatmap.data.model.GooglePlaceConverters;
import com.example.heatmap.data.model.SearchPlaces;

@Database(entities = {GooglePlace.class, SearchPlaces.class}, version = 6, exportSchema = false)
@TypeConverters(GooglePlaceConverters.class)
public abstract class GooglePlaceDatabase extends RoomDatabase {
    private  static  GooglePlaceDatabase googlePlaceDatabase;

    public synchronized  static  GooglePlaceDatabase getInstance(Context context){
        if(googlePlaceDatabase == null) {
            googlePlaceDatabase = Room
                    .databaseBuilder(context, GooglePlaceDatabase.class, "googleplace_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return googlePlaceDatabase;
    }

    public abstract GooglePlaceDao googlePlaceDao();
    public abstract SearchPlacesDao searchPlacesDao();
}
