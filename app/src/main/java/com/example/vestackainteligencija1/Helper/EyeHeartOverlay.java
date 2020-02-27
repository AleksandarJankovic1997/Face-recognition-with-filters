package com.example.vestackainteligencija1.Helper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vestackainteligencija1.R;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import java.util.List;

public class EyeHeartOverlay extends GraphicOverlay.Graphic {
    private GraphicOverlay graphicOverlay;
    private Bitmap bitmap;
    private List<FirebaseVisionPoint> leftEye;
    private List<FirebaseVisionPoint> rightEye;
    private Bitmap eyeHeart;
    float visina;
    float sirina;

    public EyeHeartOverlay(GraphicOverlay overlay, List<FirebaseVisionPoint> leftEye, List<FirebaseVisionPoint> rightEye, Bitmap bitmap) {
        super(overlay);
        this.bitmap=bitmap;
        this.leftEye=leftEye;
        this.rightEye=rightEye;

        eyeHeart= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.srce2);
        sirina=leftEye.get(8).getX()-leftEye.get(1).getX();
        visina=(leftEye.get(12).getY()-leftEye.get(4).getY())*3;
        eyeHeart=Bitmap.createScaledBitmap(eyeHeart,Math.round(sirina),Math.round(visina),false);
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,null,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),null);
        Rect rect1=new Rect(Math.round(leftEye.get(0).getX()),Math.round(leftEye.get(0).getY()-visina/2),Math.round(leftEye.get(8).getX()),Math.round(leftEye.get(8).getY()+visina/2));
        Rect rect2=new Rect(Math.round(rightEye.get(0).getX()),Math.round(rightEye.get(0).getY()-visina/2),Math.round(rightEye.get(8).getX()),Math.round(rightEye.get(8).getY()+visina/2));
        canvas.drawBitmap(eyeHeart,null,rect1,null);
        canvas.drawBitmap(eyeHeart,null,rect2,null);
    }
}
