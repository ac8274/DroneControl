package com.example.dronecontrol.Structures;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KMLparser {
    private XmlSerializer serializer;

    public KMLparser (FileOutputStream fous)
    {
        this.serializer = Xml.newSerializer();
        try {
            this.serializer.setOutput(fous,"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startWriting() throws IOException {
        this.serializer.startDocument("UTF-8",false);
        this.serializer.startTag(null,"kml");
        this.serializer.attribute(null,"creator","Arie Chernobilsky");
        this.serializer.attribute(null,"xmlns","http://earth.google.com/kml/2.2");
        this.serializer.startTag(null,"Document");
        this.serializer.startTag(null,"name")
                .text("Temporary")
                .endTag(null,"name");
        this.serializer.startTag(null,"open")
                .text("1")
                .endTag(null,"open");


        this.serializer.startTag(null,"Style")
                .attribute(null,"id", "StartIconStyle");
        this.serializer.startTag(null,"IconStyle")
                .attribute(null,"id","StartIcon");
        this.serializer.startTag(null,"Icon");
        this.serializer.startTag(null,"href")
                .text("https://www.plotaroute.com/images/icons/starticon.png")
                .endTag(null,"href");
        this.serializer.endTag(null,"Icon");
        this.serializer.endTag(null,"IconStyle");
        this.serializer.startTag(null,"LabelStyle");
        this.serializer.startTag(null,"scale")
                .text("0.7")
                .endTag(null,"scale");
        this.serializer.endTag(null,"LabelStyle");
        this.serializer.endTag(null,"Style");


        this.serializer.startTag(null,"Style")
                .attribute(null,"id", "FinishIconStyle");
        this.serializer.startTag(null,"IconStyle")
                .attribute(null,"id","FinishIcon");
        this.serializer.startTag(null,"Icon");
        this.serializer.startTag(null,"href")
                .text("https://www.plotaroute.com/images/icons/finishicon.png")
                .endTag(null,"href");
        this.serializer.endTag(null,"Icon");
        this.serializer.endTag(null,"IconStyle");
        this.serializer.startTag(null,"LabelStyle");
        this.serializer.startTag(null,"scale")
                .text("0.7")
                .endTag(null,"scale");
        this.serializer.endTag(null,"LabelStyle");
        this.serializer.endTag(null,"Style");

        this.serializer.startTag(null,"Style")
                .attribute(null,"id", "RoutePath");
        this.serializer.startTag(null,"LineStyle");
        this.serializer.startTag(null,"color")
                .text("E52975F3")
                .endTag(null,"color");
        this.serializer.startTag(null,"width")
                .text("4")
                .endTag(null,"width");
        this.serializer.endTag(null,"LineStyle");
        this.serializer.endTag(null,"Style");


        this.serializer.startTag(null,"Style")
                .attribute(null,"id", "WaypointIconStyle");
        this.serializer.startTag(null,"IconStyle")
                .attribute(null,"id","WaypointIcon");
        this.serializer.startTag(null,"Icon");
        this.serializer.startTag(null,"href")
                .text("https://www.plotaroute.com/images/icons/waypointicon.png")
                .endTag(null,"href");
        this.serializer.endTag(null,"Icon");
        this.serializer.startTag(null,"hotSpot")
                .attribute(null,"x","0.5")
                .attribute(null,"y","0.5")
                .attribute(null,"xunits","fraction")
                .attribute(null,"yunits","fraction")
                .endTag(null,"hotSpot");
        this.serializer.endTag(null,"IconStyle");
        this.serializer.startTag(null,"LabelStyle");
        this.serializer.startTag(null,"scale")
                .text("0.7")
                .endTag(null,"scale");
        this.serializer.endTag(null,"LabelStyle");
        this.serializer.endTag(null,"Style");

        this.serializer.flush(); // wright all of the above into the file.
    }


    public void writeCloseUpPoint(String lon, String lan) throws IOException {
        this.serializer.startTag(null,"LookAt");
        this.serializer.startTag(null,"longitude")
                .text(lon)
                .endTag(null,"longitude");
        this.serializer.startTag(null,"latitude")
                .text(lan)
                .endTag(null,"latitude");
        this.serializer.startTag(null,"range")
                .text("400")
                .endTag(null,"range");
        this.serializer.startTag(null,"tilt")
                .text("60")
                .endTag(null,"tilt");
        this.serializer.startTag(null,"altitudeMode")
                .text("clampToGround")
                .endTag(null,"altitudeMode");
        this.serializer.endTag(null,"LookAt");

        this.serializer.flush(); // wright all of the above into the file.
    }

    public void writeRoute(String startPoint, String endPoint, String routeCords, String routeName) throws IOException {
        this.serializer.startTag(null,"Folder")
                .attribute(null,"id", "Routenull");
        this.serializer.startTag(null,"name")
                .text(routeName)
                .endTag(null,"name");
        this.serializer.startTag(null,"Placemark");
        this.serializer.startTag(null,"name")
                .text("Drone Flight Route")
                .endTag(null,"name");
        this.serializer.startTag(null,"styleUrl")
                .text("#RoutePath")
                .endTag(null,"styleUrl");
        this.serializer.startTag(null,"LineString")
                .attribute(null, "id","Route");
        this.serializer.startTag(null,"extrude")
                .text("0")
                .endTag(null,"extrude");
        this.serializer.startTag(null,"tessellate")
                .text("1")
                .endTag(null,"tessellate");
        this.serializer.startTag(null,"coordinates")
                .text(routeCords)
                .endTag(null,"coordinates");
        this.serializer.endTag(null,"LineString");
        this.serializer.endTag(null,"Placemark");


        this.serializer.startTag(null,"Placemark");
        this.serializer.startTag(null,"name")
                .text("START")
                .endTag(null,"name");
        this.serializer.startTag(null,"styleUrl")
                .text("#StartIconStyle")
                .endTag(null,"styleUrl");
        this.serializer.startTag(null,"Point");
        this.serializer.startTag(null,"coordinates")
                .text(startPoint)
                .endTag(null,"coordinates");
        this.serializer.endTag(null,"Point");
        this.serializer.endTag(null,"Placemark");


        this.serializer.startTag(null,"Placemark");
        this.serializer.startTag(null,"name")
                .text("FINISH")
                .endTag(null,"name");
        this.serializer.startTag(null,"styleUrl")
                .text("#FinishIconStyle")
                .endTag(null,"styleUrl");
        this.serializer.startTag(null,"Point");
        this.serializer.startTag(null,"coordinates")
                .text(endPoint)
                .endTag(null,"coordinates");
        this.serializer.endTag(null,"Point");
        this.serializer.endTag(null,"Placemark");

        this.serializer.endTag(null,"Folder");
        this.serializer.endTag(null,"Document");
        this.serializer.endTag(null,"kml");

        this.serializer.flush(); // wright all of the above into the file.
    }


}
