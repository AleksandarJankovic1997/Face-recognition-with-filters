package com.example.vestackainteligencija1.Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;

public class RectOverlay extends GraphicOverlay.Graphic {
    private int color= Color.GREEN;
    private float  strokeWidth=4.0f;
    private Paint rectPaint;
    private  GraphicOverlay graphicOverlay;
    private Rect rect;
    private Bitmap bitmap;
    private FirebaseVisionFace face;


    public RectOverlay(GraphicOverlay overlay, Rect rect, Bitmap bitmap, FirebaseVisionFace face) {
        super(overlay);
        this.face=face;
        rectPaint=new Paint();
        rectPaint.setStrokeWidth(strokeWidth);
        rectPaint.setColor(color);
        rectPaint.setTextSize(30);
        rectPaint.setStyle(Paint.Style.STROKE);

        graphicOverlay=overlay;
        this.rect=rect;
        this.bitmap=bitmap;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF=new RectF(rect);
        Rect rectt=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.bottom=translateX(rectF.bottom);
        rectF.top=translateX(rectF.top);
        canvas.drawBitmap(bitmap,null,rectt,null);
        canvas.drawRect(rectF,rectPaint);
        float smiling=face.getSmilingProbability();
        float rightEyeOpenProbability=face.getRightEyeOpenProbability();
        float leftEyeOpenProbability=face.getLeftEyeOpenProbability();
        Paint paint= new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(20);
        paint.setStrokeWidth(3.0f);
        canvas.drawText("righ eye open probability: "+(double)rightEyeOpenProbability,rect.left+4,rect.bottom-44,paint);
        canvas.drawText("Smiling probability: "+(double)smiling,rect.left+4,rect.bottom-4,paint);
        canvas.drawText("left eye open probability: "+(double)leftEyeOpenProbability,rect.left+4,rect.bottom-24,paint);
        Log.d("canvasW", Integer.toString(canvas.getWidth()));
        Log.d("canvasH",Integer.toString(canvas.getHeight()));
        Log.d("bitmapW",Integer.toString(bitmap.getWidth()));
        Log.d("bitmapH",Integer.toString(bitmap.getHeight()));
    }


}
