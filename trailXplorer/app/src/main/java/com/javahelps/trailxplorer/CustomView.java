package com.javahelps.trailxplorer;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CustomView extends View {

    // Variable from main activity to know the speed
    public int[] speedpoint = MainActivity.getSpeedpoint();
    private int[] timepoint = {1,2,3,4,5,6,7,8,9,10};


    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CustomView(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

    }


    // Methode to draw the graphic
    @Override
    protected void onDraw(Canvas canvas) {
        // change the color canvas.drawColor(Color.RED);

        /*Rect rect = new Rect();
        rect.left = 100;
        rect.top = 100;
        rect.right = rect.left + 900;
        rect.bottom = rect.top + 500;*/

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);


        // Draw the rectangle and the axis of the graphic
        canvas.drawLine(100,50, 1000,50,paint);
        canvas.drawLine(100,700, 1000,700,paint);
        canvas.drawLine(100,50, 100,700,paint);
        canvas.drawLine(1000,50, 1000,700,paint);

        canvas.drawLine(150,650, 950,650,paint);
        canvas.drawLine(150,100, 150,650,paint);

        canvas.drawLine(150,100, 125,120,paint);
        canvas.drawLine(150,100, 175,120,paint);

        canvas.drawLine(950,650, 930,675,paint);
        canvas.drawLine(950,650, 930,625,paint);


        // int table of the position of the point on x axis
        int[] xpos = {150,230,310,390,470,550,630,710,790,870};

        //Draw some visual stuff for the graph
        canvas.drawText("Time",925,690,paint);
        canvas.drawText("0%",140,690,paint);
        canvas.drawText("10%",220,690,paint);
        canvas.drawText("20%",300,690,paint);
        canvas.drawText("30%",380,690,paint);
        canvas.drawText("40%",460,690,paint);
        canvas.drawText("50%",540,690,paint);
        canvas.drawText("60%",620,690,paint);
        canvas.drawText("70%",700,690,paint);
        canvas.drawText("80%",780,690,paint);
        canvas.drawText("90%",860,690,paint);

        canvas.drawText("Speed",100,80,paint);
        canvas.drawText("Km/m",100,100,paint);
        canvas.drawText("0",120,650,paint);
        canvas.drawText("1",120,595,paint);
        canvas.drawText("2",120,540,paint);
        canvas.drawText("3",120,485,paint);
        canvas.drawText("4",120,430,paint);
        canvas.drawText("5",120,375,paint);
        canvas.drawText("6",120,320,paint);
        canvas.drawText("7",120,265,paint);
        canvas.drawText("8",120,210,paint);
        canvas.drawText("9",120,155,paint);

        // Caculate the position of the point on y axis
        int ypos1 = 650 - (speedpoint[0]*55) ;
        int ypos2 = 650 - (speedpoint[1]*55);
        int ypos3 = 650 - (speedpoint[2]*55);
        int ypos4 = 650 - (speedpoint[3]*55);
        int ypos5 = 650 - (speedpoint[4]*55);
        int ypos6 = 650 - (speedpoint[5]*55);
        int ypos7 = 650 - (speedpoint[6]*55);
        int ypos8 = 650 - (speedpoint[7]*55);
        int ypos9 = 650 - (speedpoint[8]*55);
        int ypos10 = 650 -(speedpoint[9]*55);

        int[] ypos = {ypos1,ypos2,ypos3,ypos4,ypos5,ypos6,ypos7,ypos8,ypos9,ypos10};

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);

        // Draw the cross for the points
        for(int i = 0; i<xpos.length;i++){
            canvas.drawLine(xpos[i]-10,ypos[i]-10,xpos[i]+10,ypos[i]+10,paint2);
            canvas.drawLine(xpos[i]-10,ypos[i]+10,xpos[i]+10,ypos[i]-10,paint2);
        }


        //Draw the line betwwen the points
        for(int i =0;i<xpos.length-1;i++){
            canvas.drawLine(xpos[i],ypos[i],xpos[i+1],ypos[i+1],paint);
        }





    }
}
