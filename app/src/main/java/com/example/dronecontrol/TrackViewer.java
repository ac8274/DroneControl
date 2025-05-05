package com.example.dronecontrol;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dronecontrol.Structures.KMLparser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

/**
 * The {@code TrackViewer} class is an {@link AppCompatActivity} that displays a route on a Google Map.
 * It handles loading route data from GPX or KML files and rendering them as a KML layer on the map.
 */
public class TrackViewer extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * The {@link GoogleMap} instance used to display the route.
     */
    private static GoogleMap mMap;
    /**
     * The {@link Intent} that started this activity.
     */
    private Intent gi;
    /**
     * The {@link Uri} of the data (GPX or KML file) to be displayed.
     */
    private Uri data;

    private String closeUpPoint;

    /**
     * Constant indicating that the file type is not supported.
     */
    private static final int NOT_FILE = 0;
    /**
     * Constant indicating that the file type is a GPX file.
     */
    private static final int GPX_FILE = 1;
    /**
     * Constant indicating that the file type is a KML file.
     */
    private static final int KML_FILE = 2;

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
        closeUpPoint = null;
    }

    /**
     * Sets up the file for display. This method determines the file type (GPX or KML) and processes it accordingly.
     *
     * @param data The {@link Uri} of the file to be processed.
     * @throws RuntimeException If a {@link FileNotFoundException}, {@link XmlPullParserException}, {@link IOException}, or {@link URISyntaxException} occurs during file processing.
     */
    private void fileSetUp(Uri data) {
        try {
            File file = null;
            switch (checkFile()) {
                case NOT_FILE:
                    finish();
                    break;
                case GPX_FILE:
                    file = new File(this.getExternalFilesDir(null), "DroneRoute.kml");

                    boolean firstPoint = true;

                    String startPoint = "";

                    String LastPoint = "";

                    String cords = "";

                    InputStream inputStream = getContentResolver().openInputStream(data);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(inputStream, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT)// run until end of file
                    {
                        if (eventType == XmlPullParser.START_TAG && parser.getName().equals("wpt")) { //point found
                            LastPoint = parser.getAttributeValue(null, "lon") + "," +
                                    parser.getAttributeValue(null, "lat") + ",300\n"; // put a default elevation
                            parser.next();

                            while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("wpt"))) // search inside the point for elevation value
                            {
                                String s =  parser.getName();
                                if (eventType == XmlPullParser.START_TAG && s!= null && s.equals("ele")) // elevation value found
                                {
                                    parser.next();
                                    LastPoint = LastPoint.substring(0, LastPoint.length() - 4);
                                    LastPoint += parser.getText() + '\n'; // adding elevation
                                    // continue to new cords, with the \n for the next cord.
                                    break; // baby proofing.
                                }

                                parser.next();// move forward in the xml file
                                eventType = parser.getEventType();// get the event which it is
                            }

                            if (firstPoint) {
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

                    closeUpPoint = startPoint;

                    FileOutputStream os = new FileOutputStream(file);
                    KMLparser kmLparser = new KMLparser(os);
                    kmLparser.startWriting();
                    kmLparser.writeCloseUpPoint(startPoint.substring(0, firstIndex), startPoint.substring(firstIndex + 1, lastIndex));
                    kmLparser.writeRoute(startPoint, LastPoint, cords, data.toString().substring(data.toString().lastIndexOf('/') + 1));
                    break;

                case KML_FILE:
                    file = new File(new URI(data.toString()));//experimental and unsafe. need to add file checking.
            }

            KmlLayer layer = new KmlLayer(mMap, new FileInputStream(file), this);
            layer.addLayerToMap();

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (XmlPullParserException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the type of the input file (GPX or KML) based on its content and file extension.
     *
     * @return An integer representing the file type: {@link #NOT_FILE}, {@link #GPX_FILE}, or {@link #KML_FILE}.
     * @throws FileNotFoundException If the file specified by the URI is not found.
     */
    int checkFile() throws FileNotFoundException {
        if (data != null) {
            ContentResolver resolver = getContentResolver();
            String type = resolver.getType(data); // MIME type sent by WhatsApp

            String filePath = data.getPath();

            if(filePath.endsWith(".gpx") || filePath.endsWith(".kml")) {
                if(filePath.endsWith(".gpx"))
                {
                    return GPX_FILE;
                }
                else
                {
                    return KML_FILE;
                }
            }
            else {
                // Try inspecting file name (if possible)
                Cursor cursor = resolver.query(data, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        String name = cursor.getString(nameIndex);
                        if (name.endsWith(".gpx") || name.endsWith(".kml")) {
                            if(name.endsWith(".gpx"))
                            {
                                return GPX_FILE;
                            }
                            else
                            {
                                return KML_FILE;
                            }
                            // parse GPX here
                        }
                        else
                        {
                            return NOT_FILE;
                        }
                    }
                    cursor.close();
                }
            }
        }
        return NOT_FILE;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fileSetUp(data);

        if(closeUpPoint!= null)
        {
            int firstIndex = closeUpPoint.indexOf(',');
            int lastIndex = closeUpPoint.lastIndexOf(',');


            double lon = Double.valueOf(closeUpPoint.substring(0, firstIndex));
            double lat = Double.valueOf(closeUpPoint.substring(firstIndex + 1, lastIndex));

            LatLng latLng = new LatLng(lat, lon);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F));
        }
    }
}