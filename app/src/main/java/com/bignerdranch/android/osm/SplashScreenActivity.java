package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SplashScreenActivity extends Activity {
    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    StorageReference riversRef;
    FirebaseUser user;
    private String[] tip = new String[3];
    private TextView mTextView;
    private ImageView mImageView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        tip[0] = "Подсказка: при создании записи для удобства вы можете воспользоваться встроенным секундомером";
        tip[1] = "Подсказка: для удобства поиска нужной записи воспользуйтесь сортировкой или поиском по дате";
        tip[2] = "Подсказка: теперь вы можете уведомлять своего тренера о результате пробы, воспользовавшись функцией - отправить результат";
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    String Name = user.getEmail();
                    riversRef = mStorageRef.child(Name + "/noteBase.db");
                    upload();
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        setContentView(R.layout.new_splash);
        mTextView = (TextView) findViewById(R.id.splash_tip);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.osm);
        int max = 2;
        mTextView.setText(tip[(int) (Math.random() * ++max)]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // По истечении времени, запускаем главный активити, а Splash Screen закрываем
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void upload() {
        final Uri file = Uri.fromFile(new File(Environment.getDataDirectory(), "//data//" + "com.bignerdranch.android.osm"
                + "//databases//" + "noteBase.db"));
        Toast.makeText(SplashScreenActivity.this, "Идет загрузка", Toast.LENGTH_SHORT).show();
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(SplashScreenActivity.this, "Загрузка завершена успешно!", Toast.LENGTH_SHORT).show();
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(SplashScreenActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });
    }
}
