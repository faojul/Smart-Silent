package com.example.project.smartsilent;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class GeofenceTransitionsIntentService  extends IntentService {





    protected static final String TAG = "GeofenceTransitionsIS";

    private SharedPreferences prefs;
    private Gson gson;



    public GeofenceTransitionsIntentService() {

        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {



        AudioManager audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();





            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);

            //code for profile change start

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                if(audioManager.getRingerMode()!= AudioManager.RINGER_MODE_SILENT)
                {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                }
            }

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            }

            //code for profile change end
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }


    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {


        prefs = getApplicationContext().getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        gson = new Gson();


        String geofenceTransitionString = getTransitionString(geofenceTransition);
        List<String> geofenceIds= new ArrayList<>();

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesNameList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            //triggeringGeofencesIdsList.add(geofence.getRequestId());

            geofenceIds.add(geofence.getRequestId());


        }



        for (String geofenceId : geofenceIds) {
            String geofenceName = "";

            // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference
            Map<String, ?> keys = prefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String jsonString = prefs.getString(entry.getKey(), null);
                GeofenceData geofenceData = gson.fromJson(jsonString, GeofenceData.class);
                if (geofenceData.getId().equals(geofenceId)) {
                    geofenceName = geofenceData.getName();
                    triggeringGeofencesNameList.add(geofenceName);
                    break;
                }
            }
        }



        String triggeringGeofencesNameString = TextUtils.join(", ",  triggeringGeofencesNameList);

        return geofenceTransitionString + ": " + triggeringGeofencesNameString;
    }


    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), PlaceCurrent.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(PlaceCurrent.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.logo_nn)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.logo_nn))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }


    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

}
