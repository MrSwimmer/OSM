package com.bignerdranch.android.osm;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.osm.database.ExportImportDB;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Севастьян on 08.03.2017.
 */

public class NoteSettings extends AppCompatActivity {
    private static final String DIALOG_DATE = "DialogDate";
    private TextView mButton;
    private TextView mMark;
    private TextView mEnterRegButton;
    private String str = null;
    private TextView mVer;
    private AdView mAdView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.settings_note);
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
