package com.example.dronecontrol;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dronecontrol.CustomViews.Joystick;

public class Drone_Control extends AppCompatActivity {
    private Joystick rightJoystick;
    private Joystick leftJoystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_control);
        rightJoystick = findViewById(R.id.rightJoystick);
        leftJoystick = findViewById(R.id.leftJoystick);
    }
}