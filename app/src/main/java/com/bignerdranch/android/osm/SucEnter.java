package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


/**
 * Created by Севастьян on 20.05.2017.
 */

public class SucEnter extends Activity {
    expimp mExpimp;
    StorageReference riversRef;
    String Name;
    String Pass;
    private RadioButton R1;
    private RadioButton R2;
    private RadioButton R3;
    private StorageReference mStorageRef;
    private Button mButton;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        //Pass = intent.getStringExtra("pass");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.succes_enter);
        riversRef = mStorageRef.child(Name + "/noteBase.db");
        R1 = (RadioButton) findViewById(R.id.radioSwap1);
        R2 = (RadioButton) findViewById(R.id.radioSwap2);
        R3 = (RadioButton) findViewById(R.id.radioSwap3);
        mButton = (Button) findViewById(R.id.NextBut);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (R1.isChecked()) {
                    try {
                        //Toast.makeText(SucEnter.this, download().toString(), Toast.LENGTH_SHORT).show();
                        //download().toString();
                        download();
                    } catch (IOException e) {
                        Toast.makeText(SucEnter.this, "Егор", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                if (R2.isChecked()) {
                    Toast.makeText(SucEnter.this, "Успешной работы!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SucEnter.this, MainActivity.class);
                    startActivity(i);
                }
                if (R3.isChecked()) {
                    //mExpimp.exportDB();
                    upload();
                    Intent i = new Intent(SucEnter.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }

    public void upload() {
        final Uri file = Uri.fromFile(new File(Environment.getDataDirectory(), "//data//" + "com.bignerdranch.android.osm"
                + "//databases//" + "noteBase.db"));

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(SucEnter.this, "Загрузка завершена успешно!", Toast.LENGTH_SHORT).show();
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(SucEnter.this, exception.toString(), Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });
    }

    public void download() throws IOException {
        final File localFile = new File(Environment.getDataDirectory(), "//data//" + "com.bignerdranch.android.osm"
                + "//databases//" + "noteBase.db");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
//                        Intent i = new Intent(SucEnter.this, ExportImportDB.class);
//                        i.putExtra("op", "imp");
//                        startActivity(i);
                        Toast.makeText(SucEnter.this, "Скачивание завершено", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
                Toast.makeText(SucEnter.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
