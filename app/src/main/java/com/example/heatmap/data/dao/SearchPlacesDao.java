package com.example.heatmap.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.heatmap.data.model.SearchPlaces;

import java.util.List;

@Dao
public interface SearchPlacesDao {
    @Transaction
    @Query("SELECT * FROM searchplaces_table")
    List<SearchPlaces.SearchPlacesWithGooglePlaces> getPlacesFromSearch();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addSearchPlaces(SearchPlaces searchPlaces);

    @Update
    void updateSearchPlaces(SearchPlaces searchPlaces);

    @Delete
    void deleteSearchPlaces(SearchPlaces searchPlaces);

    @Query("DELETE FROM searchplaces_table")
    void clearTable();
}
