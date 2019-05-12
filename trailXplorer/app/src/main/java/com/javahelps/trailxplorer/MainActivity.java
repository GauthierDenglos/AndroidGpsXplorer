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
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Stopwatch variable. Allows to create and increment the timer
    private static Chronometer chronometer;
    private long pauseOffset = 0;
    private boolean running ;

    //Boolean to know if star botton have been touched
    private boolean isstart = false;

    //Location: Altitude, longitude and Latitude
    private TextView textview_lat;
    private TextView textview_long;
    private TextView textview_alt;
    private TextView textview_speed;
    private LocationManager locationManager;
    private Handler handler;
    private Location currentLocation;
    private double latitude;
    private double longitude;
    private double speed;
    private double calculatedSpeed = 0;
    private static double altitude;

    //variable used in the second activity
    private static double maxAtlitude;
    private static double minAtlitude = 10000;

    //Variable used to create and write on the Gpx File
    private File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    private File GPXFile;
    private Date date;
    private DateFormat format = new SimpleDateFormat("yyyy,MM.dd HH:mm:ss");
    private PrintWriter writer;

    // Permission for the user
    int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;


    // Differents variable to colect the data
    ArrayList<Double> speedlist ;
    public static int[] speedtab = new int[10];
    public static double averagespeed;
    public static double totdistance;
    public static double time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TrailXplorer");

        Toast.makeText(this, "Check if you gave us the GPS permission in your parameter !", Toast.LENGTH_LONG).show();

        chronometer = findViewById(R.id.chronometer);

        Button stopBtn = (Button) findViewById(R.id.launchActivity);
        Button startBtn = (Button) findViewById(R.id.createGpx);
        Button resetBtn = (Button) findViewById(R.id.resetAll);


        //Ask the permission to the user
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_ACCESS_FINE_LOCATION);

        // Stop button
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Star the second activity
                startActivity(new Intent(MainActivity.this, statActivity.class));


                isstart = false;

                //Close the Gpx file
                writer.println("       </trkseg>" );
                writer.println("   </trk>");
                writer.print("</gpx>");
                writer.close();

                // Calculate the average Speed
                int addspeed = 0;
                int nbspeed = speedlist.size();
                for (int i = 0; i<nbspeed; i++){
                    addspeed+= speedlist.get(i);
                }
                averagespeed = addspeed/nbspeed;

                // Calculate the total distance
                totdistance = averagespeed * (time/3600);


                // Create the array of 10 points to draw the graphic
                int increment = nbspeed/10;
                for (int i = 0 ; i<10; i++){
                    double temp = (double)speedlist.get(i*increment);
                    speedtab[i] = (int)temp;
                }

            }
        });


        //Start Button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start chronometer and put boolean to true
                startChronometer(v);
                isstart =true;

                // Initiate Variable to collect all the speed
                speedlist = new ArrayList<Double>();

                // Initiate data and the Gpx File name
                date = new Date();
                GPXFile = new File(path + "/GPStracks/", format.format(date) + ".gpx");

                //Create the file
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

                // Write the top of the GPX File
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
        textview_speed = (TextView) findViewById(R.id.textview_speed);

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
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (currentLocation != null){
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            altitude = currentLocation.getAltitude();
            speed = currentLocation.getSpeed();
        }

        handler.postDelayed(runLocation, 1000);
    }


    // Each 5 seconds we refresh data on TextView and and stuff in array and in GPX file
    public Runnable runLocation = new Runnable(){
        @Override
        public void run() {

                textview_lat.setText("Latitude = " + String.valueOf(latitude));
                textview_long.setText("Longitude = " + String.valueOf(longitude));
                textview_alt.setText("Altitude = " + String.valueOf(altitude));
                textview_speed.setText("Speed (m/s) = " + String.valueOf(speed));
                //Toast.makeText(MainActivity.this, "location check", Toast.LENGTH_SHORT).show();
                MainActivity.this.handler.postDelayed(MainActivity.this.runLocation, 5000);
            if (isstart){
                speedlist.add(speed* 3.6);
                time+=5;
                writer.println("          <trkpt lat= " + currentLocation.getLatitude() +" lon=" + currentLocation.getLongitude() + ">");
                writer.println("               <ele>" + currentLocation.getAltitude() + "</ele>");
                writer.println("           </trkpt>");
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        if (currentLocation != null) {
            double elapsedTime = (location.getTime() - currentLocation.getTime()) / 1_000; // Convert milliseconds to seconds
            calculatedSpeed = currentLocation.distanceTo(location) / elapsedTime;
        }
        this.currentLocation = location;

        //speed = location.hasSpeed() ? location.getSpeed() : calculatedSpeed;
        if(location.hasSpeed()){
            speed = location.getSpeed();
        }
        else{
            speed = calculatedSpeed;
        }
        if (Double.isInfinite(speed) || Double.isNaN(speed))
            speed = 1.11113547;
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

    public static int[] getSpeedpoint(){
        return speedtab;
    }

    public static double getDist(){
        return totdistance;
    }

    public static double getAverageSpeed(){
        return averagespeed;
    }
    public static double getATime() {
        return time;
    }
}

