package data.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import data.model.GooglePlace;

@Dao
public interface GooglePlaceDao {
    @Transaction
    @Query("SELECT * FROM place_table")
    List<GooglePlace> googlePlaces();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addGooglePlace(GooglePlace googlePlace);

    @Update
    void updateGooglePlace(GooglePlace googlePlace);

    @Delete
    void deleteGooglePlace(GooglePlace googlePlace);
}
