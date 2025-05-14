package com.example.dronecontrol.Structures;

import androidx.annotation.NonNull;

public class TrackInfo {
    private String track_name;
    private String flight_date;
    private String flight_start_time;
    private String flight_end_time;
    private String trackFileUri;

    public TrackInfo(String name, String date, String start, String end, String uri)
    {
        this.track_name = name;
        this.flight_date = date;
        this.flight_start_time = start;
        this.flight_end_time = end;
        this.trackFileUri = uri;
    }

    public TrackInfo()
    {
        this.track_name = "";
        this.flight_date = "";
        this.flight_start_time = "";
        this.flight_end_time = "";
        this.trackFileUri = "";
    }
    public TrackInfo(String name)
    {
        this.track_name = name;
        this.flight_date = "";
        this.flight_start_time = "";
        this.flight_end_time = "";
        this.trackFileUri = "";
    }

    public void setTrackName(String name)
    {
        this.track_name = name;
    }
    public void setFlightDate(String date)
    {
        this.flight_date = date;
    }
    public void setFlightStartTime(String time)
    {
        this.flight_start_time = time;
    }
    public void setFlightEndTime(String time)
    {
        this.flight_end_time = time;
    }
    public void setTrackFileUri(String uri)
    {
        this.trackFileUri = uri;
    }

    public String getTrackName()
    {
        return this.track_name;
    }
    public String getFlightDate() {
        return this.flight_date;
    }
    public String getFlightStartTime()
    {
        return this.flight_start_time;
    }
    public String getFlightEndTime()
    {
        return this.flight_end_time;
    }
    public String getTrackFileUri()
    {
        return this.trackFileUri;
    }

    @NonNull
    public String toString()
    {
        return  "Track Name: " + this.track_name +
                "\nFlight date: " + this.flight_date +
                "\nStart time: " + this.flight_start_time +
                "\nEnd time: " + this.flight_end_time +
                "\nTrack file URI: " + this.trackFileUri;
    }
}

