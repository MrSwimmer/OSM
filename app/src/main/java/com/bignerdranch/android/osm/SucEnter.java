package com.bignerdranch.android.osm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


/**
 * Created by Севастьян on 20.05.2017.
 */

public class SucEnter extends AppCompatActivity {
    StorageReference riversRef;
    String Name;
    String Action;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RadioButton R1;
    private RadioButton R2;
    private RadioButton R3;
    private StorageReference mStorageRef;
    private Button mButton;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        final Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Action = intent.getStringExtra("action");
        Log.i("LOG", Action);

        if (Action.equals("reg")) {
            setContentView(R.layout.succes_reg);
        } else
            setContentView(R.layout.succes_enter);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        riversRef = mStorageRef.child(Name + "/noteBase.db");
        R1 = (RadioButton) findViewById(R.id.r1);
        R2 = (RadioButton) findViewById(R.id.radioSwap2);
        R3 = (RadioButton) findViewById(R.id.radioSwap3);
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//             user = firebaseAuth.getCurrentUser();
//            if (user.isEmailVerified() ) {
//                Toast.makeText(SucEnter.this, "Аккаунт подтвержден!" ,  Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(SucEnter.this, "Подтвердите аккаунт!" ,  Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(SucEnter.this, MainActivity.class);
//                startActivity(i);
//            }
//        }
//    };
        mButton = (Button) findViewById(R.id.NextBut);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Action.equals("reg"))
                if (R1.isChecked()) {
                    try {
                        //Toast.makeText(SucEnter.this, download().toString(), Toast.LENGTH_SHORT).show();
                        //download().toString();
                        download();
                        Intent i = new Intent(SucEnter.this, MainActivity.class);
                        startActivity(i);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.succes_enter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                mAuth.signOut();
                Intent i = new Intent(SucEnter.this, LogInActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void upload() {
        final Uri file = Uri.fromFile(new File(Environment.getDataDirectory(), "//data//" + "com.bignerdranch.android.osm"
                + "//databases//" + "noteBase.db"));
        Toast.makeText(SucEnter.this, "Идет загрузка", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SucEnter.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });
    }
    public void download() throws IOException {
        Toast.makeText(SucEnter.this, "Идет загрузка", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SucEnter.this, "Скачивание завершено!", Toast.LENGTH_SHORT).show();
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
