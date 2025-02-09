package com.example.dronecontrol.Structures;

import com.example.dronecontrol.CustomExceptions.StreamInUseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GlobalFileHolder {
    public static boolean stopWriting;
    private static GlobalFileHolder instance = null;
    private FileOutputStream os;
    private GPXparser gpXparser;

    private GlobalFileHolder()
    {
        stopWriting = false;
        this.os = null;
    }

    public void setFile(File file) throws FileNotFoundException, StreamInUseException
    {
        if(this.os == null) {
            this.os = new FileOutputStream(file);
            this.gpXparser = new GPXparser(this.os);
        }
        else {
            throw new StreamInUseException("Stream currently in use");
        }
    }

    public void writeToFile(double latitude, double longitude, double elevation)
    {
        try {
            this.gpXparser.addPoint(latitude,longitude,elevation,"trackPoint");
            this.os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void endFileWriting() throws IOException
    {
        this.gpXparser.endWriting();
        this.os.flush();
    }

    public void closeStream() throws IOException
    {
        if(!stopWriting)
        {
            this.os.close();
            this.os = null;
        }
    }

    public static GlobalFileHolder getInstance()
    {
        if(null == instance)
        {
            instance = new GlobalFileHolder();
        }
        return instance;
    }
}
