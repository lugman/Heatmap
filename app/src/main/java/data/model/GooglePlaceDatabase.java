package data.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {GooglePlace.class, SearchPlaces.class}, version = 5, exportSchema = false)
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
