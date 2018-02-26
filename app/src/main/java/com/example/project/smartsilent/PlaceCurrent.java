package com.example.project.smartsilent;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlaceCurrent extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<Status> {

    private RadioButton buttonPlacePick;
    private RadioButton buttonPlaceList;

    //faojul

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "";
    private GoogleApiClient mGoogleApiClient;
    // private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private TextView mLatvalue;
    private TextView mLongvalue;

    // for getting Continuous update

    private final long LOC_UPDATE_INTERVAL = 10000; // 10s in milliseconds
    private final long LOC_FASTEST_UPDATE = 5000; // 5s in milliseconds
    protected LocationRequest mLocRequest;
    protected Location mCurLocation;

    //check location setting

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected LocationSettingsRequest mLocationSettingsRequest;

    private boolean mHaveLocPerm = false;

    //geofence
    protected ArrayList<Geofence> mGeofenceList;
    protected List<GeofenceData> mGeofenceDataList;
    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;
    private SharedPreferences mSharedPreferences;
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;
    private Button mAddsilentZoneButton;
    protected Double mLatvalSilent;
    protected Double mLongvalSilent;
    protected Button mList;
    private Gson gson;


    //Add Geofence Dialogue


    private String name;
    protected double latitude;
    protected double longitude;
    protected String sradius;
    protected int radius;


    final Context context = this;
    private Button buttonGeofence;




  /* Sequence of code
  *Activity Lifecycle Methods : onCreate,onStart,onStop,onResume,onPause
  *GooglePlayServices Lifecycle methods : onConnected , onConnectionFailed, onConnectionSuspended,
    onLocationChanged
  *Other Function
  * Geofance code
   */


    /**
     * Activity Lifecycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_place_current);
        getLayoutInflater().inflate(R.layout.activity_place_current, frameLayout);

        buttonPlacePick = (RadioButton) findViewById(R.id.btn1);

        buttonPlacePick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PlaceCurrent.this, PickPlace.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        buttonPlaceList = (RadioButton) findViewById(R.id.btn3);

        buttonPlaceList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PlaceCurrent.this, PlaceList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);



        //faojul

        //Geofence code start
        mAddGeofencesButton = (Button) findViewById(R.id.buttonstart);
        mRemoveGeofencesButton = (Button) findViewById(R.id.buttonstop);
       // mAddsilentZoneButton = (Button) findViewById(R.id.buttonaddsilentzone);

        mGeofenceList = new ArrayList<>();
        mGeofenceDataList = new ArrayList<>();
        mGeofencePendingIntent = null;
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        setButtonsEnabledState();

        populateGeofenceList();


        //Geofence code stop


        mLatvalue = (TextView) findViewById(R.id.valLat);

        mLongvalue = (TextView) findViewById(R.id.valLong);

        mCurLocation = null;


        // build the Play Services client object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //setting Location update setting request
        mLocRequest = new LocationRequest().setInterval(LOC_UPDATE_INTERVAL)
                .setFastestInterval(LOC_FASTEST_UPDATE)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //check the status of location settings


        // checkLocationSettings();

        //  buildLocationSettingsRequest();


        //Add Geofence Dialogue

//        GeofenceController.getInstance().init(context);
        final Context Controllercontext = this;
        buttonGeofence = (Button) findViewById(R.id.addGeofence);

        // add button listener
        buttonGeofence.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // custom dialog

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.geofence_dialogue);
                dialog.setTitle("Add A Zone");
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                Button dialogButtonSave = (Button) dialog.findViewById(R.id.dialogButtonSave);
                if (mCurLocation == null) {
                    //Toast.makeText(this,"Location Services is not ready yet", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Location is not ready yet , still you can add it manually", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    // if button is clicked, close the custom dialog
                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();


                        }
                    });


//                    ((EditText)dialog.findViewById(R.id.dialogueLatitude)).setHint(String.format(v.getResources().getString(R.string.Hint_Latitude), Constants.Geometry.MinLatitude, Constants.Geometry.MaxLatitude));
//                    ((EditText)dialog.findViewById(R.id.dialogueLongitude)).setHint(String.format(v.getResources().getString(R.string.Hint_Longitude), Constants.Geometry.MinLongitude, Constants.Geometry.MaxLongitude));
//                    ((EditText)dialog.findViewById(R.id.dialogueRadious)).setHint(String.format(v.getResources().getString(R.string.Hint_Radius), Constants.Geometry.MinRadius, Constants.Geometry.MaxRadius));


                    dialogButtonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //   if (dataIsValid()) {

                            name = ((EditText) dialog.findViewById(R.id.dialogueName)).getText().toString();

                            String slat = ((EditText) dialog.findViewById(R.id.dialogueLatitude)).getText().toString();
                            latitude = Double.parseDouble(slat);
                            String slong = ((EditText) dialog.findViewById(R.id.dialogueLongitude)).getText().toString();
                            longitude = Double.parseDouble(slong);

                            sradius = ((EditText) dialog.findViewById(R.id.dialogueRadious)).getText().toString();
                            radius = Integer.parseInt(sradius);
                            GeofenceDataProvider.setvalue(name, latitude, longitude, radius);
//                        Intent intent = new Intent(MainActivity.this,ListActivity.class);
//                        startActivity(intent);

                            populateGeofenceList();
                            if (mGeofencesAdded) {
                                addListtoGeofenceAfterSilentZoneadded();

                            }

                            dialog.dismiss();
//                        }
//                        else{
//                                showValidationErrorToast();
//                        }

                        }
                    });
                    dialog.show();
                }

                else {
                    String slat = String.valueOf(mCurLocation.getLatitude());
                    if(slat!=null)
                        ((EditText) dialog.findViewById(R.id.dialogueLatitude)).setText(slat);
                    latitude = mCurLocation.getLatitude();
                    String slong = String.valueOf(mCurLocation.getLongitude());
                    if(slong!=null)
                        ((EditText) dialog.findViewById(R.id.dialogueLongitude)).setText(slong);
                    longitude = mCurLocation.getLongitude();


                    // if button is clicked, close the custom dialog
                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();


                        }
                    });


                    dialogButtonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  if (dataIsValid()) {

                            name = ((EditText) dialog.findViewById(R.id.dialogueName)).getText().toString();

//                        longitude =((EditText)dialog.findViewById(R.id.editText3)).getText().toString();
//                        String slat= String.valueOf(mCurLocation.getLatitude());
//                        ((EditText)dialog.findViewById(R.id.editText2)).setText(slat);
//                        Slatitude = ((EditText)dialog.findViewById(R.id.editText2)).getText().toString();
                            sradius = ((EditText) dialog.findViewById(R.id.dialogueRadious)).getText().toString();
                            radius = Integer.parseInt(sradius);
                            GeofenceDataProvider.setvalue(name, latitude, longitude, radius);
//                        Intent intent = new Intent(MainActivity.this,ListActivity.class);
//                        startActivity(intent);

                            populateGeofenceList();
                            if (mGeofencesAdded) {
                                addListtoGeofenceAfterSilentZoneadded();

                            }

                            dialog.dismiss();
//                        }
//                        else{
//                            showValidationErrorToast();
//                        }

                        }
                    });

                    dialog.show();


                }


            }


        });


    }

    //faojul

    /*  //Data Validation for Dialogue box



    private boolean dataIsValid() {
        boolean validData = true;


        String name = ((EditText) dialog.findViewById(R.id.dialogueName)).getText().toString();
        String latitudeString = ((EditText) dialog.findViewById(R.id.dialogueLatitude)).getText().toString();
        String longitudeString = ((EditText) dialog.findViewById(R.id.dialogueLongitude)).getText().toString();
        String radiusString = ((EditText) dialog.findViewById(R.id.dialogueRadious)).getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(latitudeString)
                || TextUtils.isEmpty(longitudeString) || TextUtils.isEmpty(radiusString)) {
            validData = false;
        } else {
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);
            float radius = Float.parseFloat(radiusString);
            if ((latitude < Constants.Geometry.MinLatitude || latitude > Constants.Geometry.MaxLatitude)
                    || (longitude < Constants.Geometry.MinLongitude || longitude > Constants.Geometry.MaxLongitude)
                    || (radius < Constants.Geometry.MinRadius || radius > Constants.Geometry.MaxRadius)) {
                validData = false;
            }
        }

        return validData;
    }

    private void showValidationErrorToast() {
        Toast.makeText(context, getString(R.string.Toast_Validation), Toast.LENGTH_SHORT).show();
    }

    // endregion


*/






    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Connecting to Google Play Services");

        // Connect to Play Services
        GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
        int resultCode = gAPI.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            gAPI.getErrorDialog(this, resultCode, 1).show();
        }
        else {
            mGoogleApiClient.connect();
            buildLocationSettingsRequest();
        }

        if(!ConnectivityCheck.CheckInternetConnection(context)){
            new AlertDialog.Builder(context)
                    .setTitle("Internet Settings")
                    .setMessage("Your are not connected to internet. Please enable")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

//    @Override
//    protected void onResume() {
//
//    }
//
//    @Override
//    protected void onPause() {
//
//
//    }


    //check location setting start

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocRequest);
        mLocationSettingsRequest = builder.build();
        //  builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);



        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //  final LocationSettingsStates states= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    PlaceCurrent.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;
        }
    }


    //check location setting end




    /**
     * Google Play Services Lifecycle methods
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // If we're running on API 23 or above, we need to ask permission at runtime
        int permCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocRequest,this);
            initializeUI();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurLocation = location;
        updateUI();
    }



    /**
     * Called when the user has been prompted at runtime to grant permissions
     */
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results){
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                initializeUI();
            }
        }
    }


    public void setLocationFields(Location loc) {
        Log.d(TAG, "Updating location fields");
        if (loc != null) {
            mLatvalue.setText(String.format("%f", loc.getLatitude()));
            mLongvalue.setText(String.format("%f", loc.getLongitude()));


        }
    }

    /**
     * Retrieves the last known location. Assumes that permissions are granted.
     */


    protected void updateUI() {
        // take the lat and long of the current location object and add it to the list
        if (mCurLocation != null) {
            mLatvalue.setText(String.format("%f\n", mCurLocation.getLatitude()));
            mLongvalue.setText(String.format(" %f\n", mCurLocation.getLongitude()));

        }
    }

    protected void initializeUI() {
        // start by getting the last known location as a starting point
        if (mCurLocation == null) {
            try{
                mCurLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                Log.i(TAG,"Permission Denied while initizlizeUI");
            }

            // clear the locations list
            //mLocationList.setText("");
            updateUI();
        }
    }



    //Geofence code start here



    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();


        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER );

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }


    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected() ) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("GoogleApiClient is not yet connected.Plaese Try again.");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
