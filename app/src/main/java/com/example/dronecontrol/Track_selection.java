package com.example.dronecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.dronecontrol.Adapters.trackAdapter;
import com.example.dronecontrol.Structures.FireBaseUploader;
import com.example.dronecontrol.Structures.TrackInfo;
import com.example.dronecontrol.Structures.UserUid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Track_selection extends AppCompatActivity {
    TextView Title;
    ArrayList<TrackInfo> tracksList;
    trackAdapter tracksAdapter;
    ListView tracksListView;
    Intent gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_selection);

        Title = findViewById(R.id.Title);
        tracksListView = findViewById(R.id.tracksListView);

        tracksList = new ArrayList<TrackInfo>();
        tracksAdapter = new trackAdapter(this,tracksList);
        tracksListView.setAdapter(tracksAdapter);

        gi = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTracksList();
    }

    private void getTracksList()
    {
        FireBaseUploader.getDBReference().child(UserUid.user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tracksList.clear();
                for(DataSnapshot trackData : snapshot.getChildren())
                {
                    tracksList.add(trackData.getValue(TrackInfo.class));
                }
                tracksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}