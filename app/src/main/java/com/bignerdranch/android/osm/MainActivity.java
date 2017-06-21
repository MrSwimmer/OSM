package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;

/**
 * Created by Севастьян on 07.03.2017.
 */
public class MainActivity extends Activity {
    private static final String TAG = "tag";
    private static Button mNewButton;
    private Button mViewButton;
    private Button mSettings;
    private Button mInfoButton;
    private AdView mAdView;
    private Button mStatButton;
    private ImageView mImageView;
    private TheBackupAgent agent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_menu);
//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3781095842244998/4103854063");
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        mImageView.setImageResource(R.drawable.osmr);
        mNewButton = (Button) findViewById(R.id.new_note);
        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                NoteLab.get(MainActivity.this).addNote(note);
                Intent intent = NotePagerActivity.newIntent(MainActivity.this, note.getId());
                startActivity(intent);
                /*Note note = new Note();
                Intent i = new Intent(MainActivity.this, NotePagerActivity.class);
                startActivity(i);*/
//                Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
//                startService(intentVibrate);
            }
        });
        mViewButton = (Button) findViewById(R.id.all_note);
        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NoteListActivity.class);
                startActivity(i);
            }
        });
        mSettings = (Button) findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NoteSettings.class);
                startActivity(i);
            }
        });
        mInfoButton = (Button) findViewById(R.id.instruction);
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NoteInfo.class);
                startActivity(i);
            }
        });
        mStatButton = (Button) findViewById(R.id.statistic);
        mStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Statistic.class);
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
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }


}