//            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        if(mGeofenceDataList.isEmpty()){

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Place List is empty. Please add some places");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
//            Toast.makeText(this, "Place List is empty . Please add some places" , Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }


    public void removeGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }


    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }




    public void populateGeofenceList() {


        //Getting from SharedPreferences

        GeofenceController.getInstance().init(this);
        mGeofenceDataList=GeofenceController.getInstance().mGeofenceData;



        for(GeofenceData GD : mGeofenceDataList)
        {

            //add to Geofencelist

            mGeofenceList.add(new Geofence.Builder()

                    .setRequestId(GD.getId())


                    .setCircularRegion(
                            GD.getLatitude(),
                            GD.getLongitude(),
                            GD.getRadius()
                    )


                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)


                    .build());




        }
        Log.e("TAG","GeofenceList is   " +mGeofenceList.toString());

    }


    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mAddGeofencesButton.setEnabled(true);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }




    //new class added by abir . previously this was inside the addGeofencesButtonHandler
    //as problem with toggle the add remove button. set result call back is removed
    public void addListtoGeofenceAfterSilentZoneadded()
    {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {


            // Remove geofences.

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,

                    getGeofencingRequest(),

                    getGeofencePendingIntent()
            ).toString();

        } catch (SecurityException securityException) {

            logSecurityException(securityException);
        }
    }

}
