package com.bignerdranch.android.osm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.osm.database.NoteDbSchema.NoteTable;

/**
 * Created by Севастьян on 05.03.2017.
 */

public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "noteBase.db";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NoteTable.Cols.UUID + ", " +
                NoteTable.Cols.DATE + ", " +
                NoteTable.Cols.UP + ", " +
                NoteTable.Cols.DOWN + ", " +
                NoteTable.Cols.RADIO + ", " +
                NoteTable.Cols.REC + ", " +
                NoteTable.Cols.POINTS + ", " +
                NoteTable.Cols.ZONE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS + TABLE_NAME");
        onCreate(db);
    }
}
