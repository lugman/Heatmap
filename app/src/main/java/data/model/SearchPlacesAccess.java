package data.model;

import android.content.Context;

import java.util.List;

public class SearchPlacesAccess {
    private SearchPlacesDao dao;
    private static SearchPlacesAccess searchPlacesAccess;

    public static SearchPlacesAccess getInstance(Context context, GooglePlaceDatabase database){
        if(searchPlacesAccess == null) searchPlacesAccess = new SearchPlacesAccess(context, database);
        return searchPlacesAccess;
    }

    public SearchPlacesAccess(Context context, GooglePlaceDatabase database){
        dao = database.searchPlacesDao();
    }

    public List<SearchPlaces.SearchPlacesWithGooglePlaces> getAll(){
        return dao.getPlacesFromSearch();
    }

    public long add(SearchPlaces searchPlaces){
        return dao.addSearchPlaces(searchPlaces);
    }

    public void clearTable(){
        dao.clearTable();
    }
}