package com.example.project.smartsilent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class GeofenceController {

    private Context context;
    private Gson gson;
    private SharedPreferences prefs;
    private GeofenceControllerListener listener;

    private GoogleApiClient googleApiClient;


    private final String TAG = GeofenceController.class.getName();

    // public static final String PREFS_GEOFENCE="prefs_geofence";

    public List<GeofenceData> mGeofenceData;
    public List<GeofenceData> getGeofenceData() {
        return mGeofenceData;
    }

    private List<GeofenceData> GeofenceDataToRemove;






    private static GeofenceController INSTANCE;

    public static GeofenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController();
        }
        return INSTANCE;
    }



    public void init(Context context) {
        this.context = context.getApplicationContext();

        gson = new Gson();
        mGeofenceData = new ArrayList<>();
        GeofenceDataToRemove = new ArrayList<>();
        prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);


        getGsonData();



    }


    public void saveGson(GeofenceData data) {
        this.context= context.getApplicationContext();


        String json = gson.toJson(data);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(data.getId(), json);
        //editor.putString("CHECK","ok");
        editor.apply();
    }

    private   void getGsonData() {


        Map<String, ?> keys = prefs.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String jsonString = prefs.getString(entry.getKey(), null);
            GeofenceData geofenceData = gson.fromJson(jsonString, GeofenceData.class);
            mGeofenceData.add(geofenceData);

        }

    }



    public void removeGeofences(GeofenceData mGeofencDataToRemove){ //,GeofenceControllerListener listener) {
        // this.GeofenceDataToRemove = GeofencDataToRemove;
        //this.listener = listener;
        Log.e(TAG,"remove block started");
        //if(false)
        // connectWithCallbacks(connectionRemoveListener);
        //else
        //removeSavedGeofences();

        SharedPreferences.Editor editor = prefs.edit();

        // GeofenceDataToRemove.add(mGeofencDataToRemove);
        // for (GeofenceData mGeofence : GeofenceDataToRemove) {
        //   int index = mGeofenceData.indexOf(mGeofence);
        editor.remove(mGeofencDataToRemove.getId());
        //mGeofenceData.remove(index);
        editor.apply();
        Log.e(TAG,"yes");

        //}

        Log.e("TAG","ID: "+mGeofencDataToRemove.getId() + " name: " + mGeofencDataToRemove.getName());


        Log.e(TAG,"i'm in remove block");
    }
//
//    public void removeAllGeofences(GeofenceControllerListener listener) {
//        GeofenceDataToRemove = new ArrayList<>();
//        for (GeofenceData mGeofence : mGeofenceData) {
//            GeofenceDataToRemove.add(mGeofence);
//        }
//        this.listener = listener;
//
//        connectWithCallbacks(connectionRemoveListener);
//    }

    //will be deleted in main app while accumulated
//    private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
//        googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(callbacks)
//                .addOnConnectionFailedListener(connectionFailedListener)
//                .build();
//        googleApiClient.connect();
//    }

//    private GeofencingRequest getAddGeofencingRequest() {
//        List<GeofenceData> geofencesToAdd = new ArrayList<>();
//        geofencesToAdd.add(geofenceToAdd);
//        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
//        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
//        builder.addGeofences(geofencesToAdd);
//        return builder.build();
//    }



    private void removeSavedGeofences() {
        SharedPreferences.Editor editor = prefs.edit();

        for (GeofenceData mGeofence : GeofenceDataToRemove) {
            int index = mGeofenceData.indexOf(mGeofence);
            editor.remove(mGeofence.getId());
            mGeofenceData.remove(index);
            editor.apply();
        }
        Log.e(TAG,"i'm in removesavedGeofences block");

        // if (listener != null) {
        //   listener.onGeofencesUpdated();
        //}
    }

    private void sendError() {
        if (listener != null) {
            listener.onError();
        }
    }

    // endregion

    // deleted till this

    private GoogleApiClient.ConnectionCallbacks connectionRemoveListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            List<String> removeIds = new ArrayList<>();
            for (GeofenceData mGeofence : GeofenceDataToRemove) {
                removeIds.add(mGeofence.getId());
            }

            if (removeIds.size() > 0) {
                PendingResult<Status> result = LocationServices.GeofencingApi.removeGeofences(googleApiClient, removeIds);
                result.setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            removeSavedGeofences();
                        } else {
                            Log.e(TAG, "Removing geofence failed: " + status.getStatusMessage());
                            sendError();
                        }
                    }
                });
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e(TAG, "Connecting to GoogleApiClient suspended.");
            sendError();
        }
    };

    // endregion

    // region OnConnectionFailedListener

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(TAG, "Connecting to GoogleApiClient failed.");
            sendError();
        }
    };






    // endregion

    // region Interfaces

    public interface GeofenceControllerListener {
        void onGeofencesUpdated();
        void onError();
    }

    // end region

}
