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
        
    }


}
