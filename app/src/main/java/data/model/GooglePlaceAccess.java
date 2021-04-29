package data.model;

import android.content.Context;

import java.util.List;

public class GooglePlaceAccess {
    private GooglePlaceDao dao;
    private static GooglePlaceAccess googlePlaceAccess;

    public static GooglePlaceAccess getInstance(Context context, GooglePlaceDatabase database){
        if(googlePlaceAccess == null) googlePlaceAccess = new GooglePlaceAccess(context, database);
        return googlePlaceAccess;
    }

    public GooglePlaceAccess(Context context, GooglePlaceDatabase database){
        dao = database.googlePlaceDao();
    }

    public List<GooglePlace> getAll(){
        return dao.googlePlaces();
    }

    public void add(GooglePlace googlePlace){
        dao.addGooglePlace(googlePlace);
    }

    public void clearTable(){ dao.clearTable(); }
}
