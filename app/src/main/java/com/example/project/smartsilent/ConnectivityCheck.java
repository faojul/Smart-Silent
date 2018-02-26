package com.example.project.smartsilent;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class ConnectivityCheck {
    public static final boolean CheckInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }



    }
}