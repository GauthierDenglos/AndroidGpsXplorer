package com.javahelps.trailxplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
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

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Stopwatch variable. Allows to create and increment the timer
    private static Chronometer chronometer;
    private long pauseOffset = 0;
    private boolean running ;
    private boolean isstart = false;

    //Location: Altitude, longitude and Latitude
    private TextView textview_lat;
    private TextView textview_long;
    private TextView textview_alt;
    private LocationManager locationManager;
    private Handler handler;
    private Location location;
    private double latitude;
    private double longitude;
    private static double altitude;

    //variable used in the second activity
    private static double maxAtlitude;
    private static double minAtlitude;

    private File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    private File GPXFile;
    private Date date;
    private DateFormat format = new SimpleDateFormat("yyyy,MM.dd HH:mm:ss");
    private PrintWriter writer;
    int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

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

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, statActivity.class));
                isstart = false;
                writer.println("       </trkseg>" );
                writer.println("   </trk>");
                writer.print("</gpx>");
                writer.close();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
                isstart =true;
                date = new Date();
                GPXFile = new File(path + "/GPStracks/", format.format(date) + ".gpx");
                try {
                    if (GPXFile.getParentFile().exists() || GPXFile.getParentFile().mkdirs()) {
                        GPXFile.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    writer = new PrintWriter(GPXFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
                writer.println("<gpx xmlns:android=\"http://schemas.android.com/apk/res/android\"" );
                writer.println("    xmlns:app=\"http://schemas.android.com/apk/res-auto\"");
                writer.println("    xmlns:tools=\"http://schemas.android.com/tools\">");
                writer.println("   <trk>");
                writer.println("       <trkseg>");
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);
            }
        });

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
                //Toast.makeText(MainActivity.this, "location check", Toast.LENGTH_SHORT).show();
                MainActivity.this.handler.postDelayed(MainActivity.this.runLocation, 5000);
            if (isstart){
                writer.println("          <trkpt lat= " + location.getLatitude() +" lon=" + location.getLongitude() + ">");
                writer.println("               <ele>" + location.getAltitude() + "</ele>");
                writer.println("           </trkpt>");
            }
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

    public void resetChronometer (View v){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public static double getMaxAtlitude(){
        if (maxAtlitude < altitude)
            maxAtlitude = altitude;
        return maxAtlitude;
    }

    public static double getMinAtlitude(){
        if (altitude < minAtlitude)
            minAtlitude = altitude;
        return minAtlitude;
    }

    public static Chronometer getTimer(){
        return chronometer;
    }
}
