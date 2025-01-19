package com.example.poc.Structures;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public final class FireBaseUploader {
    private FireBaseUploader()
    {
    }
    public static void uploadFile(File file, String userUID,String fileType, Context context)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("userFiles/"+userUID+"/"+fileType+"Files/"+file.getName());
        Uri fileUri = Uri.fromFile(file);
        UploadTask uploadTask = ref.putFile(fileUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.println(Log.INFO,"dataUploadFailure",exception.getMessage());
                Toast.makeText(context, "failure" ,Toast.LENGTH_SHORT).show();
                deleteFile(file);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                deleteFile(file);
            }
        });
    }

    public static void deleteFile(File file)
    {
        if(file.exists())
        {
            file.delete();
            System.out.println("file deleted");
        }
    }

}