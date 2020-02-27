package com.example.vestackainteligencija1.Helper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.vestackainteligencija1.R;

public class MustacheOverlay extends GraphicOverlay.Graphic {

    private GraphicOverlay graphicOverlay;
    private Bitmap bitmap;
    private Bitmap mustache;
    private Point top;
    private Point left;
    private Point right;
    private Point bottom;



    public MustacheOverlay(GraphicOverlay overlay, Bitmap bitmap, Point top, Point left, Point right, Point bottom) {
        super(overlay);
        this.graphicOverlay=overlay;
        this.bitmap=bitmap;
        this.top=top;
        this.left=left;
        this.right=right;
        this.bottom=bottom;


        Drawable drawable=getApplicationContext().getResources().getDrawable(R.drawable.brkovi,null);
        mustache = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.brkovi);
        mustache=Bitmap.createScaledBitmap(mustache,left.x-right.x,top.y-bottom.y,false);
       // mustache.eraseColor(Color.TRANSPARENT);
        postInvalidate();

    }


    @Override
    public void draw(Canvas canvas) {
        Rect rectt=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawBitmap(bitmap,null,rectt,null);

        Rect rect=new Rect(left.x,top.y,right.x,bottom.y);
        canvas.drawBitmap(mustache,null,rect,null);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
}
