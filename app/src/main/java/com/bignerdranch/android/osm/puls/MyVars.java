package com.bignerdranch.android.osm.puls;

import android.hardware.Camera;
import android.os.PowerManager.WakeLock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;


@SuppressWarnings("unused")
public class MyVars {
    static final String APP = "Pulsometro";
    static final AtomicBoolean processing = new AtomicBoolean(false);
    static final int arraySizeAVG = 4;
    static final int[] arrayAVG = new int[arraySizeAVG];
    static final int beatsArraySize = 3;
    static final int[] beatsArray = new int[beatsArraySize];
    static SurfaceView viewFinder = null;
    static SurfaceHolder viewFinderHolder = null;
    static Camera camera = null;
    static View image = null;
    static Button save = null;
    static TextView text = null;
    static WakeLock wakeLock = null;
    static int indexAVG = 0;
    static TYPE statusBatimento = TYPE.beatOFF;
    static int beatsIndex = 0;
    static double beats = 0;
    static long startTime = 0;

    static TYPE getCurrent() {
        return statusBatimento;
    }

    public enum TYPE {
        beatOFF, beatON
    }
}
