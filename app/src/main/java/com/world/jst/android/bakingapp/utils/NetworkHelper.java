package com.world.jst.android.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkHelper {

    public static boolean hasNetworkAccess(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
