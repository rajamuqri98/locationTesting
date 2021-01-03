package com.test.dummylocation.service;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.test.dummylocation.sync.SyncKey;
import com.test.dummylocation.sync.SyncSymbol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ThreadSubmitLoc extends Thread {

    private static final String TAG = "SYNCBUILD";

    protected SharedPreferences sharedPreferences;
    protected Location loc;

    public ThreadSubmitLoc(SharedPreferences sharedPreferences, Location loc) {
        super();
        this.sharedPreferences = sharedPreferences;
        this.loc = loc;
    }

    private String getLat(Location loc) {
        return String.valueOf(loc.getLatitude()).trim();
    }

    private String getLng(Location loc) {
        return String.valueOf(loc.getLongitude()).trim();
    }

    @Override
    public void run() {
        submitLocation(getLat(loc), getLng(loc), sharedPreferences);
    }

    private static void submitLocation(String lat, String lng, SharedPreferences sharedPreferences) {
        // TODO <Integrating in CentricPOS(Cloud)>
        //  1. Check BackSync /
        //  2. retrieve token /
        //  3. create submit url /
        //  4. _headers /
        //  5. payload /
        //  6. change URL
        //  7. invokePostStaticOps /
        //  8. check response /

//        if (sharedPreferences.getInt("backStatus", 0) == 1) {
//            int outletId = sharedPreferences.getInt("backOutletId", 0);
//            String token = sharedPreferences.getString("backToken", "");
//
////            try {
////                token = new CentricRoundingEncoder().basicDecode(token, MainResources.TKNR);
////            } catch (Exception e) {
////                token = "";
////            }
//
//            if (!token.equals("")) {
//                // String submitUrl = sharedPreferences.getString("backApi", "") + "/api/outlet/" + outletId + "/start-business-day";
//
//                HashMap<String, String> _headers = new HashMap<>();
//                _headers.put("Content-Type", "application/json");
//                _headers.put("Authorization", "Bearer " + token);
//
//                String payload = SyncSymbol.OPENOBS
//                        + SyncKey.KEY_LOC
//                        + SyncKey.KEY_LAT + lat + SyncSymbol.COMMA
//                        + SyncKey.KEY_LNG + lng // + SyncSymbol.COMMA
//                        + SyncKey.KEY_DTE + getJsonStr(dateTimeHandler(GetDateTime()))
//                        + SyncSymbol.CLOSEOBS + SyncSymbol.CLOSEOBS;
//
////                String res = invokePostStaticOps(sharedPreferences, _headers, payload, submitUrl);
//
//                Log.d(TAG, "submitLocation: " + payload);
//            }
//        }

        String payload = SyncSymbol.OPENOBS
                + SyncKey.KEY_LOC
                + SyncKey.KEY_LAT + lat + SyncSymbol.COMMA
                + SyncKey.KEY_LNG + lng + SyncSymbol.COMMA
                + SyncKey.KEY_DTE + getJsonStr(dateTimeHandler(GetDateTime()))
                + SyncSymbol.CLOSEOBS + SyncSymbol.CLOSEOBS;

        Log.i(TAG, "payload: " + payload);
    }

    public static String GetDateTime() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        return SDF.format(c.getTime());
    }

    public static String dateTimeHandler(String cDate) {
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

    public static String getJsonStr(String jStr) {
        return "\"" + jStr + "\"";
    }
}
