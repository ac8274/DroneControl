package com.example.dronecontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Selection_Screen extends AppCompatActivity {
    private Button drone_control;
    private Button drone_routes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
        drone_control = findViewById(R.id.drone_control);
        drone_routes = findViewById(R.id.drone_routes);
    }

    /*
    Description:
    Function checks through permissions if hotspot was turned on.
     */
    public boolean checkHotSpot()
    {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).
    }

    public void createHotSpotAlert()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Requirements");
        adb.setMessage("Please Turn on HotSpot Before Continuing!");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkHotSpot())
                {
                    dialog.cancel();
                }
            }
        });

        AlertDialog wifiHotspotAlert = adb.create();
        wifiHotspotAlert.show();
    }

    public void Go_To_DroneControl(View view) {
        if(!checkHotSpot())
        {
            createHotSpotAlert();
        }
        Intent nextActivity = new Intent(this,Drone_Control.class);
        startActivity(nextActivity);
    }

    public void Go_To_DroneTracks_Selection(View view) {

    }
}