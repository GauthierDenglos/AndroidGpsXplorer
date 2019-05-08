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

    private int[] speedpoint = {2,6,8,8,12,10,5,10,18,3};
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



        int[] xpos = {150,230,310,390,470,550,630,710,790,870};

        int ypos1 = 650 - (speedpoint[0]*55)/2 ;
        int ypos2 = 650 - (speedpoint[1]*55)/2;
        int ypos3 = 650 - (speedpoint[2]*55)/2;
        int ypos4 = 650 - (speedpoint[3]*55)/2;
        int ypos5 = 650 - (speedpoint[4]*55)/2;
        int ypos6 = 650 - (speedpoint[5]*55)/2;
        int ypos7 = 650 - (speedpoint[6]*55)/2;
        int ypos8 = 650 - (speedpoint[7]*55)/2;
        int ypos9 = 650 - (speedpoint[8]*55)/2;
        int ypos10 = 650 -(speedpoint[9]*55)/2;

        int[] ypos = {ypos1,ypos2,ypos3,ypos4,ypos5,ypos6,ypos7,ypos8,ypos9,ypos10};

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);

        for(int i = 0; i<xpos.length;i++){
            canvas.drawLine(xpos[i]-10,ypos[i]-10,xpos[i]+10,ypos[i]+10,paint2);
            canvas.drawLine(xpos[i]-10,ypos[i]+10,xpos[i]+10,ypos[i]-10,paint2);
        }

        for(int i =0;i<xpos.length-1;i++){
            canvas.drawLine(xpos[i],ypos[i],xpos[i+1],ypos[i+1],paint);
        }





    }
}
