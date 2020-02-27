package com.example.vestackainteligencija1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FaceFilterFragment fragment;

    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout=findViewById(R.id.tablayout_id);
        fragment=new FaceFilterFragment();
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.activityFrameLayout,fragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        fragment.onActivityResult(requestCode,resultCode,data);
    }
}
