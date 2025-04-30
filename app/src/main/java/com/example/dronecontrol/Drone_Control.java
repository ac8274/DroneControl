package com.example.dronecontrol;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dronecontrol.CustomExceptions.StreamInUseException;
import com.example.dronecontrol.CustomViews.Joystick;
import com.example.dronecontrol.Structures.GlobalFileHolder;
import com.example.dronecontrol.Structures.HotSpot;
import com.example.dronecontrol.Structures.TrackInfo;
import com.example.dronecontrol.Structures.UserUid;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.example.dronecontrol.Structures.FireBaseHelper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Drone_Control extends AppCompatActivity implements OnMapReadyCallback {
    private static Joystick rightJoystick;
    private static Joystick leftJoystick;
    private static GoogleMap mMap;
    private String fileName;
    private File file;
    private GlobalFileHolder fileHolder;
    private AlertDialog firebaseUpload;
    private static Marker droneMarker; // Marker for the drone
    private TrackInfo trackInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_control);

        rightJoystick = findViewById(R.id.rightJoystick);
        leftJoystick = findViewById(R.id.leftJoystick);
        fileName = getIntent().getStringExtra("Track Name");
        setTrackInfo();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTrackInfo()
    {
        this.trackInfo = new TrackInfo(this.fileName);
        this.trackInfo.setTrackFileUri("userFiles/"+UserUid.user_uid+"/.gpxFiles/"+this.fileName+".gpx");
        this.trackInfo.setFlightDate(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        this.trackInfo.setFlightStartTime(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
    }

    public static void setPosition(double latitude, double longitude)
    {
        LatLng dronePos = new LatLng(latitude,longitude);
        if(droneMarker != null)
        {
            droneMarker.setPosition(dronePos);
        }

        //mMap.animateCamera(CameraUpdateFactory.newLatLng(dronePos));
    }

    public static double getCompassDegrees()
    {
        return (360 - rightJoystick.getAngle() + 90) % 360;
    }

    public static double getDistance() {
        return rightJoystick.getDistanceFromCenter();
    }

    public static double getElevation() {
        double distance = leftJoystick.getYDistance();
        while(distance > 10 || distance < -10)
        {
            distance = distance/10.0;
        }
        return -distance;
    }

    @Override
    public void onBackPressed() {
        if (fileHolder.stopWriting)
        {
            super.onBackPressed();
        }
        else
        {
            this.trackInfo.setFlightEndTime(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
            this.fileHolder.stopWriting = true;
            createWritingAlert();
        }
    }


    public void createWritingAlert()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Requirements");
        adb.setMessage("Saving Track ...");
        adb.setPositiveButton("OK",null);  // override the defualt behaviour of the positive button that it will not exist.

        firebaseUpload = adb.create();
        firebaseUpload.show();

        firebaseUpload.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {  // set new behaviour for the positive button
            if(GlobalFileHolder.stopWriting == false)
            {
                FireBaseUploadDialog();
            }
        });
    }

    public void FireBaseUploadDialog()
    {
        firebaseUpload.setTitle("Requirements");
        firebaseUpload.setMessage("Saving Track ...");
        firebaseUpload.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view ->{});
        FireBaseHelper.uploadFile(this.file,UserUid.user_uid,".gpx",
                new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                firebaseUpload.dismiss();
                Log.println(Log.INFO,"dataUploadFailure",exception.getMessage());
                Toast.makeText(Drone_Control.this, "failure" ,Toast.LENGTH_SHORT).show();
            }
        }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                firebaseUpload.dismiss();
                FireBaseHelper.uploadFileInfo(trackInfo);
                Toast.makeText(Drone_Control.this, "Success", Toast.LENGTH_SHORT).show();
                FireBaseHelper.deleteFile(file);
                finish();
            }
        });
        firebaseUpload.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(null);
    }



    @Override
    protected void onDestroy() {
        try {
            GlobalFileHolder.getInstance().closeStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Initial location
        LatLng dronePos = new LatLng(31.27379972458028,34.80235696568683);
        droneMarker = mMap.addMarker(new MarkerOptions().position(dronePos).title("Drone Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dronePos, 10));

        HotSpot hotSpot = new HotSpot(4454,Drone_Control.this);
        hotSpot.start();
    }
}