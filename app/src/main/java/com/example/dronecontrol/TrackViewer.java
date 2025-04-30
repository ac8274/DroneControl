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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
                File file = null;
                if (extension.equals("gpx"))
                {
                    file = new File(this.getExternalFilesDir(null), "temporary.kml");

                    boolean firstPoint = true;
                    boolean firstElevation = true;

                    String startLon = "";
                    String startLat = "";
                    String startEle = "300";

                    String LastLon = "";
                    String LastLat = "";
                    String LastEle = "300";

                    String cords = "";

                    InputStream inputStream = getContentResolver().openInputStream(data);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(inputStream,"UTF-8");
                    int eventType = parser.getEventType();
                    while(eventType != XmlPullParser.END_DOCUMENT)
                    {
                        if(eventType == XmlPullParser.START_TAG && parser.getName().equals("wpt")) {
                            LastLon = parser.getAttributeValue(null, "lon");
                            LastLat = parser.getAttributeValue(null, "lat");

                            cords += LastLon  + "," + LastLat + ",300\n";
                            // add the lat and lon to the string which contains the coordinates.
                        }
                        else if(eventType == XmlPullParser.START_TAG && parser.getName().equals("ele"))
                        {
                            cords = cords.substring(0,cords.length()-4);
                            LastEle = parser.getText();
                            cords += LastEle + "\n"; // after adding the elevation
                            // continue to new cords, with the \n for the next cord.
                        }

                        if(firstPoint)
                        {
                            startLon = LastLon;
                            startLat = LastLat;
                            firstPoint = false;
                        }
                        if(firstElevation)
                        {
                            startEle = LastEle;
                            firstElevation = false;
                        }
                        parser.next();
                    }
                }
                else {
                    file = new File(new URI(data.toString())); // experimental and unsafe. need to add file checking.
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}