package com.example.heatmap.connections;

public class ParametersPT {

    final String API_KEY = "AIzaSyCelNyXZrtQTVmxtmOZxnRcyskjZamkVG8";
    final String METHOD_GET = "get";
    final String METHOD_GET_ID = "get_id";
    final int DEFAULT_RADIUS = 180;
    final int DEFAULT_THREADS = 20;
    final boolean DEFAULT_ALL_PLACES = false;
    public double[] p1;
    public double[] p2;
    String api_key;
    String place_id;
    String[] types;
    int n_threads;
    int radius;
    boolean all_places;
    String method;

    public ParametersPT(String place_id) {
        this.api_key = API_KEY;
        this.method = METHOD_GET_ID;
        this.place_id = place_id;

    }

    public ParametersPT(String[] types, double[] p1, double[] p2) {
        this.api_key = API_KEY;
        this.method = METHOD_GET;
        this.n_threads = DEFAULT_THREADS;
        this.radius = DEFAULT_RADIUS;
        this.all_places = DEFAULT_ALL_PLACES;

        this.types = types;
        this.p1 = p1;
        this.p2 = p2;
    }

    public ParametersPT(String[] types, double[] p1, double[] p2, int n_threads) {
        this.api_key = API_KEY;
        this.method = METHOD_GET;
        this.types = types;
        this.radius = DEFAULT_RADIUS;
        this.all_places = DEFAULT_ALL_PLACES;

        this.p1 = p1;
        this.p2 = p2;
        this.n_threads = n_threads;
    }

    public ParametersPT(String[] types, double[] p1, double[] p2, int n_threads, int radius) {
        this.api_key = API_KEY;
        this.method = METHOD_GET;
        this.all_places = DEFAULT_ALL_PLACES;

        this.types = types;
        this.p1 = p1;
        this.p2 = p2;
        this.n_threads = n_threads;
        this.radius = radius;
    }

    public ParametersPT(String[] types, double[] p1, double[] p2, int n_threads, int radius, boolean all_places) {
        this.api_key = API_KEY;
        this.method = METHOD_GET;

        this.types = types;
        this.p1 = p1;
        this.p2 = p2;
        this.n_threads = n_threads;
        this.radius = radius;
        this.all_places = all_places;
    }
}
