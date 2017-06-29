package com.bignerdranch.android.osm;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.osm.database.ExportImportDB;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Севастьян on 08.03.2017.
 */

public class NoteSettings extends AppCompatActivity {
    private static final String DIALOG_DATE = "DialogDate";
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mButton;
    private TextView mMark;
    private TextView Email;
    private TextView mEnterRegButton;
    private String str = null;
    private TextView mVer;
    private AdView mAdView;

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

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_note);
        mAuth = FirebaseAuth.getInstance();
        Email = (TextView) findViewById(R.id.email);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Log.i("LOG", email);
                    mEnterRegButton.setText("Вход выполнен!");
                    Email.setText(email);
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    String email = user.getEmail();
//                    Intent i = new Intent(LogInActivity.this, SucEnter.class);
//                    i.putExtra("name", email);
//                    startActivity(i);
//                    // User is signed in
//                } else {
//                    // User is signed out
//                }
//                // ...
//            }


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3781095842244998/9275756860");
        mAdView = (AdView) findViewById(R.id.adViewtwo);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mMark = (TextView) findViewById(R.id.buttonMark);
        mEnterRegButton = (TextView) findViewById(R.id.EnterRegBut);
        mEnterRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoteSettings.this, ExportImportDB.class);
                startActivity(i);
            }
        });
        mButton = (TextView) findViewById(R.id.buttonDel);
        mMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.bignerdranch.android.osm"));
                startActivity(intent);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteLab.get(NoteSettings.this).delAllNote();
                Toast.makeText(NoteSettings.this,
                        "Все записи удалены",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mVer = (TextView) findViewById(R.id.buttonVer);
        mVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoteSettings.this, NoteVersion.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
