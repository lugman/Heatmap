package data.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GooglePlaceConverters {
    @TypeConverter
    public static List<GooglePlace.ItemPopularTimes> fromString(String value) {
        Type listType = new TypeToken<List<GooglePlace.ItemPopularTimes>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<GooglePlace.ItemPopularTimes> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static GooglePlace.Coordinates coordinatesFromString(String value) {
        Type coordinatesType = new TypeToken<GooglePlace.Coordinates>() {}.getType();
        return new Gson().fromJson(value, coordinatesType);
    }

    @TypeConverter
    public static String fromCoordinates(GooglePlace.Coordinates coordinates) {
        Gson gson = new Gson();
        String json = gson.toJson(coordinates);
        return json;
    }
}
