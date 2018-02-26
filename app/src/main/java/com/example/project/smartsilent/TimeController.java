package com.example.project.smartsilent;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashrin Tamim on 1/14/2017.
 */

public class TimeController {

    private Context context;
    private Gson gson;
    private SharedPreferences prefs;

    private final String TAG = TimeController.class.getName();

    public List<TimeDate> mTimeData;

    private static TimeController INSTANCE;

    public static TimeController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeController();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();

        gson = new Gson();
        mTimeData = new ArrayList<>();

        prefs = this.context.getSharedPreferences(Constants.SharedTimePrefs.Time, Context.MODE_PRIVATE);


        getTimeGsonData();



    }

    public void saveTimeGson(TimeDate data) {
        this.context= context.getApplicationContext();


        String json = gson.toJson(data);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(data.getId(), json);
        //editor.putString("CHECK","ok");
        editor.apply();
    }

    private   void getTimeGsonData() {


        Map<String, ?> keys = prefs.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String jsonString = prefs.getString(entry.getKey(), null);
            TimeDate timeData = gson.fromJson(jsonString, TimeDate.class);
            mTimeData.add(timeData);

        }

    }

    public void removetimeitem(TimeDate mtimeDataToRemove){


        SharedPreferences.Editor editor = prefs.edit();

        // GeofenceDataToRemove.add(mGeofencDataToRemove);
        // for (GeofenceData mGeofence : GeofenceDataToRemove) {
        //   int index = mGeofenceData.indexOf(mGeofence);
        editor.remove(mtimeDataToRemove.getId());
        //mGeofenceData.remove(index);
        editor.apply();

    }
}
