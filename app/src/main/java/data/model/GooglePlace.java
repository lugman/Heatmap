package data.model;

import androidx.annotation.NonNull;

import java.util.HashMap;
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

    //coords[0] = latitude, coords[1] = longitude
    @NonNull
    private List<Float> coords;

    //popularTimes<dayOfWeek><data<Integer>>
    //data is an array of 24 integers corresponding to hours of the day. Each integer ranges from 0-100
    @NonNull
    private Map<String,List<Integer>> popularTimes;

    //HashMap<dayOfWeek><data<Integer>>  -  same as popularTimes but with waiting time
    private Map<String,List<Integer>> timeWait;
    private double rating;
    private int currentPopularity;
    //Commented because no clear example on popularity data from web.
   // private int popularity;
    private int ratingN;
    private String phoneNum;
    private List<Integer> timeSpent;

    public GooglePlace(@NonNull String id,@NonNull String name,@NonNull String address,@NonNull List<String> types,@NonNull List<Float> coords,@NonNull Map<String,List<Integer>> popularTimes){
        this.id = id;
        this.name = name;
        this.address = address;
        this.types = types;
        this.coords = coords;
        this.popularTimes = popularTimes;
    }
    public GooglePlace(){
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
    public List<Float> getCoords() {
        return coords;
    }

    public void setCoords(@NonNull List<Float> coords) {
        this.coords = coords;
    }

    @NonNull
    public Map<String, List<Integer>> getPopularTimes() {
        return popularTimes;
    }

    public void setPopularTimes(@NonNull Map<String, List<Integer>> popularTimes) {
        this.popularTimes = popularTimes;
    }

    public Map<String, List<Integer>> getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(Map<String, List<Integer>> timeWait) {
        this.timeWait = timeWait;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingN() {
        return ratingN;
    }

    public void setRatingN(int ratingN) {
        this.ratingN = ratingN;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<Integer> getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(List<Integer> timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getCurrentPopularity() {
        return currentPopularity;
    }

    public void setCurrentPopularity(int currentPopularity) {
        this.currentPopularity = currentPopularity;
    }
}


