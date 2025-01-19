package com.example.dronecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dronecontrol.CustomExceptions.StreamInUseException;
import com.example.dronecontrol.CustomViews.Joystick;
import com.example.dronecontrol.Structures.GlobalFileHolder;
import com.example.dronecontrol.Structures.HotSpot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;


public class Drone_Control extends AppCompatActivity implements OnMapReadyCallback {
    private static Joystick rightJoystick;
    private static Joystick leftJoystick;
    private static GoogleMap mMap;
    private String fileName;
    private File file;
    private GlobalFileHolder fileHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_control);
        rightJoystick = findViewById(R.id.rightJoystick);
        leftJoystick = findViewById(R.id.leftJoystick);
        fileName = getIntent().getStringExtra("Track Name");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        file = new File(this.getExternalFilesDir(null),fileName +".gpx");
        try {
            fileHolder = GlobalFileHolder.getInstance();
            fileHolder.setFile(this.file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (StreamInUseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPosition(double latitude, double longitude)
    {
        //mMap.
    }

    @Override
    public void onBackPressed() {
        if (fileHolder.stopWriting == true)
        {
            super.onBackPressed();
        }
        else
        {
            this.fileHolder.stopWriting = true;


        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Initial location
        LatLng initialLocation = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));

        HotSpot hotSpot = new HotSpot("0.0.0.0",3245);
        hotSpot.start();
    }
}