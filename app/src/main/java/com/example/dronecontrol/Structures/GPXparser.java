package com.example.dronecontrol.Structures;

import android.location.Location;
import android.os.Build;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GPXparser {
    private XmlSerializer serializer;

    public GPXparser(FileOutputStream fous) {
        this.serializer = Xml.newSerializer();
        try {
            this.serializer.setOutput(fous,"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startWriting() throws IOException {
        this.serializer.startDocument("UTF-8",false);
        this.serializer.startTag(null,"gpx");
        this.serializer.attribute(null,"xmlns","http://www.topografix.com/GPX/1/1");
        this.serializer.attribute(null,"version","1.1");
        this.serializer.attribute(null,"creator","Cats_army");
        this.serializer.attribute(null,"xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        this.serializer.attribute(null,"xsi:schemaLocation","http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
        //First part of XML FILE most important for setting up which kind of file this is.

        this.serializer.startTag(null,"metadata");
        this.serializer.startTag(null,"name").text("Drone Track").endTag(null,"name");
        this.serializer.startTag(null,"desc").text("Drone track GPX file").endTag(null,"desc");
        this.serializer.startTag(null,"author");
        this.serializer.startTag(null,"name").text("Arie Chernobilsky").endTag(null,"name");
        this.serializer.endTag(null,"author");
        this.serializer.endTag(null,"metadata");
        //Second part finished, This part is of the Author writes.
    }

    public void addPoint(double latatiude, double longtatiude, double elevation,String name) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.serializer.startTag(null,"wpt");
        this.serializer.attribute(null,"lat",String.valueOf(latatiude));
        this.serializer.attribute(null,"lon",String.valueOf(longtatiude));
        this.serializer.startTag(null,"ele").text(String.valueOf(elevation)).endTag(null,"ele");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {this.serializer.startTag(null,"time").text(Calendar.getInstance().getTime().toInstant().toString()).endTag(null,"time");}
        else {this.serializer.startTag(null,"time").text(df.format(Calendar.getInstance().getTime())).endTag(null,"time");}
        this.serializer.startTag(null,"name").text(name).endTag(null,"name");
        this.serializer.startTag(null,"sym").text("Flag").endTag(null,"sym");
        this.serializer.endTag(null,"wpt");
    }

    public void endWriting() throws IOException {
        this.serializer.endTag(null,"gpx");
        this.serializer.endDocument();
    }
}
