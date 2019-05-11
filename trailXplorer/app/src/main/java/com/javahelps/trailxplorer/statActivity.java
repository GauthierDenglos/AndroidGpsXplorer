package com.javahelps.trailxplorer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.TextView;

public class statActivity extends AppCompatActivity {

    double minAltitude = MainActivity.getMinAtlitude();
    double maxAltitude = MainActivity.getMaxAtlitude();
    Chronometer chronometer = MainActivity.getTimer();
    double averagespeed = MainActivity.getAverageSpeed();
    double totdist = MainActivity.getDist();
    double time = MainActivity.getATime()/60 ;
    double calories = (averagespeed*time + 65)/60;

    TextView maxAlt_txt;
    TextView minAlt_txt;
    TextView aveSpeed_txt;
    TextView maxDist_txt;
    TextView maxTime_txt;
    TextView calories_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Your statistics");

        maxAlt_txt = (TextView) findViewById(R.id.maxAlt);
        minAlt_txt = (TextView) findViewById(R.id.minAlt);
        aveSpeed_txt = (TextView) findViewById(R.id.aveSpeed);
        maxDist_txt = (TextView) findViewById(R.id.maxDist);
        maxTime_txt = (TextView) findViewById(R.id.maxTime);
        calories_txt = (TextView) findViewById(R.id.caloriesburn) ;

        maxAlt_txt.setText("Maximum altitude = " + maxAltitude);
        minAlt_txt.setText("Minimum altitude = " + minAltitude);
        maxTime_txt.setText("Time taken = " + chronometer.getText().toString());
        aveSpeed_txt.setText("Average Speed = " + averagespeed + " km/h");
        maxDist_txt.setText("Total distance = " + totdist +" km");
        calories_txt.setText("Calories burn = "+ calories);


        CustomView customView = new CustomView(this);
    }


}
