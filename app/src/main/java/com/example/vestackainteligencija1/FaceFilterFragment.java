package com.example.vestackainteligencija1;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vestackainteligencija1.Helper.EyeHeartOverlay;
import com.example.vestackainteligencija1.Helper.GraphicOverlay;
import com.example.vestackainteligencija1.Helper.MustacheOverlay;
import com.example.vestackainteligencija1.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import java.util.List;


public class FaceFilterFragment extends Fragment {

    public static final int  PICK_PHOTO=1;

    private Bitmap photo;
    private Bitmap photo2;

    private LinearLayout linearLayout;
    private Button chooser;
    private ImageView slika;
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView tekst;

    private GraphicOverlay graphicOverlay;


    private FirebaseVisionFaceDetectorOptions options;
    private FirebaseVisionImage image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_face_filter, container, false);
        tekst=v.findViewById(R.id.info_from_photo);
        this.chooser=v.findViewById(R.id.image_from_gallery_button);
        this.slika=v.findViewById(R.id.slika);
        (this.button1=v.findViewById(R.id.brkovi1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               linearLayout.setVisibility(View.GONE);
                setUpFirebase(1);
            }
        });
        (this.button2=v.findViewById(R.id.brkovi2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               linearLayout.setVisibility(View.GONE);
                setUpFirebase(2);
            }
        });
        (this.button3=v.findViewById(R.id.brkovi3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                setUpFirebase(3);
            }
        });
        this.linearLayout=v.findViewById(R.id.linearLayout);
        this.graphicOverlay=v.findViewById(R.id.graphic_overlay);

        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_PHOTO);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(data!=null){
            Uri selectedImage=data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            }catch (Exception e){


            }
            linearLayout.setVisibility(View.VISIBLE);
            slika.setVisibility(View.VISIBLE);
            chooser.setVisibility(View.GONE );
            photo2=null;
            try{
                ExifInterface exif=new ExifInterface(getActivity().getContentResolver().openInputStream(selectedImage));
                int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);
                Matrix matrix=new Matrix();
                if(orientation==6){
                    matrix.postRotate(90);
                }
                else if(orientation==3){
                    matrix.postRotate(180);
                }
                else if(orientation==8){
                    matrix.postRotate(270);
                }
               photo2 =Bitmap.createBitmap(photo,0,0, photo.getWidth(),photo.getHeight(),matrix,true);

            }catch(Exception e){

            }
            slika.setImageBitmap(photo2);

        }
    }
    public void setUpFirebase(int number){
        this.options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .build();
        int width=graphicOverlay.getWidth();
        int height=graphicOverlay.getHeight();
        photo2=Bitmap.createScaledBitmap(photo2,width,height,false);
        image=FirebaseVisionImage.fromBitmap(photo2);

        FirebaseVisionFaceDetector detector= FirebaseVision.getInstance().getVisionFaceDetector(options);

        final int numberr=number;
        Task<List<FirebaseVisionFace>> result=detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                if (firebaseVisionFaces.size() == 0) {
                    tekst.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "There is no faces on this picture!", Toast.LENGTH_LONG);
                } else if (firebaseVisionFaces.size() !=0) {
                    slika.setVisibility(View.GONE);
                      graphicOverlay.setVisibility(View.VISIBLE);
                    int index=0;
                    for (FirebaseVisionFace face : firebaseVisionFaces) {
                        if (numberr == 3) {
                            Rect rect = face.getBoundingBox();
                            RectOverlay rectOverlay = new RectOverlay(graphicOverlay, rect, photo2,face);
                            graphicOverlay.add(rectOverlay);
                        } else if (numberr == 1) {
                            List<FirebaseVisionPoint> upperLipContour = face.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints();
                            List<FirebaseVisionPoint> noseContour = face.getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).getPoints();
                            FirebaseVisionPoint nose = noseContour.get(1);
                            FirebaseVisionPoint lip = upperLipContour.get(4);
                            Point topPoint = new Point(Math.round(nose.getX()), Math.round(nose.getY()));
                            Point bottomPoint = new Point(Math.round(lip.getX()), Math.round(lip.getY()));
                            lip = upperLipContour.get(0);
                            Point leftPoint = new Point(Math.round(lip.getX()), Math.round(lip.getY()));
                            lip = upperLipContour.get(10);
                            Point rightPoint = new Point(Math.round(lip.getX()), Math.round(lip.getY()));
                            MustacheOverlay mustacheOverlay = new MustacheOverlay(graphicOverlay, photo2, topPoint, leftPoint, rightPoint, bottomPoint);
                            graphicOverlay.add(mustacheOverlay);

                        } else if (numberr == 2) {
                            Log.d("Srce",Integer.toString(index)+".put");
                            List<FirebaseVisionPoint> leftEye = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                            List<FirebaseVisionPoint> rightEye = face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints();
                            EyeHeartOverlay eyeHeartOverlay = new EyeHeartOverlay(graphicOverlay, leftEye, rightEye, photo2);
                            graphicOverlay.add(eyeHeartOverlay);
                        }
                        index++;
                    }
                    linearLayout.setVisibility(View.GONE);
                    chooser.setVisibility(View.GONE);
                    slika.setImageDrawable(null);
                    // slika.setImageBitmap(mutableBitmap);
                }
               // else{
                 //   tekst.setText("It works currently for only one face!");
               //     tekst.setVisibility(View.VISIBLE);
               // }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast=Toast.makeText(getContext(),"Detekcija lica nije uspela, probajte sa drugom slikom!",Toast.LENGTH_LONG);
                linearLayout.setVisibility(View.GONE);
                //slika.setVisibility(View.GONE);
            }
        });
    }


}
