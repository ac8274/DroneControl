package com.example.dronecontrol.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dronecontrol.R;
import com.example.dronecontrol.Structures.TrackInfo;

import java.util.ArrayList;

public class trackAdapter extends BaseAdapter {
    private ArrayList<TrackInfo> trackInfoArrayList;
    LayoutInflater inflater;
    public trackAdapter(@NonNull Context context, ArrayList<TrackInfo> trackInfoArrayList) {
        this.trackInfoArrayList = trackInfoArrayList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {return this.trackInfoArrayList.size();}

    @Override
    public Object getItem(int position) {return this.trackInfoArrayList.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position,@Nullable View convertView,@Nullable ViewGroup parent) {
        convertView = inflater.inflate(R.layout.track_lv_layout,parent,false);

        TextView TNtextView = convertView.findViewById(R.id.TNtextView);
        TextView TDtextView = convertView.findViewById(R.id.TDtextView);
        TextView TStextView = convertView.findViewById(R.id.TStextView);
        TextView TEtextView = convertView.findViewById(R.id.TEtextView);

        TrackInfo info = this.trackInfoArrayList.get(position);

        TNtextView.setText("Track Name: " + info.getTrackName());
        TDtextView.setText("Track Date: " + info.getFlightDate());
        TStextView.setText("Flight Start Time: " + info.getFlightStartTime());
        TEtextView.setText("Flight End Time: "+info.getFlightEndTime());

        return convertView;
    }
}
