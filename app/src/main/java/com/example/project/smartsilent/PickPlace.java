package com.example.project.smartsilent;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class PickPlace extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks  {

    private RadioButton buttonPlaceList;
    private RadioButton buttonCurrentPlace;


    private final String TAG = "PLACEPICKER_EXERCISE";

    private final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;
    private final int PLACE_PICKER_REQUEST = 1001;
    private SharedPreferences mSharedPreferences;
    private boolean mGeofencesAdded;

    private String name;
    private double latitude;
    private double longitude;
    private int radius;


    final Context context = this;



    protected GoogleApiClient mGoogleApiClient;
    private boolean mHaveLocPerm = false;


    private void pickAPlace() {
        if (mHaveLocPerm) {
            try {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = builder.build(this);

                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void updateUI(Place chosenPlace) {
        /*TextView placeData = (TextView)findViewById(R.id.tvPlaceData);

        double lattitude = chosenPlace.getLatLng().latitude;
        double longittude= chosenPlace.getLatLng().longitude;
        String str = chosenPlace.getName() + "\n"
                + chosenPlace.getAddress() + "\n"
                + chosenPlace.getPhoneNumber() + "\n"
                + chosenPlace.getWebsiteUri() + "\n"
                + chosenPlace.getPlaceTypes() + "\n"
                + "lattitude: " + lattitude + "\n"
                + "longtitude: " + longittude;

        placeData.setText(str);*/
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_place_picker, frameLayout);

        buttonPlaceList = (RadioButton) findViewById(R.id.btn3);

        buttonPlaceList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PickPlace.this, PlaceList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        buttonCurrentPlace = (RadioButton) findViewById(R.id.btn2);

        buttonCurrentPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PickPlace.this, PlaceCurrent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);



        // Build the GoogleApiClient and connect to the Places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        findViewById(R.id.show_place_picker).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickAPlace();
                    }
                }
        );


        //faojul

        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);


    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;

            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place chosenPlace = PlacePicker.getPlace(this, data);
                    //updateUI(chosenPlace);

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.geofence_dialogue);
                    dialog.setTitle("Add A Zone");


                    Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                    Button dialogButtonSave = (Button) dialog.findViewById(R.id.dialogButtonSave);

                    //faojul
                    String sname= chosenPlace.getName().toString();
                    if(sname!=null)
                    {
                        ((EditText) dialog.findViewById(R.id.dialogueName)).setText(sname);
                    }
                    String slat = String.valueOf(chosenPlace.getLatLng().latitude);
                    if(slat!=null)
                        ((EditText) dialog.findViewById(R.id.dialogueLatitude)).setText(slat);
                    latitude = chosenPlace.getLatLng().latitude;
                    String slong = String.valueOf(chosenPlace.getLatLng().longitude);
                    if(slong!=null)
                        ((EditText) dialog.findViewById(R.id.dialogueLongitude)).setText(slong);
                    longitude = chosenPlace.getLatLng().longitude;
                    //end


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
                            name = ((EditText) dialog.findViewById(R.id.dialogueName)).getText().toString();

                            if(latitude==0 ) {
                                String slat = ((EditText) dialog.findViewById(R.id.dialogueLatitude)).getText().toString();
                                latitude = Double.parseDouble(slat);
                            }
                            if(longitude==0) {
                                String slong = ((EditText) dialog.findViewById(R.id.dialogueLongitude)).getText().toString();
                                longitude = Double.parseDouble(slong);
                            }


                            String sradius = ((EditText) dialog.findViewById(R.id.dialogueRadious)).getText().toString();
                            radius = Integer.parseInt(sradius);
                            GeofenceDataProvider.setvalue(name, latitude, longitude, radius);
                        Intent intent = new Intent(PickPlace.this,PlaceCurrent.class);
                        startActivity(intent);

//                            PlaceCurrent pc = new PlaceCurrent();
//                            pc.getApplicationContext();
//                            pc.populateGeofenceList();
//                            if (mGeofencesAdded) {
//                                pc.addListtoGeofenceAfterSilentZoneadded();
//                            }
                        }
                    });

                    dialog.show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results){
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                mHaveLocPerm = true;
            }
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Places API");

        // If we're running on API 23 or above, we need to ask permissions at runtime
        // In this case, we need Fine Location access to use Place Detection
        int permCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            mHaveLocPerm = true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            }
            catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        }
        else {
            GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
            gAPI.getErrorDialog(this, result.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection was suspended for some reason");
        mGoogleApiClient.connect();
    }
}
