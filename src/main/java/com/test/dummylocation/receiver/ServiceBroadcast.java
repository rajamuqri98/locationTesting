package com.test.dummylocation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.test.dummylocation.service.LocationService;

public class ServiceBroadcast extends BroadcastReceiver {

    private final static String TAG = "ServiceBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, LocationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(newIntent);
        } else {
            context.startService(newIntent);
        }

        Log.i(TAG, "onReceive: ");
    }
}
