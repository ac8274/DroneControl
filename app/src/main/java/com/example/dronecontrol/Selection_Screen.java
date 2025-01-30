package com.example.dronecontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;


public class Selection_Screen extends AppCompatActivity {
    private Button drone_control;
    private Button drone_routes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
        drone_control = findViewById(R.id.drone_control);
        drone_routes = findViewById(R.id.drone_routes);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks the screen in the horizontol state.
    }

    /*
    Description:
    Function checks through permissions if hotspot was turned on.
     */
    public boolean checkHotSpot()
    {
        try {
            // Get the WifiManager instance
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) return false;

            // Use reflection to call the isWifiApEnabled method
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(wifiManager);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createHotSpotAlert()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Requirements");
        adb.setMessage("Please Turn on HotSpot Before Continuing!");
        adb.setPositiveButton("OK",null);  // override the defualt behaviour of the positive button that it will not exist.

        AlertDialog wifiHotspotAlert = adb.create();
        wifiHotspotAlert.show();

        wifiHotspotAlert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {  // set new behaviour for the positive button
            if(checkHotSpot())
            {
                wifiHotspotAlert.dismiss();
            }
        });
    }

    public void Go_To_DroneControl(View view) {
        if(!checkHotSpot())
        {
            createHotSpotAlert();
        }
        else
        {
            getFileName();
        }
    }

    public void getFileName()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Track name");

        final EditText fileNameET = new EditText(this);
        fileNameET.setHint("Type File Name HERE!");
        adb.setView(fileNameET);
        adb.setPositiveButton("Save",null);

        AlertDialog fileNameAD = adb.create();
        fileNameAD.show();

        fileNameAD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            if(fileNameET.getText().toString().isEmpty())
            {
                startNextActivity(Drone_Control.class,fileNameET.getText().toString());
            }
        });
    }

    public void startNextActivity(Class <?> cls, String fileName)
    {
        Intent nextActivity = new Intent(this,cls);
        nextActivity.putExtra("Track Name",fileName);
        startActivity(nextActivity);
    }

    public void Go_To_DroneTracks_Selection(View view) {

    }
}