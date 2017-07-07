package com.bignerdranch.android.osm.puls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.osm.Note;
import com.bignerdranch.android.osm.NoteLab;
import com.bignerdranch.android.osm.NotePagerActivity;
import com.bignerdranch.android.osm.R;
import com.bignerdranch.android.osm.puls.MyVars.TYPE;

import java.util.UUID;

//import com.bignerdranch.android.osm.puls.Browser;

@SuppressLint("NewApi")


public class Pulsometro extends Activity {
    static public String text = "";
    static int beatsArrayAvg = 0;
    static int beatsArrayCnt = 0;
    static int beatsAvg = 0;
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
                    int beat = (int) MyVars.beats;
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


                for (int i = 0; i < MyVars.beatsArray.length; i++) {
                    if (MyVars.beatsArray[i] > 0) {
                        beatsArrayAvg += MyVars.beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                beatsAvg = (beatsArrayAvg / beatsArrayCnt);
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
    public int p1 = 0, p2 = 0;
    public int mas = 0;
    boolean pressed = false;
    ClipData myClip;
    ClipboardManager myClipboard;
    /**
     * {@inheritDoc}
     */
    String id;
    private Boolean actVib = false;
    private Boolean sec = true;
    private int count = 1;
    private long onesec = 0;
    private Note mNote;
    private NoteLab mNoteLab;

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

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        MyVars.viewFinder = (SurfaceView) findViewById(R.id.preview);
        MyVars.viewFinderHolder = MyVars.viewFinder.getHolder();
        MyVars.viewFinderHolder.addCallback(surfaceCallback);
        MyVars.viewFinderHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        MyVars.image = findViewById(R.id.image);
        MyVars.text = (TextView) findViewById(R.id.text);
        MyVars.save = (Button) findViewById(R.id.add_puls);
        MyVars.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pressed) {
                    p1 = beatsAvg;
                    pressed = !pressed;
                } else {
                    p2 = beatsAvg;
                    Log.i("LOL", String.valueOf(p1) + String.valueOf(p2));
                    mNote = NoteLab.get(Pulsometro.this).getNote(UUID.fromString(id));
                    mNote.setPsit(String.valueOf(p1));
                    mNote.setPstand(String.valueOf(p2));
//                    myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    Intent intent = NotePagerActivity.newIntent(Pulsometro.this, UUID.fromString(id));
//                    myClip = ClipData.newPlainText("text", p1+" "+p2);
//                    ClipData abc = myClipboard.getPrimaryClip();
//                    ClipData.Item item = abc.getItemAt(0);
//                    text = item.getText().toString();
//                    myClipboard.setPrimaryClip(myClip);
//                    intent.putExtra("p1", p1);
//                    intent.putExtra("p2", p2);
                    text = p1 + " " + p2;
                    startActivity(intent);
                    //finish();
                }
            }
        });
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        MyVars.wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

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
