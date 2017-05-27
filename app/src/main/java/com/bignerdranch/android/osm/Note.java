package com.bignerdranch.android.osm;

import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Севастьян on 01.03.2017.
 */

public class Note {
    private UUID mId;
    private String mPsit;
    private String mPstand;
    private float mPoint;
    private Date mDate;
    private String mRec;
    private int mZone;
    private RadioGroup mRadioGroup;
    private boolean mRad;

    public Note() {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public int getZone() {
        int n = 0;
        if (Points() >= 7.5)
            n = 1;
        if (Points() >= 5 && Points() < 7.5)
            n = 2;
        if (Points() >= 2.5 && Points() < 5)
            n = 3;
        if (Points() < 2.5)
            n = 4;
        return n;
    }

    public void setZone(int zone) {
        mZone = zone;
    }

    public boolean isRad() {
        return mRad;
    }

    public void setRad(boolean rad) {
        mRad = rad;
    }

    public String getRec() {
        return mRec;
    }

    public void setRec(String rec) {
        mRec = rec;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getPsit() {
        return mPsit;
    }

    public void setPsit(String psit) {
        mPsit = psit;
    }

    public int PsitToInt() {
        if (mPsit != null && mPsit != "")
            return Integer.parseInt(mPsit);
        else
            return 0;
    }

    public int PstandToInt() {
        if (mPstand != null && mPstand != "") {
            return Integer.parseInt(mPstand);
        } else
            return 0;
    }

    public float Points() {
        if (PsitToInt() != 0 && PstandToInt() != 0)
            return (float) (14.5 - 0.5 * ((float) (PsitToInt()) - 40) / 3.5 - ((float) (PstandToInt() - PsitToInt())) / 2.27 * 0.5);
        else return 0;
    }

    public String getPoint() {
        mPoint = Points();
        return String.format("%.2f", mPoint);
    }

    public void setPoint(int point) {
        mPoint = point;
    }

    public Float getPointFloat() {
        mPoint = Points();
        return mPoint;
    }

    public String getPstand() {
        return mPstand;
    }

    public void setPstand(String pstand) {
        mPstand = pstand;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDateForm() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        return format1.format(mDate);
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    public void setRadioGroup(RadioGroup radioGroup) {
        mRadioGroup = radioGroup;
    }
}
