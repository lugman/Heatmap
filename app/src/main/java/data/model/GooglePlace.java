package data.model;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GooglePlace {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String address;
    @NonNull
    private List<String> types;

    //popularTimes<dayOfWeek><data<Integer>>
    //data is an array of 24 integers corresponding to hours of the day. Each integer ranges from 0-100
    @NonNull
    private List<ItemPopularTimes> populartimes;

    @NonNull
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NonNull Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @NonNull
    private Coordinates coordinates;

    //HashMap<dayOfWeek><data<Integer>>  -  same as popularTimes but with waiting time
    private List<ItemPopularTimes> time_wait;
    private double rating;
    private int currentPopularity;
    //Commented because no clear example on popularity data from web.
   // private int popularity;
    private int rating_n;

    private String international_phone_number;
    private List<Integer> time_spent;

    public GooglePlace(@NonNull String id,@NonNull String name,@NonNull String address,@NonNull List<String> types,@NonNull List<ItemPopularTimes> populartimes,@NonNull Coordinates coordinates){
        this.id = id;
        this.name = name;
        this.address = address;
        this.types = types;
        this.populartimes = populartimes;
        this.coordinates= coordinates;
    }
    public GooglePlace(){
        this.coordinates= new Coordinates();
    }

    public void setLatitude(float latitude){
        coordinates.lat=latitude;
    }
    public void setLongitude(float longitude){
        coordinates.lng=longitude;
    }

    @NonNull
    public Float getLatitude() {
        return coordinates.lat;
    }

    @NonNull
    public Float getLongitude() {
        return coordinates.lng;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(@NonNull List<String> types) {
        this.types = types;
    }

    @NonNull
    public List<ItemPopularTimes> getPopulartimes() {
        return populartimes;
    }

    public void setPopulartimes(@NonNull List<ItemPopularTimes> populartimes) {
        this.populartimes = populartimes;
    }

    public List<ItemPopularTimes> getTimeWait() {
        return time_wait;
    }

    public void setTimeWait(List<ItemPopularTimes> time_wait) {
        this.time_wait = time_wait;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRating_n() {
        return rating_n;
    }

    public void setRating_n(int rating_n) {
        this.rating_n = rating_n;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public List<Integer> getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(List<Integer> time_spent) {
        this.time_spent = time_spent;
    }

    public int getCurrentPopularity() {
        return currentPopularity;
    }

    public void setCurrentPopularity(int currentPopularity) {
        this.currentPopularity = currentPopularity;
    }


    class ItemPopularTimes{
        String name;
        int data[];

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int[] getData() {
            return data;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ItemPopularTimes{" +
                    "name='" + name + '\'' +
                    ", data=" + Arrays.toString(data) +
                    '}';
        }
    }

    class Coordinates{
        float lng;
        float lat;

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

    }
}


