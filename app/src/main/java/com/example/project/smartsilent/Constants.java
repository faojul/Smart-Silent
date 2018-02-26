package com.example.project.smartsilent;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class Constants {

    private Constants() {
    }

    public static final String PACKAGE_NAME = "com.example.faojulahsan.smartsilent.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    private static final String TAG = "";

    public static class SharedPrefs {
        public static String Geofences = "SHARED_PREFS_GEOFENCES";
    }

    public static class SharedTimePrefs {
        public static String Time = "SHARED_PREFS_Time";
    }


    public static class Geometry {
        public static double MinLatitude = -90.0;
        public static double MaxLatitude = 90.0;
        public static double MinLongitude = -180.0;
        public static double MaxLongitude = 180.0;
        public static double MinRadius = 50; // meters
        public static double MaxRadius = 2000; // meters
    }
}
