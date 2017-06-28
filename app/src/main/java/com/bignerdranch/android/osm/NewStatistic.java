package com.bignerdranch.android.osm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by Севастьян on 27.06.2017.
 */

public class NewStatistic extends AppCompatActivity {
    private List<Note> mNotes;
    private Float one, two;
    private int One, Two;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);
        NoteLab noteLab = NoteLab.get(this);
        noteLab.setSort("DATE ASC");
        mNotes = noteLab.getNotes();
    }

    private void drawGraph() {
        Bitmap b = Bitmap.createBitmap(1080, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#4286f4"));
        paint.setStrokeWidth(3);
        String date;
        for (int i = 0; i < mNotes.size(); i++) {
            date = mNotes.get(i).getDate().toString();
            if (date.charAt(3) == '0' && date.charAt(4) == '1' && date.substring(6, 9) == "2017") {
                if (count == 0) {
                    one = Float.valueOf(mNotes.get(i).getPoint());

                }

                two = Float.valueOf(mNotes.get(i).getPoint());

            }

        }
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //canvas.drawLine();
    }
}
