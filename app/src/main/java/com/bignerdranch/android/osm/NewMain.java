package com.bignerdranch.android.osm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by Севастьян on 19.06.2017.
 */

public class NewMain extends Activity {
    Button newButton, allButton, statButton, infButton, setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_menu);
        newButton = (Button) findViewById(R.id.new_note);
        allButton = (Button) findViewById(R.id.all_note);
        statButton = (Button) findViewById(R.id.statistic);
        setButton = (Button) findViewById(R.id.settings);
        infButton = (Button) findViewById(R.id.instruction);
        //circularImageBar(imageView, 90);
        setDrawable();
    }

    private void setDrawable() {
        newButton.setBackgroundResource(R.drawable.shape);
        allButton.setBackgroundResource(R.drawable.shape);
        statButton.setBackgroundResource(R.drawable.shape);
        setButton.setBackgroundResource(R.drawable.shape);
        infButton.setBackgroundResource(R.drawable.shape);
    }


}
