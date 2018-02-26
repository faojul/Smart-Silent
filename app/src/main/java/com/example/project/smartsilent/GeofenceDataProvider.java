package com.example.project.smartsilent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class GeofenceDataProvider {
    public static List<GeofenceData> GeofencedataList;
    public static Map<String, GeofenceData> GeofencedataMap;



    //public static GeofenceController GC= new GeofenceController();




    //  prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);




    static {
        GeofencedataList = new ArrayList<>();
        GeofencedataMap = new HashMap<>();
    }




    private static void addGeofenceData(GeofenceData Data) {
        GeofencedataList.add(Data);
        GeofencedataMap.put(Data.getId(),Data);
        GeofenceController.getInstance().saveGson(Data);

    }

//    public static void populateData(){
//        GeofencedataList=GeofenceController.getInstance().getGsonData();
//
//    }



    public static void setvalue(String name,double Latitude,double Longitude,int radius)
    {
        addGeofenceData(new GeofenceData(null,name,Latitude,Longitude,radius));

    }
}
