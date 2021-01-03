package com.test.dummylocation;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.test.dummylocation.service.LocationService;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    Button btnPrev, btnStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubActivity.this, MainActivity.class));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stopService(new Intent(SubActivity.this, LocationService.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getCurrentService() {
        // This is the code to find the running services
        ActivityManager am = (ActivityManager) SubActivity.this.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = null;
        if (am != null) {
            rs = am.getRunningServices(50);
        }
        String message = "";

        if (rs != null) {
            for (int i=0; i<rs.size(); i++) {
                ActivityManager.RunningServiceInfo rsi = rs.get(i);
                Log.i("Service", "Process " + rsi.process + " with component " + rsi.service.getClassName());
                message = message + rsi.process ;
            }
        }
    }
}
