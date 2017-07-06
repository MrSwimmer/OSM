package com.bignerdranch.android.osm.puls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.osm.R;
import com.bignerdranch.android.osm.VibrateService;
import com.bignerdranch.android.osm.puls.MyVars.TYPE;

//import com.bignerdranch.android.osm.puls.Browser;

@SuppressLint("NewApi")


public class Pulsometro extends Activity {
    private static PreviewCallback previewCallback = new PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!MyVars.processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = Fotopletismografia.redAVG(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                MyVars.processing.set(false);
                return;
            }

            int arrayAVGsum = 0;
            int arrayAVGcount = 0;
            for (int i = 0; i < MyVars.arrayAVG.length; i++) {
                if (MyVars.arrayAVG[i] >= 1) {
                    arrayAVGcount++;
                    arrayAVGsum += MyVars.arrayAVG[i];
                }
            }

            int changeAVG = (arrayAVGcount > 0) ? (arrayAVGsum / arrayAVGcount) : 0;
            TYPE newType = MyVars.statusBatimento;
            if (imgAvg < changeAVG) {
                newType = TYPE.beatON;
                if (newType != MyVars.statusBatimento) {
                    MyVars.beats++;
                }
            } else if (imgAvg > changeAVG) {
                newType = TYPE.beatOFF;
            }

            if (MyVars.indexAVG == MyVars.arraySizeAVG) MyVars.indexAVG = 0;
            MyVars.arrayAVG[MyVars.indexAVG] = imgAvg;
            MyVars.indexAVG++;

            // Transitioned from one state to another to the same
            if (newType != MyVars.statusBatimento) {
                MyVars.statusBatimento = newType;
                MyVars.image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - MyVars.startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (MyVars.beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    MyVars.startTime = System.currentTimeMillis();
                    int beats = (int) MyVars.beats;
                    MyVars.beats = 0;
                    MyVars.processing.set(false);

//                    Browser browser = new Browser();
//                    browser.callBrowser(beats);

                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (MyVars.beatsIndex == MyVars.beatsArraySize) MyVars.beatsIndex = 0;
                MyVars.beatsArray[MyVars.beatsIndex] = dpm;
                MyVars.beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < MyVars.beatsArray.length; i++) {
                    if (MyVars.beatsArray[i] > 0) {
                        beatsArrayAvg += MyVars.beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                MyVars.text.setText(String.valueOf(beatsAvg));
                MyVars.startTime = System.currentTimeMillis();
                MyVars.beats = 0;


            }


            MyVars.processing.set(false);
        }
    };
    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                MyVars.camera.setPreviewDisplay(MyVars.viewFinderHolder);
                MyVars.camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                //Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = MyVars.camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(MyVars.APP, "Using width=" + size.width + " height=" + size.height);
            }
            MyVars.camera.setParameters(parameters);
            MyVars.camera.startPreview();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };
    public int mas = 0;
    private Boolean actVib = false;
    private Boolean sec = true;
    private int count = 1;
    private long onesec = 0;

    @SuppressLint("NewApi")
    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MyVars.viewFinder = (SurfaceView) findViewById(R.id.preview);
        MyVars.viewFinderHolder = MyVars.viewFinder.getHolder();
        MyVars.viewFinderHolder.addCallback(surfaceCallback);
        MyVars.viewFinderHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        MyVars.image = findViewById(R.id.image);
        MyVars.text = (TextView) findViewById(R.id.text);
        MyVars.chrono = (Chronometer) findViewById(R.id.chrono_puls);
        MyVars.start = (ImageView) findViewById(R.id.start_but);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        MyVars.wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        MyVars.chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - MyVars.chrono.getBase();
                long onesec = SystemClock.elapsedRealtime()
                        - MyVars.chrono.getBase();
                if (onesec >= 1000) {
                    onesec = 0;
                    mas = (mas + Integer.parseInt(MyVars.text.getText().toString())) / count;
                    try {
                        Toast.makeText(getApplicationContext(), mas, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(getApplicationContext(), mas, Toast.LENGTH_LONG).show();
                    count++;
                }
                if (elapsedMillis > 15000) {
                    elapsedMillis = 0;
                    MyVars.chrono.stop();
                }
                if (elapsedMillis >= 15000 && !actVib) {
                    actVib = true;
                    Intent intentVibrate = new Intent(Pulsometro.this, VibrateService.class);
                    Pulsometro.this.startService(intentVibrate);
                }
            }
        });
        MyVars.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sec) {
                    onStartClick();
                    MyVars.start.setImageResource(R.drawable.stop);
                    sec = !sec;
                } else {
                    onStopClick();
                    MyVars.start.setImageResource(R.drawable.start);
                    sec = !sec;
                }
            }
        });
    }

    public void onStartClick() {
        MyVars.chrono.setBase(SystemClock.elapsedRealtime());
        MyVars.chrono.start();
    }

    public void onStopClick() {
        MyVars.chrono.stop();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        MyVars.wakeLock.acquire();

        MyVars.camera = Camera.open();

        MyVars.startTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        MyVars.wakeLock.release();

        MyVars.camera.setPreviewCallback(null);
        MyVars.camera.stopPreview();
        MyVars.camera.release();
        MyVars.camera = null;
    }
}
