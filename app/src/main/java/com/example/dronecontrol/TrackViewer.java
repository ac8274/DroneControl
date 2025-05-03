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

import com.example.dronecontrol.Structures.KMLparser;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

public class TrackViewer extends AppCompatActivity implements OnMapReadyCallback {
    private static GoogleMap mMap;
    private Intent gi;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_viewer);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.track_viewer);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gi = getIntent();
        data = gi.getData();
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
                    file = new File(this.getExternalFilesDir(null), "DroneRoute.kml");

                    boolean firstPoint = true;

                    String startPoint = "";

                    String LastPoint = "";

                    String cords = "";

                    InputStream inputStream = getContentResolver().openInputStream(data);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(inputStream,"UTF-8");
                    int eventType = parser.getEventType();
                    while(eventType != XmlPullParser.END_DOCUMENT)// run until end of file
                    {
                        if(eventType == XmlPullParser.START_TAG && parser.getName().equals("wpt")) { //point found
                            LastPoint = parser.getAttributeValue(null, "lon")+ "," +
                                    parser.getAttributeValue(null, "lat") + ",300\n"; // put a default elevation
                            parser.next();

                            while(!parser.getName().equals("wpt")) // search inside the point for elevation value
                            {
                                if(parser.getName().equals("ele") && eventType == XmlPullParser.START_TAG) // elevation value found
                                {
                                    parser.next();
                                    LastPoint = LastPoint.substring(0,LastPoint.length() - 4);
                                    LastPoint += parser.getText() + '\n'; // adding elevation
                                    // continue to new cords, with the \n for the next cord.
                                    break; // baby proofing.
                                }

                                parser.next();// move forward in the xml file
                                eventType = parser.getEventType();// get the event which it is
                            }

                            if(firstPoint)
                            {
                                startPoint = LastPoint; // save the first point
                                firstPoint = false; // turn off the first point saving
                            }

                            cords += LastPoint; // add latest point to the coordinates list
                        }

                        parser.next(); // move forward in the xml file
                        eventType = parser.getEventType();//get the event which it is
                    }

                    int firstIndex = startPoint.indexOf(',');
                    int lastIndex = startPoint.lastIndexOf(',');

                    FileOutputStream os = new FileOutputStream(file);
                    KMLparser kmLparser = new KMLparser(os);
                    kmLparser.startWriting();
                    kmLparser.writeCloseUpPoint(startPoint.substring(0,firstIndex), startPoint.substring(firstIndex + 1,lastIndex) );
                    kmLparser.writeRoute( startPoint, LastPoint, cords, data.toString().substring(data.toString().lastIndexOf('/') + 1) );
                }
                else {
                    file = new File(new URI(data.toString()));//experimental and unsafe. need to add file checking.
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
        mMap = googleMap;
        fileSetUp(data);
    }
}