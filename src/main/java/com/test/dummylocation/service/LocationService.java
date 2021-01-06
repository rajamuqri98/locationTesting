package com.test.dummylocation.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.util.Log;
import android.widget.Toast;

import com.test.dummylocation.receiver.ServiceBroadcast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class LocationService extends Service {

    // Const values
    private static final String TAG = "LocationService";
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // in m  (1KM)
    private static final long MIN_TIME_BETWEEN_UPDATES = 5 * 1000;    // in ms (1 min)
    private static final int NOTIFICATION_ID = 1001;

    protected HandlerThread handlerThread;
    protected SharedPreferences sharedPreferences;

    protected boolean isGPSEnabled = false;
    protected boolean isNetworkEnabled = false;
    protected boolean isUpdate = false;
    protected boolean isRestart;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String locationProvider;

    protected Location currLocation;

    private ServiceHandler serviceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        String HANDLER_TAG = "LOCATION_SERVICE";
        handlerThread = new HandlerThread(HANDLER_TAG, Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRestart = true;

        Looper serviceLooper = handlerThread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyForeground();
        } else {
            startForeground(1, new Notification());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = intent.getIntExtra("Command", 0);

        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();

        if (currLocation != null) {
            Message msg = serviceHandler.obtainMessage();
            msg.arg2 = 3; // Final Location

            serviceHandler.sendMessage(msg);
        }
        locationManager.removeUpdates(locationListener);
        locationListener = null;
        locationManager = null;

        stopForeground(true);
        serviceHandler.removeCallbacksAndMessages(null);
        handlerThread.interrupt();

        if (isRestart) {
            selfBroadcast();
        }
    }

    private void checkPermission(int id) {
        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(LocationService.this, "ACCESS LOCATION not permitted.", Toast.LENGTH_SHORT).show();
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

    private static String GetDateTime() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        return SDF.format(c.getTime());
    }

    private static String dateTimeHandler(String cDate) {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SDF.setTimeZone(TimeZone.getDefault());

        Date date;
        try {
            date = SDF.parse(cDate);

            SDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (date != null) {
                return SDF.format(date);
            }
        } catch (ParseException e) {
            Log.e("ServerDate", e.toString());
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.myLocation";
        String CHANNEL_NAME = "Background Service";

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void selfBroadcast() {
        Intent  bIntent = new Intent(LocationService.this, ServiceBroadcast.class);
        sendBroadcast(bIntent);
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            // Main Job
            switch (msg.arg2) {
                case 0: {
                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    locationListener = new MyLocationListener();

                    locationProvider = locationManager.getBestProvider(new Criteria(), false);
                    isUpdate = true;

                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    checkPermission(msg.arg1);
                    checkGPS(msg.arg1);
                    assert locationManager != null;
                    locationManager.requestLocationUpdates(locationProvider,
                            MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListener);
                    currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    break;
                }
                case 1: {
                    isRestart = false;
                    stopSelf(msg.arg1);
                }
                case 2: {
                    String text = "Current Location: " +
                            "(" + currLocation.getLatitude() + "," + currLocation.getLongitude() +")\n" +
                            "Current Time: " + dateTimeHandler(GetDateTime());
                    Toast.makeText(LocationService.this, text, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, text);
                    break;
                }
                case 3: {
                    String text = "Final Location: " +
                            "(" + currLocation.getLatitude() + "," + currLocation.getLongitude() +")\n" +
                            "Current Time: " + dateTimeHandler(GetDateTime());
                    Toast.makeText(LocationService.this, text, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, text);
                    break;
                }
            }

        }
    }

    private final class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            currLocation = location;

            Message msg = serviceHandler.obtainMessage();
            msg.arg2 = 1; // update Location

            serviceHandler.sendMessage(msg);
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
    }

}
