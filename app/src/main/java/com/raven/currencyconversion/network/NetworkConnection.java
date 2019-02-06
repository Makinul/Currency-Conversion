package com.raven.currencyconversion.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
    Context context;
    private static final NetworkConnection instance = new NetworkConnection();

    public NetworkConnection() {
        // please check it
    }

    public static NetworkConnection getInstance() {
        return instance;
    }

    public boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
                return false;

            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
