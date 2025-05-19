package com.example.dronecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.example.dronecontrol.Adapters.trackAdapter;
import com.example.dronecontrol.Structures.FireBaseHelper;
import com.example.dronecontrol.Structures.TrackInfo;
import com.example.dronecontrol.Structures.UserUid;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.util.ArrayList;

public class Track_selection extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
        tracksListView.setOnItemClickListener(this);

        tracksList = new ArrayList<TrackInfo>();
        tracksAdapter = new trackAdapter(this,tracksList);
        tracksListView.setAdapter(tracksAdapter);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks the screen in the horizontol state.

        gi = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTracksList();
    }

    private void getTracksList()
    {
        FireBaseHelper.getDBReference().child(UserUid.user_uid).addValueEventListener(new ValueEventListener() {
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

    private void startNextActivity(File file)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("content://com.example.dronecontrol.gpxprovider/gpx_file/" + Uri.encode(file.getAbsolutePath())),
                "application/gpx+xml" // or "application/vnd.google-earth.kml+xml"
        );
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        File file = new File(this.getExternalFilesDir(null),tracksList.get(position).getTrackName() +".gpx");
        FireBaseHelper.downloadFile(file, "userFiles/"+UserUid.user_uid+"/.gpxFiles/"+tracksList.get(position).getTrackName()+".gpx",
                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // need to add a alert dialog later.
                startNextActivity(file);
            }
        },new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                // need to add a alert dialog later.
            }
        } );
    }
}