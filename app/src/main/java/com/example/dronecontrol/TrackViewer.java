package com.example.dronecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class TrackViewer extends AppCompatActivity implements OnMapReadyCallback {
    private static GoogleMap mMap;
    private Intent gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_viewer);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gi = getIntent();
        Uri data = gi.getData();

        fileSetUp(data);

    }

    private void fileSetUp(Uri data)
    {
        try
        {
            String filePath = data.getPath();
            String extension = String.valueOf(filePath.charAt(filePath.length() - 3)) + //extract the extension which should be no more than 3 letters.
                    String.valueOf(filePath.charAt(filePath.length() - 2)) +
                    String.valueOf(filePath.charAt(filePath.length() - 1));
            if(extension.equals("gpx") || extension.equals("kml")) {
                File file = new File(new URI(data.toString())); // experimental and unsafe. need to add file checking.
                if (extension.equals("gpx"))
                {
                    // do file conversion.
                }
                KmlLayer layer = new KmlLayer(mMap,new FileInputStream(file),this);
                layer.addLayerToMap();
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkFileExtension(String filePath)
    {


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}