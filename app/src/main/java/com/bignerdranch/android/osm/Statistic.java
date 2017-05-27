package com.bignerdranch.android.osm;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Севастьян on 03.04.2017.
 */

public class Statistic extends AppCompatActivity {
    GraphView graph;
    GraphView bar;
    //PieChart pieChart;
    TextView tr;
    String ZoneOrBall = "zone";
    String mString;
    Boolean NightOrTrain = true;
    private List<Note> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);
        NoteLab noteLab = NoteLab.get(this);
        noteLab.setSort("DATE ASC");
        mNotes = noteLab.getNotes();
        graph = (GraphView) findViewById(R.id.graph);
        bar = (GraphView) findViewById(R.id.bar);
        //pieChart = (PieChart) findViewById(R.id.piechart);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        updateGraph(graph, ZoneOrBall, NightOrTrain);
        updateBar();
        //updatePie();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ZoneOrBall = "zone";
                        NightOrTrain = true;
                        break;
                    case 1:
                        ZoneOrBall = "ball";
                        NightOrTrain = true;
                        break;
//                    case 2: ZoneOrBall = "zone"; NightOrTrain = false; break;
//                    case 3: ZoneOrBall = "ball"; NightOrTrain = false; break;
                }
                graph.removeAllSeries();
                updateGraph(graph, ZoneOrBall, NightOrTrain);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void updateBar() {
        int one = 0, two = 0, three = 0, four = 0;
        DataPoint dataPoint[] = new DataPoint[4];
        for (int i = 0; i < mNotes.size(); i++) {
            switch (mNotes.get(i).getZone()) {
                case 1:
                    one++;
                    break;
                case 2:
                    two++;
                    break;
                case 3:
                    three++;
                    break;
                case 4:
                    four++;
                    break;
            }
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, one),
                new DataPoint(2, two),
                new DataPoint(3, three),
                new DataPoint(4, four)
        });
        bar.addSeries(series);
        bar.getViewport().setMinY(0);
        bar.getViewport().setMaxX(4.5);
        series.setSpacing(50);
        series.setAnimated(true);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            int color;

            @Override
            public int get(DataPoint data) {
                switch ((int) data.getX()) {
                    case 1:
                        color = Color.rgb(66, 197, 58);
                        break;
                    case 2:
                        color = Color.rgb(49, 23, 227);
                        break;
                    case 3:
                        color = Color.rgb(235, 198, 48);
                        break;
                    case 4:
                        color = Color.rgb(234, 0, 0);
                        break;
                }
                return color;
            }
        });

        bar.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        bar.getViewport().setMinX(0.5);

//        graph.getViewport().setMaxY(4);

        bar.getViewport().setXAxisBoundsManual(true);
        bar.getViewport().setYAxisBoundsManual(true);
    }

    void updateGraph(GraphView graph, String title, Boolean moment) {
        int countPoints = 0;
        float midball = 0;
        DataPoint dataPoint[] = new DataPoint[mNotes.size()];
        final String x;
        if (title == "zone") {
            for (int i = 0; i < mNotes.size(); i++) {
//                if(moment){
//                    if(mNotes.get(i).isRad()){
                dataPoint[i] = new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).getZone());
                if (i == 0)
                    midball = mNotes.get(i).Points();
                else
                    midball += mNotes.get(i).Points() / (i + 1);
//                        countPoints++;
//                    }
//                }
                //mString+=mNotes.get(i).isRad();
//                else {
//                    if(!mNotes.get(i).isRad()){
//                        dataPoint[i] =new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).getZone());
//                    }
//                }
                //dataPoint[i] =new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).getZone());
            }
            x = "Зона: ";
        } else {
            for (int i = 0; i < mNotes.size(); i++) {
//                if(moment){
//                    if(mNotes.get(i).isRad()){
                dataPoint[i] = new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).Points());
//                    }
//                }
                //mString+=mNotes.get(i).isRad();
//                else {
//                    if(!mNotes.get(i).isRad()){
//                        dataPoint[i] =new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).Points());
//                    }
//                }
                //dataPoint[i] =new DataPoint(mNotes.get(i).getDate(), mNotes.get(i).getZone());
            }

            x = "Балл: ";
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);

        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setAnimated(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        //graph.getGridLabelRenderer().setVerticalAxisTitle(x);
        graph.getGridLabelRenderer().setNumHorizontalLabels(mNotes.size());
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                String s = format1.format(new Date((long) dataPoint.getX()));
                s += " " + x + String.format("%.2f", dataPoint.getY());
                Toast.makeText(getApplicationContext(), "Дата: " + s, Toast.LENGTH_SHORT).show();
            }
        });
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

//        graph.getViewport().setMinY(1);
//        graph.getViewport().setMaxY(4);

        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

    }
}
