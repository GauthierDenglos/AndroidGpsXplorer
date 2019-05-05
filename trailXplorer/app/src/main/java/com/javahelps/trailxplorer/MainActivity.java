package com.javahelps.trailxplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Stopwatch variable. Allows to create and increment the timer
    private Chronometer chronometer;
    private long pauseOffset = 0;
    private boolean running;

    //Location: Altitude, longitude and Latitude
    private TextView textview_lat;
    private TextView textview_long;
    private TextView textview_alt;
    private LocationManager locationManager;
    private Handler handler;
    private Location location;
    private double latitude;
    private double longitude;
    private double altitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Start Activity");

        chronometer = findViewById(R.id.chronometer);

        Button stopBtn = (Button) findViewById(R.id.launchActivity);
        Button startBtn = (Button) findViewById(R.id.createGpx);
        Button resetBtn = (Button) findViewById(R.id.resetAll);
        //Button pauseBtn = (Button) findViewById(R.id.pauseChrono);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, statActivity.class));
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);
            }
        });

        /*pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer(v);
            }
        });*/

        textview_lat = (TextView) findViewById(R.id.textview_lat);
        textview_long = (TextView) findViewById(R.id.textview_long);
        textview_alt = (TextView) findViewById(R.id.textview_alt);

        handler = new Handler();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            altitude = location.getAltitude();
        }

        handler.postDelayed(runLocation, 1000);
    }

    public Runnable runLocation = new Runnable(){
        @Override
        public void run() {
            textview_lat.setText("Latitude = " + String.valueOf(latitude));
            textview_long.setText("Longitude = " + String.valueOf(longitude));
            textview_alt.setText("Altitude = " + String.valueOf(altitude));
            Toast.makeText(MainActivity.this, "location check", Toast.LENGTH_SHORT).show();
            MainActivity.this.handler.postDelayed(MainActivity.this.runLocation, 10000);
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startChronometer (View v){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }

    }

    /*public void pauseChronometer (View v){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

    }*/

    public void resetChronometer (View v){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
}
