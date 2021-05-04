package com.example.heatmap.data.model;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

public class PlaceSearch {
    public List<Result> results;
    public String status;

    public class Result {
        public List<AddressComponent> address_components;
        public String formatted_address;
        public Geometry geometry;
        public String place_id;
        public PlusCode plus_code;
        public String[] types;
    }

    public class AddressComponent {
        public String long_name;
        public String short_name;
        public List<String> types;
    }

    public class Location {
        public double lat;
        public double lng;
    }

    public class Northeast {
        public double lat;
        public double lng;
    }

    public class Southwest {
        public double lat;
        public double lng;
    }

    public class Viewport {
        public Northeast northeast;
        public Southwest southwest;
    }

    public class Geometry {
        public Location location;
        public String location_type;
        public Viewport viewport;
    }

    public class PlusCode {
        public String compound_code;
        public String global_code;
    }
}




