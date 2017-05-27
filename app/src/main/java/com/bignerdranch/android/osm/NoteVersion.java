package com.bignerdranch.android.osm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Севастьян on 10.03.2017.
 */

public class NoteVersion extends AppCompatActivity {
    private AdView mAdView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_note);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3781095842244998/6322290464");
        mAdView = (AdView) findViewById(R.id.adViewthree);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
