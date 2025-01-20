package com.example.dronecontrol.Structures;

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
    public static void uploadFile(File file, String userUID,String fileType,OnFailureListener failureListener, OnSuccessListener successListener)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("userFiles/"+
                userUID+"/"+fileType+"Files/"+file.getName());
        Uri fileUri = Uri.fromFile(file);
        UploadTask uploadTask = ref.putFile(fileUri);

        uploadTask.addOnFailureListener(failureListener).addOnSuccessListener(successListener);
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