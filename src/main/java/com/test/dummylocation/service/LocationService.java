package com.test.dummylocation.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class LocationService extends Service {

    // Const values
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in m  (1KM)
    private static final long MIN_TIME_BETWEEN_UPDATES = 60 * 1000;    // in ms (1 min)

    protected HandlerThread handlerThread;
    protected SharedPreferences sharedPreferences;

    protected boolean isGPSEnabled = false;
    protected boolean isNetworkEnabled = false;
    protected boolean isUpdate = false;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String locationProvider;

    protected Location currLocation;

    private ServiceHandler serviceHandler;
    private String mLog, mLat;

    private ThreadSubmitLoc TSL;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        handlerThread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Looper serviceLooper = handlerThread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currLocation = location;
                mLat = Double.toString(location.getLatitude()).trim();
                mLog = Double.toString(location.getLongitude()).trim();

                if (isUpdate) {
                    String cLocation = "Current Location: (" + mLat + "," + mLog + ")";
                    Toast.makeText(LocationService.this, cLocation, Toast.LENGTH_SHORT).show();

                    TSL = new ThreadSubmitLoc(sharedPreferences, location);
                    TSL.start();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                // Deprecated in Android Q
            }

            @Override
            public void onProviderEnabled(String s) {
                String text = s + " has been enabled. Recontinuing location updates.";
                Toast.makeText(LocationService.this, text, Toast.LENGTH_SHORT).show();
                isUpdate = true;
            }

            @Override
            public void onProviderDisabled(String s) {
                String text = s + " has been disabled. Halting location updates.";
                Toast.makeText(LocationService.this, text, Toast.LENGTH_SHORT).show();
                isUpdate = false;
            }
        };

        locationProvider = locationManager.getBestProvider(new Criteria(), false);
        isUpdate = true;

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;

        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();

        if (currLocation != null) {
            String cLocation = "Final Location: (" + currLocation.getLatitude() + "," + currLocation.getLongitude() + ")";
            Toast.makeText(this, cLocation, Toast.LENGTH_SHORT).show();

            TSL = new ThreadSubmitLoc(sharedPreferences, currLocation);
            TSL.start();
        }
        locationManager.removeUpdates(locationListener);
        locationListener = null;
        locationManager = null;
        stopSelf();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            // Main Job
            if (locationManager != null) {
                checkPermission(msg.arg1);
                checkGPS(msg.arg1);
                locationManager.requestLocationUpdates(locationProvider,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener);
                currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    private void checkPermission(int id) {
        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(LocationService.this, "ACCESS FINE LOCATION not permitted.", Toast.LENGTH_SHORT).show();
                stopSelf(id);
            }
        }
        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(LocationService.this, "ACCESS COARSE LOCATION not permitted.", Toast.LENGTH_SHORT).show();
                stopSelf(id);
            }
        }
    }

    private void checkGPS(int id) {
        if (locationProvider.equals(LocationManager.GPS_PROVIDER)) {
            if (!isGPSEnabled) {
                Toast.makeText(LocationService.this, "GPS not available.", Toast.LENGTH_SHORT).show();
                isUpdate = false;
                stopSelf(id);
            }
        } else if (locationProvider.equals(LocationManager.NETWORK_PROVIDER)) {
            if (!isNetworkEnabled) {
                Toast.makeText(LocationService.this, "Network GPS not available.", Toast.LENGTH_SHORT).show();
                isUpdate = false;
                stopSelf(id);
            }
        } else {
            Toast.makeText(LocationService.this, "No Location Provider Found.", Toast.LENGTH_SHORT).show();
            isUpdate = false;
            stopSelf(id);
        }
    }

}
